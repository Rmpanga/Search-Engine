import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//TODO: Add domain name and http to link/folders when apporiate DONE
//TODO: Parse Robots.txt 
//TODO: Follow Robots.txt

/* TODO
 * 
 * Verify Robots.txt parsing
 * Verify Robots.txt datastructure
 * Verify restrict = false 
 * Walkthrough/Clean
 * 
 */

public class Spider {

	final String name = "Golden-Retriever";
	HashMap<String, LinkedList<String>> prohibitedURLs;
	LinkedList<String> urlQueue;
	LinkedList<String> processedURLs;
	
	public Spider(LinkedList<String> seeds){
		urlQueue = new LinkedList<String>();
		processedURLs = new LinkedList<String>();
		prohibitedURLs = new HashMap<String, LinkedList<String>>();
		for (String seed : seeds)
		{
			urlQueue.push(seed);
		}
	}
	
	
	/* @params
	 * - delay: policy delay
	 * - completion: max links to fetch 
	 * - restrict if true stay in cs.umass.edu
	 * 
	 * Explores the web and fetches links 
	 */
	public void explore(int delay, int completion, boolean restrict) {
		//int urlCount = 0;
		//Document doc;
		
		while (!urlQueue.isEmpty() && processedURLs.size() < completion){
			String urlString = urlPatcher(urlQueue.poll().replaceAll("#", ""));
			if (restrict == true){
				if ((!processedURLs.contains(urlString) && urlString.contains("cs.umass.edu"))){
					exploreUtlity(urlString, delay);
				/*	if ((!processedURLs.contains(urlString) && urlString.contains("cs.umass.edu"))){ //Dont need processedURLs... 
				   String domainName = getDomainName(urlString);
				   if (domainName != null && !prohibitedURLs.containsKey(domainName)){
					   processRobot(domainName);
				   }
				
					doc = createConnection(urlString);
					
				    if (doc != null){
				    	//processRobot(urlString);
				    	Elements links = doc.select("a[href]"); 
				    	LinkedList<String> disallowed = prohibitedURLs.get(domainName);
				    	for (Element link : links)
				    	{
				    		String urlPatched = urlPatcher(link);	
				    		if (!processedURLs.contains(urlPatched))
				    		{				   
				    			boolean allowed = true;
							    //String urlPatched = urlPatcher(link);							    
				    			if (urlPatched.contains("http") && ( urlPatched.contains(".pdf") || urlPatched.contains(".html")))
				    			{
				    				for (int j = 0; j < disallowed.size(); j++){
				    				  if (!disallowed.get(j).toLowerCase().equals(urlPatched.toLowerCase())){
				                          allowed = false;
				                          break;
				    				  }
				    					
				    				}
				    				if (allowed){  
				    				urlQueue.push(urlPatched);
				    				}
				    				else System.out.println("HIT ROBOTS.TXT: LINK WAS NOT ALLOWED BY ROBOTS.TXT FOR " + domainName);
				   
				    			} 
				    			else{ 
				    				System.out.println("FAILED TO PUSH: Link did not pass html and pdf test"); }
							
				    		} 
				    		else{ 
				    			System.out.println("FAILED TO PUSH: Already processed " + urlPatched); }
				    	}
				    	processedURLs.add(urlString);
				    	urlCount++;
				    	System.out.println("URL Count : " + urlCount);
				    	try {
				    		System.out.println("Delay starting");
				    		Thread.sleep(delay*1000);
				    		System.out.println("Delay ended");
				    	} catch (InterruptedException e) { e.printStackTrace(); }
				    }//end of if = null
				 
			*/	}//end of restrict == true 
			}
			else if (restrict == false)
			{
				if (!processedURLs.contains(urlString)){
					exploreUtlity(urlString, delay);
					
				}
					
			
			}
		
		}
	}
	
	/*@param 
	 *  urlString: current url being processe
	 *  delay: policy wait time
	 * 
	 * From the url get the domain name and process its robots.txt
	 * Create a connection and store the page in a document
	 * Retrieve all the a[href] (links) in that document
	 * 
	 */
	private void exploreUtlity(String urlString,  int delay ){
		Document doc;
		String domainName = getDomainName(urlString);
		  
		if (domainName != null && !prohibitedURLs.containsKey(domainName)){
			   processRobot(domainName); 
		   }
		
			doc = createConnection(urlString);
			
		    if (doc != null){
		    	//processRobot(urlString);
		    	Elements links = doc.select("a[href]"); 
		    	LinkedList<String> disallowed = prohibitedURLs.get(domainName);
		    	for (Element link : links)
		    	{
		    		String urlPatched = absUrlFormater(link);	
		    		if (!processedURLs.contains(urlPatched))
		    		{				   
		    			boolean allowed = true;
					    //String urlPatched = urlPatcher(link);							    
		    			if (urlPatched.contains("http")) //&& ( urlPatched.contains(".pdf") || urlPatched.contains(".html")))
		    			{
		    				if (disallowed != null)
		    				for (int j = 0; j < disallowed.size(); j++){
		    				  if (urlPatched.toLowerCase().contains(disallowed.get(j).toLowerCase())){
		                          allowed = false;
		                          break;
		    				  }
		    					
		    				}
		    				if (allowed){  
		    				urlQueue.push(urlPatched);
		    				}
		    				else{ 
		    					System.out.println();
		    					System.out.println("HIT ROBOTS.TXT: LINK WAS NOT ALLOWED BY ROBOTS.TXT FOR " + domainName);}
		   
		    			} 
		    			else{ 
		    				System.out.println();
		    				System.out.println("FAILED TO PUSH: Link did not pass html and pdf test"); }
					
		    		} 
		    		else{ 
		    			System.out.println();
		    			System.out.println("FAILED TO PUSH: Already processed " + urlPatched); }
		    	}
		    	processedURLs.add(urlString);
		    	//urlCount++;
		    	//System.out.println("URL Count : " + urlCount);
		    	try {
		    		Thread.sleep(delay*1000);
		    	} catch (InterruptedException e) { e.printStackTrace(); }
		    }//end of if = null
	
	}
	
    private String urlPatcher(String url){
    	String urlpatched = url;
    	if (url.contains("https")){ //Removing https and placing it with http
    		StringBuilder httpsFixer = new StringBuilder(url);
    		httpsFixer = httpsFixer.delete(0, 5);
    		httpsFixer = httpsFixer.insert(0, "http");
    		urlpatched  = httpsFixer.toString();
    		return urlpatched;
    	} 
    	else if (!url.contains("http")){ //For robots.txt when domain name is passed in
    		System.out.println();
    		System.out.println("ROBOTS.TEXT FIXER: Adding http and robots to url for processing");
    		StringBuilder httpFixer = new StringBuilder(url);
    		httpFixer = httpFixer.insert(0, "http://");
    		httpFixer = httpFixer.append("/robots.txt");
    		urlpatched = httpFixer.toString().replaceAll("#", "");
    		System.out.println();
    		return urlpatched;
    	}
    		return urlpatched;
    
      }
	
    /*
     * @param Element link = href links
     * 
     * This method gets the absolute url, 
     * Replaces https with http
     * Removes # from url
     */
    
	private String absUrlFormater(Element link){
		StringBuilder patcher = new StringBuilder(link.attr("abs:href"));
		if (patcher.toString().contains("https")){
			patcher = patcher.replace(0, 4, "http");
		}
		//System.out.println();
		//System.out.println("ABS URL " + patcher.toString().replaceAll("#", ""));

		return patcher.toString().replaceAll("#", ""); 
	}
	
	
	public String getDomainName(String url){
		URI uri;
		try {
			uri = new URI (url);
				if (uri != null){
					String domain = uri.getHost();
					if (domain != null){
					System.out.println(urlPatcher(domain));	
					return domain;
					/*if (!prohibitedURLs.containsKey(domain)){
					
					}
*/					//System.out.println(domain);
					}
				}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void testBannedUrls(){
		System.out.println();
		System.out.println("************************** TESTING BANNED URLS *************************");
	//	for (String url : bannedURLs)
		//	System.out.println(url);
		System.out.println("************************** END TESTING FOR BANNED URLS *************************");
	}
	
	public void processRobot(String domainName){
		String robotTextString = urlPatcher(domainName); //here
		boolean start = false;
	    
		
		URL robotURL;
		LinkedList<String> disallowed = new LinkedList<String>();
		try {
			robotURL = new URL(robotTextString);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(robotURL.openStream()));
			String line = null;
			   
			while ((line = in.readLine()) != null){
				line = line.toLowerCase();
				System.out.println("TESTING LINE: " + line);
				if (start = true){
					if (line.contains("disallow")){
		          		String [] split = line.split(" ");
		          		String noCrawl = split[1].toLowerCase().trim();
		          		disallowed.add(noCrawl);
		          	}
					
				}
				
				if (line.contains(name) || line.contains("user-agent: *")){
				    start = true;
				}
				
				else if (line.contains("user-agent: " )){
					start = false;
				}
			
			}
			prohibitedURLs.put(domainName, disallowed);
			
		} catch (MalformedURLException e) {
		  System.out.println("ERROR: Robot URL does not exist for " + robotTextString);
			e.printStackTrace();
		}
		
		  catch (IOException e ){
			System.out.println("ERROR: Problem with reading robots.txt file for " + robotTextString);  
		  }
	  
	}
	
	

	private Document createConnection(String urlString){
		try {
			Document doc =  Jsoup.connect(urlString).get();
			doc = Jsoup.parse(doc.html() , urlString);
			return doc;
			//return Jsoup.connect(urlString).ignoreContentType(false).get();
			
		} catch (IOException e) {
			System.out.println("ERROR: Failure in Jsoup connection connection for " +urlString);
		}
	     return null;
	}
	
	
	public void printProcessedURLs(){
		System.out.println();
		System.out.println("***************Printing out processed urls***********************");
		for (String url : processedURLs){
			System.out.println("PROCESSED: " + url);
		}
		System.out.println();
		System.out.println("Processed " + processedURLs.size() +" urls!!!!!!!!!!!");
		System.out.println("******************Crawler terminated**************************");
	}

}
