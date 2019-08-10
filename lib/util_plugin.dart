import 'dart:async';
import 'package:intl/intl.dart';

import 'package:flutter/services.dart';

class UtilPlugin {
  static const MethodChannel _channel = const MethodChannel('util_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Map<dynamic, dynamic>> get locale async {
    final version = await _channel.invokeMethod('locale') as Map<dynamic, dynamic>;
    return version;
  }

  static String formatDateString(String date, String format) {
    final f = new DateFormat(format);
    return f.format(DateTime.parse(date));
  }

  static Future<dynamic> openSocial(String channel, String nickname) async {
    return await _channel.invokeMethod('openSocial', {
      "channel": channel,
      "nick" : nickname
    });

  }

  static Future<dynamic> sendMail(String to, String subject, String content) async {
    return await _channel.invokeMethod('sendMail', {
      "to": to,
      "subject" : subject,
      "content": content
    });
  }

  static Future<dynamic> openMap(String address) async {
    return await _channel.invokeMethod('openMap', {
      "address": address
    });
  }
}
