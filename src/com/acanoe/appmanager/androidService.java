package com.acanoe.appmanager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import com.acanoe.appmanager.Appmanager;
import com.acanoe.appmanager.FileUtils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class androidService extends Service {
	int i = 0;
	int testtimes = 0;

	private static final String TAG = "PHONESERVICE";

	@Override
	public void onCreate() {
		super.onCreate();

		Log.v("Acanoe", "opennservic");
		// FileUtils.isFolderExists("/sdcard/phonemanager/versions");
		// try {
		// FileUtils.creatSDFile("/sdcard/phonemanager/versions/1.0");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// getPhotosInfo();
		Appmanager.jnipthreadinit();
		appinfolist();
//		getPhoneContacts();
		getUserInfo();
//		getBookinfo();
		mHandler.post(mRunnable);

		// new Thread() {
		// public void run() {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// openservice();
		// };
		// }.start();
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
    
    
    
    private String getNameFromPhone(String number) {
        String name = null;
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };

        Cursor cursor = this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + number + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor == null) {
            Log.d(TAG, "getPeople null");
            return null;
        }
        Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            name = cursor.getString(nameFieldColumnIndex);
            Log.i(TAG, "" + name + " .... " + nameFieldColumnIndex);
            
            
        }
        cursor.close();
        return name;
        
    }
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "start onStart~~~");
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		Log.d(TAG, "start onDestroy~~~");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}

	private int count = 0;
	private Handler mHandler = new Handler();

	private Runnable mRunnable = new Runnable() {

		public void run() {

			// 为了方便 查看，我们用Log打印出来
//			Log.e(TAG, Thread.currentThread().getName() + " " + count);
//			count++;
			// setTitle("" + count);
			// 每2秒执行一次
			switch (Appmanager.whatyouwant()) {
			case 0x01: // imageinfo
				Log.d(TAG, "get appinfo");
				break;
			case 0x02: // videoifo
				Log.d(TAG, "get videoinfo");
				break;
			case 0x03: // musicinfo
				Log.d(TAG, "get musicinfo");
				break;
			case 0x04: // appinfo
				Log.d(TAG, "get appinfo");
				appinfolist();
				Appmanager.gotosend(0x04);
				break;
			case 0x05: // mmsinfo
				Log.d(TAG, "get mmsinfo");
				break;
			case 0x06: // bookinfo
				Log.d(TAG, "get booksinfo");
				break;
			default:
				break;
			}
			mHandler.postDelayed(mRunnable, 2000);
		}

	};

	private void getVideosInfo() {
		i = 0;

		String[] thumbColumns = new String[] {
				MediaStore.Video.Thumbnails.DATA,
				MediaStore.Video.Thumbnails.VIDEO_ID };
		ContentResolver contentResolver = getContentResolver();
		String[] videoColumns = new String[] { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE };
		// Cursor cursor=
		// this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
		// mediaColumns, null, null, null);
		Cursor cursor = contentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns,
				null, null, null);
		while (cursor.moveToNext()) {
			i++;
			String _id = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
			String filePath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
			String title = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
			String size = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

			// Cursor thumbCursor =
			// this.managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
			// thumbColumns, selection, selectionArgs, null);

			Appmanager.setvideoinfo(title, filePath, size, i);
		}
	}

	public void getPhotosInfo() {
		i = 0;

		ContentResolver contentResolver = getContentResolver();

		String[] photoColumns = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE,
				MediaStore.Images.Media.MIME_TYPE,
				MediaStore.Images.Media.SIZE,
				MediaStore.Images.Media.ORIENTATION

		};
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, photoColumns,
				null, null, null);
		while (cursor.moveToNext()) {
			i++;
			// System.out.println(i);
			String _id = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
			String filePath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			String title = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
			String mime_type = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
			String size = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

			// byte[] imagebyte = Bitmap2Bytes(getImageThumbnail(filePath, 100,
			// 100));

			/*
			 * Bitmap b = getImageThumbnail(filePath, 100, 100);
			 * FileUtils.isFolderExists("/sdcard/phonemanager"); File
			 * myCaptureFile = new File( "/sdcard/phonemanager/" +title
			 * +".jpg");
			 * 
			 * BufferedOutputStream bos; try {
			 * 
			 * // Log.d("acanoe","save jpg"); bos = new BufferedOutputStream(new
			 * FileOutputStream(myCaptureFile));
			 * b.compress(Bitmap.CompressFormat.JPEG, 80, bos); bos.flush();
			 * bos.close(); } catch (FileNotFoundException e) { // TODO
			 * Auto-generated catch block Log.d("acanoe","save jpg error"); //
			 * e.printStackTrace(); } catch (IOException e) { // TODO
			 * Auto-generated catch block // e.printStackTrace(); }
			 */

			// Appmanager.setimageinfo(title,filePath,size,imagebyte,i);

			Appmanager.setimageinfo(title, filePath, size, i);
		}
	}

	private Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(imagePath, options);

		options.inJustDecodeBounds = false;
		int w = options.outWidth;
		int h = options.outHeight;

		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//		System.out.println("w" + bitmap.getWidth());
//		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public void appinfolist() {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int j = 0; j < packages.size(); j++) {
//			System.out.println("packages.size is" + packages.size());
//			System.out.println(j);
			PackageInfo packageInfo = packages.get(j);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;


			Appmanager.setappinfo(1, 1, tmpInfo.appName, tmpInfo.packageName,
					tmpInfo.versionName, "123456", j);
			/*
			 * if ((packageInfo.applicationInfo.flags &
			 * ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
			 * Appmanager.setappinfo(1 ,1,tmpInfo.appName,tmpInfo.packageName,
			 * tmpInfo.versionName, "123456", j);
			 * 
			 * }
			 */
			// if(j == 40)
			// {
			//
			// break;
			// }

			// tmpInfo.appIcon2 =
			// packageInfo.applicationInfo.loadIcon(getPackageManager());

		}
	}

	public void openservice() {
		Log.v("Acanoe", "opennservic");
		getPhotosInfo();
		appinfolist();
		Appmanager.jnipthreadinit();
		// AppInfo.getIcon();
	}
}
