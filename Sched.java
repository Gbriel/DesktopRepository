import java.io.*;
import java.util.*;

public class Sched {
	private int CPUS = 1;
	private File file;
	private ArrayList<String> jobs = new ArrayList<String>();

	public Sched(String[] args) {
		if(args.length > 1) {
			CPUS = Integer.parseInt(args[1]);
		}
		try {
			file = new File(args[0]);
			Scanner in = new Scanner(file);
			String line = "";
			while(!(line = in.nextLine()).equals(null)) {
				jobs.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		if(args.length > 0) {
			Sched sched = new Sched(args);
		} else {
			System.out.println("Please enter a filename (and the number of CPUs)");
		}
	}
}