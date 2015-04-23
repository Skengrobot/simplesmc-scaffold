#!/usr/bin/python

# A quick HMM model generator and exact data likelihood calculation script
# experts agree that this is numerically pretty sketchy

from pprint import pprint
from numpy.random import uniform

from pylab import *
import math

def forward_algorithm(observations, start, probabilities):
    likelihoods = []
    for i, obs in enumerate(observations):
        probs = []
        for state in range(5):
            if i == 0:
                probs.append(start[state]*probabilities[start[state]][obs])
            else:
                probs.append(sum([likelihoods[i-1][k]*probabilities[k][state] for k in range(5)]) * probabilities[state][obs])
        likelihoods.append(probs)

    return likelihoods

def sample_initial(probabilities):
    roll = uniform();
    for state, trans in enumerate(probabilities):
        if roll < trans:
            return state

def sample_transition(current_state, probabilities):
    roll = uniform();
    for state, trans in enumerate(probabilities[current_state]):
        if roll < trans:
            return state

def sample_emission(state, probabilities):
    return sample_transition(state, probabilities)
        

def generate_observations(length):
    observations = []
    state = sample_initial(accumulated_start)
    observations.append(sample_emission(state, accumulated_mat))
    for i in range(1, length/2):
        state = sample_transition(state, accumulated_mat)
        observations.append(sample_emission(state, accumulated_mat))
    # Changepoint
    for i in range(length/2, length):
        state = sample_transition(state, accumulated_mat2)
        observations.append(sample_emission(state, accumulated_mat2))

    return observations
        
    
# initial probabilities
start = [0, 0, 1, 0, 0]
accumulated_start = [0, 0, 1, 1, 1]

# transition and emission probability matrix
mat = [[.8, .2, 0, 0, 0],[.1, .8, .1, 0, 0],[0, .1, .8, .1, 0],[0, 0, .1, .8, .1],[0, 0, 0, .2, .8]]
accumulated_mat = [[.8, 1, 1, 1, 1],[.1, .9, 1, 1, 1],[0, .1, .9, 1, 1],[0, 0, .1, .9, 1],[0, 0, 0, .2, 1]]
# becomes less sticky after change
#mat2 = [[.6, .4, 0, 0, 0],[.2, .6, .2, 0, 0],[0, .2, .6, .2, 0],[0, 0, .2, .6, .2],[0, 0, 0, .4, .6]]
#accumulated_mat2 = [[.6, 1, 1, 1, 1],[.2, .8, 1, 1, 1],[0, .2, .8, 1, 1],[0, 0, .2, .8, 1],[0, 0, 0, .4, 1]]

accumulated_mat2 = [[1, 1, 1, 1, 1],[.1, .9, 1, 1, 1],[0, 1, .9, 1, 1],[0, 0, .1, .9, 1],[0, 0, 0, 0, 1]]

obs = generate_observations(200)
likes = forward_algorithm(obs, start, mat)

samples = []
sampling_interval = 5
last_likelihood = 0
for i in range(0, len(likes)-1, sampling_interval):
    this_likelihood = math.log(sum([sum(likes[j]) for j in range(i, i+sampling_interval)]))
    sample = this_likelihood - last_likelihood
    last_likelihood = this_likelihood
    samples.append(sample)

ax = range(len(samples))
plot(ax,samples)
title("HMM with changed emission and transition probabilities")
xlabel("Samples")
ylabel("Log Likelihood")
show()
