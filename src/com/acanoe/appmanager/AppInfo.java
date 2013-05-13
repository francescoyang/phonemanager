package com.acanoe.appmanager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

//Model类 ，用来存储应用程序信息
public class AppInfo {
	
	
	private String appLabel;    //应用程序标签
	private Drawable appIcon ;  //应用程序图像
	private Intent intent ;     //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
	private String pkgName ;    //应用程序所对应的包名
	 public static PackageManager pm;
	
	 public String systemapp="";
	 public String unsystemapp="";
	 public String sdapp="";
	 
	 public String appName="";
	    public String packageName="";
	    public String versionName="";
	    public int versionCode=0;
	    public Drawable appIcon2=null;
	    public void print()
	    {
	        Log.v("app","Name:"+appName+" Package:"+packageName);
	        Log.v("app","Name:"+appName+" versionName:"+versionName);
	        Log.v("app","Name:"+appName+" versionCode:"+versionCode);
	    }
    

	
	public AppInfo(){}
	
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appName) {
		this.appLabel = appName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public String getPkgName(){
		return pkgName ;
	}
	public void setPkgName(String pkgName){
		this.pkgName=pkgName ;
	}
	private Bitmap getOneIcon(Context context){
		Log.w("Log", "获取的本地图标---------");
		Bitmap map = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
		Collections.sort(list,new ResolveInfo.DisplayNameComparator(pm));
		for(ResolveInfo reInfo : list){
			if(context.getPackageName().equals(reInfo.activityInfo.packageName)){
				Drawable draw = reInfo.loadIcon(pm);
				map = drawableToBitmap(draw);
			}
		}
		return map;
		
	}
	
	private static  Bitmap drawableToBitmap(Drawable drawable) {
		          // 取 drawable 的长宽
		          int w = drawable.getIntrinsicWidth();
		          int h = drawable.getIntrinsicHeight();
		  
		          // 取 drawable 的颜色格式
		          Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		                  : Bitmap.Config.RGB_565;
		          // 建立对应 bitmap
		         Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		         // 建立对应 bitmap 的画布
		         Canvas canvas = new Canvas(bitmap);
		         drawable.setBounds(0, 0, w, h);
		         // 把 drawable 内容画到画布中
	         drawable.draw(canvas);
		         return bitmap;
		    }
	
	
	/**获得当前应用图标 的图像**/
//	 public static void getIcon(Context context){
	
//	 PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
	 // 查询所有已经安装的应用程序  
	 public static void getIcon(){
			Log.i("acanoe","------------app icon------------");
		 Bitmap map = null;				 
	        List<ApplicationInfo> listAppcations = pm  
	                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);  
	        Collections.sort(listAppcations,  
	                new ApplicationInfo.DisplayNameComparator(pm));// 排序
	        for(ApplicationInfo info : listAppcations){
//	        	if(info.packageName.equals(context.getPackageName())){
	        		Log.d("icon","save");
	        		map = drawableToBitmap(info.loadIcon(pm));	        		
	    			FileUtils.isFolderExists("/sdcard/phonemanager");
	    			//	File myCaptureFile = new File( "/sdcard/phonemanager" + videoList.get(position).title +".jpg");
	    				File myCaptureFile = new File( "/sdcard/phonemanager/test"+ (String) info.loadLabel(pm) +".jpg");
	    				
	    		        BufferedOutputStream bos;
	    				try {	    					
	    					Log.d("acanoe","app icon");
	    					bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
	    					 map.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//	    				        bos.flush();
//	    				        bos.close();
	    				} catch (FileNotFoundException e) {
	    					// TODO Auto-generated catch block
	    					Log.d("acanoe","save jpg error");
	    					e.printStackTrace();
	    				}
//	        	}
	        }
	 //return map;	 
	 }
}
