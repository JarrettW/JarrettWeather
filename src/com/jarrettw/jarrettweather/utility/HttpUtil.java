package com.jarrettw.jarrettweather.utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//�ӷ������˻�ȡʡ���ص�����,�������ӷ�����
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
						//�ص�onFinish��������
						listener.onFinish(in);
					}
				} catch (Exception e) {
					if(listener != null){
						//�ص�onError����
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
