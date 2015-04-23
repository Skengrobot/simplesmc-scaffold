#!/usr/bin/python

# Fast script because it's panic time
import numpy
import math
from pylab import *
from pprint import pprint


def iid_gauss_likelihood(mean, variance, obs_sum, obs_sumsq, n_obs):
    return -0.5*(obs_sumsq - 2*mean*obs_sum + mean*mean)/variance + n_obs*(-math.log(math.sqrt(2*math.pi)) - 0.5*(math.log(variance)))


# construct data
first_mean = 0
first_variance = 0.001
second_mean = 0
second_variance = 0.005

observations = []
for i in range(1000):
    observations.append(numpy.random.normal(first_mean, math.sqrt(first_variance)))

for i in range(1000):
    observations.append(numpy.random.normal(second_mean, math.sqrt(second_variance)))

# compute exact likelyhood under first model as in paper
samples = []
sampling_interval = 20
for i in range(0, len(observations)-1, sampling_interval):
    obs_sum = sum(observations[i:i+sampling_interval])
    obs_sumsq = sum([x*x for x in observations[i:i+sampling_interval]])
    samples.append(iid_gauss_likelihood(first_mean, first_variance, obs_sum, obs_sumsq, sampling_interval))

# repeat samples for entire sampling interval to plot against observations
plot_samples = [observations[i] for i in range(len(observations)) for j in range(sampling_interval)];

ax = range(len(samples))
plot(ax,samples)
title("IID Gaussian with changed variance from 0.001 to 0.005")
xlabel("Samples")
ylabel("Log Likelihood")
show()
