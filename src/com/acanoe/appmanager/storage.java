package com.acanoe.appmanager;

import java.io.File;

import android.app.Activity;
import android.app.Service;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

public class storage extends Activity{
	private static final String TAG = "Java";

	// 这个是手机内存的可用空间大小

	static public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	// 这个是手机内存的总空间大小

	static public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

//	// 这个是手机sdcard的可用空间大小
//
//	static public long getAvailableExternalMemorySize() {
//		if (externalMemoryAvailable()) {
//			File path = Environment.getExternalStorageDirectory();
//			StatFs stat = new StatFs(path.getPath());
//			long blockSize = stat.getBlockSize();
//			long availableBlocks = stat.getAvailableBlocks();
//			return availableBlocks * blockSize;
//		} else {
//			return ERROR;
//		}
//	}
//
//	// 这个是手机sdcard的总空间大小
//
//	static public long getTotalExternalMemorySize() {
//		if (externalMemoryAvailable()) {
//			File path = Environment.getExternalStorageDirectory();
//			StatFs stat = new StatFs(path.getPath());
//			long blockSize = stat.getBlockSize();
//			long totalBlocks = stat.getBlockCount();
//			return totalBlocks * blockSize;
//		} else {
//			return ERROR;
//		}
//	}

	private  void updateMemoryStatus() {
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
				// SD卡总容量
				String sdSize = formatSize(totalBlocks * blockSize);
				Log.i(TAG, "SD卡总容量: " + sdSize);
				// SD卡剩余容量
				String sdAvail = formatSize(availableBlocks * blockSize)
						+ readOnly;
				Log.i(TAG, "SD卡剩余容量: " + sdAvail);
			} catch (IllegalArgumentException e) {
				status = Environment.MEDIA_REMOVED;
			}
		}
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		// 手机内存剩余容量
		String memoryAvail = formatSize(availableBlocks * blockSize);
		Log.i(TAG, "手机内存剩余容量: " + memoryAvail);
		long totalBlocks = stat.getBlockCount();
		// 手机内存总容量
		String memorySize = formatSize(totalBlocks * blockSize);
		Log.i(TAG, "手机内存总容量: " + memorySize);
		
	}

	// 格式化 转化为.MB格式
	private String formatSize(long size) {
		return Formatter.formatFileSize(this, size);

	}

}
