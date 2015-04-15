package simplesmc.lingauss;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import simplesmc.ProblemSpecification;
import simplesmc.pmcmc.WithSignature;

/**
 * The specification of an SMC algorithm based on an linear state space model
 * with additive Gaussian noise.
 *
 * @author Rudi Plesch (skengrobot@gmail.com)
*/

public class LinGaussProblemSpecification implements ProblemSpecification<Double[]> {

	@Override
	public Pair<Double, Double[]> proposeNext(int currentSmcIteration,
			Random random, Double[] currentParticle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Double, Double[]> proposeInitial(Random random) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nIterations() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}