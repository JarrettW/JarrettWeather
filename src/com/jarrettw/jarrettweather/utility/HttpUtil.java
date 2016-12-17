package com.jarrettw.jarrettweather.utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//从服务器端获取省市县的数据,请求链接服务器
public class HttpUtil {
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable(){
			HttpURLConnection connection = null;
			public void run(){
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					if(listener != null){
						//回调onFinish（）方法
						listener.onFinish(in);
					}
				} catch (Exception e) {
					if(listener != null){
						//回调onError方法
						listener.onError(e);
					}
				}finally{
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
