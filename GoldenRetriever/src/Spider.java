import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
 * Verify Robots.txt parsing done
 * Verify Robots.txt datastructure done
 * Verify restrict = false  done
 * Walkthrough/Clean done
 * Place processed urls to a text file
 */

public class Spider {

	final String name = "Golden-Retriever";
	private HashMap<String, LinkedList<String>> prohibited;
	private LinkedList<String> frontier;
	private ArrayList<String> processedURLs;
	private int[][] stats = new int[1000][10000];
	private int pagesVisited;
	private int robotCount;
	private int urlCount;
	//private int docNum;
	public ArrayList<Integer> data = new ArrayList<Integer>(); 
	//private int tesst;
	
	public Spider(LinkedList<String> seeds){
		frontier = new LinkedList<String>();
		processedURLs = new ArrayList<String>();
		prohibited = new HashMap<String, LinkedList<String>>();
		urlCount = seeds.size();
		for (String seed : seeds)
		{
			frontier.offer(seed);
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
		
		while (!frontier.isEmpty()){  //&& processedURLs.size() < completion){
			String urlString = httpFormatter(frontier.poll().replaceAll("#", ""));
			if (restrict == true){
				if ((!processedURLs.contains(urlString) && urlString.contains("cs.umass.edu"))){
					exploreUtlity(urlString, delay, completion);
				}
			}
			else if (restrict == false)
			{
				if (!processedURLs.contains(urlString)){
					exploreUtlity(urlString, delay, completion);
					
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
	private void exploreUtlity(String urlString,  int delay, int completion ){
		Document doc;
		String domainName = getDomainName(urlString);
		int uniqueLinks = 0;
		  
		if (domainName != null && !prohibited.containsKey(domainName)){
			   processRobot(domainName); 
		   }
		
			doc = createConnection(urlString);
			if (urlCount < completion)
			pagesVisited++;
			
		    if (doc != null){
		    	//processRobot(urlString);
		    	Elements links = doc.select("a[href]"); 
		    	LinkedList<String> disallowed = prohibited.get(domainName);
		    	for (Element link : links)
		    	{
		    		String urlPatched = absUrlFormater(link);	
		    		if (!processedURLs.contains(urlPatched))
		    		{				   
		    			boolean allowed = true;
					    //String urlPatched = urlPatcher(link);							    
		    			if (urlPatched.contains("http")) // && ( urlPatched.contains(".pdf") || urlPatched.contains(".html")))
		    			{
		    				if (disallowed != null && urlCount < completion)
		    				for (int j = 0; j < disallowed.size(); j++){
		    				  if (urlPatched.toLowerCase().contains(disallowed.get(j).toLowerCase())){
		                          allowed = false;
		                          break;
		    				  }
		    					
		    				}
		    				if (allowed && urlCount < completion){  
		    					frontier.offer(urlPatched);
		    					uniqueLinks++;
		    					urlCount++;
		    				}
		    				else{ 
		    					//System.out.println();
		    					//System.out.println("HIT ROBOTS.TXT: LINK WAS NOT ALLOWED BY ROBOTS.TXT FOR " + domainName);
		    					}
		   
		    			} 
		    			else{ 
		    				//System.out.println();
		    			//	System.out.println("FAILED TO PUSH: Link did not pass html and pdf test"); 
		    				}
			
		    		} 
		    		else{ 
		    			//System.out.println();
		    			//System.out.println("FAILED TO PUSH: Already processed " + urlPatched); 
		    			}
		    	}
		    	data.add(uniqueLinks);
		    	processedURLs.add(urlString);
		    	//urlCount++;
		    	//System.out.println("URL Count : " + urlCount);
		    	try {
		    		Thread.sleep(delay*1000);
		    	} catch (InterruptedException e) { e.printStackTrace(); }
		    
		    
		    }//end of if = null
	
	}
	
	/*Param
	 * url : url to formart
	 * 
	 * Reomve https from url string
	 * 
	 * if url does not have http then its a domain name
	 * Add http:// and append /robots.txt 
     *
	 * @return formated string
	 */
	
    private String httpFormatter(String url){
    	String urlpatched = url;
    	if (url.contains("https")){ //Removing https and placing it with http
    		StringBuilder httpsFixer = new StringBuilder(url);
    		httpsFixer = httpsFixer.delete(0, 5);
    		httpsFixer = httpsFixer.insert(0, "http");
    		urlpatched  = httpsFixer.toString();
    		return urlpatched;
    	} 
    
    		return urlpatched;
      }
	
    private String robotUrlFormatter(String domainName){
    	String robotTextPath;
    	StringBuilder robotFixer = new StringBuilder(domainName);
		robotFixer = robotFixer.insert(0, "http://");
		robotFixer = robotFixer.append("/robots.txt");
		robotTextPath = robotFixer.toString().replaceAll("#", "");
    	return robotTextPath;
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
	
	
	private String getDomainName(String url){
		URI uri;
		try {
			uri = new URI (url);
				if (uri != null){
					String domain = uri.getHost();
					if (domain != null){
					//System.out.println(httpFormatter(domain));	
					return domain;
					/*if (!prohibitedURLs.containsKey(domain)){
					
					}
*/					//System.out.println(domain);
					}
				}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void testBannedUrls(){
		for (LinkedList<String> values : prohibited.values()){
			
			for (String value : values)
			//String domain = prohibitedURLs.
			System.out.println("Disallowed to go to " + value);
		}
	}
	
	private void processRobot(String domainName){
		String robotTextString = robotUrlFormatter(domainName); //here
		boolean start = false;
	    
		
		URL robotURL;
		LinkedList<String> disallowed = new LinkedList<String>();
		try {
			robotURL = new URL(robotTextString);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(robotURL.openStream()));
			String line = null;
			   
			while ((line = in.readLine()) != null){
				line = line.toLowerCase();
				//System.out.println("TESTING LINE: " + line);
				if (start = true){
					if (line.contains("disallow")){
		          		String [] split = line.split(" ");
		          		if (split.length == 2){
		          		String noCrawl = split[1].toLowerCase().trim();
		          		disallowed.add(noCrawl);
		          		}
		          	}
					
				}
				
				if (line.contains(name) || line.contains("user-agent: *")){
				    start = true;
				}
				
				else if (line.contains("user-agent: " )){
					start = false;
				}
			
			}
			robotCount++;
			prohibited.put(domainName, disallowed);
			
		} catch (MalformedURLException e) {
		  System.err.println("ERROR: Robot URL does not exist for " + robotTextString);
			//e.printStackTrace();
		}
		
		  catch (IOException e ){
			System.err.println("ERROR: Problem with reading robots.txt file for " + robotTextString);  
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
	
	public int getRobotCount(){
		return robotCount;
	}
	
	public int getFrontierSize(){
		return frontier.size();
	}
	
	public int getLinksVisted(){
		return pagesVisited;
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
