#!/usr/bin/env python

from subprocess import Popen, PIPE
from cinfony import pybel
import math, sys

#smiles = raw_input("Enter the SMILES string for BBB permeability: ")
smiles = str(sys.argv[1])

entry = ""

try:
    # Reads the molecule into pybel
    pybelMol = pybel.readstring("smi", smiles);
    pybelFP = pybelMol.calcfp()

    pybelProperties = str(pybelFP).split(",")

    properties = pybelProperties
    properties = [p.strip() for p in properties]

    for current in properties:
        fixed = 0.0;
        # Take log_2(property) (May change later)
        if current != "0":
            fixed = math.log(float(current), 2)
        # Tack onto entry
        entry = entry + str(fixed) + " "

    command = "java Predictor " + entry
    p1 = Popen([command], stdout=PIPE, shell=True)
    answer = p1.communicate()[0].strip()
    if (answer == "yes"):
        print smiles + " crosses the BBB."
    else:
        print smiles + " does not cross the BBB."
    pass
except IOError:
    print smiles + " is not a valid SMILES string."
    pass
