package com.michelle.bartybart;

import java.util.ArrayList;

public class Trip extends Object {

	String origin = null;
	String destination = null; 
	String start = null; 
	String end = null;
	ArrayList<Trip> legs = null;
	String trainHeadStation = null;
	
	
public Trip(String startOrigin, String startDestination, String startstartTime, String startendTime, String starttrainHeadStation) {
	    origin = startOrigin;
	    destination = startDestination;
	    start=startstartTime;
	    end=startendTime;
	    legs = new ArrayList<Trip>();
	    trainHeadStation = starttrainHeadStation;
	}

public void setOrigin(String s){
	origin = s;
}

public void setDestination(String s){
	destination = s;
}

public void setStart(String s){
	start = s;
}


public void settrainHeadStation(String s){
	trainHeadStation = s;
}


public void setEnd(String s){
	end = s;
}

public void addLeg (Trip t){
	legs.add(t);
}
	
public String printLegs()
	{
	
	String s = null; 
	int j;
	
    
    if (legs != null)
    
    	{
    		for (int i=0;i<legs.size();i++) 
    		{
    	        j = i+1;
    			s = s + "Leg " + j + ": " +"Take the " + legs.get(i).trainHeadStation + " train" + "\n" +  legs.get(i).origin  + " to " + legs.get(i).destination + "\n" + legs.get(i).start + " to " + legs.get(i).end + "\n";
    		}
    	
    	}
    
    else;
    
    return s;
    
	}

public String printTrip()
{

String s = null; 

s = origin + " to " + destination +"\n" + start + " to " + end + "\n";

return s;


}

}