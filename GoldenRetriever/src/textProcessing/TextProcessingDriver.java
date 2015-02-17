package textProcessing;

public class TextProcessingDriver {

	static Tokenizer tokenizer = new Tokenizer();
	static  StopWords stopWords = new StopWords();
	
	public static void main(String[] args) {
	  
     // tokenizer.addFileForProcessing("C:\Users\Richard\Desktop");
      tokenizer.readFile("C:\\Users\\Richard\\Desktop\\test.txt");
      System.out.println();
      tokenizer.printHashMap();
      
	}

}
