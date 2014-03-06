/*
 * The login page.
 */
package com.UI;
import org.json.JSONException;

import com.DB.*;
import com.INFO.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginPage extends Activity implements OnClickListener{
	static int res = -1;
	static String username = "";
	static String password = "";
	EditText usr = null;
	EditText pwd = null;
	ProgressBar bar = null;
	public static Handler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		getInfo();
		
		myHandler = new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case -1:
					Toast.makeText(getBaseContext(), "Can not connect to database!" ,Toast.LENGTH_LONG).show();
					bar.setVisibility(View.INVISIBLE);
					break;
				case 0:
					User_info.username = username;
					User_info.password = password;
					get_Vote_Info();                  //account matches, then get the votes information.
					break;
				case 1:
					Toast.makeText(getBaseContext(), "Wrong Password" ,Toast.LENGTH_LONG).show();
					bar.setVisibility(View.INVISIBLE);
					break;
				case 2:
					Toast.makeText(getBaseContext(), "No Such Username Found!" ,Toast.LENGTH_LONG).show();
					bar.setVisibility(View.INVISIBLE);
					break;
				case 3:
					Toast.makeText(getBaseContext(), "Matched!" ,Toast.LENGTH_LONG).show();
					saveInfo(username, password);                                              //save the password if required
					Intent in = new Intent(LoginPage.this, MainActivity.class);
					startActivity(in);
					bar.setVisibility(View.INVISIBLE);
					break;
				}
			}
		};
	}
	private void initView(){
		setContentView(R.layout.activity_login_page);
		usr = (EditText) findViewById(R.id.login_username);
		pwd = (EditText) findViewById(R.id.login_password);
		bar = (ProgressBar)findViewById(R.id.login_bar);
		bar.setVisibility(View.INVISIBLE);
		Button login = (Button) findViewById(R.id.login_btn_login);
		login.setOnClickListener(this);
	}
	public void get_Vote_Info(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					access_DB.Get_Vote(0);
				} catch (JSONException e) {
					Toast.makeText(getBaseContext(), "Unexpected exception in LoginPage.get_Vote_info()!" ,
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}).start();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		/************make the bar visible*************************/
		
		bar.setVisibility(View.VISIBLE);
		
		/***********get the information user inputs*****************/
		username = usr.getText().toString();
		password = pwd.getText().toString();
		
		/*************for debug and test*************/
		if(username.equals("comp3111")){
			User_info.username = "comp3111";
			User_info.password = "sung";
			Toast.makeText(getBaseContext(), "Matched!" ,Toast.LENGTH_LONG).show();
			Intent in = new Intent(LoginPage.this, MainActivity.class);
			startActivity(in);
			return;
		}
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					access_DB.Account_Vertify(username, password);
				} catch (JSONException e) {
					Toast.makeText(getBaseContext(), "Unexpected exception in LoginPage.onClick()!" ,
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void saveInfo(String u, String p){
		CheckBox cb = (CheckBox)findViewById(R.id.save_info);
		if(cb.isChecked()){
			SharedPreferences myInfo = getSharedPreferences("myInfo", 0);
			SharedPreferences.Editor myEditor = myInfo.edit();
			myEditor.putString("name", u);
			myEditor.putString("pwd", p);
			myEditor.commit();
		}
	}
	public void getInfo(){
		SharedPreferences myInfo = getSharedPreferences("myInfo", 0);
		String username = myInfo.getString("name","");
		String password = myInfo.getString("pwd","");
		usr.setText(username);
		pwd.setText(password);
	}
}
