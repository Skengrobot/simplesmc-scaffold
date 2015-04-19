package simplesmc.fancysmc;

import java.util.LinkedList;

/**
 * 	Some helper functions
 * 
 * @author Rudi Plesch
 */

public class fancySMCutils {
	
	public static void printQueue(LinkedList<Double> q){
		for (Double item : q) {
			System.out.print(item);
			System.out.print(',');
		}
		System.out.println();
	}
}