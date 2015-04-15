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
  
  public double[] sampleInitial(Random random) {
	  // TODO
	  return null;
  }
  
  public double transitionLogPr(int currentState, int nextState) {
	  // TODO
	  return 0.0;
  }
  
  public double[] sampleTransition(Random random, int currentState) {
	  // TODO
	  return null;
  }
  
  public double emissionLogPr(int latentState, int emission) {
	  // TODO
	  return 0.0;
  }

  public double[] sampleEmission(Random random, int currentState) {
	  return null;
  }
  
  public int stateSpaceDimesion() {
	  return this.stateSpaceDim;
  }

  public int observationDimesion() {
	  return this.observationDim;
  }
}