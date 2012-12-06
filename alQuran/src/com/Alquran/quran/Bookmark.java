package com.Alquran.quran;


import com.Alquran.quran.BO.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class Bookmark extends Activity {
    
	private SQLiteDatabase bookmark_Db;
	private int [] SurahDetail;
	public static Typeface font;
	private Boolean CheckDialogEnabled;
	private BookmarkListAdapter cursorAdapter; 
	
	final Context context =this;
	
	private static final String TAG = Bookmark.class.getSimpleName();
	

     @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
	        setContentView(R.layout.bookmark);
				  
	        bookmark_Db =new DbHelper(getApplicationContext()).getWritableDatabase();
	
	        Intent bookmarkIntent = getIntent();
	        if (bookmarkIntent.hasExtra("CheckDialogEnabled"))CheckDialogEnabled = bookmarkIntent.getBooleanExtra("CheckDialogEnabled",false);
	        if(bookmarkIntent.hasExtra("SurahDetail"))
	        {
	        	SurahDetail = bookmarkIntent.getIntArrayExtra("SurahDetail");
	
	        }
	        if(CheckDialogEnabled == true)
	        {
	        	
	        	DisplayDialog();
	        	        	
	        }
	        
	        LoadInList();
	       
	        
	}
    public void DisplayDialog()
    {
    	final Dialog bookmarkDialog =new Dialog(this,R.style.BookmarkDialogNoTitle);
    	bookmarkDialog.setContentView(R.layout.bookmark_dialog);
    	final EditText txtBookmark = (EditText)bookmarkDialog.findViewById(R.id.dialog_editText);
    	final Button btnCancel = (Button)bookmarkDialog.findViewById(R.id.dialog_btn_cancel);
		final Button btnAdd = (Button)bookmarkDialog.findViewById(R.id.dialog_btn_add);
		TextView txtAyatNo = (TextView)bookmarkDialog.findViewById(R.id.id_AyatsNo);
		TextView txtSurahNames = (TextView)bookmarkDialog.findViewById(R.id.bookmark_title);
    	
    	 Cursor cursorBookmark=null;
	     cursorBookmark=bookmark_Db.rawQuery("SELECT  (SurahList.SurahName || ' ' || SurahList.OriginArabic) as SurahNames,('[' || SurahList.Juz || ':' || Quran.Ayah || ']') as AyatNo FROM SurahList,Quran where [SurahList].[_id]=[Quran].[Surah] and [SurahList].[_id]=" + SurahDetail[0] + " and Quran.Ayah="+ SurahDetail[1] ,null);
	     startManagingCursor(cursorBookmark);
	     if(cursorBookmark.moveToFirst())
	     {
		     txtSurahNames.setText(cursorBookmark.getString(0));
		     txtAyatNo.setText(cursorBookmark.getString(1));
	     }
	     
	     cursorBookmark.close();
	     btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				bookmarkDialog.dismiss();
				
			}
		}); 
	     
	     btnAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					String bookmarkTitle = "";			
					bookmarkTitle = txtBookmark.getText().toString().trim();
					insertBookmark(SurahDetail[0], SurahDetail[1],bookmarkTitle);
			//		Toast.makeText(getApplicationContext(), "Record inserted successfully " , Toast.LENGTH_LONG).show();
					LoadInList();
					bookmarkDialog.dismiss();
				}
	    });  
    
	     bookmarkDialog.show();
     	 }
	 
    
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		 
		super.onStop();
		
		if(bookmark_Db != null)
		{
			if(bookmark_Db.isOpen())
			{
				bookmark_Db.close();
			}
		}
		 
	}

	

	public void close(View v)
	 {
		
		 finish();
	 }
	 
	 
	   
@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onDestroy();
		if(bookmark_Db != null)
		{
			if(bookmark_Db.isOpen())
			{
				bookmark_Db.close();
			}
		}
	}
	
	
	
	 public void LoadInList()
	 {
		 font = Typeface.createFromAsset(getAssets(), getApplicationContext().getResources().getString(R.string.font_face));
		 final ListView listview = (ListView) findViewById(R.id.bookmarkList);
		 final Cursor tempCursor;
		 	tempCursor = getRecords();
	        startManagingCursor(tempCursor);
	        String[] from = new String[]{"_id","SurahNames","AyatNo","Title"};
	        int[] to = new int[]{R.id.bookmark_id,R.id.id_SurahArabic,R.id.id_AyatsNo,R.id.bookmark_list_title};
	        
	        Log.d(TAG,"Started creating Adapter");
	        
	        cursorAdapter = new BookmarkListAdapter(this, R.layout.bookmark_list_item, tempCursor, from, to);		
	        listview.setAdapter(cursorAdapter);	
	      listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    		  
	    		  cursorAdapter.setSelectedItem(position);
	    		  	//int SurahDetail[] = { tempCursor.getInt(4),tempCursor.getInt(5) };
	    		  	//Log.e("data", SurahDetail[0]+"   "+SurahDetail[1]);
	    		  	
	    		  	Util.surahDetail[0] = tempCursor.getInt(4);
	    		  	Util.surahDetail[1] = tempCursor.getInt(5);
		    		//Intent myIntent = new Intent(getApplicationContext(), Main.class);
		    		
		    	    //myIntent.putExtra("SurahDetail",SurahDetail);
		    	    overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		    	    //startActivity(myIntent);
		    	    finish();
		    	    if(bookmark_Db != null)
		    	    {
			    	    if(bookmark_Db.isOpen())
						{
							bookmark_Db.close();
						}
		    	    }
		    	    
	    	  }
	    	});
	    	
	    	
	        Log.d("df",tempCursor.getCount()+"");
	        
	    //    tempCursor.close();
	        
	 }

	 
	 public Cursor getRecords()
	 {
		 Cursor cursor = null;
		 try {
			 cursor = bookmark_Db.rawQuery("SELECT [Bookmark].[rowid] AS _id,([SurahList].[SurahName] || ' ' || [SurahList].[OriginArabic]) as SurahNames,('[' || [SurahList].[Juz] || ':' || [Quran].[Ayah] || ']') as AyatNo,"
				     +"[Bookmark].[Title] as Title,[SurahList].[_id] as surah_No,[Quran].[Ayah] as aayat_no FROM [SurahList],[Quran],[Bookmark] where [SurahList].[_id]= [Quran].[Surah] and [SurahList].[_id] = [Bookmark].[SurahNo] and [Quran].[Ayah] = [Bookmark].[Ayah] ORDER BY _id", null);
		 
		} catch (Exception e) {
			Log.d("database error", e.toString());
			e.printStackTrace();
		}
				 
		 return cursor;
	    	
	 }
	  	  
	  
	  public void insertBookmark(int SurahNo,int Ayah,String Title)
	  {
		  try 
		  {			
			  bookmark_Db.execSQL("INSERT INTO [Bookmark] ([SurahNo],[Ayah],[Title]) VALUES("+ SurahNo +","+ Ayah +",'" + Title + "')");
		  }
		  catch (Exception e) 
		  {
				e.printStackTrace();
		  }		  		  
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