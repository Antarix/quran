package com.Alquran.quran;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Alquran.quran.BO.Reader;
import com.Alquran.quran.BO.Util;

public class Manage extends Activity {
	private ProgressDialog pd;
	private SQLiteDatabase manage_Db;
	private DbHelper dbHelper = null;
	
	// private DownloadSurahAdapter cursorAdapter;
	private Reader reader;
	private String path;

	// private static final String TAG = Manage.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_left, R.anim.hold_x);
		setContentView(R.layout.manage);
		manage_Db = new DbHelper(getApplicationContext()).getReadableDatabase();
		reader = new Reader(getApplicationContext());
		loadDownloadList();

	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_x, R.anim.slide_right);
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();

		if (manage_Db != null) {
			if (manage_Db.isOpen()) {
				manage_Db.close();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onDestroy();
		if (manage_Db != null) {
			if (manage_Db.isOpen()) {
				manage_Db.close();
			}
		}
	}
	
	public void loadDownloadList()
	{	
		int[] idAray = {R.id.layout_1,R.id.layout_2,R.id.layout_3,R.id.layout_4,R.id.layout_5,R.id.layout_6,R.id.layout_7,R.id.layout_8,R.id.layout_9,R.id.layout_10,
				R.id.layout_11,R.id.layout_12,R.id.layout_13,R.id.layout_14,R.id.layout_16,R.id.layout_17,R.id.layout_18,R.id.layout_19,R.id.layout_20,
				R.id.layout_21,R.id.layout_22,R.id.layout_23,R.id.layout_24,R.id.layout_25,R.id.layout_26,R.id.layout_27,R.id.layout_28,R.id.layout_29,R.id.layout_30,
				R.id.layout_31,R.id.layout_32,R.id.layout_33,R.id.layout_34,R.id.layout_35,R.id.layout_36,R.id.layout_37,R.id.layout_38,R.id.layout_39,R.id.layout_40,
				R.id.layout_41,R.id.layout_42,R.id.layout_43,R.id.layout_44,R.id.layout_45,R.id.layout_46,R.id.layout_47,R.id.layout_48,R.id.layout_49,R.id.layout_50,
				R.id.layout_51,R.id.layout_52,R.id.layout_53,R.id.layout_54,R.id.layout_55,R.id.layout_56,R.id.layout_57,R.id.layout_58,R.id.layout_59,R.id.layout_60,
				R.id.layout_61,R.id.layout_62,R.id.layout_63,R.id.layout_64,R.id.layout_65,R.id.layout_66,R.id.layout_67,R.id.layout_68,R.id.layout_69,R.id.layout_70,
				R.id.layout_71,R.id.layout_72,R.id.layout_73,R.id.layout_74,R.id.layout_75,R.id.layout_76,R.id.layout_77,R.id.layout_78,R.id.layout_79,R.id.layout_80,
				R.id.layout_81,R.id.layout_82,R.id.layout_83,R.id.layout_84,R.id.layout_85,R.id.layout_86,R.id.layout_87,R.id.layout_88,R.id.layout_89,R.id.layout_90,
				R.id.layout_91,R.id.layout_92,R.id.layout_93,R.id.layout_94,R.id.layout_95,R.id.layout_96,R.id.layout_97,R.id.layout_98,R.id.layout_99,R.id.layout_100,
				R.id.layout_101,R.id.layout_102,R.id.layout_103,R.id.layout_104,R.id.layout_105,R.id.layout_106,R.id.layout_107,R.id.layout_108,R.id.layout_109,R.id.layout_110,
				R.id.layout_111,R.id.layout_112,R.id.layout_113,R.id.layout_114};
		for (int i = 0; i < idAray.length; i++) {
			LinearLayout lv = (LinearLayout) findViewById(idAray[i]);
			TextView tv = (TextView) lv.getChildAt(0);
			Button btnDelete =(Button)lv.getChildAt(1);
		
			int surahNo = getSurahNoFromString(tv.getText().toString());
			String mp3File = getThreeDigitNo(surahNo) + ".mp3";
			path = Environment.getExternalStorageDirectory().getPath().toString()
					+ "/alQuranData/Reader" + reader.getSelectedReaderId()
					+ "/Surah/" + mp3File;
			if(Util.isFileExistsOnSD(path))
			{
				tv.setTextColor(Color.argb(255,0,171,0));
				btnDelete.setVisibility(View.VISIBLE);
			}
			else
			{
				btnDelete.setVisibility(View.GONE);
			}
		}
		
		idAray = null;
	}

	public void downloadSurah(View v) {

		LinearLayout lv = (LinearLayout) v;
		TextView tv = (TextView) lv.getChildAt(0);
		int surahNo = getSurahNoFromString(tv.getText().toString());
		String mp3File = getThreeDigitNo(surahNo) + ".mp3";
		String url = getUrl(surahNo);
		path = Environment.getExternalStorageDirectory().getPath().toString()
				+ "/alQuranData/Reader" + reader.getSelectedReaderId()
				+ "/Surah/" + mp3File;
		checkSurahMp3(url, mp3File);
		tv.setTextColor(Color.argb(255,0,171,0));

	}

	public int getSurahNoFromString(String surah) {
		int sno;
		try {
			String s = surah.substring(9);
			sno = Integer.parseInt(s);
		} catch (Exception e) {
			sno = 1;
		}
		return sno;
	}

	public String getUrl(int SurahNo) {

		dbHelper = new DbHelper(getApplicationContext());
		manage_Db = dbHelper.getReadableDatabase();

		Cursor c;
		c = manage_Db.rawQuery(
				"SELECT url_link from reader_mp3 WHERE surah_no = " + SurahNo
						+ " and _id = " + reader.getSelectedReaderId(), null);
		startManagingCursor(c);
		if (c != null) {
			c.moveToFirst();
		}

		manage_Db.close();
		dbHelper.close();

		return c.getString(0);
	}

	public void checkSurahMp3(final String url, final String file) {
		if (!Util.isFileExistsOnSD(path)) {

			String dir = "/alQuranData/Reader" + reader.getSelectedReaderId()
					+ "/Surah";

			pd = new ProgressDialog(Manage.this);
			pd.setMessage("Downloading Surah " + file );
			pd.setIndeterminate(false);
			pd.setMax(100);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			DownloadMP3File downloadMP3File = new DownloadMP3File();
			downloadMP3File.execute(url, dir, file);
		} else {
			Toast.makeText(getApplicationContext(), "Surah already Downloaded",
					Toast.LENGTH_LONG).show();
		}

	}
	
	public void deleteSurah(View v)
	{
		
		Toast.makeText(getApplicationContext(), "Delete Surah called", Toast.LENGTH_LONG).show();
	}

	public void deleteAllSurah(View v) {

		File SDCardRoot = new File(Environment.getExternalStorageDirectory()
				.toString()
				+ "/alQuranData/Reader"
				+ reader.getSelectedReaderId() + "/Surah");

		for (File file : SDCardRoot.listFiles()) {

			if (file.isFile()) {
				// File myFile = new File(SDCardRoot.getName());
				if (file.exists()) {
					file.delete();
					Log.d("DELETE OPERATION", "File : " + file.getName()
							+ "  Deleted!");
				}

			}
		}
		Toast.makeText(this, "All surah deleted!", Toast.LENGTH_SHORT).show();

	}

	public void gotoSettings(View v) {
		overridePendingTransition(R.anim.hold_x, R.anim.slide_right);
		finish();
	}

	public Cursor getRecords() {
		Cursor cursor = null;
		try {
			cursor = manage_Db
					.rawQuery(
							"SELECT _id,( 'Surah No  ' ||_id) AS SurahNo FROM SurahList",
							null);

		} catch (Exception e) {
			Log.d("database error", e.toString());
			e.printStackTrace();
		}

		return cursor;

	}

	public String getThreeDigitNo(int number) {
		return String.format("%03d", number);
	}

	public void createDirectories(String url) {
		Log.d("DIRECTORY CHECK", "Checking directory " + url + "  exist or not");
		File SDCardRoot = new File(Environment.getExternalStorageDirectory()
				.toString() + url);
		if (!SDCardRoot.exists()) {
			Log.d("DIRECTORY CHECK",
					"Directory doesnt exist creating directory "
							+ Environment.getExternalStorageDirectory()
									.toString());
			boolean outcome = SDCardRoot.mkdirs();

			Log.d("DIRECTORY CHECK",
					"outcome for " + SDCardRoot.getAbsolutePath() + "     "
							+ outcome);
		}

		Log.d("DIRECTORY CHECK", "Directory created successfully!");

	}

	public class DownloadMP3File extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... sUrl) {

			try {

				URL url = new URL(sUrl[0]);
				// create the new connection
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				// set up some things on the connection
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);

				// and connect!
				urlConnection.connect();

				File SDCardRoot = new File(Environment
						.getExternalStorageDirectory().toString()
						+ sUrl[1].toString());

				createDirectories(sUrl[1]);

				// create a new file, specifying the path, and the filename
				// which we want to save the file as.
				File file = new File(SDCardRoot, sUrl[2]);
				file.canRead();
				// this will be used to write the downloaded data into the file
				// we created
				FileOutputStream fileOutput = new FileOutputStream(file);

				// this will be used in reading the data from the internet
				InputStream inputStream = urlConnection.getInputStream();

				// this is the total size of the file
				int totalSize = urlConnection.getContentLength();
				// variable to store total downloaded bytes
				int downloadedSize = 0;

				// create a buffer...
				byte[] buffer = new byte[1024];
				int bufferLength = 0; // used to store a temporary size of the
										// buffer

				// now, read through the input buffer and write the contents to
				// the file
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					// add the data in the buffer to the file in the file output
					// stream (the file on the sd card
					fileOutput.write(buffer, 0, bufferLength);
					// add up the size so we know how much is downloaded
					downloadedSize += bufferLength;
					// this is where you would do something to report the
					// prgress, like this maybe

					publishProgress((int) (downloadedSize * 100 / totalSize));

				}
				// close the output stream when done
				fileOutput.close();
				Log.d("somethig", "Download Completed");

				// catch some possible errors...
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

			pd.dismiss();

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void startManagingCursor(Cursor c) {

		// To solve the following error for honeycomb:
		// java.lang.RuntimeException: Unable to resume activity
		// java.lang.IllegalStateException: trying to requery an already closed
		// cursor
		if (Build.VERSION.SDK_INT < 11) {
			super.startManagingCursor(c);
		}
	}
	
	
}
