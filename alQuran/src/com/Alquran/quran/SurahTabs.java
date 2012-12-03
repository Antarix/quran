package com.Alquran.quran;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class SurahTabs extends SherlockActivity implements ActionBar.TabListener {
    
	private SQLiteDatabase db;
	private SimpleCursorAdapter cursorAdapter; 
	private Cursor cursor;
	private static final String TAG = SurahTabs.class.getSimpleName();
	public static Typeface font;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.MyFloatingWindow); //Used for theme switching in samples
    	overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.surah_tabs);
        
        String surah = getApplicationContext().getResources().getString(R.string.surahTab);
        String juz = getApplicationContext().getResources().getString(R.string.juzTab);
        
        final ActionBar actionBar = getSupportActionBar(); 
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        
        
        Tab tab = actionBar.newTab();
        tab.setText(surah);
        tab.setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab();
        tab.setText(juz);
        tab.setTabListener(this);
        actionBar.addTab(tab);
        
        
                
    }

    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
	}


	@Override
	protected void onStop() {
		
		Log.d(TAG,"Tab activity stopped!");
		try {
		      super.onStop();

		      if (cursorAdapter !=null){
		    	  cursorAdapter.getCursor().close();
		    	  cursorAdapter= null;
		      }

		      if (cursor != null) {
		        cursor.close();
		      }
		      
		    } catch (Exception error) {
		      /** Error Handler Code **/
		    }
	}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    	
    }

    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
        if(tab.getText().charAt(0)=='S')
        {
        	setContentView(R.layout.surah_list);
        	LoadInListSurah();
        }
        if(tab.getText().charAt(0)=='J')
        {
        	setContentView(R.layout.juz_list);
            LoadInListJuz();
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }
    
    @SuppressWarnings("deprecation")
	public void LoadInListSurah()
    {
    	font = Typeface.createFromAsset(getAssets(), getApplicationContext().getResources().getString(R.string.font_face));
    	DbHelper dbHelper = new DbHelper(this);
    	db = dbHelper.getReadableDatabase();
    	final ListView listview = (ListView) findViewById(R.id.id_SurahList);
    	
    	listview.setClickable(true);
    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    	    
    	    Log.d(TAG, position+1 + "  Selected...");
    	    int pos = position + 1;
    	    int SurahDetail[] = {pos,0};
    	    //String SurahNo = String.valueOf(position + 1);
    	    Intent surahIntent = new Intent(SurahTabs.this, Main.class);
    	    surahIntent.putExtra("SurahDetail", SurahDetail);
    	    startActivity(surahIntent);
    	    finish();   
    	    
    	  }
    	});
    	
    	//fetching from database
    	cursor = getRecordsForSurah();
        startManagingCursor(cursor);
        Log.d(TAG,"Sucesssfully managed cursor");
        
        String[] from = new String[]{"SurahEnglish","SurahArabic","Verses","JuzNo","OriginEnglish"};
        int[] to = new int[]{R.id.id_SurahEnglish,R.id.id_SurahArabic,R.id.id_Verses,R.id.id_JuzNo,R.id.id_OriginEnglish,};
        
        Log.d(TAG,"Started creating Adapter");
        
        cursorAdapter = new SurahListAdapter(this, R.layout.change_surah, cursor, from, to);		
        listview.setAdapter(cursorAdapter);
        
        //Close the database connection
        db.close();
        dbHelper.close();
        Log.d(TAG,"Database connection closed");
    }
    @SuppressWarnings("deprecation")
	public void LoadInListJuz()
    {
    	font = Typeface.createFromAsset(getAssets(), getApplicationContext().getResources().getString(R.string.font_face));
    	DbHelper dbHelper = new DbHelper(this);
    	db = dbHelper.getReadableDatabase();
    	ListView listview = (ListView) findViewById(R.id.id_JuzList);
    	
    	listview.setClickable(true);
    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    	    
    	    Log.d(TAG, position + "  Selected...");
    	    int pos = position + 1;
    	    int SurahDetail[] = {pos,0};
    	    
    	    //String SurahNo = String.valueOf(position + 1);
    	    Intent surahIntent = new Intent(getApplicationContext(), Main.class);
    	    surahIntent.putExtra("SurahDetail", SurahDetail);
    	    startActivity(surahIntent);
    	    finish();
    	    
    	  }
    	});

	  	
    	//fetching from database
    	cursor = getRecordsForJuz();
        startManagingCursor(cursor);
        Log.d(TAG,"Sucesssfully managed cursor");
        
        String[] from = new String[]{"SurahArabic","JuzNo","AyatsNo","SurahNo"};
        int[] to = new int[]{R.id.id_SurahArabic,R.id.id_JuzNo,R.id.id_AyatsNo,R.id.id_SurahNo};
        
        Log.d(TAG,"Started creating Adapter");
        
        cursorAdapter = new JuzListAdapter(this, R.layout.change_juz, cursor, from, to);		
        listview.setAdapter(cursorAdapter);
        
        //Close the database connection
        db.close();
        dbHelper.close();
        Log.d(TAG,"Database connection closed");
    }
    public Cursor getRecordsForSurah()
    {
    	return db.rawQuery("SELECT rowid _id, (_id || '. ' || SurahNameArabic) AS SurahEnglish,(OriginArabic || ' ' || SurahName ) AS SurahArabic,"
        + "('Verses: ' || Ayats) AS Verses,('Juz: ' || Juz) AS JuzNo,OriginEnglish from SurahList", null);
    }
    public Cursor getRecordsForJuz()
    {
    	return db.rawQuery("SELECT rowid _id,( Juz || '  ' || 'جزء' ) AS JuzNo, ( OriginArabic  || ' ' || SurahName) AS SurahArabic," 
    	    	+ "(' .' || _id) AS SurahNo,( Ayats || '  ' || 'آية'  ) AyatsNo from SurahList", null);
    }
    
}
