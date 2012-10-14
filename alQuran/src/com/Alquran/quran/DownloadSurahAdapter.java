package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DownloadSurahAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public DownloadSurahAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder holder;
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.download_list_item, null);

	        holder = new ViewHolder();
	        holder.SurahNo = (TextView) convertView.findViewById(R.id.id_SurahNo);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    
	    int SurahNo_index = dataCursor.getColumnIndex("SurahNo"); 
	    String label_SurahNo = dataCursor.getString(SurahNo_index);
	    
	    holder.SurahNo.setText(label_SurahNo);
	    return convertView;
	}

	 	
	 public class ViewHolder {
	    TextView SurahNo;
	    
	}
}