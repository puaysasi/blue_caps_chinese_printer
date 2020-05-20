import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:blue_caps_chinese_printer/blue_caps_chinese_printer.dart';

void main() {
  //BlueCapsChinesePrinter.getBtPrinterStream_test_returnStream(
  //    'functionA')..listen((result) {print('result from test-returnStream=$result');});
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  Stream<int> _exampleStream;
  List<int> _listPrinterId = [];
  @override
  void initState() {
    super.initState();
    initPlatformState();
    _exampleStream = BlueCapsChinesePrinter.getBtPrinterStream_test_returnStream(
        'functionA')..listen((result) {
          print('result from test-returnStream=$result');
          _listPrinterId.add(result);
        });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await BlueCapsChinesePrinter.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Running on: $_platformVersion\n'),
        ),
        body: StreamBuilder<int>(
            stream: _exampleStream,
            builder: (context, snapshot) {
              return ListView.builder
                (
                  itemCount: _listPrinterId.length,
                  itemBuilder: (BuildContext ctxt, int index) {
                    return new Text('${_listPrinterId[index]}');
                  });
              if (snapshot.hasData) {
                return Text('${snapshot.data}');
              } else {
                return Text('NO DATA');
              }

            }),
      ),
    );
  }
}
