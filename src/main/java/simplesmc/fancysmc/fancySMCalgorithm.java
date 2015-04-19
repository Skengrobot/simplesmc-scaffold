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

/**
 * Modified SMC sampling scheme for a paper that I'll write, host somewhere and link.
 * 
 * @author Rudi Plesch
 */

public class fancySMCalgorithm<P> extends SMCAlgorithm<P>{

	private final int samplingInterval;
	private final int refreshInterval;
	
	/**
	 * 
	 * @param proposal	see super
	 * @param options	see super
	 * @param nSamplesPerRefresh  Number of sampling intervals to calculate intermediate likelihood over
	 * @param samplingInterval	number of new observations considered in new intermediate likelihood calculations
	 */
	public fancySMCalgorithm(ProblemSpecification<P> proposal,
			SMCOptions options, int samplingInterval, int nSamplesPerRefresh) {
		super(proposal, options);
		this.samplingInterval = samplingInterval;
		this.refreshInterval = samplingInterval*nSamplesPerRefresh;
	}
	
	/**
	 *	 
	 * @return Final particle population and List of intermediate log likelihoods.
	 */
	public Pair<ParticlePopulation<P>,ArrayList<Double>> fancySample() {
		LinkedList<Double> likelihoodQueue= new LinkedList<>();
		ArrayList<Double> intervalLikelihoods = new ArrayList<Double>();
		double intervalLikelihood = 0;
		
		ParticlePopulation<P> currentPopulation = propose(null, 0);
    
		int nSMCIterations = proposal.nIterations();
    
		// Here the intermediate likelihood queue it not full yet, so we just insert
		// At each sampling interval, the intermediate data likelihood is added to the running sum and 
		// appended to the linked list so we can keep track of them
		for (int currentIteration = 0; currentIteration <= this.refreshInterval; currentIteration++) {
			currentPopulation = propose(currentPopulation, currentIteration);
			currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);
			if (currentIteration!=0 && currentIteration % this.samplingInterval == 0){
				double intermediateLikelihood = currentPopulation.logNormEstimate();
				intervalLikelihood += intermediateLikelihood;
				likelihoodQueue.addFirst(intermediateLikelihood);
			}
		}
		
		// Here the intermediate likelihood is full so we push on likelihood and pop another, also add and subtract from the
		// running sum
		for (int currentIteration = this.refreshInterval + 1; currentIteration < nSMCIterations - 1; currentIteration++) {
			currentPopulation = propose(currentPopulation, currentIteration);
			currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);
			if (currentIteration % this.samplingInterval == 0){
				intervalLikelihood -= likelihoodQueue.removeLast();
				double intermediateLikelihood = currentPopulation.logNormEstimate();
				intervalLikelihood += intermediateLikelihood;
				likelihoodQueue.addFirst(intermediateLikelihood);
				
			}
		}
    
		return Pair.of(currentPopulation, intervalLikelihoods);
	}
	
}