/*
 * Page where user could add a new vote to database.
 * Activity started from the menu of MainActivity.
 */
package com.UI;

import java.util.Date;

import org.json.JSONException;

import com.DB.access_DB;
import com.INFO.User_info;
import com.INFO.Vote_info;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class NewVote extends Activity implements OnClickListener, OnTouchListener{
	public static Handler handler_request;
	private TextView back_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_vote);
		initBar();
		Button add_vote = (Button)findViewById(R.id.new_add);
		add_vote.setOnClickListener(this);
		
		handler_request = new Handler(){
			public void handleMessage(Message msg) {  
				if(msg.what == 0){
					Toast.makeText(getBaseContext(), "New vote added successfully." ,Toast.LENGTH_LONG).show();
					finish();
				}
				else if(msg.what == 1){
					Toast.makeText(getBaseContext(), "Failed to add new vote!" ,Toast.LENGTH_LONG).show();
				}
			}
		};
	}
	private void initBar(){
    	ActionBar action=getActionBar();
    	action.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
		action.setDisplayShowCustomEnabled(true);
		action.setDisplayShowTitleEnabled(false);
		back_txt =new TextView(getApplicationContext());
		back_txt.setText("BACK");
		back_txt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		back_txt.setClickable(true);
		back_txt.setOnTouchListener(this);
		action.setCustomView(back_txt);
    }
	@Override
	public void onClick(View v) {
		/*start a new thread to convey information to server*/
		new Thread(new Runnable(){
			@Override
			public void run() {
				try_add();
			}
		}).start();
		
	}
	
	private void try_add(){
		EditText New_Title = (EditText)findViewById(R.id.new_title);
		EditText New_Des = (EditText)findViewById(R.id.new_description);
		EditText New_Dur = (EditText)findViewById(R.id.new_duration);
		EditText[] New_Opt = new EditText[5];
		New_Opt[0] = (EditText)findViewById(R.id.new_option_one);
		New_Opt[1] = (EditText)findViewById(R.id.new_option_two);
		New_Opt[2] = (EditText)findViewById(R.id.new_option_three);
		New_Opt[3] = (EditText)findViewById(R.id.new_option_four);
		New_Opt[4] = (EditText)findViewById(R.id.new_option_five);
		
		String title = New_Title.getText().toString();
		String description = New_Des.getText().toString();
		int duration = Integer.parseInt(New_Dur.getText().toString());
		Date d = new Date();
		String[] option = new String[5];
		for(int i = 0; i < 5; i ++) 
			option[i] = New_Opt[i].getText().toString();
		
		Vote_info new_vote = new Vote_info();
		new_vote.user_name = User_info.username;
		new_vote.title = title;
		new_vote.description = description;
		new_vote.option_number = 5;
		for(int i = 0; i < 5; i ++){
			if(option[i].equals("") && new_vote.option_number == 5)
				new_vote.option_number = i;
			new_vote.option_des[i] = option[i];
			new_vote.option_num[i] = 0;
		}
		new_vote.start_time = Vote_info.date_to_string(d);
		d.setTime(System.currentTimeMillis()+(duration * 60*60*1000));
		new_vote.end_time = Vote_info.date_to_string(d);
		
		boolean res;
		try {
			res = access_DB.Add_New_Vote(new_vote);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "Fail to send data to server!" ,Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return;
		}
		
		/*send message to UI to inform if new vote was added sucessfully.*/
		Message m = new Message();
		if(res)
			m.what = 0;
		else
			m.what = 1;
		NewVote.handler_request.sendMessage(m);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == back_txt){
			finish();
			return true;
		}
		return false;
	}
}
