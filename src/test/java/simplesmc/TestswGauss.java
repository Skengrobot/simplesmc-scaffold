package simplesmc;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import simplesmc.switchinggauss.swGaussParams;
import simplesmc.switchinggauss.swGaussProblemSpecification;
import simplesmc.switchinggauss.swGaussUtils;

public class TestswGauss {
	@Test
	public void testSWGauss() {
		Random random = new Random(1);
		swGaussParams params = new swGaussParams();
		
		// Generate an observation set
		Pair<ArrayList<Pair<Integer, Double>>,ArrayList<Double>> samplePath = swGaussUtils.generate(random, params, 20);
		ArrayList<Double> observations = samplePath.getRight();
		
		swGaussProblemSpecification problem = new swGaussProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		SMCAlgorithm<Pair<Integer,Double>> smc = new SMCAlgorithm<>(problem, options);
		System.out.println("estimate = " + smc.sample().logNormEstimate());
	}
}