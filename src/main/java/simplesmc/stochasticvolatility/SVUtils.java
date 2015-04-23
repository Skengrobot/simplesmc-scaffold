package simplesmc.stochasticvolatility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

public class SVUtils {
	
	public static Pair<ArrayList<Double>,ArrayList<Double>> generate(Random random,
			SVParams params, int length) {
		ArrayList<Double> latents = new ArrayList<>();
		ArrayList<Double> observations = new ArrayList<>();
		
		double currentLatent = params.sampleInitial(random);
		double currentObs = params.sampleEmission(random, currentLatent);
		latents.add(currentLatent);
		observations.add(currentObs);
		for (int iteration = 1; iteration<length; iteration++) {
			currentLatent = params.sampleTransition(random, currentLatent);
			currentObs = params.sampleEmission(random, currentLatent);
			latents.add(currentLatent);
			observations.add(currentObs);
		}
		
		return Pair.of(latents, observations);		
	}
}
