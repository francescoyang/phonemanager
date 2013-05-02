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
		appinfolist();
//		getPhoneContacts();
		
		Appmanager.jnipthreadinit();
		
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

	
	Context mContext = null;

	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** 联系人名称 **/
	private static ArrayList<String> mContactsName = new ArrayList<String>();

	/** 联系人头像 **/
	private static ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** 联系人头像 **/
	private static ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	/** 得到手机通讯录联系人信息 **/
	public void getPhoneContacts() {
		Log.d("Java","getPhoneContacts start");
		
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				Log.d("Java","contact number:" + phoneNumber);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				Log.d("Java","contact name:" + contactName);

				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// 得到联系人头像ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				if (photoid > 0) {
//					Uri uri = ContentUris.withAppendedId(
//							ContactsContract.Contacts.CONTENT_URI, contactid);
//					InputStream input = ContactsContract.Contacts
//							.openContactPhotoInputStream(resolver, uri);
//					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					// contactPhoto =
					// BitmapFactory.decodeResource(getResources(),
					// R.drawable.contact_photo);
				}

//				mContactsName.add(contactName);
//				mContactsNumber.add(phoneNumber);
//				mContactsPhonto.add(contactPhoto);
			}

			phoneCursor.close();
		}
	}
	
	
	public void getBookinfo()
	{
		ContentResolver cr=this.getContentResolver(); 
		Cursor cursor=cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
		while (cursor.moveToNext()) { 
		        //取得联系人名字 
		        int nameFieldColumnIndex=cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME); 
		        String contact=cursor.getString(nameFieldColumnIndex); 
		        Log.d("Java","contact name:" + contact);
		        //取得电话号码 
		        int numberFieldColumnIndex=cursor.getColumnIndex(PhoneLookup.NUMBER); 
		        String number=cursor.getString(numberFieldColumnIndex); 
		        Log.d("Java","contact number:" + number);
		}	
	
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
