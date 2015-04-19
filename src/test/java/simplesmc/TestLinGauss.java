package simplesmc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

import simplesmc.lingauss.LinGaussParams;
import simplesmc.lingauss.LinGaussProblemSpecification;
import simplesmc.lingauss.LinGaussUtils;
import bayonet.distributions.Normal;
import Jama.Matrix;

public class TestLinGauss {
	@Test
	public void testLinGauss(){
		Random random = new Random(1);
		
		// Parse csv file for observations
		ArrayList<ArrayList<Double>> observations = LinGaussUtils.parseFile("/home/rudi/src/simplesmc-scaffold/data-generators/big-noise.csv");

		// These parameters match the ones used to create the dataset
		double[][] transitionMatrix = {{0.965925826289068, -0.258819045102521},{0.258819045102521, 0.965925826289068}};
		double[][] emissionMatrix = {{3,1},{4,2}};
		double[] intialProbabilities = {1,1};
		LinGaussParams system = new LinGaussParams(2, 2, 0.01, 0.01, transitionMatrix, emissionMatrix, intialProbabilities);
		
		LinGaussProblemSpecification proposal = new LinGaussProblemSpecification(system, observations);
		
		SMCOptions options = new SMCOptions();
		SMCAlgorithm<ArrayList<Double>> smc = new SMCAlgorithm<>(proposal, options);
		System.out.println("estimate = " + smc.sample().logNormEstimate());
		
	}
	
}