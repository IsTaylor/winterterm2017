import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MidiConv {
    
    private static int TEMPO = 500000;
    private static int BPM = 60000000/TEMPO;
    private static int STEP = 256 / 4;

    public static void main(String[] args) {
	Scanner scanny = null;
	try {
	    scanny = new Scanner(new File("/Users/qbp/Dropbox/WT 2017/test.csv"));
	} catch (FileNotFoundException e) {
	    System.out.println("Not a valid file:" + e.getMessage());
	    System.exit(1);
	}
	
	ArrayList<MidiArray> arrayArray = reader(scanny);
	scanny.close();
	
	for (int i = 0; i < arrayArray.size(); i++) {
	    System.out.println(arrayArray.get(i).toString());
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
		
		for (int i = 0; i < currentEvent.length; i++) {
		    System.out.print(currentEvent[i] + " ");
		}
		System.out.println("\n");
		
		
		if (currentEvent[2].equals("Note_on_c")){
		    Integer[] currentNotes = arrayArray.get(arrayArray.size() - 1).notes;
		    int midiValue = Integer.parseInt(currentEvent[4]);
		    currentNotes[midiValue] = 1;
		    System.out.println("note on");
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
