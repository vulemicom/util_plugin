package colleagues.product.skglobal.utilplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * UtilPlugin
 */
public class UtilPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "util_plugin");
        channel.setMethodCallHandler(new UtilPlugin(registrar));
    }

    Activity activity;
    Result result;

    public UtilPlugin(Registrar registrar) {
        this.activity = registrar.activity();
    }


    @Override
    public void onMethodCall(MethodCall call, Result result) {
        this.result = result;
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("openSocial")) {
            String channel = call.argument("channel");
            String nickname = call.argument("nick");

            try {
                openSocialApp(channel, nickname);
                result.success("");
            } catch (Exception e) {
                result.error("SHARE", e.getMessage(), null);
            }

        } else if (call.method.equals("sendMail")) {
            String mailTo = call.argument("to");
            String subject = call.argument("subject");
            String content = call.argument("content");
            sendMail(mailTo, subject, content);
            result.success("");
        } else {
            result.notImplemented();
        }
    }


    public void sendMail(String mailTo, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);//common intent
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
        activity.startActivity(Intent.createChooser(intent, "Email via..."));
    }

    public void openSocialApp(String socialChannel, String nickname) throws Exception {

        Intent intent;
        switch (socialChannel) {
            case "skype":
                try {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("skype:" + nickname + "?chat"));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    throw new Exception("Skype not found");
//                    result.error("SHARE", "skype not found", null);
                }
                break;

            case "twitter":
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + nickname));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + nickname));
                    activity.startActivity(intent);
                }
                break;

            case "instagram":
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + nickname));
                intent.setPackage("com.instagram.android");
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + nickname)));
                }
                break;

            case "facebook":
                try {
                    PackageManager packageManager = activity.getPackageManager();
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    String fbUrl;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        fbUrl = "fb://facewebmodal/f?href=https://www.facebook.com/" + nickname;
                    } else { //older versions of fb app
                        fbUrl = "fb://page/" + nickname;
                    }

                    activity.getPackageManager()
                            .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(fbUrl)); //Trys to make intent with FB's URI
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/" + nickname)); //catches and opens a url to the desired page
                }
                activity.startActivity(intent);
                break;

            case "linkedin":
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + nickname));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + nickname));
                    activity.startActivity(intent);
                }
                break;

            case "github":
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/" + nickname));
                activity.startActivity(intent);
                break;

            default:
                throw new Exception("Channel not found");
        }
    }
}
