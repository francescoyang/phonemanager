package com.acanoe.appmanager;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
 
 
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

public class getmmsinfo extends Activity{
	
	
	
	public void getBookinfo()
	{
		ContentResolver cr=this.getContentResolver(); 
		Cursor cursor=cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
		while (cursor.moveToNext()) { 
		        //取得联系人名字 
		        int nameFieldColumnIndex=cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME); 
		        String contact=cursor.getString(nameFieldColumnIndex); 
		        Log.d("Java",contact);
		        //取得电话号码 
		        int numberFieldColumnIndex=cursor.getColumnIndex(PhoneLookup.NUMBER); 
		        String number=cursor.getString(numberFieldColumnIndex); 
		        Log.d("Java",number);
		}	
	
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

}
