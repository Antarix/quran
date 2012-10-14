package com.Alquran.quran;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Search extends Activity 
{
	private SQLiteDatabase db;
	private SimpleCursorAdapter cursorAdapter; 
	private Cursor cursor;
	private static final String TAG = Search.class.getSimpleName();
	public static Typeface font;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
	        setContentView(R.layout.search);
	    //    LoadInList("لرحيم");
	    }
	 
	 @Override
		protected void onPause() {
			overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
			super.onPause();
		}
	 
	 public void close(View v)
	 {
		 finish();
	 }
	 public void SearchClick(View v)
	 {
		// Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
		 EditText searchBox= (EditText)findViewById(R.id.SearchText);
		 LoadInList(searchBox.getText().toString());
	//	 LoadInList("لرحيم");
	 }
	 @SuppressWarnings("deprecation")
	public void LoadInList(String SearchKey)
	 {
	    	font = Typeface.createFromAsset(getAssets(), getApplicationContext().getResources().getString(R.string.font_face));
	    	DbHelper dbHelper = new DbHelper(this);
	    	db = dbHelper.getReadableDatabase();
	    	final ListView listview = (ListView) findViewById(R.id.searchList);
	    	
	    	listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		cursor.moveToPosition(position+1);
	    		int SurahDetail[] = { cursor.getInt(4),cursor.getInt(5) }; 
	    		Intent myIntent = new Intent(getApplicationContext(), Main.class);
	    		 
	    	    myIntent.putExtra("SurahDetail",SurahDetail);
	    	    startActivity(myIntent);
	    	    finish();
	    	  }
	    	});
	    	
	    	
	    	
	    	//fetching from database
	    	cursor = getRecords(SearchKey);
	        startManagingCursor(cursor);
	        Log.d(TAG,"Sucesssfully managed cursor");
	        
	        String[] from = new String[]{"SurahNames","AyatNo","Matn"};
	        int[] to = new int[]{R.id.id_SurahArabic,R.id.id_AyatsNo,R.id.id_matn};
	        
	        Log.d(TAG,"Started creating Adapter");
	        
	        cursorAdapter = new SimpleCursorAdapter(this, R.layout.search_results, cursor, from, to);		
	        listview.setAdapter(cursorAdapter);
	        
	        //Close the database connection
	        db.close();
	        dbHelper.close();
	        Log.d(TAG,"Database connection closed");
	 }
	 
	 
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db != null)
		{
			if(db.isOpen())
			{
				db.close();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(db != null)
		{
			if(db.isOpen())
			{
				db.close();
			}
		}
	}

	public Cursor getRecords(String SearchKey)
	 {
	    	return db.rawQuery("SELECT [SurahList].[_id],([SurahList].[SurahName] || ' ' || [SurahList].[OriginArabic]) as SurahNames," +
	    			"('[' || [SurahList].[Juz] || ':' || [Quran].[Ayah] || ']') as AyatNo, Quran.[Matn] as Matn,[SurahList].[_id] as surah_No,[Quran].[Ayah] as aayat_no from [SurahList],Quran where [SurahList].[_id]=[Quran].[Surah] and  [Quran].[MatnSimple] LIKE  '%"+SearchKey+"%' limit 200", null);
	 
	 }
	@SuppressWarnings("deprecation")
	@Override
    public void startManagingCursor(Cursor c) {

        // To solve the following error for honeycomb:
        // java.lang.RuntimeException: Unable to resume activity 
        // java.lang.IllegalStateException: trying to requery an already closed cursor
        if (Build.VERSION.SDK_INT < 11) {
            super.startManagingCursor(c);
        }
    }
	 
}
	  
