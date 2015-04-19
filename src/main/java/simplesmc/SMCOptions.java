package simplesmc;

import java.util.Random;

import bayonet.smc.ResamplingScheme;
import briefj.opt.Option;


/**
 * Command line options for SMC
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 */
public class SMCOptions
{
  

  @Option(gloss = "Seed for the SMC algorithm")
  public Random random = new Random(1);
  
  @Option(gloss = "Number of particles")
  public int nParticles = 100000;

}
