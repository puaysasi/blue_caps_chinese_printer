package com.example.blue_caps_chinese_printer;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import app.loup.streams_channel.StreamsChannel;
import io.flutter.plugin.common.EventChannel;
import android.os.Handler;

/** BlueCapsChinesePrinterPlugin */
public class BlueCapsChinesePrinterPlugin implements FlutterPlugin, MethodCallHandler {
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "blue_caps_chinese_printer");
    channel.setMethodCallHandler(new BlueCapsChinesePrinterPlugin());

    final StreamsChannel streamChannel=new StreamsChannel(flutterPluginBinding.getBinaryMessenger(),"blue_caps_chinese_printer_stream");
    streamChannel.setStreamHandlerFactory(new StreamsChannel.StreamHandlerFactory(){
      @Override
      public EventChannel.StreamHandler create(Object arguments){
        return new StreamHandler();
      }
    });
  }

  //todo:addimplementationofEventChannel.StreamHandlerhere.
  //Send"Hello"10times,everysecond,thenendsthestream
  public static class StreamHandler implements EventChannel.StreamHandler{

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable(){
      @Override
      public void run(){
        if(count>10){
          eventSink.endOfStream();
        }else{
          eventSink.success( count);
        }
        count++;
        handler.postDelayed(this,1000);
      }
    };

    private EventChannel.EventSink eventSink;
    private int count=1;

    @Override
    public void onListen(Object o,final EventChannel.EventSink eventSink){
      this.eventSink=eventSink;
      // branch your code here.
      runnable.run();
    }

    @Override
    public void onCancel(Object o){
      handler.removeCallbacks(runnable);
    }
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "blue_caps_chinese_printer");
    channel.setMethodCallHandler(new BlueCapsChinesePrinterPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
