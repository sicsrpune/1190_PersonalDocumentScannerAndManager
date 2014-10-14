package com.pdocs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegisterHandler {
	
	
	public static final String UNAME = "uname";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String TABLE_NAME = "user";
	public static final String DATABASE_NAME = "pdocsdb";
	public static final int  DATABASE_VERSION = 1;
	public static final String USER_TABLE_CREATE = "create table "+TABLE_NAME+" (id integer primary key autoincrement,"+UNAME+" text not"
			+ " null,"+EMAIL+" text not null, "+PASSWORD+" text not null);";
	
	public static final String DOCDETAILS_TABLE_CREATE = "create table docdetails (id integer primary key autoincrement,docid text not"
			+ " null,docname text not null, docdesc text not null, docdue text not null,"
					+ " doccategory text not null, docimagepath text not null,docreminder text not null,uname text not null);";
	
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public  RegisterHandler(Context ctx){
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}
	private static class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
				db.execSQL(USER_TABLE_CREATE);
				db.execSQL(DOCDETAILS_TABLE_CREATE);
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
			onCreate(db);
		}
		
	}
			
	public RegisterHandler open(){
		
		db = dbhelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbhelper.close();
	}
	
	public long registerUser(String uname,String email,String password){
		ContentValues content = new ContentValues();
		content.put(UNAME, uname);
		content.put(EMAIL, email);
		content.put(PASSWORD, password);
        return db.insertOrThrow(TABLE_NAME, null, content);
		
	}
	
	public boolean checkuserexists(String uname) throws SQLException
	{
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE uname=?", new String[]{uname});
		if (c != null) {
			if(c.getCount() > 0)
			{
				return true;
			}
		}
		return false;
	} 
	
	
	public boolean login(String uname, String password) throws SQLException
	{
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE uname=? AND password=?", new String[]{uname,password});
		if (c != null) {
			if(c.getCount() > 0)
			{
				return true;
			}
		}
		return false;
	} 
}
