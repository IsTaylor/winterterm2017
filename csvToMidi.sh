#!/bin/bash
# A test script to run a program on many files
 
DIRECTORY=""
while [ ! -d "$DIRECTORY" ]; do
echo -e "Please enter the folder you've placed your Csv files in:"
read directory
DIRECTORY=$directory
if [ ! -d "$DIRECTORY" ]; then
  echo "$DIRECTORY is not a directory"
fi
done

ORGDIR=$DIRECTORY

mkdir -p ./Midi_final

counter=0
shopt -s nullglob
for file in $ORGDIR/*.csv
do
  echo -e "heyo"
  csvmidi "$file" "./Midi_final/$counter.mid"
  counter=$((counter+1))
done
shopt -u nullglob
