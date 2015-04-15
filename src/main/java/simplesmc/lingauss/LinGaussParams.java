package simplesmc.lingauss;

import java.util.Random;

/**
 *
 * Parameters for a simple linear Gaussian models
 * 
 * State transition, dimension of latent states and emissions,
 * noise covariance and all that.
 * 
 * @author Rudi Plesch (skengrobot@gmail.com)
 * 
*/

public class LinGaussParams {
	
	private int observationDim;
	private int stateSpaceDim;
	
  public double initialLogPr(int state) {
	  // TODO
	  return 0.0;
  }
  
  public Double[] sampleInitial(Random random) {
	  // TODO
	  return null;
  }
  
  public double transitionLogPr(Double[] currentState, int nextState) {
	  // TODO
	  return 0.0;
  }
  
  public Double[] sampleTransition(Random random, Double[] currentState) {
	  // TODO
	  return null;
  }
  
  public double emissionLogPr(Double[] latentState, Double[] emission) {
	  // TODO
	  return 0.0;
  }

  public Double[] sampleEmission(Random random, Double[] currentState) {
	  return null;
  }
  
  public int stateSpaceDimesion() {
	  return this.stateSpaceDim;
  }

  public int observationDimesion() {
	  return this.observationDim;
  }
}