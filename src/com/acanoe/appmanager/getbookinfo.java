package com.acanoe.appmanager;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class getbookinfo  extends Activity {

	
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
}
