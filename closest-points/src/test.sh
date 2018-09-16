#!/bin/bash

# RUN BY specifying java file to test
# EX
# ./test.sh pbsolution.java

# Source folder without last slash
DATADIR="../data"
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


# Create new output file
outputfile="output.txt"
if [ -f $outputfile ]
then
	echo -e "Deleting old output file...\n"
	rm $outputfile
else
	echo -e "Creating new output file: $outputfile\n"
	touch $outputfile
fi


# Create outputs
#for var in "$@"
facitfile="$DATADIR/closest-pair-out.txt"
for inputfile in $DATADIR/*-tsp.txt; do
	echo "Running on $inputfile"
	if [ $facitfile = $inputfile ]; then
		echo "Skipping facit file: $facitfile"
		continue
	fi
	filename=$(basename "$inputfile" ".txt")
	java $MAINFILE "$inputfile" >> "$outputfile"
done


# Compare outputs
RED="\033[0;31m"
GREEN="\033[0;32m"
NC="\033[0m" # No colour
echo -e "\nComparing outputs with $facitfile"
while read -r facitline && read -r outputline <&3;
do
	facitfilename="$(cut -d' ' -f1 <<< $facitline)"
	facitcount="$(cut -d' ' -f2 <<< $facitline)"
	facitdistance="$(cut -d' ' -f3 <<< $facitline)"
	outputfilename="$(cut -d' ' -f1 <<< $outputline)"
	outputcount="$(cut -d' ' -f2 <<< $outputline)"
	outputdistance="$(cut -d' ' -f3 <<< $outputline)"
	: '
	echo "FacitFilename: $facitfilename"
	echo "FacitCount: $facitcount"
	echo "FacitDistance: $facitdistance"

	echo "OutputFilename: $outputfilename"
	echo "OutputCount: $outputcount"
	echo "OutputDistance: $outputdistance"
	'
	# Check the same files are being compared
	if [ ! $facitfilename = $outputfilename ]; then
		echo -e "${RED}Filenames between output and facit do not correspond. Cannot compare output with facit..."
		exit 1
	fi
	
	# Calculate distance comparison
	maxDistanceDifference="0.1"
	distanceDifference=$(bc <<< "$facitdistance-$outputdistance")
	isWithinThreshold=$(bc <<< "$maxDistanceDifference > $distanceDifference && $distanceDifference > -$maxDistanceDifference")
	#echo "DistanceDifference: $distanceDifference"
	#echo "IsWithinThreshold: $isWithinThreshold"

	if [ ! $facitcount = $outputcount ] || [ ! $isWithinThreshold = 1 ]; then
		echo -e "${RED}Error: $facitfilename${NC}"

		# Check counts are equal
		if [ ! $facitcount = $outputcount ]; then
			echo -e "${RED}\tCounts not equal. Expected $facitcount, got $outputcount${NC}"
		fi

		# Check distance
		if [ ! $isWithinThreshold = 1 ]; then
			echo -e "${RED}\tDistance difference is not within threshold of $maxDistanceDifference. Expected $facitdistance, got $outputdistance${NC}"
		fi
	else
		echo -e "${GREEN}$facitfilename passed!${NC}"
	fi
	

done <$facitfile 3<$outputfile
	


: '
for outputfile in $outputdir/*.txt; do
	filename=$(basename "$outputfile")
	if ! diff $outputfile $DATADIR/$filename > /dev/null ; then
		echo -e "${RED}$outputfile is different from data/$filename${NC}"
	else
		echo -e "${GREEN}$outputfile is identical to data/$filename${NC}"
	fi
done
'

