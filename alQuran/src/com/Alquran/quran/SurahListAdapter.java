package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SurahListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public SurahListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.change_surah, null);

	        holder = new ViewHolder();
	        holder.SurahEnglish = (TextView) convertView.findViewById(R.id.id_SurahEnglish);
	        holder.SurahArabic = (TextView) convertView.findViewById(R.id.id_SurahArabic);
	        holder.Verses = (TextView) convertView.findViewById(R.id.id_Verses);
	        holder.JuzNo = (TextView) convertView.findViewById(R.id.id_JuzNo);
	        holder.OriginEnglish = (TextView) convertView.findViewById(R.id.id_OriginEnglish);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int SurahEnglish_index = dataCursor.getColumnIndex("SurahEnglish"); 
	    String label_SurahEnglish = dataCursor.getString(SurahEnglish_index);
	    
	    int SurahArabic_index = dataCursor.getColumnIndex("SurahArabic"); 
	    String label_SurahArabic = dataCursor.getString(SurahArabic_index);
	    
	    int Verses_index = dataCursor.getColumnIndex("Verses"); 
	    String label_Verses = dataCursor.getString(Verses_index);
	    
	    int JuzNo_index = dataCursor.getColumnIndex("JuzNo"); 
	    String label_JuzNo = dataCursor.getString(JuzNo_index);
	    
	    int OriginEnglish_index = dataCursor.getColumnIndex("OriginEnglish"); 
	    String label_OriginEnglish = dataCursor.getString(OriginEnglish_index);

	    holder.SurahArabic.setTypeface(SurahTabs.font);
	    holder.SurahArabic.setText(label_SurahArabic);
	        
	    holder.SurahEnglish.setText(label_SurahEnglish);
	    holder.Verses.setText(label_Verses);
	    holder.JuzNo.setText(label_JuzNo);
	    holder.OriginEnglish.setText(label_OriginEnglish);
	    
	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    TextView SurahEnglish,SurahArabic,Verses,JuzNo,OriginEnglish;
	    
	    
	}
}