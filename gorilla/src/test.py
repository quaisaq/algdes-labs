import sys
from collections import namedtuple

DEBUG = False

ResultStruct = namedtuple("ResultStruct", "name, value, str1, str2")

def main(file1, file2):
    # Parse file1 and store data in file1Data
    if DEBUG:
        print("Parsing %s" % file1)
    file1Data = parseFile(file1)

    if DEBUG:
        print("Found the following data in %s" % file1)
        for key,value in file1Data.items():
            print("%s: %s\n%s\n%s" % (value.name, value.value, value.str1, value.str2))

    # Parse file2 and store data in file2Data
    if DEBUG:
        print("Parsing %s" % file2)
    file2Data = parseFile(file2)

    if DEBUG:
        print("Found the following data in %s" % file2)
        for key,value in file2Data.items():
            print("%s: %s\n%s\n%s" % (value.name, value.value, value.str1, value.str2))

    # Check file1 and file2 contain same amount of data
    if len(file1Data) is not len(file2Data):
        print("Files do not contain same amount of data")
        print("%d data items found in %s" % (len(file1Data), file1))
        print("%d data items found in %s" % (len(file2Data), file2))
        return

    # Compare fileData
    filesIdentical = compareFileData(file1Data, file2Data)

    if filesIdentical:
        print("%s and %s are identical!" % (file1, file2))
    else:
        print("%s and %s are not identical" % (file1, file2))


def parseFile(filename):
    cnt = 0         # How many results have we parsed

    name = None     # Name of current occurrance being analyzed
    value = None    # Value of current occurrance being analyzed
    str1 = None     # First string value
    str2 = None     # Second string value

    res = {}        # Result dictionary
    with open(filename, "r") as f:
        for line in f:
            mod = cnt % 3

            if mod == 0:
                # Reset temporary values
                name = value = str1 = str2 = None

                lineparts = line.split(":")
                name = lineparts[0].strip()
                value = lineparts[1].strip()
            elif mod == 2:
                str1 = line.strip()
            else:
                str2 = line.strip()
            

            # Create new ResultStruct and append to res dictionary if all values set
            newResultStruct = createResultStruct(name, value, str1, str2)
            if newResultStruct is not None:
                if DEBUG:
                    print("Storing value for name: %s" % name)
                res[name] = newResultStruct

            cnt += 1
            

    return res

def createResultStruct(name, value, str1, str2):
    if name is None or value is None or str1 is None or str2 is None:
        if DEBUG:
            print("Values were None creating struct:\n\tName: %s\n\tValue: %s\n\tStr1: %s\n\tStr2: %s" % (name, value, str1, str2))
        return None
    
    return ResultStruct(name=name, value=value, str1=str1, str2=str2)

def compareFileData(file1Data, file2Data):
    filesIdentical = True

    for key, value in file1Data.items():
        resultStruct1 = value
        resultStruct2 = None

        # Check if the corresponding key is in file2Data. If not, try flipping stuff around
        if key in file2Data:
            resultStruct2 = file2Data[key]
        elif getAlternateKey(key) in file2Data:
            resultStruct2 = file2Data[getAlternateKey(key)]
        elif DEBUG:
            print("Could not find %s in file2Data" % key)

        if not compareResultStruct(resultStruct1, resultStruct2):
            filesIdentical = False

            print("\t%s not identical in files!")
            print("==== File1 ====")
            printStruct(resultStruct1)
            print("==== File2 ====")
            printStruct(resultStruct2)

    return filesIdentical


def getAlternateKey(key):
    keyparts = key.split("--")
    newKey = ("%s--%s" % (keyparts[1], keyparts[0]))
    return newKey

def compareResultStruct(s1, s2):
    if s1 is None or s2 is None:
        return False

    if not s1.value == s2.value:
        return False
    
    # This should consider str1 == str1, str2 == str2, name == name
    if s1 == s2:
        return True
    
    # Check str1 and 2 and set equally
    if s1.str1 == s2.str1 and s1.str2 == s2.str2:
        return True
    
    # Check if str are swapped
    if (s1.str1 == s2.str2 and s1.str2 == s2.str1) or (s1.str2 == s2.str1 and s1.str1 == s2.str2):
        return True


def printStruct(struct):
    if struct is None:
        print("Stuct is None")
    else:
        print("%s: %s\n%s\n%s\n" % (struct.name, struct.value, struct.str1, struct.str2))



if __name__ == "__main__":
    args = sys.argv
    if len(args) == 3:
        file1 = args[1]
        file2 = args[2]
        main(file1, file2)
    else:
        print("Please provide 2 filenames you would like to compare")
        print("Ex: python3 result.txt output.txt")

