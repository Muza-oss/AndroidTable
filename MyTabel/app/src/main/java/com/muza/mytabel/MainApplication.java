package com.muza.mytabel;
import android.app.*;
import android.content.*;
import android.widget.*;

public class MainApplication extends Application
{
	private static MainApplication application;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		application = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//Toast.makeText(this, "Application", Toast.LENGTH_SHORT).show();
		// Start service
		//startService(new Intent(this, RecordService.class));
	}

	public static MainApplication getInstance() {
		return application;
	}
	
}
