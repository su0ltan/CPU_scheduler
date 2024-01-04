
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.LinkedBlockingDeque;

public class main {

	public static void main(String[] args) {

		
		String filePath = "src\\jobs.txt";

		// this class creates instance to read the data from the file
		JobReader jobReader = new JobReader(filePath);

		// this thread is used to reading the data from file
		Thread fileReaderThread = new Thread(jobReader);

		// while this thread is used to continuously transferring the jobs to ready
		// queue
		Thread jobLoadertoReady = new Thread(jobReader::loadJobsToReadyQueue);

		// starting the threads
		fileReaderThread.start();

		jobLoadertoReady.start();

		int r = 0;

		// making the user who choose the alogirthm
		System.out.println("********************************");
		System.out.println("Choose from the following the Algorithm");
		System.out.println(" 1- First Come First Serve (FCFS)");
		System.out.println(" 2- Shortest Job First (SJF)");
		System.out.println(" 3- Round Robin with quantum time 3ms");
		System.out.println(" 4- Round Robin with quantum time 5ms");
		System.out.println(" 5- Enter -1 to Exit");
		Scanner input = new Scanner(System.in);
		r = input.nextInt();
		
		switch (r) {
		case 1:

			jobReader.processingFCFS();

			System.out.println("************************");
			System.out.println("Enter from the following:");
			System.out.println("1) To print all jobs details");
			System.out.println("2) Exit");
			int r1 = input.nextInt();
			switch (r1) {
			case 1:
				jobReader.printJobsDetails();
				break;
			case 2:
				return;

			}

			break;
		case 2:
			jobReader.processingUsingSJF();

			System.out.println("************************");
			System.out.println("Enter from the following:");
			System.out.println("1) To print all jobs details");
			System.out.println("2) Exit");
			r1 = input.nextInt();
			switch (r1) {
			case 1:
				jobReader.printJobsDetails();
				break;
			case 2:
				return;
			}

			break;

		case 3:
			jobReader.processingUsingRR(50);
			System.out.println("************************");
			System.out.println("Enter from the following:");
			System.out.println("1) To print all jobs details");
			System.out.println("2) Exit");
			r1 = input.nextInt();
			switch (r1) {
			case 1:
				jobReader.printJobsDetails();
				break;
			case 2:
				return;
			}

			break;
		case 4:
			jobReader.processingUsingRR(5);
			System.out.println("************************");
			System.out.println("Enter from the following:");
			System.out.println("1) To print all jobs details");
			System.out.println("2) Exit");
			r1 = input.nextInt();
			switch (r1) {
			case 1:
				jobReader.printJobsDetails();
				break;
			case 2:
				return;
			}
			break;
		case -1:

			input.close();
			return;
		
		default:
			System.out.println("Enter valid value");

		}

	}

}
