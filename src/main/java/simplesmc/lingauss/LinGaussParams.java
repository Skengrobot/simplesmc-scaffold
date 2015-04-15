package simplesmc.lingauss;

import java.util.ArrayList;
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
	//Don't support this mixing matrix currently
	//private Matrix emissionMatrix;
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
  private ArrayList<Double> shamefulIerativeBox(double[][] shame){
	  ArrayList<Double> boxed = new ArrayList<>();
	  for(int i=0; i<shame.length; i++)
		  boxed.add(i,shame[1][i]);
		  
	  return boxed;
  }
	
  public double initialLogPr(int state) {
	  // TODO
	  return 0.0;
  }
  
  public ArrayList<Double> sampleInitial(Random random) {
	  // TODO
	  return null;
  }
  
  public double transitionLogPr(ArrayList<Double> currentState, ArrayList<Double> nextState) {
	  // TODO
	  return 0.0;
  }
  
  public ArrayList<Double> sampleTransition(Random random, ArrayList<Double> currentState) {
	  Matrix stateVector = new Matrix(1,this.stateSpaceDim); 
	  for (int i=0; i<currentState.size(); i++)
		  stateVector.set(0, i,currentState.get(i));
	  Matrix noise = makeStateNoiseVector(random);
	  Matrix output = noise.plus(this.transitionMatrix.times(stateVector));
	  
	  return shamefulIerativeBox(output.getArray());
  }
  
  public double emissionLogPr(ArrayList<Double> latentState, ArrayList<Double> emission) {
	  double emissionProb = 0;
	  for(int i=0; i<this.observationDim; i++)
		  emissionProb += Normal.logDensity(emission.get(i), latentState.get(i), this.sigmaEmission);
	  
	  return emissionProb;
  }

  public ArrayList<Double> sampleEmission(Random random, ArrayList<Double> currentState) {
	  throw new RuntimeException();
  }
  
  public int stateSpaceDimesion() {
	  return this.stateSpaceDim;
  }

  public int observationDimesion() {
	  return this.observationDim;
  }
}