import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class MidiConv {

    private static int TEMPO = 500000;
    private static int BPM = 60000000/TEMPO;
    private static int STEP = 256 / 4;
    private static int fileCounter = 0;

    public static void main(String[] args) {
      String myDirectoryPath = args[0];

      File in_dir = new File("./" + myDirectoryPath);
      File[] directoryListing = in_dir.listFiles();
      if (directoryListing != null) {

    	//Make a directory to put converted files into
		  File out_dir = new File("./tensorflow_input");
		  if (!out_dir.exists()) {
		      if (out_dir.mkdir()) {
		          System.out.println("Directory is created!");
		      } else {
		          System.out.println("Failed to create directory!");
		          System.exit(1);
		      }
		  }

    	Scanner scanny = null;
        for (File child : directoryListing) {

      	//open up a new scanner with the child
	    	try {
	    	    scanny = new Scanner(child);
	    	} catch (FileNotFoundException e) {
	    	    System.out.println("Not a valid file:" + e.getMessage());
	    	    continue;
	    	}

	    //scan through the child
	    	try{
	    		ArrayList<MidiArray> arrayArray = reader(scanny);

	    		//output stuff to new files and directories
	    		try{
	    			System.out.println("Made file");
	    			File fout = new File(out_dir + "/" + fileCounter + "-tf.txt");
	    			fileCounter ++;

	    			FileOutputStream fos = new FileOutputStream(fout);
	    			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

	    			for (int i = 0; i < arrayArray.size(); i++) {
		    	    	bw.write(arrayArray.get(i).toString());
                bw.newLine();
		    		}



	    		} catch (Exception e) {
	    		   System.out.println("I couldn't make the file :( " + e);
	    		}





	    	} catch (Exception e){
	    		System.out.println("Encounterd a non-midi file: " + child);
	    	}

        }
        scanny.close();
      } else {
    	System.out.println("Not a valid directory, please try again");

	    System.exit(0);
      }
    }

    private static ArrayList<MidiArray> reader(Scanner scanny) {
	ArrayList<MidiArray> arrayArray = new ArrayList<MidiArray>();

	int time = 0;

	MidiArray startArray = new MidiArray(0);
	arrayArray.add(startArray);

	while (scanny.hasNextLine()) {
	    String[] currentEvent = parseLine(scanny.nextLine());
	    int track = Integer.parseInt(currentEvent[0]);
	    int currentTime = Integer.parseInt(currentEvent[1]);

	    //keeps track of tempo as a a global variable
	    if (currentEvent[2].equals("Tempo") && currentEvent[1].equals("0")) {
		TEMPO = Integer.parseInt(currentEvent[3]);
	    }

	  //keeps track of tempo as a a global variable
	    if (currentEvent[2].equals("Header")) {
		STEP = Integer.parseInt(currentEvent[5]) / 4;
	    }

	    if (track == 2) {
		if (currentTime != time) {
		    time = currentTime;
		    MidiArray midiObject = new MidiArray(time);
		    Integer[] pastNotes = arrayArray.get(arrayArray.size() - 1).notes;
		    for(int j = 0; j < pastNotes.length; j++){
			midiObject.notes[j] = pastNotes[j];
		    }

		    arrayArray.add(midiObject);
		}

		/*for (int i = 0; i < currentEvent.length; i++) {
		    System.out.print(currentEvent[i] + " ");
		}
		System.out.println("\n");
		*/


		if (currentEvent[2].equals("Note_on_c")){
		    Integer[] currentNotes = arrayArray.get(arrayArray.size() - 1).notes;
		    int midiValue = Integer.parseInt(currentEvent[4]);
		    currentNotes[midiValue] = 1;
		    //System.out.println("note on");
		}
		if (currentEvent[2].equals("Note_off_c")){
		    Integer[] currentNotes = arrayArray.get(arrayArray.size() - 1).notes;
		    int midiValue = Integer.parseInt(currentEvent[4]);
		    currentNotes[midiValue] = 0;
		}
	    }
	}
	return arrayArray;
    }

    private static String[] parseLine(String line) {
	String[] parts = line.split(",\\s+");
	return parts;
    }

    public static class MidiArray {
	public Integer[] notes;
	public int timing;

	public MidiArray(int time) {
	    this.notes = new Integer[128];
	    for (int i = 0; i < notes.length; i++ ){
		notes[i] = 0;
	    }
	    this.timing = time;
	}

	public String toString() {
	    String retStr = "" + this.timing + " [";
	    for (int i = 0; i < notes.length; i++) {
		retStr += notes[i] + ", ";
	    }
	    retStr += "]";
	    return retStr;

	}
    }

}
