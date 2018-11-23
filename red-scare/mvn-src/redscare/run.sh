mvn compile

for f in $@; do
	mvn exec:java -Dinput=$f
done
