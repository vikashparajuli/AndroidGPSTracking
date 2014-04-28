package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SqlDbHelper extends SQLiteOpenHelper {
 public static final String DATABASE_TABLE = "moveon";
 
 public static final String Altitude = "altitude";
// public static final String COMMENT = "comment";

 private static final String SCRIPT_CREATE_DATABASE = "create table "
		 				+ DATABASE_TABLE+ " ("+Altitude+" text primary key );";
 
 public SqlDbHelper(Context context, String name, CursorFactory factory,
   int version) {
  super(context, name, factory, version);
  // TODO Auto-generated constructor stub
 
 }
 
 @Override
 public void onCreate(SQLiteDatabase db) {
  // TODO Auto-generated method stub
  db.execSQL(SCRIPT_CREATE_DATABASE);
 
 }
 
 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  // TODO Auto-generated method stub
  db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
  onCreate(db);
 }
 
}