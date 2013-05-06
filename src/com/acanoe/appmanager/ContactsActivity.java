package com.acanoe.appmanager;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends ListActivity {

//	Context mContext = null;
//
//	/** 获取库Phon表字段 **/
//	private static final String[] PHONES_PROJECTION = new String[] {
//			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
//
//	/** 联系人显示名称 **/
//	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
//
//	/** 电话号码 **/
//	private static final int PHONES_NUMBER_INDEX = 1;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		mContext = this;
//		/** 得到手机通讯录联系人信息 **/
//		getPhoneContacts();
//
//		// myAdapter = new MyListAdapter(this);
//		// setListAdapter(myAdapter);
//		//
//		//
//		// mListView.setOnItemClickListener(new OnItemClickListener() {
//		//
//		// @Override
//		// public void onItemClick(AdapterView<?> adapterView, View view,
//		// int position, long id) {
//		// //调用系统方法拨打电话
//		// Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
//		// .parse("tel:" + mContactsNumber.get(position)));
//		// startActivity(dialIntent);
//		// }
//		// });
//
//		super.onCreate(savedInstanceState);
//	}
//
//	/** 得到手机通讯录联系人信息 **/
//	private void getPhoneContacts() {
//		ContentResolver resolver = mContext.getContentResolver();
//
//		// 获取手机联系人
//		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
//				PHONES_PROJECTION, null, null, null);
//
//		if (phoneCursor != null) {
//			while (phoneCursor.moveToNext()) {
//
//				// 得到手机号码
//				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
//				// 当手机号码为空的或者为空字段 跳过当前循环
//				if (TextUtils.isEmpty(phoneNumber))
//					continue;
//				Log.v("java", "phoneNumber :" + phoneNumber);
//				// 得到联系人名称
//				String contactName = phoneCursor
//						.getString(PHONES_DISPLAY_NAME_INDEX);
//				Log.v("java", "contactName" + contactName);
//
//			}
//		}
//	}

}