package com.Alquran.quran;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainSurahListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	private LayoutInflater mInflater;
	private int selectedItem;
	private int ayahNo;
	
	@SuppressWarnings("deprecation")
	public MainSurahListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	        //initial fill
	       
	}

	public int getSelectedPosition()
	{
		return selectedItem;
	}
	
	public int getSelectedAyahNo()
	{
		return ayahNo;
		
	}
	public void setSelectedItem(int position) {
		
	    selectedItem = position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder holder;
		   
		    
		    if (convertView == null) {
		        convertView = mInflater.inflate(R.layout.mainlvtextstyle, null);

		        holder = new ViewHolder();
		        holder.textMatn = (TextView) convertView.findViewById(R.id.list_content);
		        holder.textAyah = (TextView) convertView.findViewById(R.id.id_AyatsNo);
		        
		        convertView.setTag(holder);
		    } else {
		        holder = (ViewHolder) convertView.getTag();
		    }

		    dataCursor.moveToPosition(position);
		    int label_index = dataCursor.getColumnIndex("Matn"); 
		    String label = dataCursor.getString(label_index);
		    
		    int index_Ayah = dataCursor.getColumnIndex("Ayah"); 
		    String label_Ayah = dataCursor.getString(index_Ayah);

		    holder.textMatn.setTypeface(Main.font);
		    holder.textMatn.setText(label);
		    
		    holder.textAyah.setText(label_Ayah);
		  
		    if (position == selectedItem){		    	
		    	convertView.setBackgroundResource(R.drawable.background_dark_blue);
		    	ayahNo =Integer.parseInt(label_Ayah);
		    }
		    else{
		    	convertView.setBackgroundColor(Color.WHITE);
		    }
		    return convertView;	    
		    
	}

	 	
	 public class ViewHolder {
	    TextView textMatn,textAyah;
	
	}
}