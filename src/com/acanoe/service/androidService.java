package com.acanoe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.acanoe.appmanager.Appmanager;




public class androidService  extends Service{
	int testtimes = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		/* �����ڲ���sysBroadcastReceiver ��ע��registerReceiver */
//		startservice();
		
		new Thread() {
			public void run() {
				openservice();
				//Thread.sleep(1000);
			};
		}.start();
	}

	
 
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void openservice()
	{
		Log.v("Acanoe","Thread test");
		Appmanager.startservice();
		//startservice();		
		
	}
	
//    public native static void startservice();
//    static {
//        System.loadLibrary("appmanager");
//    }
}


