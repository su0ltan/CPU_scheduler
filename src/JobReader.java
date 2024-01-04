import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

//This class is used to read the data from input file and perform
//the CPU exection
public class JobReader implements java.lang.Runnable {
	private String fileBath;
	private BlockingQueue<PCB> jobsQueue;
	private BlockingQueue<PCB> readyQueue, terminatedJobs;
	private int availableMemory;
	PriorityQueue<PCB> shortestJobQueue = new PriorityQueue<>(Comparator.comparingInt(pcb -> pcb.getBurstTime()));

	// This function will reads the data from file
	public JobReader(String fileBath) {

		this.fileBath = fileBath;
		// this jobs queue that will store all jobs
		jobsQueue = new LinkedBlockingDeque<>();

		// this queue will store all jobs that finished
		terminatedJobs = new LinkedBlockingDeque<>();

		// this the limit memory
		availableMemory = 8192;

		// priority queue based on burst time ( to perform the shortest job first )
		shortestJobQueue = new PriorityQueue<>(Comparator.comparingInt(pcb -> pcb.getBurstTime()));

		// this will store the jobs that are ready to be executed
		readyQueue = new LinkedBlockingQueue<>();

	}

	// This function will execute when job terminated from CPU we need to get back
	// to reseved memory
	public void increaseMemory(int size) {
		availableMemory = availableMemory + size;
	}

	// This function will be executes continously with different thread
	public synchronized void loadJobsToReadyQueue() {

		/*
		 * * To make the thread always running and transfaring jobs from job Queue to
		 * ready Queue ( because the ready it may be full so we need to make sure that
		 * the thread always checking if there available place of memory to put the job
		 * in readyQueue
		 */
		while (true) {
			// if job Queue is not empty then perform the followng:
			if (!jobsQueue.isEmpty()) {
				// we need to take job from jobs queue
				// poll retrieve the object and remove it from the job Queue
				PCB job = jobsQueue.peek();

				// We need to check the there is available memory to put the job in ready Queue
				if (availableMemory > job.getMemoryRequired()) {
					// Adding the job to ready queue and chaning job's state to ready
					job.setProcessState("ready");
					readyQueue.add(job);
					shortestJobQueue.add(job);
					jobsQueue.poll();

					// after we added the job to ready Queue we need to reduce size of memory
					// To make sure that if there is unvailable size it won't be loaded to ready
					// Queue
					availableMemory = availableMemory - job.getMemoryRequired();

				} else {

					// This means there is not space. so,we need to get the job back to jobs queue
					job.setProcessState("waiting");
					//jobsQueue.add(job);

				}
			}
		}
	}

	// by calling the Thread this function will be execute
	@Override
	public void run() {

		BufferedReader br;
		try {
			// to read from file
			FileReader fr = new FileReader(fileBath);
			br = new BufferedReader(fr);
			String line = br.readLine();

			// getting the file data line by line
			while ((line = br.readLine()) != null) {
				if (line.contains(",")) {
					line = line.trim();

					// the data formal as: 3, 4, 5;
					String[] temJob = line.split(", ");

					// create job or process
					PCB pcb = new PCB(Integer.parseInt(temJob[0]), Integer.parseInt(temJob[1]),
							Integer.parseInt(temJob[2]));

					// adding the job to the list
					jobsQueue.add(pcb);

				}

			}
			br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	// Here the round robin algorithm
	public void processingUsingRR(int quantumTime) {

		// total waiting variable to calculate all waiting time for all processes
		// size variable to calculate the number of jobs
		double totalWaitingTime = 0;
		double totalCompletionTime =0 ;
		int size = 0;

		// This is used as line of time from starting execution until ending
		int currentTime = 0;

		// This variable used to represent the grant chart for the jobs(processes)
		String grant = "";

		// we need to make sure that all proccess will executed until the job, and ready
		// Queues bacame empty
		while (true) {

			// if ready queue is not empty perfome the following
			if (!readyQueue.isEmpty()) {

				PCB job = readyQueue.poll();
				grant += currentTime + "|p" + job.getProcessID() + " ";

				// if the remining burst time more than quantum number provided berform the
				// following
				if (job.getRemainingBurstTime() > quantumTime) {

					currentTime = currentTime + quantumTime;
					job.setRemainingBurstTime(job.getRemainingBurstTime() - quantumTime);

					// becase the remaining burst time is more than qunatum time we need to get back
					// the job
					// to ready queue again
					readyQueue.add(job);

				} else {
					// this means the quantum time more that remaining burst time, so this last time
					// for
					// the job to be executed.
					// in this stage we should terminate the job and send it to terminated Queue

					currentTime = currentTime + job.getRemainingBurstTime();
					job.setRemainingBurstTime(0);
					job.setCompletionTime(currentTime);
					totalCompletionTime += job.getCompletionTime();
					job.setWaitingTime(currentTime - job.getBurstTime());
					totalWaitingTime += job.getWaitingTime();
					size++;
					terminatedJobs.add(job);

					// we need to get back to memory space that reserved by this job
					// to able the remaining jobs( in jobs queue ) to join the ready queue.
					increaseMemory(job.getMemoryRequired());
				}
			}

			// we should make sure that the jobs queue and ready queue are became
			// empty to exist the loop ( bacuse the loop while(true)).
			if (jobsQueue.isEmpty())
				if (readyQueue.isEmpty())
					break;

		}
		grant += currentTime;
		System.err.println(grant);
		if (size == 0)
			return;
		System.out.println("\n- The Average completion Time is: " + (totalCompletionTime / size)
				+ " \n- The average waiting time is: " + (totalWaitingTime / size));
		System.out.println("The number is processes not executed is: " + readyQueue.size());

	}

	// This function will retreive all finished jobs and print the details of each
	// process
	public void printJobsDetails() {
		while (!terminatedJobs.isEmpty())
			terminatedJobs.poll().print();
	}

	// shortest job first algorithm
	public void processingUsingSJF() {

		// total waiting variable to calculate all waiting time for all processes
		// size variable to calculate the number of jobs
		double totalCompletionTime =0 ;
		double totalWaitingTime = 0;
		int size = 0;

		// This is used as line of time from starting execution until ending
		int currentTime = 0;

		// This variable used to represent the grant chart for the jobs(processes)
		String grant = "";

		// we need to make sure that all proccess will executed until the job, and ready
		// Queues bacame empty
		while (true) {

			if (!shortestJobQueue.isEmpty()) {
				// retreiving job from the priority queue
				PCB job = shortestJobQueue.poll();
				grant += currentTime + "|p" + job.getProcessID() + " ";
				currentTime = currentTime + job.getBurstTime();
				job.setCompletionTime(currentTime);
				job.setWaitingTime(job.getCompletionTime() - job.getBurstTime());
				size++;
				totalWaitingTime += job.getWaitingTime();
				totalCompletionTime += job.getCompletionTime();
				job.setProcessState("terminated");
				increaseMemory(job.getMemoryRequired());
				terminatedJobs.add(job);
			}

			// we should make sure that the jobs queue and ready queue are became
			// empty to exist the loop ( bacuse the loop while(true)).
			if (jobsQueue.isEmpty())
				if (shortestJobQueue.isEmpty())
					break;

		}
		grant += currentTime;
		System.err.println(grant);
		if (size == 0)
			return;
		System.out.println("- The Average completion Time is: " + (totalCompletionTime / size)
				+ " \n- The average waiting time is: " + (totalWaitingTime / size));

		// just in case there are remaining jobs not performed
		try {
			Thread.sleep(2000);
			System.out.println("The number is processes not executed is: " + shortestJobQueue.size());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// first come first serve algorith
	public void processingFCFS() {

		// This function is simplest, when ever process became in ready queue
		// we need to perform the execution and send it to terminate queue

		// total waiting variable to calculate all waiting time for all processes
		// size variable to calculate the number of jobs
		double totalCompletionTime = 0;
		double totalWaitingTime = 0;
		int size = 0;

		// This is used as line of time from starting execution until ending
		int currentTime = 0;

		// This variable used to represent the grant chart for the jobs(processes)
		String grant = "";

		// we need to make sure that all proccess will executed until the job, and ready
		// Queues bacame empty
		while (true) {

			if (!readyQueue.isEmpty()) {

				PCB job = readyQueue.poll();
				grant += currentTime + "|p" + job.getProcessID() + " ";
				job.setProcessState("running");
				currentTime = currentTime + job.getBurstTime();
				job.setCompletionTime(currentTime);
				job.setWaitingTime(job.getCompletionTime() - job.getBurstTime());
				size++;
				totalCompletionTime += job.getCompletionTime();
				totalWaitingTime += job.getWaitingTime();
				job.setProcessState("terminated");
				terminatedJobs.add(job);

				// we need to get back to memory space that reserved by this job
				// to able the remaining jobs to join the ready queue
				increaseMemory(job.getMemoryRequired());

			}

			// we should make sure that the jobs queue and ready queue are became
			// empty to exist the loop ( bacuse the loop while(true)).
			if (jobsQueue.isEmpty())
				if (readyQueue.isEmpty())
					break;

		}
		grant += currentTime;
		System.err.println(grant);

		if (size == 0)
			return;
		System.out.println("- The Average completion Time is: " + (totalCompletionTime / size)
				+ " \n- The average waiting time is: " + (totalWaitingTime / size));
		try {
			Thread.sleep(1000);
			System.out.println("The number is processes not executed is: " + readyQueue.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
