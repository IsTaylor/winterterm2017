import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConvMidi {
    public static void main(String[] args) {
	Scanner scanny = null;
	try {
	    scanny = new Scanner(new File(args[0]));
	} catch (FileNotFoundException e) {
	    System.out.println("Not a valid file:" + e.getMessage());
	    System.exit(1);
	}
	
	ArrayList<String> output_lines = reader(scanny);
	for (int i = 0; i < output_lines.size(); i++) {
	    System.out.println(output_lines.get(i));
	}

    }

    private static ArrayList<String> reader(Scanner scanny) {
	ArrayList<String> output_lines = new ArrayList<String>(); // define return variable
	
	int time_sig;
	ArrayList<Integer> previous = new ArrayList<Integer>(); // define start array
	for (int i = 0; i < 127; i++) { // set start to 0
	    previous.add(0);
	}
	ArrayList<Integer> current = new ArrayList<Integer>(); // define next array
	
	while (scanny.hasNextLine()) { // for every line in input file
	    String line = scanny.nextLine();
	    String[] line_parts = line.split("["); // split to grab time_sig, cuts off open brace "["
	    time_sig = Integer.parseInt(line_parts[0]);
	    String line_part2_edited = line_parts[1].substring(0, line_parts[1].length() - 1); // strips end brace "]"
	    String[] vec_parts = line_parts[1].split(",\\s+"); //split to grab array components
	    
	    System.out.println(time_sig + " " + vec_parts.toString()); // test line, remove
	    
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
