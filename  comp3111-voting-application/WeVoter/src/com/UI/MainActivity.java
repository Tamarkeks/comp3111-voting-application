/*
 * Main page extends from TabActivity, divided into four parts.
 */
package com.UI;

import org.json.JSONException;

import com.UI.sub.*;

import com.DB.access_DB;
import com.INFO.User_info;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{
	public static Handler myHandler;
	private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		myHandler = new Handler(){
			public void handleMessage(Message msg) {  
				int res = msg.what;
				if(res == 0)
    				Toast.makeText(getBaseContext(), "Password changed successfully." ,Toast.LENGTH_LONG).show();
				else if(res == 1)
    				Toast.makeText(getBaseContext(), "Fail to change password!" ,Toast.LENGTH_LONG).show();
    		}
		};
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.LogOff:
    		myLogOff();
    		return true;
    	case R.id.ChangePwd:
    		myChangePwd();
    		return true;
    	case R.id.NewVote:
    		myNewVote();
    		return true;
    	case R.id.Refresh:
    		myRefresh();
    		return true;
    	default:
			return super.onOptionsItemSelected(item);
    	}
	}
    public static void myRefresh(){
    	new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					access_DB.Get_Vote(1);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		}).start();
    }
    private void myChangePwd(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle(User_info.username);
    	alert.setMessage("Input new password");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this);
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		final String value = input.getText().toString();
    		new Thread(new Runnable(){
    			@Override
    			public void run() {
    				try {
    					access_DB.Change_Password(value);
    				} catch (JSONException e) {
    					Toast.makeText(getBaseContext(), "Unexpected exception in myChangePwd()!" ,
    							Toast.LENGTH_LONG).show();
    					e.printStackTrace();
    				}	
    			}
    		}).start();
    	}
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			// Canceled.
    		}
    	});

    	alert.show();
    }
    private void myLogOff(){
    	finish();
    }
    private void myNewVote(){
    	Intent in = new Intent(this, NewVote.class);
		startActivity(in);
    }
    private void initView(){
    	tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent=new Intent().setClass(this, tab1.class);
        spec=tabHost.newTabSpec("ongoing").setIndicator("ongoing").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,tab2.class);
        spec=tabHost.newTabSpec("over").setIndicator("over").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, tab3.class);
        spec=tabHost.newTabSpec("my").setIndicator("my").setContent(intent);
        tabHost.addTab(spec);
       
        intent=new Intent().setClass(this, tab4.class);
        spec=tabHost.newTabSpec("search").setIndicator("search").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}
