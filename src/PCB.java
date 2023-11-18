
public class PCB {

	private int processID;
	private String processState;
	public int burstTime;
	private int memoryRequired;
	private int waitingTime, completionTime;

	public int getProcessID() {
		return processID;
	}

	public String getProcessState() {
		return processState;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public int getMemoryRequired() {
		return memoryRequired;
	}

	public PCB(int processID, int burstTime, int memoryRequired) {
		super();
		this.processID = processID;
		this.processState = "new";
		this.burstTime = burstTime;
		this.memoryRequired = memoryRequired;
		waitingTime =completionTime= 0;
		
		
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(int completionTime) {
		this.completionTime = completionTime;
	}

	public void print() {
		System.out.println("Process ID: " + getProcessID() + " process burst time: " + getBurstTime()
				+ " Memory requires: " + getMemoryRequired()
				+ "\nWaiting time for the job: " + getWaitingTime()
				+ "\nCompletion time needed: "+ getCompletionTime());
	}

}
