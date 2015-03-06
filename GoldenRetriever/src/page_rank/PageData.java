package page_rank;

import java.util.ArrayList;
import java.util.HashMap;

public class PageData {
	
	private String name;
	//private double oldRank;
	private double newRank;
	private int incomingCount = 0;
	private int outgoingCount = 0; 
	public  ArrayList<String >incomingLinks = new ArrayList<String> (); 
	public  ArrayList<String > outgoingLinks = new ArrayList<String>(); 
	 
	
	
	public PageData (String name){
		
		this.name = name; 
	}
	
	public void setRank(int rank){
		this.newRank = rank;
	}

	
	public boolean getRankSink(){
		return (outgoingCount == 0);
	}
	
	public double getRank(){
		return newRank;
	}
	
	public String getName(){
		return name;
	}
	
	
	public void addIncomingLinks(String incoming){
		if (!incomingLinks.contains(incoming)){
			incomingLinks.add(incoming);
			incomingCount++; 
		}
		
	}
	public void addOutGoingLinks(String outgoing){
		if (!outgoingLinks.contains(outgoing)){
			outgoingLinks.add(outgoing);
			outgoingCount++; 
		}
		
	}
	
}
