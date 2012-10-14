package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ReaderListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	private int selectedItem;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public ReaderListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}
	
	public void setSelectedItem(int position) {
	    selectedItem = position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.reader_list_item, null);

	        holder = new ViewHolder();
	        
	        
	        holder.ReaderName = (TextView) convertView.findViewById(R.id.radioReaderName);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    
	    int ReaderName_index = dataCursor.getColumnIndex("reader_name"); 
	    String label_ReaderName = dataCursor.getString(ReaderName_index);
	    
	   // int ReaderId_index = dataCursor.getColumnIndex("_id"); 
	    //String label_ReaderId = dataCursor.getString(ReaderId_index);
	   
	    int ReaderNameSelected_index = dataCursor.getColumnIndex("isSelected");
	    int label_ReaderNameSelected = dataCursor.getInt(ReaderNameSelected_index);
	   
	    if(label_ReaderNameSelected == 1)
	    {
	    	//holder.RaderName.setChecked(true);
	    	
	    	holder.ReaderName.setTextColor(Color.GREEN);
	    	
	   }
	   else
	   {
	    //	holder.RaderName.setChecked(false);
	    	holder.ReaderName.setTextColor(Color.WHITE);
	    	
	   }
	    
	    holder.ReaderName.setText(label_ReaderName);
	        
	    if (position == selectedItem)
	    {
	      // holder.main_content.setBackgroundResource(R.drawable.background_dark_blue);
	   	
	       
	    }

	    
	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    //RadioButton ReaderName;
		 TextView ReaderName;
	    
	}
}