#!/bin/sh
if [ ! -e log ]; then
   mkdir log
fi
if [ ! -e out ]; then
   mkdir out
fi
cd src
javac -d ../out SAPGA/MainSAPGA.java
cd ../out
java SAPGA.MainSAPGA