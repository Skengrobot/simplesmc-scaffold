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
        noise = self.sigma_emission * numpy.random.randn(self.transition.shape[0],1)
        return self.transition * current_state + noise

    def generate_emission(self, current_state):
        noise = self.sigma_emission * numpy.random.randn(self.emission.shape[0],1)
        return self.emission * current_state + noise

    def observe(self, num_iterations):
        state = self.generate_initial()
        for i in range(num_iterations):
            observation = self.generate_emission(state)
            state = self.generate_next(state)
            yield observation

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'Enter an iteration count (length of process) and file name to output to'
    
    # 2-d rotation
    system = LinearGaussianSystem('0.965925826289068 -0.258819045102521; 0.258819045102521 0.965925826289068', '3 1; 4 2', 0.001, 0.01)

    outfile = open(sys.argv[2],'w')

    for observation in system.observe(int(sys.argv[1])):
        obs_list = [i[0] for i in observation.tolist()] 

        # Put everything in csv string
        out_string = str(obs_list[0])
        for num in obs_list[1:-1]:
            out_string = out_string + ',' + str(num)
        out_string = out_string + ',' + str(obs_list[-1]) + '\n'

        # Print to file
        outfile.write(out_string)

    outfile.close()
