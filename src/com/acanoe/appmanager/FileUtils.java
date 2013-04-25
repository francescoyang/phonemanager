package com.acanoe.appmanager;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileUtils {
    private static String SDPATH;

    public String getSDPATH() {
            return SDPATH;
    }

    public  FileUtils() {
            // 得到当前外部存储设备的目录
            // /SDCARD
            SDPATH = Environment.getExternalStorageDirectory() +"/";
    }

    /**
     * 在SD卡上创建文件
     * 
     * @throws IOException
     */
    public File creatSDFile(String fileName) throws IOException {
            File file = new File(SDPATH + fileName);
            file.createNewFile();
            return file;
    }

    /**
     * 在SD卡上创建目录
     * 
     * @param dirName
     */
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

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName) {
            File file = new File(SDPATH + fileName);
            return file.exists();
    }

    /**
     * 创建
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