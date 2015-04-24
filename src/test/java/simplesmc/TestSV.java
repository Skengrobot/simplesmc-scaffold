package simplesmc;

import org.junit.Test;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import bayonet.smc.ParticlePopulation;
import simplesmc.fancysmc.fancySMCalgorithm;
import simplesmc.stochasticvolatility.SVParams;
import simplesmc.stochasticvolatility.SVProblemSpecification;
import simplesmc.stochasticvolatility.SVUtils;

import java.util.Random;

public class TestSV {
	//@Test
	public void testSV() {
		Random random = new Random(1);
		SVParams params = new SVParams();
		
		Pair<ArrayList<Double>, ArrayList<Double>> generated = SVUtils.generate(random, params, 20);
		ArrayList<Double> observations = generated.getRight();
		
		SVProblemSpecification problem = new SVProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		SMCAlgorithm<Double> smc = new SMCAlgorithm<>(problem, options);
		System.out.println("estimate = " + smc.sample().logNormEstimate());
	}
	
	//@Test
	public void testSVFancySample() {
		Random random = new Random(1);
		SVParams params = new SVParams();
		
		Pair<ArrayList<Double>, ArrayList<Double>> generated = SVUtils.generate(random, params, 500);
		ArrayList<Double> observations = generated.getRight();
		
		SVProblemSpecification problem = new SVProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Double> smc = new fancySMCalgorithm<>(problem , options, 10, 1);
		Pair<ParticlePopulation<Double>, ArrayList<Double>> output = smc.fancySample();
		System.out.println("Final log likelihood = " + output.getLeft().logNormEstimate());
		System.out.println();
	}
	
	@Test
	public void testSVChangepoint() {
		Random random = new Random(5);
		SVParams params = new SVParams();
		
		System.out.println("GO!");
		Pair<ArrayList<Double>, ArrayList<Double>> generated = SVUtils.generateWithChangepoint(random, params, 1000);
		ArrayList<Double> observations = generated.getRight();
		ArrayList<Double> state = generated.getLeft();
		
		SVProblemSpecification problem = new SVProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Double> smc = new fancySMCalgorithm<>(problem , options, 20, 1);
		Pair<ParticlePopulation<Double>, ArrayList<Double>> output = smc.fancySample();
		System.out.println("Final log likelihood = " + output.getLeft().logNormEstimate());
		
	}
	
}