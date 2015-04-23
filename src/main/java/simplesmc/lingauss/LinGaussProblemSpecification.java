package simplesmc.lingauss;

import java.util.ArrayList;
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

public class LinGaussProblemSpecification implements ProblemSpecification<ArrayList<Double>> {

	private final LinGaussParams parameters;
	private final List<ArrayList<Double>> observations;
	
	public LinGaussProblemSpecification(LinGaussParams parameters, List<ArrayList<Double>> observations) {
		this.parameters = parameters;
		this.observations = observations;
	}
	
	@Override
	public Pair<Double, ArrayList<Double>> proposeNext(int currentSmcIteration,
			Random random, ArrayList<Double> currentParticle) {
		ArrayList<Double> proposedParticle = this.parameters.sampleTransition(random, currentParticle);
		double weightUpdate = this.parameters.emissionLogPr(proposedParticle, observations.get(currentSmcIteration));
		System.out.println(weightUpdate);
		Pair<Double, ArrayList<Double>>  update = Pair.of(weightUpdate, proposedParticle);
		return update;
	}

	@Override
	public Pair<Double, ArrayList<Double>> proposeInitial(Random random) {
		ArrayList<Double> proposedParticle = this.parameters.sampleInitial(random);
		Double weight = this.parameters.emissionLogPr(proposedParticle, observations.get(0));
		Pair<Double, ArrayList<Double>> update = Pair.of(weight, proposedParticle);
		return update;
	}

	@Override
	public int nIterations() {
		return this.observations.size();
	}
	
}