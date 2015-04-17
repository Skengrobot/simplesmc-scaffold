package simplesmc.fancysmc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.SplittableRandom;

import org.apache.commons.lang3.tuple.Pair;

import simplesmc.ProblemSpecification;
import simplesmc.SMCAlgorithm;
import simplesmc.SMCOptions;
import bayonet.smc.ParticlePopulation;
import bayonet.smc.ResamplingScheme;

public class fancySMCalgorithm<P> extends SMCAlgorithm<P>{

	private final int samplingInterval;
	private final int refreshInterval;
	
	public fancySMCalgorithm(ProblemSpecification<P> proposal,
			SMCOptions options, int samplingInterval, int refreshInterval) {
		super(proposal, options);
		this.samplingInterval = samplingInterval;
		this.refreshInterval = refreshInterval;
	}
	
	/**
	 *	 
	 * @return Final particle population and List of intermediate log likelihoods.
	 */
	public Pair<ParticlePopulation<P>,ArrayList<Double>> fancySample() {
		LinkedList<Double> likelihoodQueue= new LinkedList<>();
		ArrayList<Double> intervalLikelihoods = new ArrayList<Double>(); 
		
		ParticlePopulation<P> currentPopulation = propose(null, 0);
    
		int nSMCIterations = proposal.nIterations();
    
		for (int currentIteration = 0; currentIteration < nSMCIterations - 1; currentIteration++) {
			currentPopulation = propose(currentPopulation, currentIteration);
			currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);
		}
    
		return Pair.of(currentPopulation, intervalLikelihoods);
	}
	
}