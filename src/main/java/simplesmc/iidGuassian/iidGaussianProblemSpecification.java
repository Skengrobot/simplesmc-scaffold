package simplesmc.iidGuassian;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.distribution.NormalDistribution;

import simplesmc.ProblemSpecification;
import simplesmc.lingauss.LinGaussUtils;
import bayonet.distributions.Normal;

public class iidGaussianProblemSpecification implements ProblemSpecification<Double> {
	
	private final ArrayList<Double> observations;
	private final double mean;
	private final double variance;
	private final NormalDistribution normDist;
	
	public iidGaussianProblemSpecification(ArrayList<Double> observations,
			double mean, double variance) {
		this.observations = observations;
		this.mean = mean;
		this.variance = variance;
		
		normDist = new NormalDistribution(mean, Math.sqrt(variance));
		
	}

	public iidGaussianProblemSpecification(String filename,
			double mean, double variance) {
		this.mean = mean;
		this.variance = variance;
		this.observations = new ArrayList<Double>();

		normDist = new NormalDistribution(mean, Math.sqrt(variance));
		
		// Lazy, hacky reuse of file parser
		ArrayList<ArrayList<Double>> parsed = LinGaussUtils.parseFile(filename);
		for (ArrayList<Double> arr : parsed)
			observations.add(arr.get(0));
	}

	@Override
	public Pair<Double, Double> proposeNext(int currentSmcIteration,
			Random random, Double currentParticle) {
		double proposedParticle = Normal.generate(random, mean, variance);
		double weightUpdate = Normal.logDensity(observations.get(currentSmcIteration), mean, variance);
		return Pair.of(weightUpdate, proposedParticle);
	}

	@Override
	public Pair<Double, Double> proposeInitial(Random random) {
		double proposedParticle = Normal.generate(random, mean, variance);
		double weightUpdate = this.normDist.logDensity(observations.get(0));
		System.out.println(observations.get(0));
		System.out.println();
		System.out.println(this.normDist.density(observations.get(0)));
		System.out.println(weightUpdate);
		System.out.println();
		System.out.println(Normal.logDensity(observations.get(0), mean, variance));

		return Pair.of(weightUpdate, proposedParticle);
	}

	@Override
	public int nIterations() {
		return this.observations.size();
	}
	
	public double getFinalLogLikelihood() {
		double sum = 0;
		double sumSq = 0;
		for (double obs : observations) {
			sum += obs;
			sumSq += obs*obs;
		}
		return Normal.logProb(this.mean, this.variance, sum, sumSq, this.nIterations());
	}
	
}