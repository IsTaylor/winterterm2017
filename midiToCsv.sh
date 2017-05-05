#!/bin/bash
# A test script to run a program on many files
DIRECTORY=""
while [ ! -d "$DIRECTORY" ]; do
echo -e "Please enter the folder you've placed your Midi files in:"
read directory
DIRECTORY=$directory
if [ ! -d "$DIRECTORY" ]; then
  echo "$DIRECTORY is not a directory"
fi
done
 
ORGDIR=$DIRECTORY

mkdir -p ./csv_in;

counter=0
shopt -s nullglob
for file in $ORGDIR/*.mid
do
  midicsv "$file" "./csv_in/$counter.csv"
  counter=$((counter+1))
done
shopt -u nullglob
