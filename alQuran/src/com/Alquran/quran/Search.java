package com.Alquran.quran;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.Alquran.quran.BO.Util;


public class Search extends Activity 
{
	private SQLiteDatabase db;
	private SimpleCursorAdapter cursorAdapter; 
	private Cursor cursor;
	private static final String TAG = Search.class.getSimpleName();
	public static Typeface font;
	EditText searchBox;
	ListView listview;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
	        setContentView(R.layout.search);
	        initializeGlobals();
	    //    LoadInList("لرحيم");
	    }
	 
	 @Override
		protected void onPause() {
			overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
			super.onPause();
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

	 public void close(View v)
	 {
		 finish();
	 }
	 public void SearchClick(View v)
	 {
		 //	 LoadInList("لرحيم");
		 LoadInList(searchBox.getText().toString());
	
	 }
	 
	 private void initializeGlobals()
	 {
		 searchBox= (EditText)findViewById(R.id.SearchText);
		 listview = (ListView) findViewById(R.id.searchList);
		 searchBox.setOnEditorActionListener(new OnEditorActionListener() {
			 
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					
					if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
						 LoadInList("لرحيم");
						//LoadInList(searchBox.getText().toString());
		            }
					return false;
				}
		    });
	 }
	 @SuppressWarnings("deprecation")
	public void LoadInList(String SearchKey)
	 {
	    	font = Typeface.createFromAsset(getAssets(), getApplicationContext().getResources().getString(R.string.font_face));
	    	DbHelper dbHelper = new DbHelper(this);
	    	db = dbHelper.getReadableDatabase();
	    
	    	listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		cursor.moveToPosition(position);
	    		//int SurahDetail[] = { cursor.getInt(4),cursor.getInt(5) };
	    		
	    		Util.surahDetail[0] = cursor.getInt(4);
	    	    Util.surahDetail[1] = cursor.getInt(5);
	    		//Intent myIntent = new Intent(getApplicationContext(), Main.class);
	    		 
	    	    //myIntent.putExtra("SurahDetail",SurahDetail);
	    	    //startActivity(myIntent);
	    	
	    	    onPause();
	    	    //finish();
	    	  }
	    	});
	    	
	    	
	    	//fetching from database
	    	cursor = getRecords(SearchKey);
	        //startManagingCursor(cursor);
	        Log.d(TAG,"Sucesssfully managed cursor");
	        
	        String[] from = new String[]{"SurahNames","AyatNo","Matn"};
	        int[] to = new int[]{R.id.id_SurahArabic,R.id.id_AyatsNo,R.id.id_matn};
	        
	        Log.d(TAG,"Started creating Adapter");
	        
	        cursorAdapter = new SimpleCursorAdapter(this, R.layout.search_results, cursor, from, to);		
	        listview.setAdapter(cursorAdapter);
	        
	        //Close the database connection
	        Log.d(TAG,"Database connection closed");
	 }
	 
	public Cursor getRecords(String SearchKey)
	 {
	    	return db.rawQuery("SELECT [SurahList].[_id],([SurahList].[SurahName] || ' ' || [SurahList].[OriginArabic]) as SurahNames," +
	    			"('[' || [SurahList].[Juz] || ':' || [Quran].[Ayah] || ']') as AyatNo, Quran.[Matn] as Matn,[SurahList].[_id] as surah_No,[Quran].[Ayah] as aayat_no from [SurahList],Quran where [SurahList].[_id]=[Quran].[Surah] and  [Quran].[MatnSimple] LIKE  '%"+SearchKey+"%' limit 200", null);
	 
	 }
		 
}
	  
