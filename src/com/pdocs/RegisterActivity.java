package com.pdocs;

import java.util.logging.Logger;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText txt_uname,txt_email,txt_password;
	Button btn_register,btn_back;
	
	RegisterHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
		txt_uname = (EditText) findViewById(R.id.register_editTextUsername);
		txt_email = (EditText) findViewById(R.id.register_editTextEmail);
		txt_password = (EditText) findViewById(R.id.register_editTextPassword);
		btn_register = (Button) findViewById(R.id.register_buttonSubmit);
		btn_back = (Button) findViewById(R.id.register_buttonBack);
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String get_uname = txt_uname.getText().toString();
				String get_email = txt_email.getText().toString();
				String get_password = txt_password.getText().toString();
				
				
				
				if(get_uname.length() > 0 && get_email.length() > 0 && get_password.length() > 0){
						if(isValidEmail(get_email)){
					
							handler = new RegisterHandler(getBaseContext());
							handler.open();
							if(handler.checkuserexists(get_uname)){
								Toast.makeText(getBaseContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
							}else{
								long id = handler.registerUser(get_uname,get_email, get_password);
								
								Toast.makeText(getBaseContext(), "Registered successfully.", Toast.LENGTH_SHORT).show();
								
								
								Intent intentObject = new Intent(getApplicationContext(),MainActivity.class);
								startActivity(intentObject);
								
							}
								handler.close();
						}else{
							Toast.makeText(getBaseContext(), "Invalid email.", Toast.LENGTH_SHORT).show();
						}
				
			}else{
				Toast.makeText(getBaseContext(), "Please fill all details.", Toast.LENGTH_SHORT).show();
			 }
		   }//onClick.
		});//setOnClickListener.
		
		
		
		
		//Back button on registration screen.
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentObject = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intentObject);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) 
	        return false;

	    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}
