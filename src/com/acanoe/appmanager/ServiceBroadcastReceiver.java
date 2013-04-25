package com.acanoe.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ServiceBroadcastReceiver extends BroadcastReceiver {
	private static String START_ACTION = "ServiceStart";
	private static String STOP_ACTION = "ServiceStop";
	
	private static String START_EXIT = "ExitServiceStart";
	private static String STOP_EXIT = "ExitServiceStop";

	@Override
	public void onReceive(Context context, Intent intent) {
		
//
		String action = intent.getAction();
		if (START_ACTION.equalsIgnoreCase(action)) {
			Log.v("Acanoe","action get START_ACTION");
			context.startService(new Intent(context, androidService.class));
//			context.startService(new Intent(context, Appmanager.class));
		
			
		} else if (STOP_ACTION.equalsIgnoreCase(action)) {
			Log.v("Acanoe","action get STOP_ACTION");
			context.stopService(new Intent(context, androidService.class));			
		}
		
		if (START_EXIT.equalsIgnoreCase(action)) {
			Log.v("Acanoe","action get START_EXIT");
			//context.startService(new Intent(context, Appmanager.class));
			context.startService(new Intent(context, exitprocess.class));
		
			
		} else if (STOP_EXIT.equalsIgnoreCase(action)) {
			Log.v("Acanoe","action get STOP_EXIT");
			context.stopService(new Intent(context, exitprocess.class));			
		}
		
	}

}
