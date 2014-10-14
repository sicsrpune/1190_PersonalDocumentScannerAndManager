package com.pdocs;



import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends Activity {
    TextView txtv_newdoc,txtv_viewdoc;
	SharedPreferences sharedpreferences;
	DocumentDetailsHandler handler;
	String uname;
	Cursor c;
	final ArrayList<String> notification_name = new ArrayList<String>();
	final ArrayList<String> notification_desc = new ArrayList<String>();
	final ArrayList<String> notification_duedates = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
		
		checkNotifications();
		
		final ActionBar bar = getActionBar();
		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
		
		txtv_newdoc = (TextView) findViewById(R.id.dashboard_textviewNewDoc);
		txtv_viewdoc = (TextView) findViewById(R.id.dashboard_textViewDoc);
		
		
		if(sharedpreferences.contains("view")){
    		Editor editor = sharedpreferences.edit();
	      	editor.remove("view"); // Storing shared pref.
	      	editor.commit();
    	}
		
		
		txtv_newdoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sharedpreferences.contains("docid")){
					Editor editor = sharedpreferences.edit();
			      	editor.remove("docid"); // Storing shared pref.
			      	editor.commit();
				}
				Intent intentobject = new Intent(getApplicationContext(),DocumentDetails_Activity.class);
				startActivity(intentobject);
				
			}
		});
		
		txtv_viewdoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentobject = new Intent(getApplicationContext(),ListDocumentsActivity.class);
				startActivity(intentobject);
				
			}
		});
		
	}
	
	public void checkNotifications(){
		handler = new DocumentDetailsHandler(getBaseContext());
		handler.open();
		
		if(sharedpreferences.contains("uname")){
			uname = sharedpreferences.getString("uname", "");
		}
		c = handler.reminddue(uname);
		while(c.moveToNext()){
			notification_name.add(c.getString(0));
			notification_desc.add(c.getString(1));
			notification_duedates.add(c.getString(2));
			
     	}
		
		
		handler.close();
		for(int i=0;i<notification_name.size();i++){
			
		
			
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
			Intent notificationIntent = new Intent(getBaseContext(), ListDocumentsActivity.class);
			notificationIntent.putExtra("NotificationMessage", i);
			
		    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		    
		 
		   
		    
			mBuilder.setSmallIcon(R.drawable.ic_action_about);
			mBuilder.setContentTitle(notification_name.get(i));
			mBuilder.setContentText("Due on: "+notification_duedates.get(i));
			mBuilder.setPriority(5 - i);
			mBuilder.setContentIntent(intent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				    
				// notificationID allows you to update the notification later on.
			    int notificationID = i;
				mNotificationManager.notify(notificationID, mBuilder.build());
		
			
		}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
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
		
		if(id == R.id.about_icon){
			//Log.d("gone", "goa");
			
			Intent int_about = new Intent(getApplicationContext(),About.class);
			startActivity(int_about);
		}
		
		if(id == R.id.logout_icon){
			if(sharedpreferences.contains("uname")){
				Editor editor = sharedpreferences.edit();
		      	editor.remove("uname"); // Storing shared pref.
		      	editor.commit();
			}
			if(sharedpreferences.contains("docid")){
				Editor editor = sharedpreferences.edit();
		      	editor.remove("docid"); // Storing shared pref.
		      	editor.commit();
			}
			if(sharedpreferences.contains("view")){
				Editor editor = sharedpreferences.edit();
		      	editor.remove("view"); // Storing shared pref.
		      	editor.commit();
			}
			Intent intentObject = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(intentObject);
			
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
