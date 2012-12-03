package com.Alquran.quran;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Alquran.quran.BO.Reader;
import com.Alquran.quran.BO.Util;




public class Main extends Activity implements OnCompletionListener,SeekBar.OnSeekBarChangeListener{
	
//variables
	
	private ProgressDialog pd;
	private String DownloadURL;
	private int SurahNo;
	private int ayat_No;
	private int ayat_No_fromOtherActivity;
	private int SurahDetail[];
	private SQLiteDatabase db;
	private static final String TAG = Main.class.getSimpleName();
	private String Filename;
	private String path;
	private final Context mainContext=this;
	private boolean DatabaseDownloading=false;
	public static Typeface font;
	
	//Media player variables
	private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private MediaPlayer mp;
    private ImageButton play_pause;
	private TextView current_duration;
	private TextView total_duration;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private SeekBar songProgressBar;
    
    
    
	//cursors
	private MainSurahListAdapter cursorMainAdapter; 
	private Cursor cursorMain;
	private Cursor cursorMainSurah;
	private Cursor cursorPlayer;
	
	//cursor adapter
	private SimpleCursorAdapter cursorPlayerAdapter;
	
	
	private LinearLayout popupMenu;
	private RelativeLayout mediaPlayer;
	private ListView listview;
	
	
	//global objects
	   
    private Handler mHandler = new Handler();;
    private Util utils;
    private DbHelper dbHelper = null;
    private Reader reader;
    
    
  
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeGlobalVariables();
        if (Util.checkDataBase())
        {
        	reader = new Reader(mainContext);
        	        	
        	LoadList();
        	setMediaPlayer();
        	            
        }
        else
        {
        	if(Util.hasConnection(mainContext))
        	{
        		downloadDB();
   			}
        	else
        	{
       	  	      showAlert("Network info","Network Not Available. Please connect with internet to get quran database for one time. Once it is downloaded, you can use it offline!");
        	}
        }
    }
    
//Activity Methods
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!DatabaseDownloading)
		{
			if(Util.checkDataBase())
			{
				LoadList();
				LoadPlayerList();
		
			}
		}
		
	}
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "Main activity PAUSED");
		if(mediaPlayer.getVisibility() == View.VISIBLE)
	    {
	    	mediaPlayer.setVisibility(View.GONE);
	    }
	    if(popupMenu.getVisibility() == View.VISIBLE)
	    {
	    	popupMenu.setVisibility(View.GONE);
	    }
	//	if(mp!=null)
	//	{
	//		try
	//		{
	//		if(mp.isPlaying())
	//		{
	//			mp.stop();
	//			mp.release();
	//		}
	//		}
	//		catch(Exception e)
	//		{
	//			Log.e("mediaplayer","error while stopping media in onpause");
	//		}
			
	//	}
	}  		
    @Override
	protected void onStop() {
		
		Log.d(TAG,"Main activity stopped");
		try {
		      super.onStop();

		      if (cursorMainAdapter !=null){
		    	  cursorMainAdapter.getCursor().close();
		    	  cursorMainAdapter= null;
		      }
		      
		      if(cursorPlayerAdapter != null){
		    	  cursorPlayerAdapter.getCursor().close();
		    	  cursorPlayerAdapter=null;
		      }

		      if (cursorMain != null) {
		        cursorMain.close();
		      }
		      if(cursorMainSurah != null )
		      {		    	  
		    	  cursorMainSurah.close();
		      }
		      if(cursorPlayer != null)
		      {
		    	  cursorPlayer.close();
		      }
		      
		      if(mp != null)
		      {
		    	   mp.release();
		      }
		      
		    } catch (Exception error) {
		      
		    	Log.d(TAG, "Cursor closing error : " + error);
		    }
	}
	
   
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	if(mp != null)
    {
  	   mp.release();
    }
	finish();
	
}
    
//User Defined Methods   
public void initializeGlobalVariables()
{
	ayat_No=0;
	ayat_No_fromOtherActivity=0;
    
	dbHelper = new DbHelper(mainContext);
    dbHelper.getReadableDatabase();
    dbHelper.close();
    utils = new Util();
	
	popupMenu = (LinearLayout) findViewById(R.id.popupMenu);
    popupMenu.setVisibility(View.GONE);
    mediaPlayer = (RelativeLayout) findViewById(R.id.mediaPlayer);
    mediaPlayer.setVisibility(View.GONE);
    play_pause = (ImageButton)findViewById(R.id.id_play_pause);
	play_pause.setImageResource(R.drawable.play);
	current_duration = (TextView) findViewById(R.id.current_duration);
	total_duration = (TextView) findViewById(R.id.total_duration);
	btnForward = (ImageButton) findViewById(R.id.id_forward);
	btnBackward = (ImageButton) findViewById(R.id.id_rewind);
    songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
	current_duration.setText("0.00");
	total_duration.setText("0.00");
	
	
    // Listeners
   
    
    
    

}
public void setMediaPlayer()
{
	LoadPlayerList();
    
    play_pause.setOnClickListener(new View.OnClickListener() {
   	 
        @Override
        public void onClick(View arg0) {
            // check for already playing
            if(mp.isPlaying()){
                if(mp!=null){
                    mp.pause();
                    // Changing button image to play button
                    play_pause.setImageResource(R.drawable.play);
                }
            }else{
                // Resume song
                if(mp!=null){
                    mp.start();
                    // Changing button image to pause button
                    play_pause.setImageResource(R.drawable.pause);
                }
            }

        }
    });
    
    /**
     * Forward button click event
     * Forwards song specified seconds
     * */
    btnForward.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // get current song position
        	try
        	{
            int currentPosition = mp.getCurrentPosition();
            // check if seekForward time is lesser than song duration
            if(currentPosition + seekForwardTime <= mp.getDuration()){
                // forward song
                mp.seekTo(currentPosition + seekForwardTime);
            }else{
                // forward to end position
                mp.seekTo(mp.getDuration());
            }
        	}
        	catch(Exception e)
        	{
        		mp.reset();
        	}
        }
    });

    
    btnBackward.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
        	try
        	{
            // get current song position
            int currentPosition = mp.getCurrentPosition();
            // check if seekBackward time is greater than 0 sec
            if(currentPosition - seekBackwardTime >= 0){
                // forward song
                mp.seekTo(currentPosition - seekBackwardTime);
            }else{
                // backward to starting position
                mp.seekTo(0);
            }
        	}
        	catch(Exception e)
        	{
        		mp.reset();
        	}

        }
    });
    

}

public void reciteAayat(View v)
{
		
	String mp3File = getThreeDigitNo(SurahNo) + getThreeDigitNo(cursorMainAdapter.getSelectedAyahNo()) + ".mp3";
	String url = "http://shiaword.com/quran_mp3/minshawi_part/" + mp3File;
	path = Environment.getExternalStorageDirectory().getPath().toString() + "/alQuranData/Reader"+ reader.getSelectedReaderId() +"/Aayat/" + mp3File;
	Log.d("PATH", path);
	checkAayatMp3(url, mp3File);
	
}

public String getThreeDigitNo(int number)
{
	
	return String.format(Locale.getDefault(), "%03d", number);
	
}

//User Defined Methods
public void surahList(View v)
{
	Intent myIntent = new Intent(this, SurahTabs.class);
	startActivity(myIntent);
	
}


public void closePopupMenu(View v)
{
	if(popupMenu.getVisibility() == View.VISIBLE)
	{
		popupMenu.setVisibility(View.GONE);
	}
}

public void recite(View v)
{
	if(mediaPlayer.getVisibility() == View.GONE)
	{
		if(popupMenu.getVisibility() == View.VISIBLE)
		{
			popupMenu.setVisibility(View.GONE);
		}
		mediaPlayer.setVisibility(View.VISIBLE);
	}
	else{
		mediaPlayer.setVisibility(View.GONE);
	}
	
}
public void search(View v)
{
	Intent myIntent = new Intent(this, Search.class);
	startActivity(myIntent);
	
}


public void bookMark(View v)
{    	
	Intent myIntent = new Intent(this, Bookmark.class);
	myIntent.putExtra("CheckDialogEnabled", false);
	startActivity(myIntent);    
}

public void bookMarkWithDialog(View v)
{
	
	int surah[]={SurahNo,cursorMainAdapter.getSelectedAyahNo()};
	
	Intent myIntent = new Intent(mainContext, Bookmark.class);
	myIntent.putExtra("CheckDialogEnabled",true);
	myIntent.putExtra("SurahDetail",surah);
	
	startActivity(myIntent);    
	
}
   

public void settings(View v)
{
	Intent myIntent = new Intent(this, Settings.class);
	startActivity(myIntent);    
}


public void scroll_down(View v)
{
	
	if(cursorMainAdapter.getSelectedPosition()<cursorMainAdapter.getCount())
	{
	    int pos=cursorMainAdapter.getSelectedPosition()+1;
		cursorMainAdapter.setSelectedItem(pos);
		listview.setAdapter(cursorMainAdapter);
		listview.setSelection(pos);
	}

}

public void scroll_up(View v)
{
	if(cursorMainAdapter.getSelectedPosition()>0)
	{
		int pos=cursorMainAdapter.getSelectedPosition()-1;
		cursorMainAdapter.setSelectedItem(pos);
		listview.setAdapter(cursorMainAdapter);
		listview.setSelection(pos);
	
	}
 
}

    public void showAlert(String title, String msg)
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();
	      alertDialog.setTitle(title);
	      alertDialog.setMessage(msg);
	      alertDialog.show();
    }
    public void downloadDB()
    {
    	pd = new ProgressDialog(Main.this);
		DatabaseDownloading=true;
		pd.setMessage("Downloading data from the server for the first time");
		pd.setIndeterminate(false);
		pd.setMax(100);
		
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		DownloadFile downloadFile = new DownloadFile();
		//downloadFile.execute("http://b33h.com/Quran/db/quran_Db.db");
		//downloadFile.execute("http://205.196.122.201/heazzinf9ang/krphna0ac0z639b/quran_Db.db");
		downloadFile.execute("http://shiaword.com/quran_Db.db");
    }
    
    
  public void setMainListViewProperty()
  {
	  	listview = (ListView) findViewById(R.id.listView1);
  		listview.setFocusableInTouchMode(false);
  		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  		listview.setClickable(true);
    	
    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    	  @Override
    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	    
    		cursorMainAdapter.setSelectedItem(position);
    		
			
    	    //listview.setAdapter(cursorMainAdapter);
    	    //listview.setSelection(position);
    	    
    	    if(mediaPlayer.getVisibility() == View.VISIBLE)
    	    {
    	    	mediaPlayer.setVisibility(View.GONE);
    	    }
    	    
    	    if(popupMenu.getVisibility() == View.VISIBLE)
    	    {
    	    	popupMenu.setVisibility(View.GONE);
    	    	
    	    }
    	    else
    	    {
    	    	popupMenu.setVisibility(View.VISIBLE);
    	    	
    	    }
    	      
    	  }
    	  
    	});
    	

  }
    
    public void LoadList()
    {
    	if (!Util.checkDataBase())return;
    	
    	
    	font = Typeface.createFromAsset(getAssets(),mainContext.getResources().getString(R.string.font_face));
    	dbHelper = new DbHelper(mainContext);
    	db = dbHelper.getReadableDatabase();
    	
    	setMainListViewProperty();
    	getSurahDetails();
    	    	
       	cursorMainSurah = getSurahNames(SurahNo);
    	
    	TextView surahEnglish = (TextView) findViewById(R.id.id_SurahEnglish);
    	TextView surahArabic = (TextView) findViewById(R.id.id_SurahArabic);
    	
    	TextView juzNo = (TextView) findViewById(R.id.id_JuzNo);
    	surahEnglish.setText(cursorMainSurah.getString(0).toString());
    	surahArabic.setText(cursorMainSurah.getString(1).toString());
    	juzNo.setText(cursorMainSurah.getString(2).toString());
    	surahArabic.setTypeface(font);
    	juzNo.setTypeface(font);
    	
    	
    	cursorMain = getRecords(SurahNo);
        startManagingCursor(cursorMain);
        Log.d(TAG,"Sucesssfully managed cursorMain");
        
        String[] from = new String[]{"Ayah","Matn"};
        int[] to = new int[]{R.id.id_AyatsNo,R.id.list_content};
        
        Log.d(TAG,"Started creating cursorMainAdapter");
        
        cursorMainAdapter = new MainSurahListAdapter(this, R.layout.mainlvtextstyle, cursorMain, from, to);		
        listview.setAdapter(cursorMainAdapter);
        
        //Close the database connection
        db.close();
        dbHelper.close();
        Log.d(TAG,"Database connection closed");
        if (ayat_No!=0)moveCursortoAyat();
    }
    public void moveCursortoAyat()
    {
    		if(ayat_No_fromOtherActivity!=ayat_No)Log.e("my error","it seems different");
    		cursorMainAdapter.setSelectedItem(ayat_No-1);
    		listview.setAdapter(cursorMainAdapter);
    		listview.setSelection(ayat_No-1);
    		
    }
    public void getSurahDetails()
    {
    	Intent myIntent = getIntent();
    	if (myIntent.hasExtra("SurahDetail"))
    	{
    	 //SurahDetail= myIntent.getIntArrayExtra("SurahDetail");
    	SurahNo=SurahDetail[0];
    	ayat_No=SurahDetail[1];
    	ayat_No_fromOtherActivity=SurahDetail[1];
    	}
    	if(SurahNo ==0 )
    	{
    		SurahNo = 1;
    	}
  
    }
    
    public Cursor getRecords(int SurahNo)
    {
    	return db.rawQuery("Select rowid _id,Ayah, Matn from Quran where Surah = " + SurahNo, null);
    }
    
    public Cursor getSurahNames(int SurahNo)
    {
    	Cursor cursorMainMainSurah = db.rawQuery("SELECT ( _id || '. ' || SurahNameArabic) AS SurahEnglish, ( OriginArabic  || ' ' || SurahName) AS SurahArabic,"
        + "(Juz || ' ' || 'جزء' ) AS JuzNo from SurahList WHERE _id = " + SurahNo, null);
    	
    	startManagingCursor(cursorMainMainSurah);
    	if(cursorMainMainSurah != null)
    	{
    		cursorMainMainSurah.moveToFirst();
    	}
    	return cursorMainMainSurah;
    	
    }
    
    public Cursor getPlaylistRecords()
    {
    	
    	return db.rawQuery("SELECT rowid _id, (_id || '. ' || SurahNameArabic) AS SurahEnglish,(OriginArabic || ' ' || SurahName ) AS SurahArabic"
        + " from SurahList", null);
    }
    
    public String getUrl(int SurahNo)
    {
    	
    	dbHelper = new DbHelper(mainContext);
    	db = dbHelper.getReadableDatabase();
    	
    	Cursor c;
    	c = db.rawQuery("SELECT url_link from reader_mp3 WHERE surah_no = " + SurahNo + " and _id = " + reader.getSelectedReaderId() , null);
    	startManagingCursor(c);
    	if(c != null)
    	{
    		c.moveToFirst();
    	}
    	
    	db.close();
        dbHelper.close();
        
    	return c.getString(0);
    }
    
    public void createDirectories(String url)
    {
        Log.d("DIRECTORY CHECK", "Checking directory " + url + "  exist or not");
        File SDCardRoot = new File(Environment.getExternalStorageDirectory().toString() + url);
        if (!SDCardRoot.exists()) {
            Log.d("DIRECTORY CHECK","Directory doesnt exist creating directory " + Environment.getExternalStorageDirectory().toString());
            boolean outcome = SDCardRoot.mkdirs();

            Log.d("DIRECTORY CHECK","outcome for " + SDCardRoot.getAbsolutePath() + "     " + outcome);
        }
        
        Log.d("DIRECTORY CHECK", "Directory created successfully!");

    }
    
    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }   
 
    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
           public void run() {
        	   try
        	   {
        		   if(mp!=null)
        		   {
        			   long totalDuration = mp.getDuration();
        			   long currentDuration = mp.getCurrentPosition();
 
		               // Displaying Total Duration time
		               total_duration.setText(""+utils.milliSecondsToTimer(totalDuration));
		               // Displaying time completed playing
		               current_duration.setText(""+utils.milliSecondsToTimer(currentDuration));
		 
		               // Updating progress bar
		               int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
		               //Log.d("Progress", ""+progress);
		               songProgressBar.setProgress(progress);
		 
		               // Running this thread after 100 milliseconds
		               mHandler.postDelayed(this, 100);
        		   }
        	   }
           
           catch(Exception e)
           {
        	   mp=null;
           }
           }
        };
 
    /**
     *
     * */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		play_pause.setImageResource(R.drawable.play);
		try
		{
		if(mp.isPlaying())mp.stop();	
		
		mp.seekTo(0);
		}catch(Exception e)
		{
			mp.reset();
			mp= null;
			Log.e("MediaPlayer","error in media player");
		}
		
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		 // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		  mHandler.removeCallbacks(mUpdateTimeTask);
		  try
		  {
	      int totalDuration = mp.getDuration();
	      int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
	 
	      // forward or backward to certain seconds
	      mp.seekTo(currentPosition);
	 
	      // update timer progress again
	      updateProgressBar();
		  }
		  catch(Exception e)
		  {
			  mp.reset();
		  }
	}
	
	 @Override
     public void onDestroy(){
		 super.onDestroy();
		 if(mp != null)
		 {
			 
			 mp.release();
		 }
		
		 if(db != null)
		 {	 
			 if(db.isOpen())
			 {
				 db.close();
			 }
			 
			 if(dbHelper != null)
			 {
				 dbHelper.close();
			 }
		 }
		 
     }
	 
	 public void checkAayatMp3(final String url,final String file)
	 {
		 
		    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        
        	        switch (which){
        	        case DialogInterface.BUTTON_POSITIVE:
        	        	
        	        	String dir = "/alQuranData/Reader" + reader.getSelectedReaderId() + "/Aayat";
        	        	        	        	
        	        	pd = new ProgressDialog(Main.this);
        	        	pd.setMessage("Downloading Aayat " + file + " from server");
        	        	pd.setIndeterminate(false);
        	        	pd.setMax(100);
        	        	pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                		DownloadMP3File downloadMP3File = new DownloadMP3File();
                		downloadMP3File.execute(url,dir,file);
                		break;
        
        	        case DialogInterface.BUTTON_NEGATIVE:
        	            //No button clicked
        	            break;
        	        }
        	    }
        	};
        	AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
    	    if(Util.isFileExistsOnSD(path))
    	    {
    	    	Toast.makeText(mainContext, "Path : " + path, Toast.LENGTH_LONG).show();
    	    	PlayMedia(path);
    	    	
    	    }
    	    else
    	    {
    	    	
    	    	builder.setMessage("Download this Aayat?").setPositiveButton("Yes", dialogClickListener)
    	    	    .setNegativeButton("No", dialogClickListener).show();
    	    	//Toast.makeText(mainContext, "File Does not Exists", Toast.LENGTH_LONG).show();
    	    	
    	    }
    	
	 }
	 

	 
	    public void PlayMedia(String path)
	    {
	    	 mp = new MediaPlayer();
	    	 songProgressBar.setOnSeekBarChangeListener(this); // Important
	    	 mp.setOnCompletionListener(this); // Important
	    	 
	    	 
	    	try {
				mp.reset();
				mp.setDataSource(path);
				mp.prepare();
				mp.start();
				
				//change play image to pause
				play_pause.setImageResource(R.drawable.pause);
				
				 // set Progress bar values
	            songProgressBar.setProgress(0);
	            songProgressBar.setMax(100);
	 
	            // Updating progress bar
	            updateProgressBar();
	            
			} catch (Exception e) {
				Log.e("alquran","Error in media player");
			}
	    	
	    }
	    
	    public String GetFileName(String filename)
	    {		
	    	String name;
	    	int len = filename.length();
	    	name = filename.substring(len - 7);
	    	Log.d("Filename : ", name);
	    	return name;
	    }
	 
	 public void LoadPlayerList()
	 {
		 	
	    	dbHelper = new DbHelper(mainContext);
			db = dbHelper.getReadableDatabase();
			final ListView listview = (ListView) findViewById(R.id.id_SurahList);

			// fetching from database
			cursorPlayer = getPlaylistRecords();
			startManagingCursor(cursorPlayer);
			Log.d(TAG, "Sucesssfully managed cursorPlayer");

			String[] from = new String[] { "SurahEnglish", "SurahArabic" };
			int[] to = new int[] { R.id.id_SurahEnglish, R.id.id_SurahArabic };

			Log.d(TAG, "Started creating cursorPlayerAdapter");

			cursorPlayerAdapter = new PlayListAdapter(this, R.layout.play_list_item,
					cursorPlayer, from, to);
			listview.setAdapter(cursorPlayerAdapter);

			// Close the database connection
			db.close();
			dbHelper.close();
			
			listview.setClickable(true);

	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    	@Override
		    	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		    	
		    		Log.d(TAG, position+1 + "  Selected...");
		    		
		    	    DownloadURL = getUrl(position+1);
		    		Filename = GetFileName(DownloadURL);
		    	    path = Environment.getExternalStorageDirectory() + "/alQuranData/Reader" + reader.getSelectedReaderId() + "/Surah/"+ Filename;

		    	    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		        	    @Override
		        	    public void onClick(DialogInterface dialog, int which) {
		        	        switch (which){
		        	        case DialogInterface.BUTTON_POSITIVE:
		        	        	
		        	        	pd = new ProgressDialog(Main.this);
		        	        	pd.setMessage("Downloading surah from server");
		        	        	pd.setIndeterminate(false);
		        	        	pd.setMax(100);
		        	        	String dir = "/alQuranData/Reader" + reader.getSelectedReaderId() + "/Surah";
		                		//pd = ProgressDialog.show(this, "Downloading data from the server for the first time", "Please wait...");
		        	        	pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		                		DownloadMP3File downloadMP3File = new DownloadMP3File();
		                		downloadMP3File.execute(DownloadURL,dir,Filename);
		                		
		                		//DownloadFile downloadFile = new DownloadFile();
		                		//downloadFile.execute(DownloadURL);
		                		break;
		                	
		        	        case DialogInterface.BUTTON_NEGATIVE:
		        	            //No button clicked
		        	            break;
		        	        }
		        	    }
		        	};
		        	AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
		    	    if(Util.isFileExistsOnSD(path))
		    	    {
		    	    	//Toast.makeText(mainContext, "File Exists", Toast.LENGTH_LONG).show();
		    	    	PlayMedia(path);
		    	    	
		    	    }
		    	    else
		    	    {
		    	    	
		    	    	builder.setMessage("Surah File does not exist. Do You want to Download it?").setPositiveButton("Yes", dialogClickListener)
		    	    	    .setNegativeButton("No", dialogClickListener).show();
		    	    	//Toast.makeText(mainContext, "File Does not Exists", Toast.LENGTH_LONG).show();
		    	    	
		    	    }
		    	}
	    	});
	    		    	
	       
	    }

    
    public class DownloadFile extends AsyncTask<String, Integer, String>
    {
    	
    	//DbHelper db= new DbHelper(mainContext);
   	 @Override
   	    protected String doInBackground(String... sUrl) {
   		 
   		 try
   		 {
   		 
            
            URL url = new URL(sUrl[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            
            OutputStream output = new FileOutputStream("/data/data/com.Alquran.quran/databases/quran_Db.db");

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
          //  mydb.createDataBase();
        }
   		 catch (Exception e) {
       	 
        }
   		
        return null;
   	 }
   	 
   	 
   	 @Override
   	    protected void onPreExecute() {
   	        super.onPreExecute();
   	        pd.show();
   	    }

   	    @Override
   	    protected void onProgressUpdate(Integer... progress) {
   	        super.onProgressUpdate(progress);
   	        pd.setProgress(progress[0]);
   	    }
   	    @Override
   	    protected void onPostExecute(String result) {
   	    	// TODO Auto-generated method stub
   	    	
   	    	super.onPostExecute(result);
   	    	try{
   	    	if(pd.isShowing())pd.dismiss();
   	    	pd=null;
   	    	}catch(Exception e){}
   	    	DatabaseDownloading=false;
   	    	LoadList();
   	    }


    }
    
    public class DownloadMP3File extends AsyncTask<String, Integer, String>
    {
    	
		@Override
    	    protected String doInBackground(String... sUrl) {
    		 
    		 try
    		 {
    		 
             	 URL url = new URL(sUrl[0]);
    		        //create the new connection
    		        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

    		        //set up some things on the connection
    		        urlConnection.setRequestMethod("GET");
    		        urlConnection.setDoOutput(true);

    		        //and connect!
    		        urlConnection.connect();

       		        File SDCardRoot = new File(Environment.getExternalStorageDirectory().toString() + sUrl[1].toString());
    		        
    		        createDirectories(sUrl[1]);
    		        
    		        //create a new file, specifying the path, and the filename
    		        //which we want to save the file as.
    		        File file = new File(SDCardRoot,sUrl[2]);
    		        file.canRead();
    		        //this will be used to write the downloaded data into the file we created
    		        FileOutputStream fileOutput = new FileOutputStream(file);

    		        //this will be used in reading the data from the internet
    		        InputStream inputStream = urlConnection.getInputStream();

    		        //this is the total size of the file
        		    int totalSize = urlConnection.getContentLength();
    		        //variable to store total downloaded bytes
    		        int downloadedSize = 0;

    		        //create a buffer...
    		        byte[] buffer = new byte[1024];
    		        int bufferLength = 0; //used to store a temporary size of the buffer

    		        //now, read through the input buffer and write the contents to the file
    		        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
    		                //add the data in the buffer to the file in the file output stream (the file on the sd card
    		                fileOutput.write(buffer, 0, bufferLength);
    		                //add up the size so we know how much is downloaded
    		                downloadedSize += bufferLength;
    		                //this is where you would do something to report the prgress, like this maybe
    		                
    		              publishProgress((int) (downloadedSize * 100 / totalSize));

    		        }
    		        //close the output stream when done
    		        fileOutput.close();
    		        Log.d("somethig", "Download Completed");

    		//catch some possible errors...
    		} catch (MalformedURLException e) {
    		        e.printStackTrace();
    		        
    		} catch (IOException e) {
    		        e.printStackTrace();
    		        
    		}
    			
    		
         return null;
    	 }
    	 
    	 
    	 @Override
    	    protected void onPreExecute() {
    	        super.onPreExecute();
    	        pd.show();
    	    }

    	    @Override
    	    protected void onProgressUpdate(Integer... progress) {
    	        super.onProgressUpdate(progress);
    	        pd.setProgress(progress[0]);
    	    }
    	    @Override
    	    protected void onPostExecute(String result) {
    	    	// TODO Auto-generated method stub
    	    	super.onPostExecute(result);
    	    	try{
    	   	    	if(pd.isShowing())pd.dismiss();
    	   	    	pd=null;
    	   	    	}catch(Exception e){}

    	    	PlayMedia(path);
    	    	
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
