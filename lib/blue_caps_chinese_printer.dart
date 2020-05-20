

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:streams_channel/streams_channel.dart';

class BlueCapsChinesePrinter {
  static const MethodChannel _channel =
      const MethodChannel('blue_caps_chinese_printer');

  static StreamsChannel _streamChannel=StreamsChannel('blue_caps_chinese_printer_stream');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void getBtPrinterStream(String argument){
    print('begin:getBtPrinterStream');
    WidgetsFlutterBinding.ensureInitialized();
    _streamChannel.receiveBroadcastStream(argument)
        .listen((i) {print('the stream said $i');});
    print('after listening');
  }

  static Stream<int> getBtPrinterStream_test_returnStream(String argument) {
    WidgetsFlutterBinding.ensureInitialized();
    return _streamChannel.receiveBroadcastStream(argument).map<int>((v) => (v) );
  }
}
