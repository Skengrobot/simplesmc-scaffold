package simplesmc.lingauss;

import java.util.Random;

import bayonet.distributions.Normal;
import Jama.Matrix;
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
	
	private double[] initialProbs;
	private Matrix transitionMatrix;
	private double sigmaStateTransition;
	private Matrix emissionMatrix;
	private double sigmaEmission;
	
  /**
  *	Make a vector of Gaussian randoms,
  */
  private Matrix makeStateNoiseVector(Random random){
	  Matrix noise = new Matrix(1,this.stateSpaceDim);
	  for(int i=0; i<this.stateSpaceDim; i++)
		  noise.set(0,i,Normal.generate(random, 0.0, this.sigmaStateTransition));
	  return noise;
  }
  
  /**
   * Ahem
   */
  private Double[] shamefulIerativeBox(double[][] shame){
	  Double[] boxed = new Double[this.];
	  for(int i=0; i<shame.length; i++)
		  boxed[i] = shame[1][i];
		  
	  return boxed;
  }
	
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
	  Matrix stateVector = new Matrix(1,this.stateSpaceDim); 
	  for (int i=0; i<currentState.length; i++)
		  stateVector.set(0, i,currentState[i]);
	  Matrix noise = makeStateNoiseVector(random);
	  Matrix output = noise.plus(this.transitionMatrix.times(stateVector));
	  
	  return shamefulIerativeBox(output.getArray());
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