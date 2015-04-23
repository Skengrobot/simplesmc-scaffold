#!/usr/bin/python

# A quick HMM model generator and exact data likelihood calculation script

from pprint import pprint
from numpy.random import uniform

def forward_algorithm(observations, start, probabilities):
    likelihoods = []
    for i, obs in enumerate(observations):
        print 'observation ' + str(obs)
        probs = []
        for state in range(5):
            if i == 0:
                probs.append(start[state]*probabilities[start[state]][obs])
            else:
                probs.append(sum([likelihoods[i-1][k]*probabilities[k][state] for k in range(5)]) * probabilities[state][obs])
        print probs
        print sum(probs)
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
accumulated_mat = [[.8, 1, 1, 1, 1],[.1, .9, 1, 0, 0],[0, .1, .9, 1, 0],[0, 0, .1, .9, 1],[0, 0, 0, .2, 1]]
# becomes less sticky after change
mat4 = [[.6, .4, 0, 0, 0],[.2, .6, .2, 0, 0],[0, .2, .6, .2, 0],[0, 0, .2, .6, .2],[0, 0, 0, .4, .6]]
accumulated_mat2 = [[.6, 1, 1, 1, 1],[.2, .8, 1, 0, 0],[0, .2, .8, 1, 0],[0, 0, .2, .8, 1],[0, 0, 0, .4, 1]]


obs = generate_observations(100)

likes = forward_algorithm(obs, start, mat)
