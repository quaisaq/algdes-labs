#!/bin/bash

# RUN BY specifying java file to test
# EX
# ./test.sh pbsolution.java

# Source folder without last slash
DATADIR="./../data"
#MAINFILE="pbsolution"

if [ ! -f $1 ] 
then
	echo "Could not find file: $1"
	exit 2
fi
MAINFILE=$(basename $1 ".java")


# Compile java
if ! javac $MAINFILE.java ;
then
	echo "\nCould not compile java files. Exiting..."
	exit 1
fi


# Create output directory and clean
outputdir="output"
if [ -d $outputdir ]
then
	echo -e "Deleting old output files...\n"
	rm $outputdir/*.txt
else
	echo -e "Creating output directory: $outputdir\n"
	mkdir $outputdir
fi


# Create outputs
#for var in "$@"
for inputfile in $DATADIR/*-in.txt; do
	echo "Running on $inputfile"
	filename=$(basename "$inputfile" "-in.txt")
	java $MAINFILE "$inputfile" > "$outputdir/$filename-out.txt"
done


# Compare outputs
RED="\033[0;31m"
GREEN="\033[0;32m"
NC="\033[0m" # No colour
echo -e "\nComparing outputs..."
for outputfile in $outputdir/*.txt; do
	filename=$(basename "$outputfile")
	if ! diff $outputfile $DATADIR/$filename > /dev/null ; then
		echo -e "${RED}$outputfile is different from data/$filename${NC}"
	else
		echo -e "${GREEN}$outputfile is identical to data/$filename${NC}"
	fi
done

