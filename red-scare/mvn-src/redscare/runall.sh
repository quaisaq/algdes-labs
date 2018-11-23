mvn compile

for f in ../../data/*.txt; do
	mvn exec:java -Dinput=$f
done
