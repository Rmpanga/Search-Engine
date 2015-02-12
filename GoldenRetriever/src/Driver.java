import java.util.LinkedList;


public class Driver {
    public static LinkedList<String> seeds = new LinkedList<String>();
	
	public static void main(String[] args) {
		seeds.add("https://ciir.cs.umass.edu");
		Spider spider = new Spider(seeds);
		spider.explore(10, 1000 , false);
		spider.printProcessedURLs();
		System.out.println("The frontier still has " + spider.getFrontierSize() + " unique links");
		System.out.println("The crawler has visted " + spider.getLinksVisted() + " pages");
		System.out.println("Robots.txt found: " + spider.getRobotCount());
		System.out.println();
		System.out.println("Printing data for plot");
		
	}
}
