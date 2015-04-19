package simplesmc.lingauss;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 	Some helper functions for the linear Gaussian model
 * 
 * @author Rudi Plesch
 */

public class LinGaussUtils {
	
	public static ArrayList<ArrayList<Double>> parseFile(String fileName){
		
		ArrayList<ArrayList<Double>> observations = new ArrayList<ArrayList<Double>>();
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(fileName));
			
			while ((line = br.readLine())!=null){
				String[] strs = line.split(",");  
				ArrayList<Double> state = new ArrayList<>();
				for (String s : strs)
					state.add(Double.parseDouble(s));
				observations.add(state);
			}
		}
		catch (IOException e){e.printStackTrace();}
		finally {
			try
				{if(br!=null){br.close();}}
			catch (IOException e){e.printStackTrace();}
		}
		
		return observations;
	}
}