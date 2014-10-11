package com.michelle.bartybart;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ShowTrips extends Activity{

	  HashMap<String, String> hashMap;
	  
	  public String stringURL = null;
	  //@Override
	  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trips);
        
        Intent intent = getIntent();
        stringURL = intent.getStringExtra(BartyBartMain.BART_URL);
        getTrips();
   
    }

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_trips, menu);
        return true;
    }
    
    
 public void getTrips() {
    	
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        
        	new DownloadWebpageText().execute(stringURL);
        
        	
        } else {
            // display error
        	TextView tv = (TextView) findViewById(R.id.trips);
        	tv.setText("No network connection");
        }
    }
    
    
    
 // Explains AsyncTask http://stackoverflow.com/questions/10423094/use-of-async-task
    private class DownloadWebpageText extends AsyncTask<String,Void,List<Trip>> {
    	  
    	 ProgressDialog progressDialog;
    	
     //   @Override
		protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ShowTrips.this, "Loading", ""); 
        }	
    	
	  //   @Override
	        protected List<Trip> doInBackground(String... urls) {  
	            // params comes from the execute() call: params[0] is the url.
	            try {
	                return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                return null;
	            }
	        }
	        // onPostExecute displays the results of the AsyncTask.
	     //   @Override
	        protected void onPostExecute(List<Trip> result) {
	        //print the list of trips to the TextView
	        	
	        	progressDialog.dismiss();
	        	
	        	TextView tv = (TextView) findViewById(R.id.trips);
	        	
	        	
	        	tv.setText("");
	        	for (int i=0;i<result.size();i++) {
	        	    
	        	    tv.append(result.get(i).printTrip());
	        	}
	        	
	        	
	       }	     
	        
	        
   
    }
    
    
    private List<Trip> downloadUrl(String myurl) throws IOException {
        InputStream is = null;
  
            
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("debugtagorsomething", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            try {
            	
				return processXML(is);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
    
// Returns a LIST from an InputStream
    
    public List<Trip> processXML(InputStream is)
            throws XmlPullParserException, IOException
        { System.out.println("DEBUG: processXML started!");
    	// Create a factory so you can create a parser
    	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // Create a new parser from the factory
        XmlPullParser xpp = factory.newPullParser();
        // Set the InputStream arg as the input for the new parser. Encoding set to "null"
        
        List<Trip> list = new ArrayList<Trip>();
        xpp.setInput (is, null);
        
        int e; 
        String name;
            do { 
                e = xpp.getEventType();
                name = xpp.getName(); 
                
                if (e == XmlPullParser.START_TAG && name.equals("trip"))
                	{
                		
                		list = tripHandler(xpp, list);
                		
                	}
                xpp.next();
                
            	} while (xpp.getEventType() != XmlPullParser.END_DOCUMENT);
            
          return list;
        }

    public List<Trip> tripHandler(XmlPullParser xpp, List<Trip> list) throws XmlPullParserException, IOException{
    	//create a new trip
    	Trip t = new Trip(null, null, null, null, null);

    	
    	// create an eventType variable
    	int eventType = xpp.getEventType();
    	
    	
    	//add the details for the trip from the current "trip" start tag
    	   for (int i=0; i<xpp.getAttributeCount();i++) 
           {
    		   String name = xpp.getAttributeName(i);
               if (name.equals("origin"))  t.setOrigin(xpp.getAttributeValue(i));
               else if (name.equals("destination")) t.setDestination(xpp.getAttributeValue(i));
               else if (name.equals("origTimeMin")) t.setStart(xpp.getAttributeValue(i));
               else if (name.equals("destTimeMin")) t.setEnd(xpp.getAttributeValue(i));
               else ;
           }
   
   
    	
        
    	//Look for the legs
        do {
           if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("leg")) 
            	
        	   {
        	       
        	  t.legs.add(returnLeg(xpp));
        	 
        	   
        	   }
           else;
            
           eventType = xpp.next();
            
        } 
        
        //When the trip ends, close the loop. 
        while (!(xpp.getName().equals("trip") && eventType==XmlPullParser.END_TAG)) ;
        
        
        //Finally add, the trip you completed to your list of trips. 
        
        list.add(t);
        return list;
        
    }
    
    
    public Trip returnLeg(XmlPullParser xpp){
    	Trip leg = new Trip(null, null, null, null, null);
    	
 	leg.settrainHeadStation("blah2");
 	   // When you find a leg, add the details to the leg
 	    
 	   		    for (int i=0; i<xpp.getAttributeCount();i++) 
 	            {String name = xpp.getAttributeName(i);
 	               if (name.equals("origin"))  leg.setOrigin(xpp.getAttributeValue(i));
 	               else if (name.equals("destination")) leg.setDestination(xpp.getAttributeValue(i));
 	               else if (name.equals("origTimeMin")) leg.setStart(xpp.getAttributeValue(i));
 	               else if (name.equals("destTimeMin")) leg.setEnd(xpp.getAttributeValue(i));
 	               else if (name.equals("trainHeadStation")) leg.settrainHeadStation(xpp.getAttributeValue(i));
 	               else ;
 	               
 	              
 	            }
 	   		    
 	   		    return leg;
    }
    
    
}
