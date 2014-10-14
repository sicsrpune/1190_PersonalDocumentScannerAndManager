package com.pdocs;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DocumentDetailsHandler {
	public static final String DOCID = "docid";
	public static final String DOCNAME = "docname";
	public static final String DOCDESC = "docdesc";
	public static final String DOCDUE = "docdue";
	public static final String DOCCATEGORY = "doccategory";
	public static final String DOCIMAGEPATH = "docimagepath";
	public static final String DOCREMINDER = "docreminder";
	public static final String DOCUNAME = "uname";
	public static final String TABLE_NAME = "docdetails";
	public static final String DATABASE_NAME = "pdocsdb";
	public static final int  DATABASE_VERSION = 1;
	
	
	
	
 
	DocumentDetailsDatabaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public  DocumentDetailsHandler(Context ctx){
		this.ctx = ctx;
		dbhelper = new DocumentDetailsDatabaseHelper(ctx);
	}
	private static class DocumentDetailsDatabaseHelper extends SQLiteOpenHelper{
		
		public DocumentDetailsDatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
				//db.execSQL(DOCDETAILS_TABLE_CREATE);
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
	
	
	
public DocumentDetailsHandler open(){
		
		db = dbhelper.getWritableDatabase();
		return this;
	}

public long insertNewDocument(String docid,String docname,String docdesc,String docdue,
		String doccategory,String docimagepath,String docreminder,String uname){
				ContentValues content = new ContentValues();
				content.put(DOCID, docid);
				content.put(DOCNAME, docname);
				content.put(DOCDESC, docdesc);
				content.put(DOCDUE, docdue);
				content.put(DOCCATEGORY, doccategory);
				content.put(DOCIMAGEPATH,docimagepath);
				content.put(DOCREMINDER, docreminder);
				content.put(DOCUNAME, uname);
			    return db.insertOrThrow(TABLE_NAME, null, content);
			    
	
}

public long updateDocument(String docid,String docname,String docdesc,String docdue,
		String doccategory,String docimagepath,String docreminder,String uname,String autoid){
				ContentValues content = new ContentValues();
				content.put(DOCID, docid);
				content.put(DOCNAME, docname);
				content.put(DOCDESC, docdesc);
				content.put(DOCDUE, docdue);
				content.put(DOCCATEGORY, doccategory);
				content.put(DOCIMAGEPATH,docimagepath);
				content.put(DOCREMINDER, docreminder);
				content.put(DOCUNAME, uname);
			    return db.update(TABLE_NAME, content, "id="+autoid,null);
			    
	
}


public Cursor listDocuments(String uname){
	
	return db.rawQuery("SELECT docid,docname FROM " + TABLE_NAME + " WHERE uname=?", new String[]{uname});
	
	
}

public Cursor readDocument(String docid,String uname){
	return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE uname=? AND docid=? LIMIT 1", new String[]{uname,docid});
}

public Cursor reminddue(String uname){
	return db.rawQuery("SELECT docname,docdesc,docdue FROM " + TABLE_NAME + " WHERE uname=? and docreminder='Y' and  docdue > strftime('%d-%m-%Y','now') ORDER BY docdue desc LIMIT 3", new String[]{uname});
}



public long deleteDocument(String autoid){
	return db.delete(TABLE_NAME, "id="+autoid, null);
}

	
	public void close(){
		dbhelper.close();
	}
	
}
