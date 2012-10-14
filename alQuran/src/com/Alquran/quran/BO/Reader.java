package com.Alquran.quran.BO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Alquran.quran.DbHelper;
//import android.sax.StartElementListener;

public class Reader {

	private SQLiteDatabase db;
	//private int reader_id;
	private String reader_name;
	//private Boolean isSelected;
	private Context context;

	public Reader(Context cont) {
		context = cont;
	}

	public String getReaderName() {
		return reader_name;
	}

	public Boolean IsReaderSelected(int id) {
		return false;
	}
	

	public int getSelectedReaderId() {
		int r_id = 0;
		DbHelper dbHelper = new DbHelper(context);
		db = dbHelper.getReadableDatabase();
		Cursor tempCursor = null;

		tempCursor = db.rawQuery(
				"select _id from reader where isSelected=1", null);

		if (tempCursor.moveToFirst()) // data?
		{
			Log.d("database", tempCursor.getString(tempCursor
					.getColumnIndex("_id")));
			r_id = tempCursor.getInt(tempCursor
					.getColumnIndex("_id"));
		}
		db.close();
		dbHelper.close();
		tempCursor.close();

		return r_id;
	}


	public String getSelectedReader() {
		String r_name = null;
		DbHelper dbh = new DbHelper(context);
		db = dbh.getReadableDatabase();
		Cursor tempCursor = null;

		tempCursor = db.rawQuery(
				"select reader_name from reader where isSelected=1", null);

		if (tempCursor.moveToFirst()) // data?
		{
			Log.d("database", tempCursor.getString(tempCursor
					.getColumnIndex("reader_name")));
			r_name = tempCursor.getString(tempCursor
					.getColumnIndex("reader_name"));
		}
		db.close();
		dbh.close();
		tempCursor.close();

		return r_name;
	}

	public Boolean setReader(int id) {
		try {
			DbHelper dbh = new DbHelper(context);
			db = dbh.getReadableDatabase();
			db.execSQL("update reader set isSelected=0");
			db.execSQL("update reader set isSelected=1 where _id=" + id);
			Log.d("database", "executed successfully :)");
			db.close();
			dbh.close();
		} catch (SQLException ex) {
			Log.d("database error", "Error in database");
			return false;

		}

		return true;
	}

	public Cursor getAllReaders() {
		DbHelper dbh = new DbHelper(context);
		db = dbh.getReadableDatabase();
		Cursor tempCursor = null;
		tempCursor = db.rawQuery("select rowid _id, reader_name,isSelected from reader",
				null);
		return tempCursor;

	}

}
