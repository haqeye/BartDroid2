package com.michelle.bartybart;

/* To Do:
 * Turn "ShowTrips" into List View
 * Store user's Recents and display as a List Fragment in the Main View
 * Develop Graphics 
 * Find someone to do a code review
 */

import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

public class BartyBartMain extends FragmentActivity implements OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener  {
	public final static String BART_URL = "com.michelle.bartybart.MESSAGE";
	public final static String REVERSE_MAP = "com.michelle.bartybart.REVERSEMAP";
	public final static String ORIGIN_STATION = "com.michelle.bartybart.ORIGIN";
	public final static String DEST_STATION = "com.michelle.bartybart.DESTINATION";
	
String origin = "CIVC";
String dest = "CIVC";
String time = "now";
String date = "now";
Spinner originSpinner = null;
Spinner destSpinner = null;
HashMap<String, String> hash= new HashMap<String, String>();
HashMap<String, String> revhash= new HashMap<String, String>();
Button timeButton = null;
Button dateButton = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barty_bart_main);
        
        // create a hashmap of station names and station codes
        String[] keys= getResources().getStringArray(R.array.stations);
    	String[] vals= getResources().getStringArray(R.array.stationcodes);
    	
     
    	for(int i= 0; i < keys.length; i++){
    	   hash.put(keys[i], vals[i]);
    	   revhash.put(vals[i], keys[i]);
    	}
    	
    	timeButton = (Button) findViewById(R.id.timebutton);
    	timeButton.setText("Time: "+ time);
    	
    	dateButton = (Button) findViewById(R.id.datebutton);
    	dateButton.setText("Date: today");
    	
        
        // Create the origin spinner
        originSpinner = (Spinner) findViewById(R.id.origin);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stations, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        originSpinner.setAdapter(adapter);
        originSpinner.setOnItemSelectedListener(this);
        
        // Create the destination spinner
        destSpinner = (Spinner) findViewById(R.id.destination);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.stations, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        destSpinner.setAdapter(adapter2);
       destSpinner.setOnItemSelectedListener(this);
       
       
  
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_barty_bart_main, menu);
        return true;
    }
    
public void myClickHandler2(View view){
    	
        if (origin.equals(dest))
        {;}
        else{
    	Intent intent = new Intent(this, ShowTrips.class);
    	String stringURL = "http://api.bart.gov/api/sched.aspx?cmd=depart&orig=" + origin + "&dest=" + dest + "&time=" + time + "&date" + date + "&key=EEH2-JDYI-QKUE-SKIS&b=1&a=3";
    	
    	intent.putExtra(BART_URL, stringURL);
    	
    	startActivity(intent);}
    }
    
public void TimeClickHandler(View view){
	DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getSupportFragmentManager(), "timePicker");
        
    }


public void DateClickHandler(View view){
	DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getSupportFragmentManager(), "datePicker");
        
    }

@Override
public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	
	String stringMinute = null;
	
	if (minute<10)
	{stringMinute = "0" + minute;}
	else {stringMinute = ""+ minute;}
	
	if (hourOfDay>12){
		time = (hourOfDay - 12) + ":" + stringMinute + "PM";
	}
	
	else {time = hourOfDay + ":" + stringMinute + "AM";}
	
	timeButton.setText("Time: "+ time);
}
    

/* 
 * onDateSet is the function that is called when the date is set inside of the DatePicker
 * 
 */

@Override
public void onDateSet(DatePicker view, int year, int monthOfYear,
		int dayOfMonth) {

// The BART API takes "date" in the format "mm/dd/yyyy" so I have to create the "date" string in that format
	
	String mm = null;
	String dd = null;
	
// Set the string "mm"	
	
	if (monthOfYear<10){
		mm = "0" + Integer.toString(monthOfYear);
	}
	
	else {mm = Integer.toString(monthOfYear);}
	
// Set the string "dd"	
	
	if (dayOfMonth<10){
		dd = "0" + Integer.toString(dayOfMonth);
	}
	else {dd = Integer.toString(dayOfMonth);}
	
//Set the string "yyyy"
	String yyyy = Integer.toString(year);
	
// Set the "date" string in the format "mm/dd/yyyy"
	
	date = mm + "/" + dd + "/" + yyyy; 
	
// Change the text inside of the date button to display the chosen date. 
	
	dateButton.setText("Date: " + date);
	
}	



    	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		Spinner spinner = (Spinner) parent;
	     if(spinner.getId() == R.id.origin)
	     {
	       origin = hash.get(parent.getItemAtPosition(pos));
	     }
	     else if(spinner.getId() == R.id.destination)
	     {
	       dest = hash.get(parent.getItemAtPosition(pos));
	      
	     }	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static class TimePickerFragment extends DialogFragment {

        //blah blah changes changes

		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// 	Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), (BartyBartMain)getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
			
		}

		
	}
	
	public static class DatePickerFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// 	Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), (BartyBartMain)getActivity(), year, month, day);
		}

	}

	


}
    
   