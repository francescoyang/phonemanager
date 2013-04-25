package com.acanoe.appmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.acanoe.appmanager.OnClickListener;
import com.acanoe.appmanager.R;

import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class Appmanager extends Activity {
	int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v("apk","Appmanager start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        	findViewById(R.id.button1).setBackgroundColor(Color.BLUE);
        	Log.v("apk", "jniservice");
        	Log.v("Acanoe","opennservic");
    		Appmanager.jnipthreadinit();
    		Appmanager.imageinfosend();
        }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appmanager, menu);
        return true;
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
    public native static void  startservice();
    public native static void  jnipthreadinit();
    public native static void  imageinfosend();
    static {
        System.loadLibrary("appmanager");
    }
}
