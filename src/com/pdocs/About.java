package com.pdocs;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends Activity {

	TextView txt_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		final ActionBar bar = getActionBar();
		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
		txt_view = (TextView) findViewById(R.id.about_view);
		
		txt_view.setText("Pdocs is an application to manage documents easily. "
				+ "\n"
				+ "Current Version : 1.0 \n\n"
				+ "Key Features: \n"
				+ "1. Manage and maintain documents. \n"
				+ "2. Add reminders to due dates and get notifications. \n"
				+ "3. Read text from document image using camera icon.\n"
				+ "\n"
				+ "Key Steps : \n"
				+ "1. To Add a new document : \n Go to dashboard -> Add New Document \n"
				+ "2. To Edit/ Delete Document : \n Go to dashboard -> View/Edit Document and select specific document \n"
				+ "3. To Read text from image : \n Click camera icon next to the input field in add/ edit document. \n\n"
				+ "Developed by: \n"
				+ " Pratik Mehta : \n 13030142013@sicsr.ac.in \n"
				+ "Rishabh Bidya : \n 13030142039@sicsr.ac.in \n\n");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
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
		return super.onOptionsItemSelected(item);
	}
}
