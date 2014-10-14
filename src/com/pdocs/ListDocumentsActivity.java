package com.pdocs;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListDocumentsActivity extends Activity {
ListView doclist;
DocumentDetailsHandler handler;
private ArrayAdapter<String> listAdapter ;
final ArrayList<String> list = new ArrayList<String>();
final ArrayList<String> docids = new ArrayList<String>();
SharedPreferences sharedpreferences;
Cursor c;
String uname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_documents);
		
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
		//Saving recently accessed document id in shared preference file name Mypref:
		   
			sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
			 onNewIntent(getIntent());
			if(sharedpreferences.contains("uname")){
				uname = sharedpreferences.getString("uname", "");
			}else{
				
				Intent intentobj = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intentobj);
				finish();
			}
		doclist = (ListView) findViewById(R.id.listdocuments_listview);
		handler = new DocumentDetailsHandler(getBaseContext());
		handler.open();
		c = handler.listDocuments(uname);
		
		
		
		
		while(c.moveToNext()){
			list.add(c.getString(1));
			docids.add(c.getString(0));
		}
		/*boolean res = true;
		   if(res){
		    	Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
		    }else{
		    	Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
		    }
		*/
		/*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
		        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
		        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
		        "Android", "iPhone", "WindowsMobile" };*/
		//list.addAll(values);
		listAdapter = new ArrayAdapter<String>(ListDocumentsActivity.this,R.layout.activity_list_documents
				,R.id.listdocuments_textview,list);
		doclist.setAdapter(listAdapter);
		handler.close();
		
		
		doclist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			    {
			        //String selectedFromList = (doclist.getItemAtPosition(position).toString());
			      
			      	//Toast.makeText(getApplicationContext(), docids.get(position), Toast.LENGTH_SHORT).show();
			      
			      	Editor editor = sharedpreferences.edit();
			      	editor.putString("docid", docids.get(position));  // Storing shared pref.
			      	editor.commit();
			      
				    Intent intentobject = new Intent(getApplicationContext(),
				    		DocumentDetails_Activity.class);
				    startActivity(intentobject);
				    finish();
				  
			    }});
		
		
	}

	
	@Override
	public void onNewIntent(Intent intent){
	    Bundle extras = intent.getExtras();
	    if(extras != null){
	        if(extras.containsKey("NotificationMessage"))
	        {
	            
	            // extract the extra-data in the Notification
	            int msg = extras.getInt("NotificationMessage");
	           // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	           // System.out.println("DsDSD0"+msg);
	         	Editor editor = sharedpreferences.edit();
		      	editor.putString("view", "true");  // Storing shared pref.
		      	editor.commit();
	            
	        }
	    }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK  || keyCode == KeyEvent.KEYCODE_HOME ) {
	       
	    	if(sharedpreferences.contains("view")){
	    		Editor editor = sharedpreferences.edit();
		      	editor.remove("view"); // Storing shared pref.
		      	editor.commit();
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
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
