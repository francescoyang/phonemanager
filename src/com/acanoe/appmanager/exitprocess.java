package com.acanoe.appmanager;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import com.acanoe.appmanager.Appmanager;




public class exitprocess  extends Service{
	int i = 0;
	int testtimes = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		new Thread() {
			public void run() {
				openservice();
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
			Log.v("Acanoe","exit");

	}
}


