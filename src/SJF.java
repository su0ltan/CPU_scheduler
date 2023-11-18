import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;

public class SJF {

	public void processing(BlockingQueue<PCB> rd) {

		double totalWaitingTime = 0;
		int size = 0;
		int currentTime = 0;
		PriorityQueue<PCB> shortestJobQueue = new PriorityQueue<>(Comparator.comparingInt(pcb -> pcb.getBurstTime()));

		shortestJobQueue.addAll(rd);

		while (!shortestJobQueue.isEmpty()) {
			PCB job = shortestJobQueue.poll();
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
