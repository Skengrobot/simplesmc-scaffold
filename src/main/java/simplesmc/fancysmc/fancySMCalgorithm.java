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
 * Modified SMC sampling scheme for a paper that I'll write, host somewhere, and link.
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
		double lastLikelihood = 0;
		
		ParticlePopulation<P> currentPopulation = propose(null, 0);
    
		int nSMCIterations = proposal.nIterations();
		
		//Fill queue with 0s to start
		for (int i=0; i<this.refreshInterval/this.samplingInterval; i++)
			likelihoodQueue.addFirst(0.0);
    
		// At each sampling interval, the intermediate data likelihood is computed
		// appended to the linked list so we can keep track of them
		// Push one likelihood and pop another, also add and subtract from the running sum
		currentPopulation = propose(currentPopulation, 0);
		currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);
		for (int currentIteration = 1; currentIteration < nSMCIterations - 1; currentIteration++) {
			currentPopulation = propose(currentPopulation, currentIteration);
			currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);

			// At the end of each sampling interval, compute intermediate likelihoods
			if (currentIteration % this.samplingInterval == 0){
				intervalLikelihood -= likelihoodQueue.removeLast();
				double currentLikelihood = currentPopulation.logNormEstimate();
				double intermediateLikelihood = currentLikelihood - lastLikelihood;
				lastLikelihood = currentLikelihood;
				intervalLikelihood += intermediateLikelihood;
				likelihoodQueue.addFirst(intermediateLikelihood);
				intervalLikelihoods.add(intervalLikelihood);
				fancySMCutils.printQueue(likelihoodQueue);
			}
		}
    
		return Pair.of(currentPopulation, intervalLikelihoods);
	}
	
}