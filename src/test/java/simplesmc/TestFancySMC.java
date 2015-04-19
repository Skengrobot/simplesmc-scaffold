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


public class TestFancySMC {
	
	@Test
	public void testFancySMC() {
		Random random = new Random(1);
		ToyHMMParams hmmParams = new ToyHMMParams(5);
    
		Pair<List<Integer>, List<Integer>> generated = HMMUtils.generate(random, hmmParams, 200);
		List<Integer> observations = generated.getRight();
    
		System.out.println("exact = " + HMMUtils.exactDataLogProbability(hmmParams, observations));
    
		HMMProblemSpecification proposal = new HMMProblemSpecification(hmmParams, observations);
    
		SMCOptions options = new SMCOptions();
		fancySMCalgorithm<Integer> smc = new fancySMCalgorithm<>(proposal, options, 10, 4);
		Pair<ParticlePopulation<Integer>, ArrayList<Double>> output = smc.fancySample();
		System.out.println("Final log likelihood = " + output.getLeft().logNormEstimate());
	}
}