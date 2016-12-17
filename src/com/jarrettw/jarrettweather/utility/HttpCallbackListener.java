package com.jarrettw.jarrettweather.utility;

import java.io.InputStream;

public interface HttpCallbackListener {
	void onFinish(InputStream in);
	
	void onError(Exception e);
}
