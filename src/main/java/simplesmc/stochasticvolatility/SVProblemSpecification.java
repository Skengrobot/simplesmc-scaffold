package simplesmc.stochasticvolatility;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import simplesmc.ProblemSpecification;

public class SVProblemSpecification implements ProblemSpecification<Double> {
	
	private final SVParams parameters;
	private final ArrayList<Double> observations;
	
	public SVProblemSpecification(SVParams parameter, ArrayList<Double> observations) {
		this.parameters = parameter;
		this.observations = observations;
	}

	@Override
	public Pair<Double, Double> proposeNext(int currentSmcIteration,
			Random random, Double currentParticle) {
		double proposedParticle = this.parameters.sampleTransition(random, currentParticle);
		double weightUpdate = this.parameters.emissionLogPr(proposedParticle, observations.get(currentSmcIteration));
		return Pair.of(weightUpdate, proposedParticle);
	}

	@Override
	public Pair<Double, Double> proposeInitial(Random random) {
		double particle = this.parameters.sampleInitial(random);
		double weight = this.parameters.sampleEmission(random, particle);
		return Pair.of(weight, particle);
	}

	@Override
	public int nIterations() {
		return this.observations.size();
	}
	
}