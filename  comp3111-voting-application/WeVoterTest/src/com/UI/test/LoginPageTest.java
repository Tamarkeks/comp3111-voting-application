package com.UI.test;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.UI.LoginPage;
import com.UI.MainActivity;

public class LoginPageTest extends ActivityInstrumentationTestCase2<LoginPage> {

	private LoginPage loginActivity;
	Button loginButton;
	EditText username, password;
	CheckBox checkbox;
	
	Instrumentation instrument;
	
	public LoginPageTest()						//Test case constructor, Used to find the application and activity for test/
	{
		super(LoginPage.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		loginActivity = (LoginPage) getActivity();	//Get current activity
		instrument = getInstrumentation();
		instrument.waitForIdleSync();
		
		loginButton = (Button) loginActivity.findViewById(com.UI.R.id.login_btn_login);
		username = (EditText) loginActivity.findViewById(com.UI.R.id.login_username);
		password = (EditText) loginActivity.findViewById(com.UI.R.id.login_password);
		checkbox = (CheckBox) loginActivity.findViewById(com.UI.R.id.save_info);
		
		
		getActivity().runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				checkbox.setChecked(false);
				username.setText("");
				password.setText("");
			}
		});
	}
	
	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	
	public void testonCreate_valid()
	{	
		ActivityMonitor activityMonitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		//Input username and password
		TouchUtils.tapView(this, username);
		sendKeys("SHIFT_RIGHT E C H O");
		instrument.waitForIdleSync();
		TouchUtils.tapView(this, password);
		sendKeys("U U U U");
		instrument.waitForIdleSync();
		TouchUtils.clickView(this, loginButton);
		
		MainActivity mainActivity = (MainActivity) instrument.waitForMonitor(activityMonitor);
		assertNotNull(mainActivity);
		instrument.removeMonitor(activityMonitor);
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		instrument.waitForIdleSync();
	}
	
	public void testonCreate_invalid_username()
	{
		ActivityMonitor activityMonitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		//Input username and password
		TouchUtils.tapView(this, username);
		sendKeys("A C H O");
		instrument.waitForIdleSync();
		TouchUtils.tapView(this, password);
		sendKeys("U U U U");
		instrument.waitForIdleSync();
		TouchUtils.clickView(this, loginButton);
		
		MainActivity mainActivity = (MainActivity) instrument.waitForMonitorWithTimeout(activityMonitor, 3000);
		assertNull(mainActivity);
		instrument.removeMonitor(activityMonitor);
		instrument.waitForIdleSync();
	}
	
	public void testonCreate_invalid_password()
	{
		ActivityMonitor activityMonitor = instrument.addMonitor(MainActivity.class.getName(), null, false);
		
		//Input username and password
		TouchUtils.tapView(this, username);
		sendKeys("A C H O");
		instrument.waitForIdleSync();
		TouchUtils.tapView(this, password);
		sendKeys("L A L A L A");
		instrument.waitForIdleSync();
		TouchUtils.clickView(this, loginButton);
		
		MainActivity mainActivity = (MainActivity) instrument.waitForMonitorWithTimeout(activityMonitor, 3000);
		assertNull(mainActivity);
		instrument.removeMonitor(activityMonitor);
		instrument.waitForIdleSync();
	}
	
	/*public void testsaveInfo()
	{
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(LoginPage.class.getName(), null, false);
		
		String test_username = "SHIFT RIGHT E C H O";
		String test_password = "4*u";
		
		TouchUtils.tapView(this, username);
		sendKeys("SHIFT_RIGHT E C H O");
		TouchUtils.tapView(this, password);
		sendKeys("4*U");
		TouchUtils.clickView(this, loginButton);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(mActivity.getCurrentActivity(), com.UI.R.id.LogOff, 0);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LoginPage loginActivity = (LoginPage) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 3000);
		assertNotNull(loginActivity);
	}*/
}
