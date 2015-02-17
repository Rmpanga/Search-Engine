package textProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StopWords {
	
	//Advantage of storing boolean 
	// Can turn stop word off
	
	private HashMap<String , Boolean> stopwords;
	
	public StopWords(){
		stopwords = new HashMap<String , Boolean>();
	}
	
	
	public void insertStopWordsFromFile(String file){
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = in.readLine()) != null){
				line = line.toLowerCase().trim();
				String [] words = line.split(" ");
				
				for (int j = 0; j< words.length; j ++){
					if (!stopwords.containsKey(words[j])){
						stopwords.put(words[j], true); 
					}
				}	
			}
		
		} 
		catch(IOException e){
			System.err.println("Problem reading stop words file");
		}
	}
	
	public void insertStopWord(String word){
		
		if (!stopwords.containsKey(word)){
			stopwords.put(word, true);
			return;
		}

	 System.out.println("Already contains stop word ");
	}
	
	public boolean isStopWord(String word){
		
		
		if (stopwords.containsKey(word)){
			return stopwords.get(word);
		}
		return false; 
	}
	
	public void changeStopWord (String word , boolean value ){
		
		if (stopwords.containsKey(word)){
			stopwords.put(word , value);
			return;
		}
		System.out.println("Did not change stopword value, word does not exist");
	}
	

}
 