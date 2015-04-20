package simplesmc.fancysmc;

import java.util.LinkedList;

/**
 * 	Some helper functions
 * 
 * @author Rudi Plesch
 */

public class fancySMCutils {
	
	public static void printQueue(LinkedList<Double> q){
		double total = 0;
		for (Double item : q) {
			System.out.print(item);
			System.out.print(',');
			total += item;
		}
		System.out.println(" Total: " + total);
	}
}