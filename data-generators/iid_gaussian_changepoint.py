#!/usr/bin/python

import sys
import numpy


if len(sys.argv) < 2:
    print 'Enter an iteration count (length of process) and file name to output to'

mean = 0
variance = 0.1

outfile = open(sys.argv[2],'w')

for i in range(int(sys.argv[1])):
    roll = numpy.random.normal();
    outfile.write(str(roll)+'\n')

outfile.close()
