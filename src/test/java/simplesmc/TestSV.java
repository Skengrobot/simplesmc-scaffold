package simplesmc;

import org.junit.Test;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import simplesmc.stochasticvolatility.SVParams;
import simplesmc.stochasticvolatility.SVProblemSpecification;
import simplesmc.stochasticvolatility.SVUtils;

import java.util.Random;

public class TestSV {
	@Test
	public void testSV() {
		Random random = new Random(1);
		SVParams params = new SVParams();
		
		Pair<ArrayList<Double>, ArrayList<Double>> generated = SVUtils.generate(random, params, 200);
		ArrayList<Double> observations = generated.getRight();
		
		SVProblemSpecification problem = new SVProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		SMCAlgorithm<Double> smc = new SMCAlgorithm<>(problem, options);
		System.out.println("estimate = " + smc.sample().logNormEstimate());
	}
}