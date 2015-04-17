package simplesmc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

import simplesmc.lingauss.LinGaussUtils;
import bayonet.distributions.Normal;
import Jama.Matrix;

public class TestLinGauss {
	@Test
	public void testLinGauss(){
		Random random = new Random(1);
		
		// Parse csv file for observations
		ArrayList<ArrayList<Double>> observations = LinGaussUtils.parseFile("/home/rudi/src/simplesmc-scaffold/data-generators/test.csv");
		
		
	}
	
}