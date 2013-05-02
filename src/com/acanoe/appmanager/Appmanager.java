package com.acanoe.appmanager;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.acanoe.appmanager.OnClickListener;
import com.acanoe.phonemanager.R;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class Appmanager extends Activity {
	int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v("apk","Appmanager start");
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        TextView tv = new TextView(this);
      tv.setText(getSmsInPhone());

      ScrollView sv = new ScrollView(this);
      sv.addView(tv);
      
      setContentView(sv);
      
        
//		FileUtils.isFolderExists("/sdcard/phonemanager/versions");
//		try {
//			FileUtils.creatSDFile("/sdcard/phonemanager/versions/1.0");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        
//        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
//        	public void onClick(View v) {
//        	findViewById(R.id.button1).setBackgroundColor(Color.BLUE);
//        	Log.v("Acanoe","activity");
////        	android.os.Process.killProcess(android.os.Process.myPid()) ;
//        	
////    		Appmanager.jnipthreadinit();
////    		Appmanager.imageinfosend();
//        	
////            TextView tv = new TextView(this);
////            tv.setText(getSmsInPhone());
////
////            ScrollView sv = new ScrollView(this);
////            sv.addView(tv);
////            
////            setContentView(sv);
////        	 getSmsInPhone();
//        }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appmanager, menu);
        return true;
    }
    
    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";                          
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

//        String Builder smsBuilder = new String Builder();
      StringBuilder smsBuilder = new StringBuilder();

        try{
                 Uri uri = Uri.parse(SMS_URI_ALL);
                 String[] projection = new String[] { "_id", "address","person", "body", "date", "type" };
                 Cursor cur = getContentResolver().query(uri, projection, null, null, "datedesc");                  // 获取手机内部短信

                 if(cur.moveToFirst()) {
                          int index_Address = cur.getColumnIndex("address");    // 获取sms数据库字段
                          int index_Person = cur.getColumnIndex("person");
                          int index_Body = cur.getColumnIndex("body");
                          int index_Date = cur.getColumnIndex("date");
                          int index_Type = cur.getColumnIndex("type");

                          do{
                                    String strAddress = cur.getString(index_Address);
                                    int intPerson = cur.getInt(index_Person);
                                    String strbody = cur.getString(index_Body);
                                    long longDate = cur.getLong(index_Date);
                                    int intType = cur.getInt(index_Type);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = new Date(longDate);
                                    String strDate = dateFormat.format(d);

                                    String strType = "";  // 接收/发送（1/2）形式转换
                                    if(intType == 1) {
                                             strType= "接收";
                                    }else if (intType == 2) {
                                             strType= "发送";
                                    }else {
                                             strType= "null";
                                    }

                                    smsBuilder.append("[");
                                    smsBuilder.append(strAddress+ ", ");
                                    smsBuilder.append(intPerson+ ", ");
                                    smsBuilder.append(strbody +", ");
                                    smsBuilder.append(strDate+ ", ");
                                    smsBuilder.append(strType);
                                    smsBuilder.append("]\n\n");
                          }while (cur.moveToNext());

                          if(!cur.isClosed()) {           // 关闭游标
                                    cur.close();
                                    cur= null;
                          }
                 }else {
                          smsBuilder.append("noresult!");
                 }// end if

                 smsBuilder.append("getSmsInPhonehas executed!");

        }catch (SQLiteException ex) {
                 Log.d("SQLiteExceptionin getSmsInPhone", ex.getMessage());
        }

        return smsBuilder.toString();
}
    
    
    private void getVideosInfo() {
    	i = 0;
		ContentResolver contentResolver=getContentResolver();
		String [] videoColumns=new String[]{
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.MIME_TYPE,
				MediaStore.Video.Media.SIZE
		};
		Cursor cursor=contentResolver.query
		(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
		while (cursor.moveToNext()) {
			i++;
			String _id=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
			String filePath=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
			String title=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
			String size=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
		}
	}
    
    
    public void getPhotosInfo() {
    	i = 0;
		ContentResolver contentResolver=getContentResolver();
		String [] photoColumns=new String[]{
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.TITLE,
				MediaStore.Images.Media.MIME_TYPE,
				MediaStore.Images.Media.SIZE,
				MediaStore.Images.Media.ORIENTATION
				
		};
		Cursor cursor=contentResolver.query
		(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, photoColumns, null, null, null);
		while (cursor.moveToNext()) {
			i++;
			String _id=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
			String filePath=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			String title=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
			String mime_type=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
			String size=
			cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
		   //setimageinfo(title,filePath,size,i);			
		}
	 
 }
    
    
  	 private void getAudiosInfo() {
  		 	i = 0;
  			ContentResolver contentResolver=getContentResolver();
  			String [] audioColumns=new String[]{
  					MediaStore.Audio.Media._ID,
  					MediaStore.Audio.Media.DATA,
  					MediaStore.Audio.Media.TITLE,
  					MediaStore.Audio.Media.SIZE
  			};
  			Cursor cursor=contentResolver.query
  			(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns, null, null, null);
  			while (cursor.moveToNext()) {
  				i++;
  				String _id=
  				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
  				String filePath=
  				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
  				String title=
  				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
  				String size=
  				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
  			}
  	 } 

  	public static native void  setappinfo(int apppath ,int appattribute,String appname,String packname, String appversion, String size,  int count);
    public static native void  setimageinfo(String name,String path, String size,int count);
    public static native void  setvideoinfo(String name,String path, String size, int count);
    public static native void  setmusicinfo(String name,String path, String size, int count);
    public static native void  gotosend(int argv);
    public native static void  startservice();
    public native static void  jnipthreadinit();
    public native static void  imageinfosend();
    public native static int   whatyouwant();
    static {
        System.loadLibrary("appmanager");
    }
}
