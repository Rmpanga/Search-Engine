import java.util.LinkedList;


public class Driver {
    public static LinkedList<String> seeds = new LinkedList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		seeds.add("https://ciir.cs.umass.edu");
		//seeds.add("https://ciir.cs.umass.edu/");
		Spider spider = new Spider(seeds);
		//spider.processRobot("https://ciir.cs.umass.edu");
		//spider.testBannedUrls();
		spider.explore(5,20 , false);
		//spider.testBannedUrls();
		spider.printProcessedURLs();
		//spider.getDomainName("https://ciir.cs.google.edu.com/index/php");
	}

}
