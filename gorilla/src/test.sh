#!/bin/bash

# RUN BY specifying java file to test
# EX
# ./test.sh pbsolution.java

# Source folder without last slash
DATADIR="./../data"
MAINFILE=$1


# Check any input/argument was given
if [[ $MAINFILE == "" ]] ; then
	echo "Please give a java file as argument"
	echo "Ex: ./test.sh myfile.java"
	exit 9001
fi


# Check if MAINFILE ends with .java. If not, append
if [[ ! "$MAINFILE" == *.java ]]; then
	MAINFILE="$MAINFILE.java"
fi


# Check if file exists
if [ ! -f $MAINFILE ] 
then
	echo "Could not find file: $MAINFILE"
	exit 2
fi


# Compile java
if ! javac $MAINFILE ;
then
	echo "\nCould not compile java file $MAINFILE. Exiting..."
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
MAINFILENOEXT=$(basename $MAINFILE ".java") # Main filename without extension
for inputfile in $DATADIR/*-in.txt; do
	echo "Running on $inputfile"
	filename=$(basename "$inputfile" "-in.txt") # Get input filename without "-in.txt". Allows for finding corresponding out-file
	java $MAINFILENOEXT "$inputfile" > "$outputdir/$filename-out.txt"
done


# Compare outputs
RED="\033[0;31m"
GREEN="\033[0;32m"
NC="\033[0m" # No colour
echo -e "\nComparing outputs..."
for outputfile in $outputdir/*.txt; do
	filename=$(basename "$outputfile")
	python3 outputcompare.py $outputfile $DATADIR/$filename
	: '
	if ! diff $outputfile $DATADIR/$filename > /dev/null ; then
		echo -e "${RED}$outputfile is different from data/$filename${NC}"
	else
		echo -e "${GREEN}$outputfile is identical to data/$filename${NC}"
	fi
	'
done

