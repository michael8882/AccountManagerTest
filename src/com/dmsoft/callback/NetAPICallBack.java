package com.dmsoft.callback;

import org.json.JSONObject;

public interface NetAPICallBack {
	public void succeed(JSONObject responseObj);
	public void failed(JSONObject errorObj);
}
