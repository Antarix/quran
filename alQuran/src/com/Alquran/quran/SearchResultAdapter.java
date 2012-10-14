package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public SearchResultAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.search_results, null);

	        holder = new ViewHolder();
	        holder.SurahNames = (TextView) convertView.findViewById(R.id.id_SurahArabic);
	        holder.AyatNo = (TextView) convertView.findViewById(R.id.id_AyatsNo);
	        holder.Matn = (TextView) convertView.findViewById(R.id.id_matn);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int SurahNames_index = dataCursor.getColumnIndex("SurahNames"); 
	    String label_SurahNames = dataCursor.getString(SurahNames_index);
	    
	    int AyatNo_index = dataCursor.getColumnIndex("AyatNo"); 
	    String label_AyatNo = dataCursor.getString(AyatNo_index);
	    
	    int Matn_index = dataCursor.getColumnIndex("Matn"); 
	    String label_Matn = dataCursor.getString(Matn_index);
	    
	    
	    holder.SurahNames.setTypeface(Search.font);
	    holder.SurahNames.setText(label_SurahNames);
	    
	    holder.AyatNo.setTypeface(Search.font);
	    holder.AyatNo.setText(label_AyatNo);
	    
	    holder.Matn.setTypeface(Search.font);
	    holder.Matn.setText(label_Matn);
	    
	    
	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    TextView SurahNames,AyatNo,Matn;
	    
	    
	}
}