package textProcessing;

public class TextProcessingDriver {

	static Tokenizer tokenizer = new Tokenizer();
	static  StopWords stopWords = new StopWords();
	
	public static void main(String[] args) {
	  
     // tokenizer.addFileForProcessing("C:\Users\Richard\Desktop");
      tokenizer.readFile("C:\\Users\\Richard\\Documents\\CS446\\Project Docs\\P2\\test.txt");
		
		//stopWords.insertStopWordsFromFile("C:\\Users\\Richard\\Documents\\CS446\\Project Docs\\P2\\Stopwords.txt");
		System.out.println();
		//stopWords.printStopWords();
     tokenizer.printHashMap();
      
	}

}
