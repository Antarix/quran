package com.Alquran.quran;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class BookmarkListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	private int selectedItem;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public BookmarkListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
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
	        convertView = mInflater.inflate(R.layout.bookmark_list_item, null);

	        holder = new ViewHolder();
	        
	        holder.Id = (TextView) convertView.findViewById(R.id.bookmark_id);
	        holder.SurahArabic = (TextView) convertView.findViewById(R.id.id_SurahArabic);
	        holder.AyatNo = (TextView) convertView.findViewById(R.id.id_AyatsNo);
	        holder.BookmarkTitle = (TextView) convertView.findViewById(R.id.bookmark_list_title);
	        holder.main_content = (RelativeLayout) convertView.findViewById(R.id.bookmark_content);
	        
	        
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    
	    int Id_index = dataCursor.getColumnIndex("_id"); 
	    String label_Id = dataCursor.getString(Id_index);
	    
	    int SurahArabic_index = dataCursor.getColumnIndex("SurahNames"); 
	    String label_SurahArabic = dataCursor.getString(SurahArabic_index);
	    
	    int AyatNo_index = dataCursor.getColumnIndex("AyatNo"); 
	    String label_AyatNo = dataCursor.getString(AyatNo_index);
	    
	    int BookmarkTitle_index = dataCursor.getColumnIndex("Title"); 
	    String label_BookmarkTitle = dataCursor.getString(BookmarkTitle_index);
	    
	    holder.SurahArabic.setTypeface(Bookmark.font);
	    holder.SurahArabic.setText(label_SurahArabic);
	        
	    holder.AyatNo.setText(label_AyatNo);
	    holder.BookmarkTitle.setText(label_BookmarkTitle);
	    holder.Id.setText("." + label_Id);
	    
	    if (position == selectedItem)
	    {
	       holder.main_content.setBackgroundResource(R.drawable.background_dark_blue);
	       
	    }

	    
	    return convertView;
	}

	
	
	 	
	 public class ViewHolder {
	    TextView SurahArabic,AyatNo,BookmarkTitle,Id;
	    RelativeLayout main_content;
	    
	    
	    
	}
}