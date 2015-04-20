package simplesmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import bayonet.smc.ParticlePopulation;
import simplesmc.fancysmc.fancySMCalgorithm;
import simplesmc.hmm.HMMProblemSpecification;
import simplesmc.hmm.HMMUtils;
import simplesmc.hmm.ToyHMMParams;
import simplesmc.lingauss.LinGaussParams;
import simplesmc.lingauss.LinGaussProblemSpecification;
import simplesmc.lingauss.LinGaussUtils;


public class TestFancySMC {
	
	@Test
	public void testFancySMC() {
		Random random = new Random(1);
		ToyHMMParams hmmParams = new ToyHMMParams(5);
    
		Pair<List<Integer>, List<Integer>> generated = HMMUtils.generate(random, hmmParams, 400);
		List<Integer> observations = generated.getRight();
    
		System.out.println("exact = " + HMMUtils.exactDataLogProbability(hmmParams, observations));
    
		HMMProblemSpecification proposal = new HMMProblemSpecification(hmmParams, observations);
    
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Integer> smc = new fancySMCalgorithm<>(proposal, options, 10, 4);
		Pair<ParticlePopulation<Integer>, ArrayList<Double>> output = smc.fancySample();
		System.out.println("Final log likelihood = " + output.getLeft().logNormEstimate());
	}
	
	/**
	 * See  TestLinGauss if you want to know more about usage
	 * 
	 * This CSV file contains a change in noise parameter at 200
	 */
	//@Test
	public void testFancyWithGaussian() {
		ArrayList<ArrayList<Double>> observations = LinGaussUtils.parseFile("/home/rudi/src/simplesmc-scaffold/data-generators/small-big.csv");

		// These parameters match the ones used to create the 
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