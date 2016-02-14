package com.dmsoft.apitest;

import org.json.JSONObject;

import com.dmsoft.callback.NetAPICallBack;
import com.dmsoft.communications.CommunicationAPIManager;
import com.dmsoft.global.WebConfig;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AccountAuthenticatorActivity {

	private EditText 		mEdtPhoneNumber;
	private EditText 		mEdtInternationalCode;
	private TextView		mTxtPin;
	private ProgressDialog _pd;
	
	private final String USERNAME ="user1";
	private final String ACCOUNTTYPE = "com.dmsoft.apitest.DEMOACCOUNT";
	private final String PASSWORD = "mypassword";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		eventHandlers();		
	}
	
	private void initUI(){
		mEdtPhoneNumber = (EditText)findViewById(R.id.edt_main_phone);
		mEdtInternationalCode = (EditText)findViewById(R.id.edt_main_country);
		mTxtPin = (TextView)findViewById(R.id.txt_main_pin_content);
	}
	
	private void eventHandlers(){
		findViewById(R.id.btn_main_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ifSubmitable()) return;
				if (_pd != null) _pd.dismiss();
				_pd = ProgressDialog.show(MainActivity.this, "", "");
				String strPhone = mEdtPhoneNumber.getText().toString();
				String strInternationalCode = mEdtInternationalCode.getText().toString();
				new CommunicationAPIManager(MainActivity.this).sendSubmitRequest(strPhone, strInternationalCode, new NetAPICallBack(){
					@Override
					public void succeed(final JSONObject responseObj) {
						runOnUiThread(new Runnable(){
							@Override
							public void run(){
								if (_pd != null) _pd.dismiss();
								JSONObject obj = responseObj;
								try{
									if (obj == null){
										Toast.makeText(MainActivity.this, R.string.toast_error, Toast.LENGTH_SHORT).show();
									}else{
										if (obj.has(WebConfig.PIN_TAG)){
											String strPin = obj.optString(WebConfig.PIN_TAG, "");
											mTxtPin.setText(strPin);
										}
										if (obj.has(WebConfig.TOKEN_TAG)){
											String strToken = obj.optString(WebConfig.TOKEN_TAG, "");
											AccountManager accountManager = AccountManager.get(MainActivity.this);
											Account account = new Account(USERNAME, ACCOUNTTYPE);
											accountManager.addAccountExplicitly(account, PASSWORD, null);
											accountManager.setAuthToken(account, ACCOUNTTYPE, strToken);
										    boolean success = accountManager.addAccountExplicitly(account, PASSWORD, null);
										    if(success){
												Toast.makeText(MainActivity.this, R.string.toast_add_token_success, Toast.LENGTH_SHORT).show();										    	
										    }else{
												Toast.makeText(MainActivity.this, R.string.toast_add_token_error, Toast.LENGTH_SHORT).show();
										    }
										}
									}
								}catch(Exception e){
									e.printStackTrace();
									Toast.makeText(MainActivity.this, R.string.toast_error, Toast.LENGTH_SHORT).show();
								}
							}
						});
					}

					@Override
					public void failed(final JSONObject errorObj) {
						runOnUiThread(new Runnable(){
							@Override
							public void run(){
								if (_pd != null) _pd.dismiss();
								JSONObject obj = errorObj;
								if (obj == null){
									Toast.makeText(MainActivity.this, R.string.toast_error, Toast.LENGTH_SHORT).show();
								}else if (obj.has(WebConfig.ERROR_MESSAGE_TAG)){
									Toast.makeText(MainActivity.this, obj.optString(WebConfig.ERROR_MESSAGE_TAG, getString(R.string.toast_error)), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}					
				});
			}
		});
	}
	
	private boolean ifSubmitable(){
		String strPhone = mEdtPhoneNumber.getText().toString();
		if (strPhone == null || strPhone.isEmpty() || strPhone.length() >= 11){
			Toast.makeText(this, R.string.toast_phone_error, Toast.LENGTH_SHORT).show();
			mEdtPhoneNumber.requestFocus();
			return false;
		}
		
		String strInternationalCode = mEdtInternationalCode.getText().toString();
		if (strInternationalCode == null || strInternationalCode.isEmpty() || strInternationalCode.length() != 3){
			Toast.makeText(this, R.string.toast_international_error, Toast.LENGTH_SHORT).show();
			mEdtInternationalCode.requestFocus();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
