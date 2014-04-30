package com.UI.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.TabActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;

import com.INFO.Vote_info;
import com.UI.Comment;
import com.UI.LoginPage;
import com.UI.MainActivity;
import com.UI.sub.tab4;

public class CommentTest extends ActivityInstrumentationTestCase2<LoginPage> {

	private Instrumentation instrument;
	private Instrumentation.ActivityMonitor monitor;
	//private myAdapter_Sub myAdapter;
	private TabHost tabHost;
	Activity currentActivity;
	int position = 0;
	
	LoginPage login;
	
	public static ListView mylistview;
	
	public CommentTest() {
		super(LoginPage.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		instrument = getInstrumentation();
		
		//Add monitor for LoginPage and start activity
		monitor = instrument.addMonitor(LoginPage.class.getName(), null, false);
		login = getActivity();
		instrument.waitForIdleSync();
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 1000);
		
		//Uncheck save password
		View currentView = currentActivity.findViewById(com.UI.R.id.save_info);
		TouchUtils.clickView(this, currentView);
		
		//Tap username editView and send "echo"
		currentView = currentActivity.findViewById(com.UI.R.id.login_username);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("echo");
		instrument.waitForIdleSync();
		
		//Tap password editView and send "uuuu"
		currentView = currentActivity.findViewById(com.UI.R.id.login_password);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("uuuu");		//Send a password string - uuuu	
		instrument.waitForIdleSync();
		
		//Remove the monitor for LoginPage before moving to MainActivity and add a new monitor for MainActivity
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		//Press Login button
		currentView = currentActivity.findViewById(com.UI.R.id.login_btn_login);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();
		
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = (MainActivity) instrument.waitForMonitor(monitor);
		instrument.waitForIdleSync();
		instrument.removeMonitor(monitor);		
		monitor = instrument.addMonitor(tab4.class.getName(), null, false);		
		instrument.waitForIdleSync();
		
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			
			public void run()
			{
				tabHost = ((TabActivity) currentActivity).getTabHost();
				tabHost.setCurrentTab(3);
			}
		});
		
		currentActivity = (tab4) instrument.waitForMonitor(monitor);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentView = currentActivity.findViewById(com.UI.R.id.search_content);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("Test");
		instrument.waitForIdleSync();
		
		currentView = currentActivity.findViewById(com.UI.R.id.search_btn);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();
		
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(Comment.class.getName(), null, false);
		
		currentView = currentActivity.findViewById(com.UI.R.id.search_list);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();
	}
	
	@Override
	public void tearDown() throws Exception
	
	{
		//Exit from main menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.waitForIdleSync();
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.LogOff, 0);
		
	    super.tearDown();
	}

	public void testAddVote_1()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		/*currentActivity = (MainActivity) instrument.waitForMonitorWithTimeout(monitor, 4000);	
		instrument.removeMonitor(monitor);		
		monitor = instrument.addMonitor(tab4.class.getName(), null, false);		
		instrument.waitForIdleSync();
		
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			
			public void run()
			{
				tabHost = ((TabActivity) currentActivity).getTabHost();
				tabHost.setCurrentTab(3);
			}
		});
		
		currentActivity = (tab4) instrument.waitForMonitor(monitor);
		
		View currentView = currentActivity.findViewById(com.UI.R.id.search_content);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("Test");
		instrument.waitForIdleSync();
		
		currentView = currentActivity.findViewById(com.UI.R.id.search_btn);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();
		
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(Comment.class.getName(), null, false);
		
		currentView = currentActivity.findViewById(com.UI.R.id.search_list);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();*/
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 4000);
		instrument.waitForIdleSync();
		instrument.removeMonitor(monitor);
		
		View currentView = currentActivity.findViewById(com.UI.R.id.comment_option_1);
		TouchUtils.tapView(this, currentView);
		instrument.waitForIdleSync();
		
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instrument.waitForIdleSync();
		
		assertTrue(true);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	}
	
	public void testAddVote_2()
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 4000);
		instrument.waitForIdleSync();
		instrument.removeMonitor(monitor);
		
		View currentView = currentActivity.findViewById(com.UI.R.id.comment_option_2);
		TouchUtils.tapView(this, currentView);
		instrument.waitForIdleSync();
		
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instrument.waitForIdleSync();
		
		assertTrue(true);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	}
	
	public void testAddComment()
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 4000);
		instrument.waitForIdleSync();
		instrument.removeMonitor(monitor);
		
		View currentView = currentActivity.findViewById(com.UI.R.id.comment_list);
		TouchUtils.tapView(this, currentView);
		instrument.waitForIdleSync();
		
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instrument.waitForIdleSync();
		instrument.sendStringSync("Testing");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instrument.waitForIdleSync();
		
		assertTrue(true);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	}
	
}
