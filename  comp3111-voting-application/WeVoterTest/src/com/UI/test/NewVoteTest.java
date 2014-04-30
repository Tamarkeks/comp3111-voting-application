package com.UI.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;

import com.INFO.Vote_info;
import com.UI.LoginPage;
import com.UI.MainActivity;
import com.UI.NewVote;

public class NewVoteTest extends ActivityInstrumentationTestCase2<LoginPage> {

	private Instrumentation instrument;
	private Instrumentation.ActivityMonitor monitor;
	Activity currentActivity;
	
	LoginPage login;
	
	public NewVoteTest() {
		super(LoginPage.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setUp() throws Exception
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.setUp();
		
		instrument = getInstrumentation();
		
		//Add monitor for LoginPage and start activity
		monitor = instrument.addMonitor(LoginPage.class.getName(), null, false);
		login = getActivity();
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 2000);
		
		//Uncheck save password
		View currentView = currentActivity.findViewById(com.UI.R.id.save_info);
		TouchUtils.clickView(this, currentView);
		instrument.waitForIdleSync();
		
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
	}
	
	@Override
	public void tearDown() throws Exception
	{
		//Exit from main menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.LogOff, 0);
		
	    super.tearDown();
	}

	public void testTryAdd_andRefresh()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = (MainActivity) instrument.waitForMonitorWithTimeout(monitor, 3000);	
		instrument.removeMonitor(monitor);		
		monitor = instrument.addMonitor(NewVote.class.getName(), null, false);
		
		//Open menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.waitForIdleSync();
		
		//Invoke the menu to create a newVote, get newVote activity, remove monitor, get new monitor for main activity
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.NewVote, 0);
		instrument.waitForIdleSync();
		
		currentActivity = (NewVote) instrument.waitForMonitorWithTimeout(monitor, 3000);
		
		//Setup a test vote page
		View currentView = currentActivity.findViewById(com.UI.R.id.new_title);
		TouchUtils.clickView(this, currentView);
		instrument.sendStringSync("Test_Title");
		instrument.waitForIdleSync();
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_duration);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("1");
		instrument.waitForIdleSync();
		//instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_description);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("a vote topic for testing purposes");
		instrument.waitForIdleSync();
		//instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_option_one);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("test_option_1");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_option_two);
		//TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("test_option_2");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_option_three);
		//TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("test_option_3");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_option_four);
		//TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("test_option_4");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_option_five);
		//TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("5");
		instrument.waitForIdleSync();
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		instrument.waitForIdleSync();
		
		currentView = currentActivity.findViewById(com.UI.R.id.new_add);
		TouchUtils.clickView(this, currentView);
		
		//Back to main activity
		currentActivity = (MainActivity) instrument.waitForMonitorWithTimeout(monitor, 3000);
		instrument.removeMonitor(monitor);
			
		//Invoke the menu action - press refresh key
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.Refresh, 0);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		
		//Check if new vote is there
		
		if(Vote_info.VoteInfo.get(Vote_info.VoteInfo.size() - 1).title.equals("Test_Title"))
			assertTrue(true);
		else
			assertTrue(false);
				
	}
	
}
