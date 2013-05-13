package com.acanoe.appmanager;


import java.io.File;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class filelist extends Activity
{ /* 声明对象变量 */
  private Button mButton;
  private EditText mKeyword;
  private TextView mResult;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
//    super.onCreate(savedInstanceState); /* 载入main.xml Layout */
//    setContentView(R.layout.main); /* 初始化对象 */
//    mKeyword = (EditText) findViewById(R.id.mKeyword);
//    mButton = (Button) findViewById(R.id.mButton);
//    mResult = (TextView) findViewById(R.id.mResult); /* 将mButton加入onClickListener */
//    mButton.setOnClickListener(new Button.OnClickListener()
//    {
//      public void onClick(View v)
//      { /* 取得输入的关键词 */
//        String keyword = mKeyword.getText().toString();
//        if (keyword.equals(""))
//        {
//          mResult.setText("请勿输入空白的关键词!!");
//        } else
//        {
//          mResult.setText(searchFile(keyword));
//        }
//      }
//    });
  } /* 搜索文件的method */
  
  
//  private static String searchFile(String keyword)
static  void searchFile(String keyword)
  {
    String result = "";
    /*File f = new File("/")指在当前盘符路径下*/
    /*listFiles()可以把目录下面的文件和子目录都打出来*/
    File[] files = new File("/").listFiles();
    
    
    
    for (File f : files)
    {
    	
    	Log.d("java","" + f.getPath());
      /*//判断文件名f中是否包含keyword
      if (f.getName().indexOf(keyword) >= 0)
      {
        //f.getPath()返回文件的路径
        result += f.getPath() + "/n";
      }*/
    }
//    if (result.equals(""))
//      result = "找不到文件!!";
//    return result;
  }
}