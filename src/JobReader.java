import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class JobReader implements java.lang.Runnable {
	private String fileBath;
	private BlockingQueue<PCB> jobsQueue;
	private BlockingQueue<PCB> readyQueue;
	public int availableMemory;

	public JobReader(String fileBath, BlockingQueue<PCB> jobsQueue) {

		this.fileBath = fileBath;
		this.jobsQueue = jobsQueue;
		availableMemory = 0;
		readyQueue = new LinkedBlockingQueue<>();

	}

	//This function will be executes continously with different thread
	public synchronized void loadJobsToReadyQueue() {
		while (true) {
			if (!jobsQueue.isEmpty()) {
				PCB job = jobsQueue.poll();
				if (checkMemoryAvailability(job.getMemoryRequired())) {
					readyQueue.add(job);
					//System.out.println("Job " + job.getProcessID() + " loaded to ready queue.");
				} else {
					System.out.println("Not enough memory for Job " + job.getProcessID() + ". Skipping.");
					
					
				}
			}
		}
	}

	
	//this function is to check the buffer size 
	private boolean checkMemoryAvailability(int jobSize) {
		if (availableMemory > 8192)
			return false;
		return jobSize <= availableMemory;
	}

	public BlockingQueue<PCB> getReadyQueue() {
		return readyQueue;
	}

	public void setReadyQueue(BlockingQueue<PCB> readyQueue) {
		this.readyQueue = readyQueue;
	}

	//by calling the Thread this function will be execute
	@Override
	public void run() {

		BufferedReader br;
		try {
			//to read from file
			FileReader fr = new FileReader(fileBath);
			br = new BufferedReader(fr);
			String line = br.readLine();

			
			//getting the file data line by line
			while ((line = br.readLine()) != null) {
				if (line.contains(",")) {
					line = line.trim();

					//the data formal as: 3, 4, 5;
					String[] temJob = line.split(", ");

					//create job or process
					PCB pcb = new PCB(Integer.parseInt(temJob[0]), Integer.parseInt(temJob[1]),
							Integer.parseInt(temJob[2]));

					//adding the job to the list
					jobsQueue.add(pcb);
					availableMemory = availableMemory + pcb.getMemoryRequired();
					

				}

			}
			br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
