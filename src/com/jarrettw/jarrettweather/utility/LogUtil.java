package com.jarrettw.jarrettweather.utility;

import android.util.Log;

//自定义日志工具
public class LogUtil {
	
	//1-6为调试级别
	public static final int VERBOSE = 1;
	
	public static final int DEBUG = 2;
	
	public static final int INFO = 3;
	
	public static final int WARN = 4;
	
	public static final int ERROR = 5;
	
	public static final int NOTHING = 6;
	
	public static int LEVEL = VERBOSE;
	
	public static void log(String tag, String msg){
			switch(LEVEL){
			case VERBOSE:
				Log.v(tag, msg);
				break;
			case DEBUG:
				Log.d(tag, msg);
				break;
			case INFO:
				Log.i(tag, msg);
				break;
			case WARN:
				Log.w(tag, msg);
				break;
			case ERROR:
				Log.e(tag, msg);
				break;
			case NOTHING:
			default:
				break;
			}
	}
}
