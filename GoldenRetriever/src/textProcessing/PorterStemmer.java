package textProcessing;

import java.util.ArrayList;
import java.util.Hashtable;

public class PorterStemmer {

	private Hashtable<Character , Boolean> vowels;
	
	public PorterStemmer (){
		initialize();
	}
	
	private  void initialize(){
		vowels = new Hashtable<Character, Boolean>();
		vowels.put('a', true);
		vowels.put('e', true);
		vowels.put('i', true);
		vowels.put('o', true);
		vowels.put('u', true);
	}
	
	
	public String porterStemmer(String word){
	 
	 //If suffix is us or ss do nothing (e.g., stress —> stress).
	 if (word.endsWith("ss") || word.endsWith("us")){
		 return word;
	 }
	 
	 StringBuilder stemmer = new StringBuilder(word);
	 
	 
	 //Replace sses by ss (e.g., stresses —> stress)
		if (word.contains("sses")){
			int idx = stemmer.indexOf("sses");
			
			stemmer.replace(idx, idx+4, "ss");
		    word = stemmer.toString();
		  //  stemmer = new StringBuilder(word);
		}
		
		//Delete s if the preceding word part contains a vowel not immediately before
		//the s (e.g., gaps —> gap but gas —> gas).
		if (word.charAt(word.length()-1) == 's' && vowels.get(word.charAt(word.length() -2)) == null ){
			for (int j = 0; j <word.length()-2; j++){
				System.out.println("Char @ j : " + j);
				if (vowels.get(word.charAt(j)) != null){
					stemmer.deleteCharAt(word.length()- 1);
					word = stemmer.toString();
				//	stemmer = new StringBuilder(word);
					break;
				}
			}	
		}
		
		//Replace ied or ies by  if preceded by more than one letter, otherwise by ie
		//(e.g., ties —»tie, cries —» cri)
		if (word.contains("ied")){
			System.out.println("Word Contains ied");
			int idx  = stemmer.indexOf("ied");
			if (idx > 1)
			stemmer.replace(idx, idx+3, "i");
			else
			stemmer.replace(idx, idx+3, "ie");
			
			word = stemmer.toString();
		//	stemmer = new StringBuilder(word);
		}
		else if (word.contains("ies")){
			System.out.println("Word Contains ies");
			int idx  = stemmer.indexOf("ies");
			if (idx > 1)
			stemmer.replace(idx, idx+3, "i");
			else
			stemmer.replace(idx, idx+3, "ie");
			
			word = stemmer.toString();
		//	stemmer = new StringBuilder(word);
		}
		
		if (word.contains("eedly")){
			System.out.println("Word Contains eedly");
			boolean vowelHit = false;
			int idx = stemmer.indexOf("eedly");
			for (int j = 0; j < idx; j ++){
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+5, "ee");
						word = stemmer.toString();
						break;
					}
				}
			}
			
		}
		
		//Replace eed, eedly by ee if it is in the part of the word after the first nonvowel
		//following a vowel (e.g., agreed —> agree, feed —> feed).
		
		if (word.contains("eed")){
			System.out.println("Word Contains eed");
			boolean vowelHit = false;
			int idx = stemmer.indexOf("eed");
			for (int j = 0; j < idx; j ++){
				System.out.println("for loop");
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+3, "ee");
						word = stemmer.toString();
						break;
					}
				}
			}
			
		}
		
		if (word.contains("edly")){
			System.out.println("The word " + word + "contains edly");
			boolean vowelHit = false;
			boolean repeatedLetters = false; 
			int idx = stemmer.indexOf("edly");
			for (int j = 0; j < idx; j ++){
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				System.out.println("Word size " + word.length());
				//System.out.println("Char At J is " + word.charAt(j));
			/*	if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
						repeatedLetters = true;
					}
				} */
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+4, "");
						word = stemmer.toString();
					
						if (word.endsWith("at")){
							stemmer.append('e');
							word = stemmer.toString();
						}
						
						else if (word.endsWith("bl")){
							stemmer.append('e');
							word = stemmer.toString();
						}
							
							
						}
						else if (word.endsWith("iz")){
							stemmer.append('e');
							word = stemmer.toString();
							
						}
					
					
				}
			
			}
			for (int j = 0; j < word.length()-1; j++){
				if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
					repeatedLetters = true;
					}
			
					if (repeatedLetters){
				
						stemmer.deleteCharAt(stemmer.length()-1);
						word = stemmer.toString();
						return word; 
					}
				}
			
			}	
		
		}
		
		if (word.contains("ingly")){
			System.out.println("The word " + word + "contains ingly");
			boolean vowelHit = false;
			boolean repeatedLetters = false; 
			int idx = stemmer.indexOf("ed");
			for (int j = 0; j < idx; j++){
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				System.out.println("Word size " + word.length());
				//System.out.println("Char At J is " + word.charAt(j));
			/*	if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
						repeatedLetters = true;
					}
				} */
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+5, "");
						word = stemmer.toString();
					
						if (word.endsWith("at")){
							stemmer.append('e');
							word = stemmer.toString();
						}
						
						else if (word.endsWith("bl")){
							stemmer.append('e');
							word = stemmer.toString();
						}
							
							
						}
						else if (word.endsWith("iz")){
							stemmer.append('e');
							word = stemmer.toString();
							
						}
					
					
				}
			
			}
			for (int j = 0; j < word.length()-1; j++){
				if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
					repeatedLetters = true;
					}
			
					if (repeatedLetters){
				
						stemmer.deleteCharAt(stemmer.length()-1);
						word = stemmer.toString();
						return word; 
					}
				}
			
			}	
		
		}
		
		if (word.contains("ed")){
			System.out.println("The word " + word + "contains ed");
			boolean vowelHit = false;
			boolean repeatedLetters = false; 
			int idx = stemmer.indexOf("ed");
			for (int j = 0; j < idx; j ++){
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				System.out.println("Word size " + word.length());
				//System.out.println("Char At J is " + word.charAt(j));
			/*	if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
						repeatedLetters = true;
					}
				} */
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+2, "");
						word = stemmer.toString();
					
						if (word.endsWith("at")){
							stemmer.append('e');
							word = stemmer.toString();
						}
						
						else if (word.endsWith("bl")){
							stemmer.append('e');
							word = stemmer.toString();
						}
							
							
						}
						else if (word.endsWith("iz")){
							stemmer.append('e');
							word = stemmer.toString();
							
						}
					
					
				}
			
			}
			for (int j = 0; j < word.length()-1; j++){
				if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
					repeatedLetters = true;
					}
			
					if (repeatedLetters){
				
						stemmer.deleteCharAt(stemmer.length()-1);
						word = stemmer.toString();
						return word; 
					}
				}
			
			}	
		
		}
		
		if (word.contains("ing")){
			System.out.println("The word " + word + "contains ing");
			boolean vowelHit = false;
			boolean repeatedLetters = false; 
			int idx = stemmer.indexOf("ing");
			for (int j = 0; j < idx; j ++){
				if (vowels.get(word.charAt(j)) != null){
					vowelHit = true; 
				}
				System.out.println("Word size " + word.length());
				//System.out.println("Char At J is " + word.charAt(j));
			/*	if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
						repeatedLetters = true;
					}
				} */
				if (vowelHit){
					if (vowels.get(word.charAt(j)) == null){
						stemmer.replace(idx, idx+3, "");
						word = stemmer.toString();
					
						if (word.endsWith("at")){
							stemmer.append('e');
							word = stemmer.toString();
						}
						
						else if (word.endsWith("bl")){
							stemmer.append('e');
							word = stemmer.toString();
						}
							
							
						}
						else if (word.endsWith("iz")){
							stemmer.append('e');
							word = stemmer.toString();
							
						}
					
					
				}
			
			}
			for (int j = 0; j < word.length()-1; j++){
			//System.out.println("Repeats ? " + word.charAt(j) + word.charAt(j+1));
				if (word.charAt(j) == word.charAt(j+1)){
					if (word.charAt(j)!= 'l' && word.charAt(j) !='s' && word.charAt(j) != 'z'){
					System.out.println("Hello");
					repeatedLetters = true;
					}
			
					if (repeatedLetters){
				
						stemmer.deleteCharAt(stemmer.length()-1);
						word = stemmer.toString();
						return word; 
					}
				}
			
			}	
		
		}
		
		
		return word;
	}
	
	
	public static void  main (String [] arg){
		
		//initialize();
		PorterStemmer s = new PorterStemmer();
		
		System.out.println(s.porterStemmer("testing123"));
	
		
		
	
	}
	
	
	
}
