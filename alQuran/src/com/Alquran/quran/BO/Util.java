package com.Alquran.quran.BO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.Alquran.quran.DbHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Util{

 // public static final String font = "font/DroidSansArabic.ttf";
   
	public static int surahDetail[] = {1,0};
	public static boolean isDownloadCanceled = false;
	public static boolean hasConnection(Context cont) {
	    ConnectivityManager cm = (ConnectivityManager) cont.getApplicationContext().getSystemService(
	        Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	}
	
	
	public static boolean checkDataBase(){
    	
 
    	try
    	{
    	File file = new File(DbHelper.DB_PATH+DbHelper.DB_NAME);
    	long fileSize = file.length();
    	if (file.exists() &&(fileSize > 2524512)){
    		
    	return true;
    	} else {
    	return false;
    	}
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    
    }
 	
	@SuppressWarnings("resource")
	public static boolean isFileExistsOnSD(String fileName)
	{
			
		try {
			File myFile = new File(fileName);
			FileInputStream fIn = new FileInputStream(myFile);
			
			@SuppressWarnings("unused")
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			return true;	
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	
	/**
	 * Function to convert milliseconds time to Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	/**
	 * Function to get Progress percentage
	 * 
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public int getProgressPercentage(long currentDuration, long totalDuration) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * 
	 * @param progress
	 *            -
	 * @param totalDuration
	 *            returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}
	
		
}
