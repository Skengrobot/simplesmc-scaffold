#!/usr/bin/python

#   Simple script to generate observations from a linear Gaussian System
# outputs in a file that's read by the Java program.
#
# Noise covariance matrix has to be of the form diag(sigma), (all entries the same) because that's all the that the 
# program can simulate as of now. This might change, in which case I will certainly forget to update this.
#
#   Make it a class since I'm on a Java flex.

import sys
import numpy

class LinearGaussianSystem:
    
    def __init__(self, transition_matrix, emission_matrix, sigma_emission, sigma_transition):
        self.transition = numpy.matrix(transition_matrix)
        self.emission = numpy.matrix(emission_matrix)
        self.sigma_transition = sigma_transition 
        self.sigma_emission = sigma_emission 

    def generate_initial(self):
        # Arbitrary initialization
        return numpy.ones((self.transition.shape[0],1))

    def generate_next(self, current_state):
        noise = self.sigma_emission * numpy.random.randn(self.transition.shape[0])
        return self.transition * current_state + noise

    def generate_emission(self, current_state):
        noise = self.sigma_emission * numpy.random.randn(self.emission.shape[0])
        return self.emission * current_state + noise

    def observe(self, num_iterations):
        state = self.generate_initial()
        for i in range(num_iterations):
            observation = self.generate_emission(state)
            state = self.generate_next(state)
            yield observation

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'Enter an iteration count (length of process)'
    
    system = LinearGaussianSystem('1 2; 1 4', '3 1; 1 5', 0.01, 0.01)

    for i in range(int(sys.argv[1])):
        for j in system.observe(int(sys.argv[1])):
            print j
