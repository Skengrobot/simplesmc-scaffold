package simplesmc.switchinggauss;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

import bayonet.distributions.Bernoulli;
import bayonet.distributions.Normal;

public class swGaussParams {
	
	// transition in each state
	public final double[] transition = {1.2, 0.9};
	// switching probability
	public final double selfTransition = 0.8;
	// noise variance
	public static double var = .01;
	
	public double initialLogPr(Pair<Integer, Double> state)
	{
	  return Math.log(0.5) + Normal.logDensity(state.getRight(), 0, var);
	}
	
	public Pair<Integer, Double> sampleInitial(Random random) {
		int mcstate = (random.nextBoolean()) ? 1 : 0;
		double dynamicstate = Normal.generateStandardNormal(random);
		return Pair.of(mcstate, dynamicstate);
	}
	
	public double transitionLogPr(Pair<Integer, Double> currentState, Pair<Integer, Double> nextState) {
		double mctrans = nextState.getLeft() == currentState.getLeft() ? Math.log(selfTransition) : Math.log(1-selfTransition); 
		double dynamictrans = Normal.logDensity(nextState.getRight(), transition[currentState.getLeft()], var);
		return mctrans + dynamictrans;
	}

	public Pair<Integer, Double> sampleTransition(Random random, Pair<Integer, Double> currentState) {
		// Sample Markov chain transition
		int nextmc;
		if (Bernoulli.generate(random, selfTransition))
			nextmc = currentState.getLeft();
		else
			nextmc = Math.abs(1-currentState.getLeft()); // because java can't convert from int to bool
		
		double dynamictran = Normal.generate(random, transition[nextmc], var);
		
		return Pair.of(nextmc, dynamictran);
	}
	
	public Pair<Integer, Double> sampleChangedTransition(Random random, Pair<Integer, Double> currentState) {
		// Sample Markov chain transition
		int nextmc;
		if (Bernoulli.generate(random, 1-selfTransition))
			nextmc = currentState.getLeft();
		else
			nextmc = Math.abs(1-currentState.getLeft()); // because java can't convert from int to bool
		
		double dynamictran = Normal.generate(random, transition[nextmc], var*var);
		
		return Pair.of(nextmc, dynamictran);
	}
	
	public double emissionLogPr(Pair<Integer, Double> state, Double observation) {
		return Normal.logDensity(observation, 0, var);
	}
	
	public double sampleEmission(Random random, Pair<Integer, Double> state) {
		return Normal.generate(random, transition[state.getLeft()] * state.getRight(), var);
	}
	
	
}