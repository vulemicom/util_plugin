import Flutter
import UIKit

public class SwiftUtilPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "util_plugin", binaryMessenger: registrar.messenger())
        let instance = SwiftUtilPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if ("locale" == call.method) {
            self.locale(flutterResult: result)
        } else if ("openSocial" == call.method) {
            if let arg = call.arguments as? Dictionary<String, String> {
                if let channel = arg["channel"], let nick = arg["nick"] {
                    self.openSocials(channel: channel, nick: nick)
                }
            }
        } else if ("sendMail" == call.method) {
            if let arg = call.arguments as? Dictionary<String, String> {
                if let mail = arg["to"] {
                    self.sendMail(to: mail)
                }
            }
        } else if ("openMap" == call.method) {
            if let arg = call.arguments as? Dictionary<String, String> {
                if let mail = arg["address"] {
                    self.sendMail(to: mail)
                }
            }
        }
    }
    
    private func locale(flutterResult: FlutterResult) {
        let preferredLanguages = Locale.preferredLanguages.first?.split(separator: "-")
        
        var lanCode = "en"
        if (preferredLanguages != nil && (preferredLanguages?.count)! >= 1) {
            lanCode = String(preferredLanguages![0])
        }
        
        var countryCode = "US"
        if (preferredLanguages != nil && (preferredLanguages?.count)! >= 2) {
            countryCode = String(preferredLanguages![1])
        }
        let results = [
            "lanCode":lanCode,
            "countryCode":countryCode] as [String : Any]
        flutterResult(results)
    }
    
    private func sendMail(to mail: String) {
        if let url = URL(string: "mailto:\(mail)") {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
    private func openSocials(channel: String, nick: String) {
        var appURL = ""
        var webURL = ""
        switch channel {
        case "facebook":
            appURL = "fb://profile?id=\(nick)"
            webURL = "https://facebook.com/\(nick)"
            break
        case "skype":
            appURL = "skype:\(nick)?chat"
            webURL = "http://itunes.com/apps/skype/skype"
            break
        case "instagram":
            appURL = "instagram://user?username=\(nick)"
            webURL = "https://www.instagram.com/\(nick)/"
            break
        case "twitter":
            appURL = "twitter://user?screen_name=\(nick)"
            webURL = "https://twitter.com/\(nick)"
            break
        case "linkedin":
            appURL = "linkedin://profile/\(nick)"
            webURL = "http://www.linkedin.com/profile/view?id=\(nick)"
            break
        case "github":
            appURL = "https://github.com/\(nick)"
            webURL = appURL
            break
        default:
            break
        }
        openURL(with: NSURL(string: appURL)! as URL, secondURL: NSURL(string: webURL)! as URL)
    }
    
    private func openMap(address: String) {
        if let url = URL(string: "comgooglemaps://?q=\(address.replacingOccurrences(of: " ", with: "+"))") {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
    private func openURL(with URL: URL, secondURL: URL) {
        if UIApplication.shared.canOpenURL(URL) {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(URL, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(URL)
            }
        } else {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(secondURL, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(secondURL)
            }
        }
    }
}

