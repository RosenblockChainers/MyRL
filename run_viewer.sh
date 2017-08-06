#!/bin/sh
if [ ! -e log ]; then
   mkdir log
fi
if [ ! -e out ]; then
   mkdir out
fi
cd src
javac -d ../out Viewer/Viewer.java
cd ../out
java Viewer.Viewer