package com.UI.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.TabActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;

import com.UI.LoginPage;
import com.UI.MainActivity;
import com.UI.NewVote;
import com.UI.sub.tab2;
import com.UI.sub.tab3;
import com.UI.sub.tab4;

public class MainActivityTest extends ActivityInstrumentationTestCase2<LoginPage>
{	
	private Instrumentation instrument;
	private Instrumentation.ActivityMonitor monitor;
	private TabHost tabHost;
	Activity currentActivity;
	
	LoginPage login;
	
	public MainActivityTest() 
	{
		super(LoginPage.class);
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
		//set current activity back to main activity
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
		
		//Setting up a new monitor for MainActivity
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		instrument.addMonitor(monitor);
		
		//Go back to MainActivity
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		currentActivity = (MainActivity) instrument.waitForMonitor(monitor);
		instrument.removeMonitor(monitor);
	}
	
	public void testinitView_tab1()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitor(monitor);
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(tab2.class.getName(), null, false);		
	
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				tabHost = ((TabActivity) currentActivity).getTabHost();
				tabHost.setCurrentTab(1);
			}
		});
		
		//Check if tab2 is hit
		currentActivity = (tab2) instrument.waitForMonitor(monitor);		
		assertNotNull(currentActivity);		//Return true if hit
		
		instrument.removeMonitor(monitor);	//Remove monitor for Dialog box
		
	}
	
	public void testinitView_tab2()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitor(monitor);
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(tab3.class.getName(), null, false);		
	
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				tabHost = ((TabActivity) currentActivity).getTabHost();
				tabHost.setCurrentTab(2);
			}
		});
		
		//Check if tab2 is hit
		currentActivity = (tab3) instrument.waitForMonitor(monitor);		
		assertNotNull(currentActivity);		//Return true if hit
		
		instrument.removeMonitor(monitor);	//Remove monitor for Dialog box
	}
	
	public void testinitView_tab3()
	{
		//Get the current activity which should be MainActivity if the login is successful.
		currentActivity = instrument.waitForMonitor(monitor);
		instrument.removeMonitor(monitor);
		monitor = instrument.addMonitor(tab4.class.getName(), null, false);		
	
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				tabHost = ((TabActivity) currentActivity).getTabHost();
				tabHost.setCurrentTab(3);
			}
		});
		
		//Check if tab2 is hit
		currentActivity = (tab4) instrument.waitForMonitor(monitor);		
		assertNotNull(currentActivity);		//Return true if hit
		
		instrument.removeMonitor(monitor);	//Remove monitor for Dialog box
	}
}
