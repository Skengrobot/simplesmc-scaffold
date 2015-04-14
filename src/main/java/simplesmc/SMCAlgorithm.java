package simplesmc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;

import bayonet.smc.ParticlePopulation;
import bayonet.smc.ResamplingScheme;


/**
 * An SMC algorithm using multi-threading for proposing and suitable
 * for abstract 'SMC samplers' problems as well as more classical ones.
 * 
 * Also performs adaptive re-sampling by monitoring ESS.
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 * @param <P> The type (class) of the individual particles
 */
public class SMCAlgorithm<P>
{
  public final ProblemSpecification<P> proposal;
  private final SMCOptions options;
  
  /**
   * This is used to ensure that the result is deterministic even in a 
   * multi-threading context: each particle index has its own unique random 
   * stream
   */
  private final Random[] randoms;
  
  /**
   * Compute the SMC algorithm
   * 
   * @return The particle population at the last step
   */
  public ParticlePopulation<P> sample()
  {
    ParticlePopulation<P> currentPopulation = propose(null, 0);
    
    int nSMCIterations = proposal.nIterations();
    
    for (int currentIteration = 0; currentIteration < nSMCIterations - 1; currentIteration++)
    {
    	currentPopulation = propose(currentPopulation, currentIteration);
    	currentPopulation = currentPopulation.resample(randoms[0], ResamplingScheme.MULTINOMIAL);
    }
    
    return currentPopulation;
  }
  
  /**
   * Calls the proposal options.nParticles times, form the new weights, and return the new population.
   * 
   * If the provided currentPopulation is null, use the initial distribution, otherwise, use the 
   * transition. Both are specified by the proposal object.
   * 
   * @param currentPopulation The population of particles before the proposal
   * @param currentIteration The iteration of the particles used as starting points for the proposal step
   * @return
   */
  private ParticlePopulation<P> propose(final ParticlePopulation<P> currentPopulation, final int currentIteration)
  {
    final boolean isInitial = currentPopulation == null;
    ArrayList<P> particles = new ArrayList<>();
    double[] weights = new double[this.options.nParticles];
    
    double logScaling;
    
    if(isInitial) {
    	for(int i=0; i<this.options.nParticles; i++){
    		Pair<Double, P> newParticle = this.proposal.proposeInitial(randoms[i]);
    		particles.add(newParticle.getRight());
    		weights[i] = newParticle.getLeft();
    	}
    	logScaling = 0.0;
    }
    
    else {
    	for(int i=0; i<this.options.nParticles; i++){
    		Pair<Double, P> newParticle= this.proposal.proposeNext(currentIteration, randoms[i], currentPopulation.particles.get(i));
    		particles.add(newParticle.getRight());
    		weights[i] = Math.log(currentPopulation.getNormalizedWeight(i)) + newParticle.getLeft();
    	}
    	logScaling = currentPopulation.logScaling;
    }
    return ParticlePopulation.buildDestructivelyFromLogWeights(weights, particles, logScaling);
  }

  public SMCAlgorithm(ProblemSpecification<P> proposal, SMCOptions options)
  {
    this.proposal = proposal;
    this.options = options;
    this.randoms = new Random[options.nParticles];
    SplittableRandom splitRandom = new SplittableRandom(options.random.nextLong());
    for (int i = 0; i < options.nParticles; i++)
      this.randoms[i] = new Random(splitRandom.split().nextLong());
  }
}
