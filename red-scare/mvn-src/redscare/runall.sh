mvn compile

for f in ../../data/common-*.txt; do
	java.exe -jar redscare.jar $f
done
