package simplesmc.lingauss;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
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
	private Matrix emissionMatrix;
	
	private double sigmaTransitionProposal;
	private double sigmaEmissionProposal;
	
  public LinGaussParams(int observationDim, int stateSpaceDim,
		  double sigmaTransitionProposal, double sigmaEmissionProposal,
		  double[][] transitionMatrix, double[][] emissionMatrix,
		  double[] initialProbabilities)  {
	  
	  this.observationDim = observationDim;
	  this.stateSpaceDim = stateSpaceDim;
	  this.sigmaEmissionProposal = sigmaEmissionProposal;
	  this.sigmaTransitionProposal = sigmaTransitionProposal;
	  
	  this.transitionMatrix = new Matrix(transitionMatrix);
	  this.emissionMatrix = new Matrix(emissionMatrix);
	  this.initialProbs = initialProbabilities;
	  
  }

  /**
  *	Make a vector of Gaussian randoms,
  */
  private Matrix makeStateNoiseVector(Random random){
	  Matrix noise = new Matrix(1,this.stateSpaceDim);
	  for(int i=0; i<this.stateSpaceDim; i++)
		  noise.set(0,i,Normal.generate(random, 0.0, this.sigmaTransitionProposal));
	  return noise.transpose();
  }
  
  /**
   * Ahem
   */
  private ArrayList<Double> shamefulIerativeBox(double[][] shame){
	  ArrayList<Double> boxed = new ArrayList<>();
	  for(int i=0; i<shame.length; i++)
		  boxed.add(shame[i][0]);
		  
	  return boxed;
  }
	
  public double initialLogPr(int state) {
	  // This is 0, since we assume we know the starting state
	  return 0.0;
  }
  
  public ArrayList<Double> sampleInitial(Random random) {
	  ArrayList<Double> state = new ArrayList<>(this.stateSpaceDim);
	  for (int i=0; i<this.stateSpaceDim; i++)
		  state.add(this.initialProbs[i]);
	  return state;
  }
  
  public double transitionLogPr(ArrayList<Double> currentState, ArrayList<Double> nextState) {
	  // Have to use the multivariate Gaussian from org.apachecommons.math3 
	  // This doesn't cause a random seeding problem because I'm only using it for likelihoods.
	  double[][] covariances = this.transitionMatrix.times(this.transitionMatrix.transpose()).getArray();
	  
	  // Put everything into primitive arrays
	  double[] current = new double[currentState.size()];
	  for(int i=0; i<currentState.size(); i++)
		  current[i] = currentState.get(i);
	  double[] next = new double[nextState.size()];
	  for(int i=0; i<nextState.size(); i++)
		  next[i] = nextState.get(i);
	  
	  // Get to work
	  MultivariateNormalDistribution dist = new MultivariateNormalDistribution(current, covariances);
	  double emissionProb = Math.log(dist.density(next));
	  
	  System.out.println(emissionProb);
	  return emissionProb;
  }
  
  public ArrayList<Double> sampleTransition(Random random, ArrayList<Double> currentState) {
	  Matrix stateVector = new Matrix(1,this.stateSpaceDim); 
	  for (int i=0; i<currentState.size(); i++)
		  stateVector.set(0, i,currentState.get(i));
	  Matrix noise = makeStateNoiseVector(random);
	  Matrix output = noise.plus(this.transitionMatrix.times(stateVector.transpose()));
	  
	  return shamefulIerativeBox(output.getArray());
  }
  
  public double emissionLogPr(ArrayList<Double> latentState, ArrayList<Double> emission) {
	  // Have to use the multivariate Gaussian from org.apachecommons.math3 
	  // This doesn't cause a random seeding problem because I'm only using it for likelihoods.
	  double[][] covariances = this.emissionMatrix.times(this.emissionMatrix.transpose()).getArray();
	  
	  // Put everything into primitive arrays
	  double[] emissionArray = new double[emission.size()];
	  for(int i=0; i<emission.size(); i++)
		  emissionArray[i] = emission.get(i);
	  double[] state = new double[latentState.size()];
	  for(int i=0; i<latentState.size(); i++)
		  emissionArray[i] = latentState.get(i);
	  
	  // Get to work
	  MultivariateNormalDistribution dist = new MultivariateNormalDistribution(state, covariances);
	  double emissionProb = Math.log(dist.density(emissionArray));
	  
	  return emissionProb;
  }

  public ArrayList<Double> sampleEmission(Random random, ArrayList<Double> currentState) {
	  // Don't think I need to use this
	  throw new RuntimeException();
  }
  
  public int stateSpaceDimesion() {
	  return this.stateSpaceDim;
  }

  public int observationDimesion() {
	  return this.observationDim;
  }
}