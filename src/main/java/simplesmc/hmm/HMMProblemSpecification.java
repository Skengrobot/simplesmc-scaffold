package simplesmc.hmm;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import simplesmc.ProblemSpecification;
import simplesmc.pmcmc.WithSignature;


/**
 * The specification of an SMC algorithm based on an HMM.
 * 
 * More precisely, this builds the so called 'bootstrap sampler'
 * which consists in sampling from the transition probability, and
 * updating the weights with the emission probability.
 * 
 * Note that it does not requires the HMMParams' state space to 
 * be finite.
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class HMMProblemSpecification implements ProblemSpecification<Integer>
  
{
  private final ToyHMMParams parameters;
  private final List<Integer> observations;
  
  public HMMProblemSpecification(ToyHMMParams parameters, List<Integer> observations)
  {
    this.parameters = parameters;
    this.observations = observations;
  }
  
  /**
   * Computes a proposal and the LOG weight update for that proposed particle.
   * 
   * @param currentSmcIteration The index of particle currentParticle (0, 1, 2, ..)
   * @param random
   * @param currentParticle
   * @return A pair of (1) LOG weight update, and (2) proposed particle
   */
  public Pair<Double, Integer>  proposeNext(int currentSmcIteration, Random random, Integer currentParticle) {
	  int proposedParticle = this.parameters.sampleTransition(random, currentParticle);
	  double weightUpdate = this.parameters.sampleEmission(random, proposedParticle);
	  Pair<Double, Integer> update= Pair.of(weightUpdate, proposedParticle);
	  return update;
  }
  /**
   * 
   * @param random
   * @return A pair of (1) LOG weight update, and (2) proposed particle for the zeroth iteration
   */
  public Pair<Double, Integer>  proposeInitial(Random random) {
	  Integer particle = this.parameters.sampleInitial(random);
	  int emission = this.parameters.sampleEmission(random, particle);
	  Double weight = this.parameters.emissionLogPr(particle,emission);
	  Pair<Double, Integer> initialProposal = Pair.of(weight, particle); 
	  return initialProposal;
  }
  
  /**
   * @return Number of iterations, including the initial step. For example, this is the length of
   *   the chain in an HMM context
   */
  public int nIterations() {
	  return this.observations.size(); 
  }
   
}
