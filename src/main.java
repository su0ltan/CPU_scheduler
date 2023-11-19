
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.LinkedBlockingDeque;

public class main {

	public static void main(String[] args) {

		BlockingQueue<PCB> jobQueue = new LinkedBlockingDeque<>();
		String filePath = "src\\jobs.txt";

		// this class creates instance to read the data from the file
		JobReader jobReader = new JobReader(filePath, jobQueue);

		// this thread is used to reading the data from file
		Thread fileReaderThread = new Thread(jobReader);

		// while this thread is used to continuously transferring the jobs to ready
		// queue
		Thread jobLoadertoReady = new Thread(jobReader::loadJobsToReadyQueue);

		// starting the threads
		fileReaderThread.start();
		jobLoadertoReady.start();

		int r = 0;
		while (r != -1) {
			// making the user who choose the alogirthm
			System.out.println("********************************");
			System.out.println("Choose from the following the Algorithm");
			System.out.println(" 1- First Come First Serve (FCFS)");
			System.out.println(" 2- Shortest Job First (SJF)");
			System.out.println(" 3- Round Robin with quantum time 3ms");
			System.out.println(" 4- Round Robin with quantum time 5ms");
			System.out.println("Enter -1 to Exit");
			Scanner input = new Scanner(System.in);
			r = input.nextInt();
			BlockingQueue<PCB> jobReady = jobReader.getReadyQueue();
			switch (r) {
			case 1:
				FCFS fcfs = new FCFS();

				fcfs.processing(jobReady);

				break;
			case 2:
				SJF sjf = new SJF();
				sjf.processing(jobReady);
				break;

			case 3:
				RR rr3 = new RR();
				rr3.processing(jobReady, 3);
				break;
			case 4:
				RR rr5 = new RR();
				rr5.processing(jobReady, 5);
				break;
			case -1:

				input.close();
				return;
			default:
				System.out.println("Enter valid value");

			}

		}

	}

}
