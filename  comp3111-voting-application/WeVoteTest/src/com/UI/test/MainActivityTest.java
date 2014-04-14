package com.UI.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;

import com.UI.LoginPage;
import com.UI.MainActivity;
import com.UI.NewVote;

public class MainActivityTest extends ActivityInstrumentationTestCase2<LoginPage>
{	
	private Instrumentation instrument;
	private Instrumentation.ActivityMonitor monitor;
	Activity currentActivity;
	
	LoginPage login;
	
	public MainActivityTest() 
	{
		super(LoginPage.class);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		instrument = getInstrumentation();
		
		//Add monitor for LoginPage and start activity
		monitor = instrument.addMonitor(LoginPage.class.getName(), null, false);
		login = getActivity();
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 1000);
		
		//Uncheck save password
		View currentView = currentActivity.findViewById(com.UI.R.id.save_info);
		TouchUtils.clickView(this, currentView);
		
		//Tap username editView and send "echo"
		currentView = currentActivity.findViewById(com.UI.R.id.login_username);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("echo");
		
		//Tap password editView and send "uuuu"
		currentView = currentActivity.findViewById(com.UI.R.id.login_password);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("uuuu");		//Send a password string - uuuu	
		
		//Remove the monitor for LoginPage before moving to MainActivity and add a new monitor for MainActivity
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		//Press Login button
		currentView = currentActivity.findViewById(com.UI.R.id.login_btn_login);
		TouchUtils.clickView(this, currentView);
	}
	
	@Override
	public void tearDown() throws Exception
	{
		//Exit from main menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.LogOff, 0);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		
	    super.tearDown();
	}
	
	public void testonOptionsItemSelected_logoff()		//Testing the logoff button on the menu by checking if it returns back to the login screen.
	{        
        //Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitorWithTimeout(monitor, 1000);
		
		//Open menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		
		//Remove monitor for MainActivity and add a LoginPage Monitor
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(LoginPage.class.getName(), null, false);
		
		//Invoke the menu action - press LogOff key
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.LogOff, 0);
		
		//Check if LoginPage is hit
		currentActivity = (LoginPage) instrument.waitForMonitorWithTimeout(monitor, 2000);		
		
		//Return true if hit
		assertNotNull(currentActivity);		
		
		instrument.removeMonitor(monitor);
	}
	
	public void testonOptionsItemSelected_NewVote()
	{	
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitor(monitor);	
		
		//Open menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);		
		
		//Remove monitor for MainActivity
		instrument.removeMonitor(monitor);		
		
		//Open new monitor for NewVote Page
		monitor = instrument.addMonitor(NewVote.class.getName(), null, false);	
		
		//Invoke the menu action - press NewVote key
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.NewVote, 0);		
		
		//Check if NewVote Page is hit
		currentActivity = (NewVote) instrument.waitForMonitor(monitor);		
		assertNotNull(currentActivity);		//Return true if hit
		instrument.removeMonitor(monitor);	//Remove monitor for NewVote
		
		//Go back to MainActivity
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	}
	
	/*public void testonOptionsItemSelected_ChangePwd()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitor(monitor);	
				
		//Open menu
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);			
			
		//Invoke the menu action - press NewVote key
		instrument.invokeMenuActionSync(currentActivity, com.UI.R.id.ChangePwd, 0);		
			
		//Check if NewVote Page is hit
		currentActivity = (MainActivity) instrument.waitForMonitor(monitor);		
		assertNotNull(currentActivity);		//Return true if hit
		instrument.removeMonitor(monitor);	//Remove monitor for NewVote
				
		//Go back to MainActivity
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	}*/
	
	/*public void testonOptionsItemSelected_Refresh()
	{
		Instrumentation instrument = getInstrumentation();		//get instrumentation
		Instrumentation.ActivityMonitor activityMonitor = instrument.addMonitor(LoginPage.class.getName(), null, false);		//add a monitor for the mainActivity page
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName(instrument.getTargetContext(), LoginPage.class.getName());
		instrument.startActivitySync(intent);		//Launch the loginPage activity
		
		Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 1000);		//Get current activity, which should be LoginPage
		
		View currentView = currentActivity.findViewById(com.UI.R.id.save_info);		//Uncheck the save password option to prevent automated input errors
		TouchUtils.clickView(this, currentView);
		
		currentView = currentActivity.findViewById(com.UI.R.id.login_username);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("echo");		//Send a username string - echo
		
		currentView = currentActivity.findViewById(com.UI.R.id.login_password);
		TouchUtils.tapView(this, currentView);
		instrument.sendStringSync("uuuu");		//Send a password string - uuuu
		
		instrument.removeMonitor(activityMonitor);		//remove the monitor for LoginPage before moving into the MainActivity
		activityMonitor = instrument.addMonitor(MainActivity.class.getName(), null, false);		//Add a MainActivity monitor to check for a MainActivity hit later
		
		currentView = currentActivity.findViewById(com.UI.R.id.login_btn_login);
		TouchUtils.clickView(this, currentView);		//Click the login button to move into MainActivity
		
		currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 1000);		//Get the current activity which should be MainActivity if the login is successful.
		
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);		//Open menu
		
		instrument.removeMonitor(activityMonitor);
		activityMonitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		getInstrumentation().invokeMenuActionSync(currentActivity, com.UI.R.id.Refresh, 0);		//Invoke the menu action - press Refresh key
		
		currentActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 2000);		//Check if MainActivity is hit
		assertNotNull(currentActivity);		//Return true if hit
	}*/
	
	
	
}
