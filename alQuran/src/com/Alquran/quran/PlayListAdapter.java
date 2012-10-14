package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PlayListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public PlayListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.play_list_item, null);

	        holder = new ViewHolder();
	        holder.SurahEnglish = (TextView) convertView.findViewById(R.id.id_SurahEnglish);
	        holder.SurahArabic = (TextView) convertView.findViewById(R.id.id_SurahArabic);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int SurahEnglish_index = dataCursor.getColumnIndex("SurahEnglish"); 
	    String label_SurahEnglish = dataCursor.getString(SurahEnglish_index);
	    
	    int SurahArabic_index = dataCursor.getColumnIndex("SurahArabic"); 
	    String label_SurahArabic = dataCursor.getString(SurahArabic_index);
	    
	    
	    holder.SurahArabic.setTypeface(Main.font);
	    holder.SurahArabic.setText(label_SurahArabic);
	        
	    holder.SurahEnglish.setText(label_SurahEnglish);
	    
	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    TextView SurahEnglish,SurahArabic;
	    
	    
	}
}