package com.Alquran.quran;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
	        holder.text1 = (TextView) convertView.findViewById(R.id.list_content);
	        holder.Ayah = (TextView) convertView.findViewById(R.id.id_AyatsNo);
	        holder.main_content = (LinearLayout) convertView.findViewById(R.id.main_list_content);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int label_index = dataCursor.getColumnIndex("Matn"); 
	    String label = dataCursor.getString(label_index);
	    
	    int index_Ayah = dataCursor.getColumnIndex("Ayah"); 
	    String label_Ayah = dataCursor.getString(index_Ayah);

	    holder.text1.setTypeface(Main.font);
	    holder.text1.setText(label);
	    
	    holder.Ayah.setText(label_Ayah);
	    // to make the selection
	  //  LinearLayout activeItem = (LinearLayout) rowView;
	    if (position == selectedItem)
	    {
	       holder.main_content.setBackgroundResource(R.drawable.background_dark_blue);
	       ayahNo =Integer.parseInt(label_Ayah);


	    }
	    
	    
	    
	    return convertView;
	    
	}

	
	
	 	
	 public class ViewHolder {
	    TextView text1,Ayah;
	    LinearLayout main_content;
	    
	}
}