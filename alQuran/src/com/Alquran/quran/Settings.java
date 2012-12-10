package com.Alquran.quran;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Alquran.quran.BO.Reader;

public class Settings extends Activity {

	// private SQLiteDatabase settings_Db;
	private String TAG = Settings.class.getSimpleName();
	private TextView readerName;
	private Reader reader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.settings);
		reader = new Reader(getApplicationContext());
		readerName = (TextView) findViewById(R.id.selected_reader_name);
		readerName.setText(reader.getSelectedReader());
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();

	}

	public void close(View v) {
		finish();
	}

	public void manageFiles(View v) {
		Intent manageIntent = new Intent(this, Manage.class);
		startActivity(manageIntent);
	}

	public void gotoAbout(View v) {
		Intent WebviewIntent = new Intent(this, WebviewActivity.class);
		WebviewIntent.putExtra("WebActivityExtra", "About");
		startActivity(WebviewIntent);
	}

	public void gotoHelp(View v) {
		Intent WebviewIntent = new Intent(this, WebviewActivity.class);
		WebviewIntent.putExtra("WebActivityExtra", "Help");
		startActivity(WebviewIntent);
	}

	public void selectRecitation(View v) {
		DisplayDialog();
	}

	public void selectAyahReader(View v) {
		final Dialog selectAyahReaderDialog = new Dialog(this,
				R.style.BookmarkDialogNoTitle);

		selectAyahReaderDialog.setContentView(R.layout.select_reader_dialog);
		TextView textTitle = (TextView)selectAyahReaderDialog
				.findViewById(R.id.dialog_title);
		Button btnCancel = (Button) selectAyahReaderDialog
				.findViewById(R.id.dialog_btn_cancel);
		textTitle.setText("Select Ayah Reader");
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectAyahReaderDialog.dismiss();

			}
		});
		selectAyahReaderDialog.show();
	}

	@SuppressWarnings("deprecation")
	public void DisplayDialog() {
		final Dialog selectReaderDialog = new Dialog(this,
				R.style.BookmarkDialogNoTitle);

		selectReaderDialog.setContentView(R.layout.select_reader_dialog);
		TextView textTitle = (TextView)selectReaderDialog
				.findViewById(R.id.dialog_title);
		Button btnCancel = (Button) selectReaderDialog
				.findViewById(R.id.dialog_btn_cancel);
		textTitle.setText("Select Surah Reader");
		final ReaderListAdapter cursorAdapter;
		ListView listview = (ListView) selectReaderDialog
				.findViewById(R.id.id_ReaderList);
		final Cursor tempCursor;
		reader = new Reader(getApplicationContext());
		tempCursor = reader.getAllReaders();
		startManagingCursor(tempCursor);
		String[] from = new String[] { "reader_name" };
		int[] to = new int[] { R.id.radioReaderName };

		Log.d(TAG, "Started creating Reader Adapter");

		cursorAdapter = new ReaderListAdapter(this, R.layout.reader_list_item,
				tempCursor, from, to);

		listview.setAdapter(cursorAdapter);
		listview.setItemsCanFocus(false);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				position++;
				Log.d("POSITION", "Selected position : " + position);
				reader = new Reader(getApplicationContext());
				reader.setReader(position);
				readerName.setText(reader.getSelectedReader());
				selectReaderDialog.dismiss();
			}

		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectReaderDialog.dismiss();

			}
		});
		selectReaderDialog.show();
	}

}
