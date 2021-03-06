package simplesmc.switchinggauss;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

public class swGaussUtils {
	
	public static Pair<ArrayList<Pair<Integer, Double>>,ArrayList<Double>> generate(Random random, swGaussParams params, int length) {
		ArrayList<Pair<Integer, Double>> states = new ArrayList<>();
		ArrayList<Double> observations = new ArrayList<>();
		
		// Start sampling
		Pair<Integer, Double> currentState = params.sampleInitial(random);
		double currentObservation = params.sampleEmission(random, currentState);
		states.add(currentState);
		observations.add(currentObservation);
		for (int i=1; i<length; i++) {
			 currentState = params.sampleTransition(random, currentState);
			 currentObservation = params.sampleEmission(random, currentState);
			 states.add(currentState);
			 observations.add(currentObservation);
		}
		
		return Pair.of(states, observations);
	}
	
	public static Pair<ArrayList<Pair<Integer, Double>>,ArrayList<Double>> generateWithChangepoint(Random random, swGaussParams params, int length) {
		ArrayList<Pair<Integer, Double>> states = new ArrayList<>();
		ArrayList<Double> observations = new ArrayList<>();
		
		// Start sampling
		Pair<Integer, Double> currentState = params.sampleInitial(random);
		double currentObservation = params.sampleEmission(random, currentState);
		states.add(currentState);
		observations.add(currentObservation);
		for (int i=1; i<Math.floor(length/2); i++) {
			 currentState = params.sampleTransition(random, currentState);
			 currentObservation = params.sampleEmission(random, currentState);
			 states.add(currentState);
			 observations.add(currentObservation);
		}
		for (int iteration = (int) Math.floor(length/2); iteration<length; iteration++) {
			currentState = params.sampleChangedTransition(random, currentState);
			currentObservation = params.sampleEmission(random, currentState);
			states.add(currentState);
			observations.add(currentObservation);
		}
		
		return Pair.of(states, observations);
	}
}