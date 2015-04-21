package simplesmc;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import bayonet.smc.ParticlePopulation;
import simplesmc.fancysmc.fancySMCalgorithm;
import simplesmc.iidGuassian.*;

public class TestIIDGaussian {
	@Test
	public void testIIDGaussian() {
		iidGaussianProblemSpecification problem = new iidGaussianProblemSpecification(
			"/home/rudi/src/simplesmc-scaffold/data-generators/datasets/gauss-mix.csv", 0, 0.01);
	
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Double> smc = new fancySMCalgorithm<>(problem, options, 10, 2);
		Pair<ParticlePopulation<Double>, ArrayList<Double>> output = smc.fancySample();
	
		System.out.println("Final log likelihood estimate: " + output.getLeft().logNormEstimate());
		System.out.println("Exact log likelihood: " + problem.getFinalLogLikelihood());
	}
}