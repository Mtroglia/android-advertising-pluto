#!/bin/sh
# compile.sh

javac -classpath .:./lib/weka.jar.bk edu/illinois/seclab/android/tools/Pluto.java edu/illinois/seclab/android/tools/ARFF.java edu/illinois/seclab/android/tools/DataParser.java edu/illinois/seclab/android/tools/Log.java edu/illinois/seclab/android/tools/Report.java edu/illinois/seclab/android/tools/DataAnalysis.java edu/illinois/seclab/android/tools/Device.java edu/illinois/seclab/android/tools/Menu.java edu/illinois/seclab/android/tools/Preferences.java edu/illinois/seclab/android/tools/Utils.java