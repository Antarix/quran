package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class JuzListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public JuzListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.change_juz, null);

	        holder = new ViewHolder();
	        
	        holder.SurahArabic = (TextView) convertView.findViewById(R.id.id_SurahArabic);
	        holder.JuzNo = (TextView) convertView.findViewById(R.id.id_JuzNo);
	        holder.AyatsNo = (TextView) convertView.findViewById(R.id.id_AyatsNo);
	        holder.SurahNo = (TextView) convertView.findViewById(R.id.id_SurahNo);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    
	    int SurahArabic_index = dataCursor.getColumnIndex("SurahArabic"); 
	    String label_SurahArabic = dataCursor.getString(SurahArabic_index);
	    
	    int JuzNo_index = dataCursor.getColumnIndex("JuzNo"); 
	    String label_JuzNo = dataCursor.getString(JuzNo_index);
	    
	    int AyatsNo_index = dataCursor.getColumnIndex("AyatsNo"); 
	    String label_AyatsNo = dataCursor.getString(AyatsNo_index);
	    
	    
	    int SurahNo_index = dataCursor.getColumnIndex("SurahNo"); 
	    String label_SurahNo = dataCursor.getString(SurahNo_index);

	    holder.SurahArabic.setTypeface(SurahTabs.font);
	    holder.SurahArabic.setText(label_SurahArabic);
	    
	    holder.JuzNo.setTypeface(SurahTabs.font);
	    holder.JuzNo.setText(label_JuzNo);
	    
	    holder.AyatsNo.setTypeface(SurahTabs.font);
	    holder.AyatsNo.setText(label_AyatsNo);
	    
	    holder.SurahNo.setText(label_SurahNo);
	    	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    TextView SurahArabic,JuzNo,AyatsNo,SurahNo;
	    
	    
	}
}