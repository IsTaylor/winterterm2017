import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConvMidi {
    
    static int num_lines = 0;
    
    public static void main(String[] args) {
	Scanner scanny = null;
	try {
	    scanny = new Scanner(new File(args[0]));
	} catch (FileNotFoundException e) {
	    System.out.println("Not a valid file:" + e.getMessage());
	    System.exit(1);
	}
	
	// Start of MIDI file (header) not created by TF
	String header_str = "0, 0, Header, 1, 2, 256\n1, 0, Start_track\n1, 0, Time_signature"
		+ ", 4, 2, 24, 8\n1, 0, Key_signature, 0, \"major\n1, 0, Tempo, 750000\n1, 20481,"
		+ " End_track\n2, 0, Start_track\n2, 0, Program_c, 0, 0\n2, 0, Title_t, \"Piano\"";
	
	String ender_str = "2, 20993, End_track\n0, 0, End_of_file";
	
	ArrayList<String> output_lines = reader(scanny);
	
	// begin file with header
	System.out.println(header_str);
	
	// print each new MIDI-formatted line into file
	for (int i = 0; i < output_lines.size(); i++) {
	    System.out.println(output_lines.get(i));
	}
	
	System.out.println(ender_str);
	System.out.println("Found " + num_lines + " lines in input file.");

    }

    private static ArrayList<String> reader(Scanner scanny) {
	ArrayList<String> output_lines = new ArrayList<String>(); // define return variable
	
	int time_sig = 0;
	ArrayList<Integer> previous = new ArrayList<Integer>(); // define start array
	for (int i = 0; i < 127; i++) { // set start to 0
	    previous.add(0);
	}
	ArrayList<Integer> current = new ArrayList<Integer>(); // define next array

	while (scanny.hasNextLine()) { // for every line in input file
	    num_lines++;
	    String line = scanny.nextLine();
	    String[] vec_parts = line.split(",\\s+"); //split to grab array components
	    
	    // strip leading and trailing braces
	    vec_parts[0] = vec_parts[0].substring(1, vec_parts[0].length());
	    vec_parts[vec_parts.length-1] = vec_parts[vec_parts.length-1].substring(0, vec_parts[vec_parts.length-1].length() - 1);
		    
	    time_sig = time_sig + 128;
	    
	    // System.out.println(time_sig + " " + vec_parts.toString()); // test line, remove
	    
	    for (int j = 0; j < vec_parts.length; j++) { // moving to integer array
		current.add(Integer.parseInt(vec_parts[j]));
	    }
	    
	    int diff;
	    for (int i = 0; i < current.size(); i++) { // figure out which notes change, add lines
		diff = current.get(i) - previous.get(i);
		if (diff > 0){
		    output_lines.add("2, " + time_sig + ", Note_on_c, 0, " + i + ", 80");
		} else if (diff < 0) {
		    output_lines.add("2, " + time_sig + ", Note_off_c, 0, " + i + ", 0");
		}
	    }
	    
	    previous = current; // setup for next loop
	    current.clear(); // clear current
	}
	
	return output_lines; // returns array of strings to write to file
    }
}
