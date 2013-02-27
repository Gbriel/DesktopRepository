import java.io.*;
import java.util.*;

public class Sched {
	private int CPUS = 1;
	private File file;
	private ArrayList<String[]> initial_jobs = new ArrayList<String[]>();
	private int[] steps_completed;
	private JobProcessingUnit[] devices;
	private int timer = 0;
	private LinkedList<String[]> CPU_queue = new LinkedList<String[]>();
	private boolean jobs_all_done = false;
	private int jobs_remaining;
	public Sched(String[] args) {
		if(args.length > 1) {
			CPUS = Integer.parseInt(args[1]);
		}
		try {
			file = new File(args[0]);
			Scanner in = new Scanner(file);
			String line = "";
			String[] first_split;
			String[] second_split;
			String[] chopped;
			int job_number = 0;
			while(!(line = in.nextLine()).equals(null)) {
				first_split = line.split("\t");
				second_split = first_split[1].split(" ");
				chopped = new String[second_split.length +2];
				chopped[0] = "" + job_number;
				chopped[1] = first_split[0];
				for(int i = 2; i < chopped.length; i++) {
					chopped[i] = second_split[i-2];
				}
				initial_jobs.add(chopped);
				job_number++;
			}
			steps_completed = new int[initial_jobs.size()];
			jobs_remaining = initial_jobs.size();
			for(int i = 0; i < steps_completed.length; i++) {
				steps_completed[i] = 0;
			}
			initDevices();
			orderForQueue(initial_jobs);
			sched();
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

	public void initDevices() {
		devices = new JobProcessingUnit[26+CPUS];
		for(int i = 0; i < 26 + CPUS; i++) {
			if(i>25) {
				devices[i] = new JobProcessingUnit();
			} else {
				devices[i] = new JobProcessingUnit((char)('a'+1));
			}
		}
	}

	protected void orderForQueue(ArrayList<String[]> jobs) {
		int g = jobs.size();
		String[] best_job = new String[2];
		for(int i = 0; i < g; i++) {
			int min_time = 100000000;
			for(String[] job : jobs) {
				if(Integer.parseInt(job[1]) < min_time) {
					best_job = job;
				}
			}
			CPU_queue.add(best_job);
			jobs.remove(best_job);
		}
	}

	public void sched() {
		while(jobs_remaining > 0) {
			//increment time, remove finished jobs, add to next queue
			// if the queue isn't empty, add to tempQ if empty
			// add tempQ jobs to regular queue
			//then send jobs to be processed
		
			for(int i = 0; i < CPUS; i++) {
				if(devices[26+i].processing == false) {
					if(CPU_queue.size() != 0) {
						String[] toProcess = CPU_queue.remove();
						System.out.println("Job " + toProcess[0] + " is executing on CPU " + i + " at t=" + timer);
						devices[26+i].process(toProcess);	
					}
				}
			}
			timer++;
		}
	}

private class JobProcessingUnit {
	private LinkedList<String[]> device_queue = new LinkedList<String[]>();
	private boolean has_jobs_waiting = false;
	private char type = 'A';
	int time_finished = -1;
	private int current_job_length;
	String[] current_job;
	boolean processing = false;	

	public JobProcessingUnit() {
		
	}

	public JobProcessingUnit(char device_type) {
		type = device_type;
	}

	public void process(String[] job) {
		current_job = job;
		time_finished = timer + Integer.parseInt(job[steps_completed[Integer.parseInt(job[0])]+1]);
		processing = true;
	}

}
}
