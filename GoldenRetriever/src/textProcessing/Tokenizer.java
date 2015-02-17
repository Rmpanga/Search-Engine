package textProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Tokenizer {

	static HashMap  <String, Integer> tokens = new HashMap<String , Integer> (); 
	LinkedList<String> waitingFiles = new LinkedList<String> ();
	
	
	public void addFileForProcessing(String file){
		if (!waitingFiles.contains(file))
			waitingFiles.offer(file);
	}
	
	public void readFile(String file){
		
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
						String line = null;
			while ((line = in.readLine()) != null){
				System.out.println("What is this line: " + line);
				line = line.toLowerCase().trim().replaceAll("\\.", "");     //.replaceAll(".", ""); Replaced all characters (almost all)
				System.out.println("What is this line after trim: " + line);
	
				ArrayList<String> newTokens =	tokenize(line);
				
				for (int j = 0; j<newTokens.size(); j++){
					
					if (tokens.containsKey(newTokens.get(j))){
						tokens.put( newTokens.get(j), tokens.get(newTokens.get(j)) + 1 );
					}
					else if (!tokens.containsKey(newTokens.get(j))){
						tokens.put(newTokens.get(j), 1);
						
					}
					
				}

			
			}
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
			
		
	}
	
	/*Ugly but functional
	 * Problem [ Richard  | Best | fea | tures | null | null | null | null | ]
	 * ArrayList Remove method shifts the words down
	 * Find a way to utilize this feature 
	 * 
	 * 
	 * 
	 */
	
	private static ArrayList<String> tokenize(String line){ //Ugly but functionally
		//String [] pretokens = line.split(" ");
		
		///pretokens.to
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(line.split(" ")));
		
		
	
		for (int j = 0; j < tokens.size(); j++){
			String unprocessedToken = tokens.get(j);	
			//System.out.println("Unprocessed Token: " + unprocessedToken);
			if (unprocessedToken != null && unprocessedToken.contains(",") ){
				if (unprocessedToken.charAt(unprocessedToken.length()-1) != ','){
					String [] newTokens = unprocessedToken.split(",");
					tokens.set(j, null);
					tokens = insertNewTokens(newTokens, tokens);
					
					
				}
				else {
					//remove comma at the end
				}
			}
			 
			 unprocessedToken = tokens.get(j);	
			 System.out.println("After Comma Check: " + unprocessedToken);
			if (unprocessedToken != null && unprocessedToken.contains("'")){
				if (unprocessedToken.charAt(unprocessedToken.length()-1) != '\''){
					String [] newTokens = unprocessedToken.split("'");
					tokens.set(j, null);
					tokens = insertNewTokens(newTokens, tokens);
					
				}
			}
			
			 unprocessedToken = tokens.get(j);	
			// System.out.println("After Apostophre  Check: " + unprocessedToken);
			if (unprocessedToken != null && unprocessedToken.contains("\"")){
				if (unprocessedToken.charAt(unprocessedToken.length()-1) != '\"'){
					String [] newTokens = unprocessedToken.split("\"");
					tokens.set(j, null);
					tokens = insertNewTokens(newTokens , tokens);
				}
				
				
			}
			System.out.println("Processed Token: " + tokens.get(j));
			
			
			tokens.removeAll(Collections.singleton(null));
		}
		
			    
		return tokens;
	}
	
	private static ArrayList<String> insertNewTokens(String [] nt, ArrayList<String> t){
		ArrayList<String> tokens = t;
		String [] newTokens = nt;
		int idx = 0;
		
		for (int j = 0; j < tokens.size(); j++){
			
			if (tokens.get(j) == null && idx < newTokens.length){
			    tokens.add(j , newTokens[idx++]);
			}
			
			if (idx >= newTokens.length)
				return tokens;
		}
		
		if (idx < newTokens.length){
			for (int i = idx; i < newTokens.length; i++){
				tokens.add(newTokens[i]);
			}	
		}
		
		
		return tokens;
	}
	
	
	public void printHashMap(){
		System.out.println();
		System.out.println("Printing  hashmap values");
		  for (String key : tokens.keySet()) {
		        System.out.println(key + " : " + tokens.get(key));
		    }

	}
	
	
	
	public static void main(String [] args){
		
	 String test = "Richard,s best's feature's are,s he's mi,nds Richard";
	 
	 ArrayList<String> t;
	 
	 t = tokenize(test);
	 System.out.println();
	 
	 System.out.println(t.size());
	 
		for (int j = 0; j<t.size(); j++){
			
			if (tokens.containsKey(t.get(j))){
				tokens.put( t.get(j), tokens.get(t.get(j)) + 1 );
			}
			else if (!tokens.containsKey(t.get(j))){
				tokens.put(t.get(j), 1);
				
			}
			
		}
	 
		System.out.println(tokens.get("h"));
		

	 //System.out.println(t.get(0));
	 
	 
	 /*
	 for (int j = 0; j < t.size(); j++){
		
		 if (t.get(j) != null)
	   System.out.println(t.get(j));
		 else {
			 System.out.println("I should not see this!!!!!!!!!!! : " + j);
			 break;
		 }
	}
	 
    
	 */
	 
	
	//System.out.println(testArray[0]);
		/*
		tokens.put("the", 4);
	//	tokens.put("abc", 4);
		
		for (String key : tokens.keySet()){
			if (tokens.containsKey("the"))
			{
			int value = tokens.get(key) + 1;
			tokens.put(key, tokens.get(key) + 1);
			}
			System.out.println(tokens.get(key));
		}
	*/	
	}
	
}
