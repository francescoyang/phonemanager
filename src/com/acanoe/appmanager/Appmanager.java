package com.acanoe.appmanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.acanoe.appmanager.ContactsActivity;

import com.acanoe.appmanager.OnClickListener;
import com.acanoe.phonemanager.R;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Appmanager extends Activity {
	 Context mContext = null;
	int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v("apk","Appmanager start");
        super.onCreate(savedInstanceState);
       	setContentView(R.layout.main);

       	mContext = this;
        
//		FileUtils.isFolderExists("/sdcard/phonemanager/versions");
//		try {
//			FileUtils.creatSDFile("/sdcard/phonemanager/versions/1.0");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
//        	findViewById(R.id.button1).setBackgroundColor(Color.BLUE);
//        	Log.v("Acanoe","activity");
        	
//        		getbookinfo.getUserInfo();
//        	Log.d("Java","" + getSmsInPhone());
//        	android.os.Process.killProcess(android.os.Process.myPid()) ;
        	
//    		Appmanager.jnipthreadinit();
//    		Appmanager.imageinfosend();
        	
//            TextView tv = new TextView(this);
//            tv.setText(getSmsInPhone());
//
//            ScrollView sv = new ScrollView(this);
//            sv.addView(tv);
//            
//            setContentView(sv);
//        	 getSmsInPhone();
        }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appmanager, menu);
        return true;
    }
    
    
    


    public void getUserInfo(){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.d("java"    , "Name is : "+name);
            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if(isHas>0){
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                         ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + id,null,null);
                while(c.moveToNext()){
                    String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d("java"   , "Number is : "+number);
                }
                c.close();
            }
        }
        cursor.close();
 }

    
    
    
	public String getSmsInPhone() {
		final String SMS_URI_ALL = "content://sms/"; 		//所有短信
		final String SMS_URI_INBOX = "content://sms/inbox";	//收信箱
		final String SMS_URI_SEND = "content://sms/sent";	//发信箱
		final String SMS_URI_DRAFT = "content://sms/draft";	//草稿箱
		StringBuilder smsBuilder = new StringBuilder();
		try {
			ContentResolver cr = getContentResolver();
			String[] projection = new String[] { "_id", "address", "person",
					"body", "date", "type" };
			Uri uri = Uri.parse(SMS_URI_ALL);
			Cursor cur = cr.query(uri, projection, null, null, "date desc");
			if (cur.moveToFirst()) {
				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;
				int nameColumn = cur.getColumnIndex("person");//姓名
				int phoneNumberColumn = cur.getColumnIndex("address");//手机号
				int smsbodyColumn = cur.getColumnIndex("body");//短信内容
				int dateColumn = cur.getColumnIndex("date");//日期
				int typeColumn = cur.getColumnIndex("type");//收发类型 1表示接受 2表示发送
				do {
					name = cur.getString(nameColumn);
					phoneNumber = cur.getString(phoneNumberColumn);
					smsbody = cur.getString(smsbodyColumn);
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
					date = dateFormat.format(d);
					int typeId = cur.getInt(typeColumn);
					if (typeId == 1) {
						type = "接收";
					} else if (typeId == 2) {
						type = "发送";
					} else {
						type = "";
					}
					smsBuilder.append("[");
					
					smsBuilder.append(name + ",");
					Log.d("java","name" + name);
					
					smsBuilder.append(phoneNumber + ",");
					Log.d("java","PhoneNumber" + phoneNumber );
					
					smsBuilder.append(smsbody + ",");
					Log.d("java","smsbody" + smsbody );
					
					smsBuilder.append(date + ",");
					Log.d("java","date" + date );
					
					smsBuilder.append(type);
					Log.d("java","type " + type );
					
					smsBuilder.append("] ");
					if (smsbody == null)
						smsbody = "";
				} while (cur.moveToNext());
			} else {
				smsBuilder.append("没有记录!");
			}
			smsBuilder.append("获取彩信完成!");
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
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
