# blue_caps_chinese_printer

How to create Plugin that Consume StreamChannel:
	1. create a plug-in project with command:
		flutter create --template=plugin -i objc -a java yourpluginname
		
		From <https://flutter.dev/docs/development/packages-and-plugins/developing-packages> 
		
		
	2. Add dependency on your pubspec.yaml file
		dependencies:
  streams_channel: ^0.3.0
		
		From <https://pub.dev/packages/streams_channel> 
		
		
	3. on Android code side [???plugin.java] (use edit on adrioid menu for java code support.):
		a. import:
			import app.loup.streams_channel.StreamsChannel;
			import io.flutter.plugin.common.EventChannel;
			import android.os.Handler;
			
			From <https://github.com/loup-v/streams_channel/blob/master/example/android/app/src/main/java/app/loup/streams_channel_example/PluginExample.java> 
			
			
			
			From <https://github.com/loup-v/streams_channel/blob/master/example/android/app/src/main/java/app/loup/streams_channel_example/PluginExample.java> 
			
			
		b. on method registerWith:
			i. add Stream channel setup here:
			
			//todo:addstreamChannelsetuphere
			final StreamsChannel streamChannel=newStreamsChannel(registrar.messenger(),"your_stream_channel_name");
			streamChannel.setStreamHandlerFactory(new StreamsChannel.StreamHandlerFactory(){
			@Override
			public EventChannel.StreamHandler create(Object arguments){
			return new StreamHandler();
			}
			});
			
			
		c. on method onAttachedToEngine:
			i. add Stream channel setup here:
			final StreamsChannel streamChannel=newStreamsChannel(flutterPluginBinding.getBinaryMessenger(),"your_stream_channel_name");
			streamChannel.setStreamHandlerFactory(new StreamsChannel.StreamHandlerFactory(){
			@Override
			public EventChannel.StreamHandler create(Object arguments){
			return new StreamHandler();
			}
			});
		d. implements a new method called StreamHandler to handle each call. (do especially note about onListen and onCancel implementation.
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
		eventSink.success("Hello"+count+"/10");
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
		runnable.run();
		}
		
		@Override
		public void onCancel(Object o){
		handler.removeCallbacks(runnable);
		}
		}
		
	4. on your dart plugin:
		a. import:
			i. import'package:streams_channel/streams_channel.dart';
			ii. import'package:flutter/material.dart';
		b. create a StreamChannel to hold on to channel.
		static StreamsChannel _streamChannel=StreamsChannel('your_stream_channel_name');
		c. then. crate your custom plugin method to provide the functionality (you can return value espicially a Stream to let your plug-in user to use that stream for UI/interaction/functionality.
		
		static void getBtPrinterStream(String argument){
		print('begin:getBtPrinterStream');
		WidgetsFlutterBinding.ensureInitialized();
		_streamChannel.receiveBroadcastStream(argument)
		.listen((i){print('the stream said$i');});
		print('after listening');
		}
		
		** ITS VERY IMPORTANT TO CALL ensureInitialized!!!!!
		 
	5. Make sure to call WidgetsFlutterBinding.ensureInitialized();
Since it's inteneded to run under runApp method. if we ensure this we can run this every where.
