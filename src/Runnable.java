
public interface Runnable {
	
	//runs as a separate thread.
	//making the thread eligible to be run by the JVM.
	public abstract void run();
	
	//allocates memory and initializes a new thread in the JVM
	public abstract void start();
	
	
	//
	public abstract void join();
	
	

}
