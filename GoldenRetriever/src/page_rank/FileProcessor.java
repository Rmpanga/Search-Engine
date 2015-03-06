package page_rank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


/** Multithreading idea
 *  - Have a thread read a line at a time 
 *  - insert line into datastructures
 * 
 *  I have to redo to storing files
 *  Store a doc with lists of incoming links 
 *  instad of doc with list of outgoing linkks
 *  
 * 
 */

public class FileProcessor implements Runnable  {

	FileInputStream is;
	Scanner sc;
	static String linkFile = "C:\\Users\\Richard\\Documents\\CS446\\Project Docs\\P3\\links.srt";
	static String testFile = "C:\\Users\\Richard\\Documents\\CS446\\Project Docs\\P3\\testranksinks.txt";
	static String dir = "C:\\Users\\Richard\\Documents\\CS446\\Project Docs\\P3\\OutgoingPageLinks\\";
	static ArrayList<String> fileLocations = new ArrayList<String> ();
	
	public FileProcessor(String file){
		
		initialize(file);
		
	}
	
	private void initialize(String file){
	
		try {
			is = new FileInputStream(file);
			sc = new Scanner(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("File " + file + " was not found");
		}
	}
	
	
	public boolean scannerHasNext(){
	 return sc.hasNextLine();
	}
	

	public String getNext(){
		String line = null;
		
		if (sc == null){
			return line;
		}
		if (scannerHasNext()){
		 line = sc.nextLine();
		 return line; 
		}
		
		else{
			if (is != null)
				try {
					is.close();
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("Problem closing InputStream || Scanner || Both");
				}
			
			return line;
		}
	}
	
	public void write(String filename, ArrayList<String> data){
		int pageLocation = 0;
		try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
	            new DeflaterOutputStream(new FileOutputStream(filename))))) {
	        dos.writeInt(data.size());
	        dos.writeUTF(data.get(pageLocation));
	        
	        /* Iterate through the links */ 
	        
	        for (int j = 1; j < data.size(); j++){
	        	dos.writeUTF(data.get(j));
	        }
	    
	        dos.close();
		} catch(IOException e){
			System.err.println("Problem with writing outgoing links to disk"); 
		}
	}
	
	//public void writeHash
	
	public  ArrayList<String> read(String filename)  {
	    ArrayList<String> ret = new ArrayList<String>();
	    try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
	            new InflaterInputStream(new FileInputStream(dir + filename))))) {
	    	//String page = dis.readUTF();
	        for (int i = 0, size = dis.readInt() -1; i <= size; i++) {
	        	//System.out.println("Reading from disk page: " + dis.readUTF());
	            ret.add(dis.readUTF());
	        }
	    }catch(IOException e){
	    	System.err.println("Problem with reading outgoing links data structure from disk");
	    	e.printStackTrace();
	    }
	    return ret;
	}
	
	private int getRankSinkCountAndRemoveNulls(HashMap<String, Boolean> hashmap){
		Iterator<String> it = hashmap.keySet().iterator();
	    int count = 0; 
		while (it.hasNext()){
			String key = (String) it.next();
			if (hashmap.get(key) == true){
				count++;
			}
			else{
				it.remove(); 
			}
		}
		
		return count; 
	}
	
	public HashMap<String, PageData>  processFile(){
		   
		HashMap<String ,PageData> graph = new HashMap<String , PageData> ();  
			String page = null;
			String destLink = null; 
			while (scannerHasNext()){
				String line = getNext();
				String [] breakdown = line.split("\t");
				destLink = breakdown[1];
				if (page == null){
					page = breakdown[0];
					//destLink = breakdown[1];
					
					/* Adding pagedata to source link */
					PageData sourcePageData = new PageData (page);
					sourcePageData.addOutGoingLinks(destLink);
					graph.put(page, sourcePageData);
					
				
					/* Adding incoming link to destination link */
					PageData destPageData = new PageData(destLink);
					destPageData.addIncomingLinks(page);
					graph.put(destLink, destPageData);
				
				}
				
				else if (page.trim().equals(breakdown[0].trim())){
					PageData sourcePageData = graph.get(page);
					sourcePageData.addOutGoingLinks(breakdown[1]);
					graph.put(page, sourcePageData);
					if (graph.containsKey(breakdown[1])){
						PageData destPageData = graph.get(breakdown[1]);
						destPageData.addIncomingLinks(page);
						graph.put(breakdown[1], destPageData);
					}
					
					else if (!graph.containsKey(breakdown[1])){
						PageData destPageData = new PageData(breakdown[1]);
						destPageData.addIncomingLinks(page);
						graph.put(breakdown[1], destPageData);
					}
				}
			
				 if (!page.trim().equals(breakdown[0].trim())){
					 
					 /* Adding source page data */
					 page = breakdown[0];
					 
					 if (graph.containsKey(breakdown[1])){
							PageData destPageData = graph.get(breakdown[1]);
							destPageData.addIncomingLinks(page);
							graph.put(breakdown[1], destPageData);
						}
						
					else if (!graph.containsKey(breakdown[1])){
							PageData destPageData = new PageData(breakdown[1]);
							destPageData.addIncomingLinks(page);
							graph.put(breakdown[1], destPageData);
						}
					 
					 
					 if (!graph.containsKey(page)){
					 PageData sourcePageData = new PageData (page);
					 sourcePageData.addOutGoingLinks(breakdown[1]);
					 graph.put(page, sourcePageData);
					 
					 
				
					 }
					 else if (graph.containsKey(page)){
						 PageData sourcePageData = graph.get(page);
						 sourcePageData.addOutGoingLinks(breakdown[1]);
						 graph.put(page, sourcePageData);
						
					 }
					 
					 else{ //Graph does not contain page
						 PageData sourcePageData = new PageData (page);
						 sourcePageData.addOutGoingLinks(breakdown[1]);
						 graph.put(page, sourcePageData);
						 
					 }
					 
				 
				 }
		 }
		return graph; 
	}
	
	
	
	
   public void processFile2(String file){
	   
	   HashMap<String, Boolean > potentialRankSinks = new HashMap<String, Boolean>(); 
		ArrayList<String> currentPageLinks = new ArrayList<String>();
		String page = null; 
		int page_count = 0;
		while (scannerHasNext()){
			String line = getNext();
			String [] breakdown = line.split("\t");
			if (page == null){

				page_count ++;
				page = breakdown[0];
				//currentPageLinks.add(breakdown[0]);
				//currentPageLinks.add(breakdown[1]);
				potentialRankSinks.put(page, false);
				potentialRankSinks.put(breakdown[1], true);
			}
			
			else if (page.trim().equals(breakdown[0].trim())){
				if (!potentialRankSinks.containsKey(breakdown[1]))
					potentialRankSinks.put(breakdown[1], true);
				    //currentPageLinks.add(breakdown[1]);
			}
		
			 if (!page.trim().equals(breakdown[0].trim())){
				 page_count++;
				 String pageLinkLocation = dir + page;
				 fileLocations.add(page);
				 write(pageLinkLocation, currentPageLinks); 
				 //currentPageLinks = new ArrayList<String> ();
				 
			    page = breakdown[0];
				//currentPageLinks.add(page);
				//currentPageLinks.add(breakdown[1]); 
			    if (potentialRankSinks.containsKey(page)){
			    	potentialRankSinks.replace(page, false);
			    }
			    else {
			    	potentialRankSinks.put(page, false);
			    	
			    }
			}
			
		if (!scannerHasNext()){
			 page = breakdown[0];
			 currentPageLinks.add(page);
			 currentPageLinks.add(breakdown[1]); 
			 String pageLinkLocation = dir + page;
			 write(pageLinkLocation, currentPageLinks); 
			 fileLocations.add(pageLinkLocation);
		}
		
	 }
   }
	
   @Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public static void main (String [] args){
		
		FileProcessor fp = new FileProcessor(linkFile);
		HashMap<String, PageData > graph = fp.processFile();
		
		for (String key : graph.keySet()){
			System.out.println("For Key :" + key );
			System.out.println(" Has outgoing links");
		
		 for (String out : graph.get(key).outgoingLinks)
			 System.out.println("	Outgoing: " + out );
		 
			System.out.println(" Has incoming links...");
		 for (String in : graph.get(key).incomingLinks){
			 System.out.println("	Incoming: " + in );
		  }
		System.out.println("----------------------------------------------------------");
		}
	}

}




