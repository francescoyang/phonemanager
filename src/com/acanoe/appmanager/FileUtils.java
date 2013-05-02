package com.acanoe.appmanager;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileUtils {
	
	// 电子证据存放基本路径
	 private static String basePath;
	 // 电子证据存放根路径
	 private static String evidencePath;
	 // 照片基本存放路径
	 private static String imageBasePath;
    private static String SDPATH;
    
    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    

    

    public String getSDPATH() {
            return SDPATH;
    }

    public  FileUtils() {
            // 寰楀埌褰撳墠澶栭儴瀛樺偍璁惧鐨勭洰褰�            // /SDCARD
            SDPATH = Environment.getExternalStorageDirectory() +"/";
    }

    /**
     * 鍦⊿D鍗′笂鍒涘缓鏂囦欢
     * 
     * @throws IOException
     */
    public static File creatSDFile(String fileName) throws IOException {
            File file = new File(fileName);
            file.createNewFile();
            return file;
    }

 
    public static File creatSDDir(String dirName) {
           // File dir = new File(SDPATH + dirName);
    	 File dir = new File(dirName);
            dir.mkdir();
            return dir;
    }
    

    
    public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);        
        if (!file.exists()) {
            if (file.mkdirs()) {                
                return true;
            } else {
            	creatSDDir(strFolder);
                return false;

            }
        }
        return true;

    }

    
    public boolean isFileExist(String fileName) {
            File file = new File(SDPATH + fileName);
            return file.exists();
    }

    /**
     * 鍒涘缓
     */
    public File write2SDFromInput(String path, String fileName) {
            File file = null;
            try {
                    creatSDDir(path);
                    file = creatSDFile(path + fileName);
            } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
            }
            return file;
    }

}