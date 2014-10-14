package com.pdocs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

public class DocumentDetails_Activity extends Activity {

	String [] categories;
	Spinner sp;
	DocumentDetailsHandler handler;
	Button btn_submit,btn_delete;
	EditText docid,docname,docdesc,docdue;
	ImageButton btn_scan;
	CheckBox docreminder;
	SharedPreferences sharedpreferences;
	String pull_docid,pull_uname,autoid;
	Cursor c;
	private static final String DATE_PATTERN = 
	          "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
	 
	
	boolean flag = false;
	
	public static final String PACKAGE_NAME = "com.pdocs";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/Pdocs/";

	// The trained data file is in the assets folder.
	public static final String lang = "eng";
	protected String _path;
	protected boolean _taken;
	private static final String TAG = "DocumentDetails.java";
	
	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Teserract creation of directory
				String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

				for (String path : paths) {
					File dir = new File(path);
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
							return;
						} else {
							Log.v(TAG, "Created directory " + path + " on sdcard");
						}
					}

				}
				
				if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
					try {

						AssetManager assetManager = getAssets();
						InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
						//GZIPInputStream gin = new GZIPInputStream(in);
						OutputStream out = new FileOutputStream(DATA_PATH
								+ "tessdata/" + lang + ".traineddata");

						// Transfer bytes from in to out
						byte[] buf = new byte[1024];
						int len;
						//while ((lenf = gin.read(buff)) > 0) {
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						in.close();
						//gin.close();
						out.close();
						
						Log.v(TAG, "Copied " + lang + " traineddata");
					} catch (IOException e) {
						Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
					}
				}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_details);
		_path = DATA_PATH + "/ocr.jpg";
		
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8800")));
		
	
		
	
		
		

		
		sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
		
		if(sharedpreferences.contains("docid")){
			pull_docid = sharedpreferences.getString("docid", "");
			flag = true;
		}
		
		if(sharedpreferences.contains("uname")){
			pull_uname = sharedpreferences.getString("uname", "");
		}
	
		
		
		
		
		sp = (Spinner) findViewById(R.id.spinnerCategory);
	    categories = getResources().getStringArray(R.array.categories);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categories);
	    
	    sp.setAdapter(adapter);
	    
	    
	    docid = (EditText) findViewById(R.id.editTextDocumentId);
	    docname = (EditText) findViewById(R.id.editTextDocumentName);
	    docdesc = (EditText) findViewById(R.id.editTextDocumentDescription);
	    docdue = (EditText) findViewById(R.id.editTextDocumentDue);
	    docreminder = (CheckBox)findViewById(R.id.checkBoxReminder);
	    btn_submit = (Button) findViewById(R.id.document_submit);
	    btn_delete = (Button) findViewById(R.id.document_delete);
	    btn_delete.setVisibility(View.GONE);
	    btn_scan = (ImageButton) findViewById(R.id.imageButton_scan);
	  if(flag){
		  
		  btn_delete.setVisibility(View.VISIBLE);
		  
			handler = new DocumentDetailsHandler(getBaseContext());
			handler.open();
			
			c = handler.readDocument(pull_docid, pull_uname);
			
			while(c.moveToNext()){
				autoid = c.getString(0);
				docid.setText(c.getString(1));
				docname.setText(c.getString(2));
				docdesc.setText(c.getString(3));
				docdue.setText(c.getString(4));
				int index = 0;
				for(int i=0;i<categories.length;i++){
					
					
					if(categories[i].equals(c.getString(5))){
						
						
						index = i;
					}
				}
				
				if(c.getString(7).equals("Y")){
					docreminder.setChecked(true);
				}
				
				//Toast.makeText(getBaseContext(), index, Toast.LENGTH_SHORT).show();
				sp.setSelection(index);
			}
			
			handler.close();
			
			if(sharedpreferences.contains("view")){
				docid.setFocusable(false);
				docid.setBackgroundColor(0);
				docname.setFocusable(false);
				docname.setBackgroundColor(0);
				docdesc.setFocusable(false);
				docdesc.setBackgroundColor(0);
				docdue.setFocusable(false);
				docdue.setBackgroundColor(0);
				docreminder.setVisibility(View.INVISIBLE);
				btn_submit.setVisibility(View.INVISIBLE);
				btn_delete.setVisibility(View.INVISIBLE);
				btn_scan.setVisibility(View.INVISIBLE);
				sp.setEnabled(false);
				sp.setBackgroundColor(0);
			}
		}
	    
	    btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler = new DocumentDetailsHandler(getBaseContext());
				handler.open();
				
				String docid_val = docid.getText().toString();
				String docname_val = docname.getText().toString();
				String docdesc_val = docdesc.getText().toString();
				String docdue_val = docdue.getText().toString();
				String doccategory_val = sp.getSelectedItem().toString();
				String docimagepath = "c://";
				
				String docreminder_val = "";
				
				//Checking if reminder checkbox is checked:
				if(docreminder.isChecked()){
					docreminder_val = "Y";
				}else{
					docreminder_val = "N";
				}
				
				String uname = "";
				//Storing currently logged in uname into uname variable from shared preferences.
				if(sharedpreferences.contains("uname")){
					uname = sharedpreferences.getString("uname","");
				}
				if(flag){
					if(docid_val.trim().length() > 0 && docname_val.trim().length() > 0){	
						
						boolean datevalidation  = docdue_val.matches(DATE_PATTERN);
						
						if(!datevalidation){
							Toast.makeText(getBaseContext(), "Date must be correct format.",
									Toast.LENGTH_SHORT).show();
							
						}
                        
						if(datevalidation){
							//docdue_val = docdue_val.replaceAll("[\\s\\-()]", "");
							long id = handler.updateDocument(docid_val, docname_val, docdesc_val, docdue_val
									, doccategory_val,docimagepath,docreminder_val,uname,autoid);
							Toast.makeText(getBaseContext(), "Document updated successfully.", Toast.LENGTH_SHORT).show();
						  }
					}else{
						Toast.makeText(getBaseContext(), "Please fill the document id and name.",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					
				if(docid_val.trim().length() > 0 && docname_val.trim().length() > 0){	
				
					boolean datevalidation  = docdue_val.matches(DATE_PATTERN);
					
					if(!datevalidation){
						Toast.makeText(getBaseContext(), "Date must be correct format.",
								Toast.LENGTH_SHORT).show();
						
					}
					if(datevalidation){
						long id = handler.insertNewDocument(docid_val, docname_val, docdesc_val, docdue_val
								, doccategory_val,docimagepath,docreminder_val,uname);
						
						Toast.makeText(getBaseContext(), "Document inserted successfully.",
								Toast.LENGTH_SHORT).show();
						
						docid.setText("");
						docname.setText("");
						docdue.setText("");
						docdesc.setText("");
						docreminder.setChecked(false);
						sp.setSelection(0);
					}
				}else{
						Toast.makeText(getBaseContext(), "Please fill the document id and name.",
								Toast.LENGTH_SHORT).show();
				  }
				}
				handler.close();
				
			}
		});
	    
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    builder.setTitle("Confirm Delete");
			    builder.setMessage("Are you sure you want to delete?");

			    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing but close the dialog

						handler = new DocumentDetailsHandler(getBaseContext());
							handler.open();
							
							handler.deleteDocument(autoid);
							
							Toast.makeText(getBaseContext(), "Document deleted.", Toast.LENGTH_SHORT).show();
							handler.close();
							Intent intentobj = new Intent(getApplicationContext(),ListDocumentsActivity.class);
							startActivity(intentobj);
							finish();
			            
			        }

			    });

			    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing
			            dialog.dismiss();
			        }
			    });

			    AlertDialog alert = builder.create();
			    alert.show();
				
			
			}
		});
	    
	    
		 btn_scan.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.v("DDDDD", "Starting Camera app");
						startCameraActivity();
					}
				});
	    
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
	
	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			onPhotoTaken();
		} else {
			Log.v(TAG, "User cancelled");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(DocumentDetails_Activity.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(DocumentDetails_Activity.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ) {
	       
	    	if(sharedpreferences.contains("view")){
	    		Editor editor = sharedpreferences.edit();
		      	editor.remove("view"); // Storing shared pref.
		      	editor.commit();
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}

	protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		// _image.setImageBitmap( bitmap );
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			//recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			docname.setText(docname.getText().toString().length() == 0 ? recognizedText : docname.getText() + " " + recognizedText);
			docname.setSelection(docname.getText().toString().length());
		}
		
		// Cycle done.
	}

}
