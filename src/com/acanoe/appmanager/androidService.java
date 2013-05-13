package com.acanoe.appmanager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.acanoe.appmanager.Appmanager;
import com.acanoe.appmanager.FileUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class androidService extends Service {
	int i = 0;
	int testtimes = 0;
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

	private static final String TAG = "Java";

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
		// openservice();

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
		Appmanager.exitprograme();
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

	public void openservice() {
		Log.v("Java", "setallinfo");
		// getPhotosInfo();
		// appinfolist();
		// getUserInfo();
		// getSmsInPhone();
		// sendSMS("10086" , "3053");
		updateMemoryStatus();
		Appmanager.gotosend(7);
	}

	public String getSmsInPhone() {
		i = -1;
		final String SMS_URI_ALL = "content://sms/"; // 所有短信
		final String SMS_URI_INBOX = "content://sms/inbox"; // 收信箱
		final String SMS_URI_SEND = "content://sms/sent"; // 发信箱
		final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿箱
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
				int nameColumn = cur.getColumnIndex("person");// 姓名
				int phoneNumberColumn = cur.getColumnIndex("address");// 手机号
				int smsbodyColumn = cur.getColumnIndex("body");// 短信内容
				int dateColumn = cur.getColumnIndex("date");// 日期
				int typeColumn = cur.getColumnIndex("type");// 收发类型 1表示接受 2表示发送
				do {
					i++;
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
					// Log.d("java", "name" + name);

					smsBuilder.append(phoneNumber + ",");
					// Log.d("java", "PhoneNumber" + phoneNumber);

					smsBuilder.append(smsbody + ",");
					// Log.d("java", "smsbody" + smsbody);

					smsBuilder.append(date + ",");
					// Log.d("java", "date" + date);

					smsBuilder.append(type);
					// Log.d("java", "type " + type);

					Appmanager.setmmsinfo(name, phoneNumber, smsbody, date,
							typeId, i);

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

	public void getUserInfo() {
		i = -1;
		Cursor cursor = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {

			String id = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// Log.d("java" , "Name is : "+name);
			int isHas = Integer
					.parseInt(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
			if (isHas > 0) {
				Cursor c = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				while (c.moveToNext()) {
					i++;
					String number = c
							.getString(c
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Log.d("java" , "Name is : "+name +
					// "  Number is : "+number);
					if (Appmanager.setbookinfo(name, number, i) < 0) {
						Log.d("java", "error");
						--i;
					}
				}
				c.close();
			}
		}
		cursor.close();
	}

	// private String getNameFromPhone(String number) {
	// String name = null;
	// String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
	// ContactsContract.CommonDataKinds.Phone.NUMBER };
	//
	// Cursor cursor = this.getContentResolver().query(
	// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	// projection, // Which columns to return.
	// ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number
	// + "'", // WHERE clause.
	// null, // WHERE clause value substitution
	// null); // Sort order.
	//
	// if (cursor == null) {
	// Log.d(TAG, "getPeople null");
	// return null;
	// }
	// Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
	// for (int i = 0; i < cursor.getCount(); i++) {
	// cursor.moveToPosition(i);
	//
	// int nameFieldColumnIndex = cursor
	// .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
	// name = cursor.getString(nameFieldColumnIndex);
	// Log.i(TAG, "" + name + " .... " + nameFieldColumnIndex);
	//
	// }
	// cursor.close();
	// return name;
	//
	// }

	private int count = 0;
	private Handler mHandler = new Handler();

	private Runnable mRunnable = new Runnable() {
		String mmsnumber;
		String mmsdata;

		public void run() {

			// 为了方便 查看，我们用Log打印出来
			// Log.e(TAG, Thread.currentThread().getName() + " " + count);
			// count++;
			// setTitle("" + count);
			// 每2秒执行一次
			// #define CMD_IMAGE 0X01
			// 20 #define CMD_VIDEO 0X02
			// 21 #define CMD_MUSIC 0X03
			// 22 #define CMD_APP 0X04
			// 23 #define CMD_MMS 0X05
			// 24 #define CMD_BOOK 0X06

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
				Appmanager.gotosend(4);
				break;
			case 0x05: // mmsinfo
				Log.d(TAG, "get mmsinfo");
				getSmsInPhone();
				Appmanager.gotosend(5);
				break;
			case 0x06: // bookinfo
				Log.d(TAG, "get booksinfo");
				getUserInfo();
				Appmanager.gotosend(6);
				break;
			case 0x07: // storageinfo
				Log.d(TAG, "get storageinfo");
				updateMemoryStatus();
				Appmanager.gotosend(7);
				break;
			case 0x08: // storageinfo
				mmsnumber = Appmanager.getmmsnumber();
				mmsdata = Appmanager.getmmsdata();
				Log.d(TAG, "get messageinfo" + mmsnumber + mmsdata);
				sendSMS(mmsnumber, mmsdata);
				Appmanager.gotosend(8);
				break;
			default:
				break;
			}
			mHandler.postDelayed(mRunnable, 1000);
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
		// System.out.println("w" + bitmap.getWidth());
		// System.out.println("h" + bitmap.getHeight());
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
		i = -1;
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int j = 0; j < packages.size(); j++) {
			
			i ++;
			// System.out.println("packages.size is" + packages.size());
			// System.out.println(j);
			PackageInfo packageInfo = packages.get(j);
			AppInfo tmpInfo = new AppInfo();
//			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
//					getPackageManager()).toString();
//			tmpInfo.packageName = packageInfo.packageName;
//			tmpInfo.versionName = packageInfo.versionName;
//			tmpInfo.versionCode = packageInfo.versionCode;

//			Appmanager.setappinfo(1, 1, tmpInfo.appName, tmpInfo.packageName,
//					tmpInfo.versionName, "123456", j);

			// Log.d("java","" + tmpInfo.appName + "   flags   " +
			// packageInfo.applicationInfo.flags + "   FLAG_SYSTEM: " +
			// ApplicationInfo.FLAG_SYSTEM + "   FLAG_EXTERNAL_STORAGE:   "
			// +ApplicationInfo.FLAG_EXTERNAL_STORAGE);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { // 系统应用
			 
				tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
						getPackageManager()).toString();
				tmpInfo.packageName = packageInfo.packageName;
				tmpInfo.versionName = packageInfo.versionName;
				tmpInfo.versionCode = packageInfo.versionCode;
				
//				Log.d("java", "systemapp      " + tmpInfo.appName);
				if(Appmanager.setappinfo(0, 0, tmpInfo.appName,
			 tmpInfo.packageName, tmpInfo.versionName, "123456", i) < 0)
					i--;

//				tmpInfo.systemapp = packageInfo.applicationInfo.loadLabel(
//						getPackageManager()).toString();
				
			}

			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
		
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
					
					
					tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
							getPackageManager()).toString();
					tmpInfo.packageName = packageInfo.packageName;
					tmpInfo.versionName = packageInfo.versionName;
					tmpInfo.versionCode = packageInfo.versionCode;
					
//					Log.d("java", "systemapp      " + tmpInfo.appName);
					 if(Appmanager.setappinfo(1, 1, tmpInfo.appName,
					 tmpInfo.packageName, tmpInfo.versionName, "123456", i) < 0)
						 i--;
					 
					 
//					tmpInfo.unsystemapp = packageInfo.applicationInfo
//							.loadLabel(getPackageManager()).toString();
//					Log.d("Java", "unsystemapp  on sdcard  " + tmpInfo.unsystemapp);
				} else{
					tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
							getPackageManager()).toString();
					tmpInfo.packageName = packageInfo.packageName;
					tmpInfo.versionName = packageInfo.versionName;
					tmpInfo.versionCode = packageInfo.versionCode;
					
//					Log.d("java", "systemapp      " + tmpInfo.appName);
					 if(Appmanager.setappinfo(0, 1, tmpInfo.appName,
					 tmpInfo.packageName, tmpInfo.versionName, "123456", i) < 0)
						 i--;
					
					
//					tmpInfo.unsystemapp = packageInfo.applicationInfo
//							.loadLabel(getPackageManager()).toString();
//					Log.d("Java", "unsystemapp  on phone  " + tmpInfo.unsystemapp);
					
				}
			}
//
//			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) { // SD应用
//			// Appmanager.setappinfo(0, 1, tmpInfo.appName,
//			// tmpInfo.packageName, tmpInfo.versionName, "123456", j);
//				tmpInfo.sdapp = packageInfo.applicationInfo.loadLabel(
//						getPackageManager()).toString();
//				Log.d("java", "sdapp      " + tmpInfo.sdapp);
//
//			}

			// if(j == 40)
			// {
			//
			// break;
			// }

			// tmpInfo.appIcon2 =
			// packageInfo.applicationInfo.loadIcon(getPackageManager());

		}
	}

	// private PackageManager pm;
	//
	// public static final int FILTER_ALL_APP = 0; // 所有应用程序
	// public static final int FILTER_SYSTEM_APP = 1; // 系统程序
	// public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
	// public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
	//
	// // 根据查询条件，查询特定的ApplicationInfo
	// private List<AppInfo> queryFilterAppInfo(int filter) {
	// pm = this.getPackageManager();
	// // 查询所有已经安装的应用程序
	// List<ApplicationInfo> listAppcations = pm
	// .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
	// Collections.sort(listAppcations,
	// new ApplicationInfo.DisplayNameComparator(pm));// 排序
	// List<AppInfo> appInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo
	// // 根据条件来过滤
	// switch (filter) {
	// case FILTER_ALL_APP: // 所有应用程序
	// appInfos.clear();
	// for (ApplicationInfo app : listAppcations) {
	// appInfos.add(getAppInfo(app));
	// }
	// // return appInfos;
	// break;
	// case FILTER_SYSTEM_APP: // 系统程序
	// appInfos.clear();
	// for (ApplicationInfo app : listAppcations) {
	// if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
	// appInfos.add(getAppInfo(app));
	// }
	// }
	// return appInfos;
	// case FILTER_THIRD_APP: // 第三方应用程序
	// appInfos.clear();
	// for (ApplicationInfo app : listAppcations) {
	// if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
	// appInfos.add(getAppInfo(app));
	// }
	// }
	// break;
	// case FILTER_SDCARD_APP: // 安装在SDCard的应用程序
	// appInfos.clear();
	// for (ApplicationInfo app : listAppcations) {
	// if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
	// appInfos.add(getAppInfo(app));
	// }
	// }
	// return appInfos;
	// default:
	// return null;
	// }
	// return appInfos;
	// }
	//
	// // 构造一个AppInfo对象 ，并赋值
	// private AppInfo getAppInfo(ApplicationInfo app) {
	// AppInfo appInfo = new AppInfo();
	// appInfo.setAppLabel((String) app.loadLabel(pm));
	// appInfo.setAppIcon(app.loadIcon(pm));
	// appInfo.setPkgName(app.packageName);
	// return appInfo;
	// }

	private void updateMemoryStatus() {

		String sdSize = null; // SD卡总容量
		String sdAvail = null; // SD卡剩余容量
		String memoryAvail = null; // 手机内存剩余容量
		String memorySize = null; // 手机内存总容量

		String status = Environment.getExternalStorageState();
		String readOnly = "";
		// 是否只读
		if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			status = Environment.MEDIA_MOUNTED;
			// readOnly = getString(R.string.read_only);
		}
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				long availableBlocks = stat.getAvailableBlocks();

				Log.i(TAG, "totalBlocks 	long :" + totalBlocks);

				// Log.i(TAG, "SD卡剩余容量 long: " + availableBlocks);
				// Log.i(TAG, "SD卡总容量 	long :" + blockSize);
				// SD卡总容量
				sdSize = formatSize(totalBlocks * blockSize);
				Log.i(TAG, "SD卡总容量: " + sdSize);
				// SD卡剩余容量
				sdAvail = formatSize(availableBlocks * blockSize) + readOnly;
				Log.i(TAG, "SD卡剩余容量: " + sdAvail);
			} catch (IllegalArgumentException e) {
				status = Environment.MEDIA_REMOVED;
			}
		}
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();

		// Log.i(TAG, "手机内存剩余容量 long: " + blockSize);
		// Log.i(TAG, "手机内存剩余容量 long: " + availableBlocks);
		// 手机内存剩余容量
		memoryAvail = formatSize(availableBlocks * blockSize);

		Log.i(TAG, "手机内存剩余容量: " + memoryAvail);
		long totalBlocks = stat.getBlockCount();
		// 手机内存总容量
		memorySize = formatSize(totalBlocks * blockSize);
		Log.i(TAG, "手机内存总容量: " + memorySize);

		Appmanager.setstorageinfo(sdSize, sdAvail, memorySize, memoryAvail);
	}

	// 格式化 转化为.MB格式
	private String formatSize(long size) {
		return Formatter.formatFileSize(this, size);

	}

	// private void sendSMS(String phoneNumber , String message) {

	private void sendSMS(String phoneNumber, String message) {
		// create the sentIntent parameter
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
				0);

		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
				deliverIntent, 0);
		// register the Broadcast Receivers
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(),
					// "SMS sent success actions",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					//
					// Toast.makeText(getBaseContext(),
					// "SMS generic failure actions",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					// Toast.makeText(getBaseContext(),
					// "SMS radio off failure actions",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					// Toast.makeText(getBaseContext(),
					// "SMS null PDU failure actions",
					// Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT_SMS_ACTION));

		// ---sends an SMS message to another device---
		SmsManager sms = SmsManager.getDefault();
		// PendingIntent pi = PendingIntent.getActivity(this, 0,
		// new Intent(this,MainActivity.class), 0);
		// if message's length more than 70 ,
		// then call divideMessage to dive message into several part
		// and call sendTextMessage()
		// else direct call sendTextMessage()
		if (message.trim().length() > 70) {
			ArrayList<String> msgs = sms.divideMessage(message);
			for (String msg : msgs) {
				sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
			}
		} else {
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
		}
		// Toast.makeText(MainActivity.this, "短信发送完成",
		// Toast.LENGTH_LONG).show();
	}

}
