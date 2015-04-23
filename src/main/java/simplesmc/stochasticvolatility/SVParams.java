package simplesmc.stochasticvolatility;

import java.util.Random;

import bayonet.distributions.Normal;

public class SVParams {
	public double alpha = 0.91;
	public double sigma = 1.0;
	public double beta = 0.5;
	 
	public double initialLogPr(double state) {
		return Normal.logDensity(state, 0, sigma*sigma/(1-alpha*alpha));
	}
	  
	public double sampleInitial(Random random){
		return Normal.generate(random, 0, sigma*sigma/(1-alpha*alpha));
	}
	  
	public double transitionLogPr(double currentState, double nextState) {
		return Normal.logDensity(nextState, alpha*currentState, sigma*sigma);
	}
	
	public double sampleTransition(Random random, double currentState) {
		double noise = sigma * Normal.generate(random, 0, 1);
		return alpha * currentState + noise;
	}
	
	public double sampleChangedTransition(Random random, double currentState) {
		double noise = 10*sigma * Normal.generate(random, 0, 1);
		return alpha * currentState + noise;
	}
	  
	public double emissionLogPr(double latentState, double emission) {
		return Normal.logDensity(emission, 0, beta*beta*Math.exp(latentState));
	}
	
	public double sampleEmission(Random random, double currentState) {
		return beta*Math.exp(currentState/2) * Normal.generate(random, 0, 1);
	}
	  
}