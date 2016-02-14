package com.dmsoft.communications;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dmsoft.callback.NetAPICallBack;
import com.dmsoft.global.WebConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("DefaultLocale")
public class CommunicationAPIManager {
	
	private static final String UNIREST_STRING_ACCEPT 		= "accept";
	private static final String UNIREST_STRING_JSON 		= "application/json";
	private static final String UNIREST_STRING_CONTENT_TYPE = "Content-Type";
	
	Context mContext;
	public CommunicationAPIManager(Context ctx){
		mContext = ctx;
	}

    private void sendRequestServerByPost(final String strURL, final HashMap<String, Object> params, final NetAPICallBack callback){
    	try{
	        new Thread(){
	        	@Override
	        	public void run(){
	        		HttpResponse<JsonNode> response;
	        		try {
	        			JSONObject js1 = new JSONObject(params);
	        			JsonNode js = new JsonNode(js1.toString());
	        			response = Unirest.post(strURL)
	        					.header(UNIREST_STRING_ACCEPT, UNIREST_STRING_JSON)
	        					.header(UNIREST_STRING_CONTENT_TYPE, UNIREST_STRING_JSON)
	        					.body(js).asJson();
	        			JSONObject result = response.getBody().getObject();
	        			if (callback != null) callback.succeed(result);
	        		} catch (UnirestException e) {
	        			e.printStackTrace();
	        			if (callback != null)callback.failed((JSONObject)null);
	        		}catch(Error err){
	        			err.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}catch(Exception e){
	        			e.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}catch(Throwable th){
	        			th.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}finally{}
	        	}
	        }.start();
    	}catch(Exception e){
    		e.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}catch(Error err){
    		err.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}catch(Throwable th){
    		th.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}finally{}
    }  
    
    private void sendRequestServerByPost(final String strURL, final JSONObject jsObject, final NetAPICallBack callback){
    	try{
	        new Thread(){
	        	@Override
	        	public void run(){
	        		HttpResponse<JsonNode> response;
	        		try {
	        			JSONObject js1 = jsObject;
	        			JSONArray jsArray = new JSONArray();
	        			jsArray.put(js1);
	        			JsonNode js = new JsonNode(js1.toString());
	        			response = Unirest.post(strURL)
	        					.header(UNIREST_STRING_ACCEPT, UNIREST_STRING_JSON)
	        					.header(UNIREST_STRING_CONTENT_TYPE, UNIREST_STRING_JSON)
	        					.body(js).asJson();
	        			if (response.getBody() == null){
	            			if (callback != null) callback.succeed((JSONObject)null);
	        			}else{
	        				JSONObject result = response.getBody().getObject();
	        				if (callback != null) callback.succeed(result);
	        			}
	        		} catch (UnirestException e) {
	        			e.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}catch(Error err){
	        			err.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}catch(Exception e){
	        			e.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}catch(Throwable th){
	        			th.printStackTrace();
	        			if (callback != null) callback.failed((JSONObject)null);
	        		}finally{}
	        	}
	        }.start();
    	}catch(Exception e){
    		e.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}catch(Error err){
    		err.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}catch(Throwable th){
    		th.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}finally{}
    }
    
    public void sendSubmitRequest(String phone, String internationalCode, final NetAPICallBack callback){
    	try{
	        final String  strURL = WebConfig.BASE_URL + WebConfig.CREATE_URL;
	        HashMap<String, Object> params = new HashMap<String, Object>();
	        params.put(WebConfig.PHONE_TAG, phone);
	        params.put(WebConfig.INTERNATIONAL_TAG, internationalCode);
	        sendRequestServerByPost(strURL, params, callback);
    	}catch(Exception e){
    		e.printStackTrace();
			if (callback != null) callback.failed((JSONObject)null);
    	}catch(Error err){
    		err.printStackTrace();
			if (callback != null) callback.failed((JSONObject)null);
    	}catch(Throwable th){
    		th.printStackTrace();
    		if (callback != null) callback.failed((JSONObject)null);
    	}finally{}
    }
}