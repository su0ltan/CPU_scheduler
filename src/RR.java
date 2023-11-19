import java.util.concurrent.BlockingQueue;

public class RR {

	public void processing(BlockingQueue<PCB> rd, int quantumTime) {

		double totalWaitingTime = 0;
		int size = 0;
		int currentTime = 0;

		while (!rd.isEmpty()) {

			PCB job = rd.poll();

			if (job.getRemainingBurstTime() > quantumTime) {

				currentTime = currentTime + quantumTime;
				job.setRemainingBurstTime(job.getRemainingBurstTime() - quantumTime);
				rd.add(job);

			} else {

				currentTime = currentTime + job.getRemainingBurstTime();
				job.setRemainingBurstTime(0);
				job.setCompletionTime(currentTime);
				job.setWaitingTime(currentTime - job.getBurstTime());
				totalWaitingTime += job.getWaitingTime();
				job.print();
				size++;

			}

		}
		if (size == 0)
			return;
		System.out.println("- The Average completion Time is: " + (currentTime / size)
				+ " \n- The average waiting time is: " + (totalWaitingTime / size));

	}
}
