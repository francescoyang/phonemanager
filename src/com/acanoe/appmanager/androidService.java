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
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import com.acanoe.appmanager.Appmanager;
import com.acanoe.appmanager.FileUtils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class androidService extends Service {
	int i = 0;
	int testtimes = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		Log.v("Acanoe", "opennservic");
		//getPhotosInfo();
		appinfolist();
		Appmanager.jnipthreadinit();

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

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

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
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
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
			System.out.println("packages.size is" + packages.size());
			System.out.println(j);
			PackageInfo packageInfo = packages.get(j);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;
			
			//String size = 
			
			Log.d("apk", tmpInfo.appName);
			Log.d("apk", tmpInfo.packageName);
			Log.d("apk", tmpInfo.versionName);
			Log.d("apk", "" + tmpInfo.versionCode);
			Log.d("apk", "");
		  	
			Appmanager.setappinfo(1 ,1,tmpInfo.appName,tmpInfo.packageName, tmpInfo.versionName, "123456",  j);
/*
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				//	非系统应用
				Appmanager.setappinfo(1 ,1,tmpInfo.appName,tmpInfo.packageName, tmpInfo.versionName, "123456",  j);

			}
			*/
			if(j == 40)
			{
				
				break;
			}

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
