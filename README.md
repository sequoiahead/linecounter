# Purpose
The app counts lines in java files supplied as arguments. If directory supplied as the argument the app will recursively 
walk directory tree and count. 

# Build

mvn -U clean package

# Usage

java -jar linecounter-1.0-SNAPSHOT.jar <dirname/filename> [dirname/filename]...
