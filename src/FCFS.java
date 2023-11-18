import java.util.concurrent.BlockingQueue;

public class FCFS {

	public void processing(BlockingQueue<PCB> rd) {

		double totalWaitingTime = 0;
		int size = 0;
		int currentTime = 0;

		while (!rd.isEmpty()) {
			PCB job = rd.poll();
			currentTime = currentTime + job.getBurstTime();
			job.setCompletionTime(currentTime);
			job.setWaitingTime(job.getCompletionTime() - job.getBurstTime());
			size++;
			totalWaitingTime += job.getWaitingTime();
			job.print();

		}
		if (size == 0)
			return;
		System.out.println("- The Average completion Time is: " + (currentTime / size)
				+ " \n- The average waiting time is: " + (totalWaitingTime / size));

	}
}
