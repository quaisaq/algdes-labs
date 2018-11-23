mvn compile

for f in ../../data/*.txt; do
	mvn exec:java -Dinput=$f
	#java -jar redscare-1.0-SNAPSHOT.jar $f
done
