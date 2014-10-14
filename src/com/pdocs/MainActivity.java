package com.pdocs;





import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	RegisterHandler handler;
	Button btn_login;
	EditText txt_uname,txt_password;
	TextView txtv_registerlink;
	SharedPreferences sharedpreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar bar = getActionBar();
		
		btn_login = (Button)findViewById(R.id.buttonSubmit);
		
		txt_uname = (EditText) findViewById(R.id.editTextUsername);
		txt_password = (EditText) findViewById(R.id.editTextPassword);
		txtv_registerlink = (TextView) findViewById(R.id.registerlink);
		
		
		//Saving uname in shared preference file name Mypref:
		sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
		
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler = new RegisterHandler(getBaseContext());
				handler.open();
				
				String uname = txt_uname.getText().toString();
				String password = txt_password.getText().toString();
				
				if(uname.length() > 0 && password.length() > 0){
				
					boolean login_result = handler.login(uname, password);
					if(login_result){
						
						//Saving uname in shared preferences:
						Editor editor = sharedpreferences.edit();
					    editor.putString("uname", uname);  // Storing shared pref.
					    editor.commit();
					    
					    //Changing activity to dashboard activity.
						Intent intentobject = new Intent(getApplicationContext(),DashboardActivity.class);
						startActivity(intentobject);
						finish();
					}else{
						Toast.makeText(getBaseContext(), "Invalid Login Details", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(getBaseContext(), "Username or password is empty", Toast.LENGTH_SHORT).show();
				}
					
				handler.close();
			}
		});
		
		
		
		
		//Register link click event on the login main screen activity:
		
		txtv_registerlink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentObject = new Intent(getApplicationContext(),RegisterActivity.class);
				startActivity(intentObject);
				
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
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
		return super.onOptionsItemSelected(item);
	}
}
