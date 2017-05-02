# winterterm2017
Attempting machine learning

## Getting Started

### Prerequisites
If you want to train with your own midi files, you'll want to download Midicsv(http://www.fourmilab.ch/webtools/midicsv/).
This website has a tar file about halfway down, along with usage information. Once you have downloaded it, follow the instructions in their README, go ahead and test it out if you want.

### Installing
Pull from github, but the only files you'll really care about are MidiConv.java and tf-test.py

MidiConv - Despite the misleading name, this converts csv files into tensorflow-compatible files. It is optimized for large file sets.
	Prereqs:
		1. A folder with all the converted csv files in the same directory as MidiConv
	Usage:
		java MidiConv input_dir (where input_dir is the name of your folder without the "./" So if my folder were "./csvIn", all you would type is "csvIn")
	Outputs:
		MidiConv outputs a folder called tensorflow_input. In this folder are a list of converted files, with titles in the format of "#-tf.txt", where number is the order in which the files were seen (ie 0-tf.txt, 1-tf.txt, 2-tf.txt). these are ready to be used to train your network
		
		