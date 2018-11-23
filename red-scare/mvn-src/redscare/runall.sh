mvn compile

for f in ../../data/common-1-2*.txt; do
	mvn exec:java -Dinput=$f
done
