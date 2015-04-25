package simplesmc;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import bayonet.smc.ParticlePopulation;
import simplesmc.fancysmc.fancySMCalgorithm;
import simplesmc.switchinggauss.swGaussParams;
import simplesmc.switchinggauss.swGaussProblemSpecification;
import simplesmc.switchinggauss.swGaussUtils;

public class TestswGauss {
	@Test
	public void testSWGauss() {
		Random random = new Random(3);
		swGaussParams params = new swGaussParams();
		
		// Generate an observation set
		Pair<ArrayList<Pair<Integer, Double>>,ArrayList<Double>> samplePath = swGaussUtils.generateWithChangepoint(random, params, 5000);
		ArrayList<Double> observations = samplePath.getRight();
		
		swGaussProblemSpecification problem = new swGaussProblemSpecification(params, observations);
		
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Pair<Integer,Double>> smc = new fancySMCalgorithm<>(problem, options,40,1);
		Pair<ParticlePopulation<Pair<Integer, Double>>, ArrayList<Double>> output = smc.fancySample();
		System.out.println("Final log likelihood = " + output.getLeft().logNormEstimate());
	}
}