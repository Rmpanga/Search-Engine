import java.util.LinkedList;


public class Driver {
    public static LinkedList<String> seeds = new LinkedList<String>();
	
	public static void main(String[] args) {
		seeds.add("https://ciir.cs.umass.edu");
		Spider spider = new Spider(seeds);
		spider.explore(1, 100 , true);
		spider.printProcessedURLs();
		System.out.println("The frontier still has " + spider.getFrontierSize() + " unique links");
		System.out.println("The crawler has visted " + spider.getLinksVisted() + " pages");
	}

}
