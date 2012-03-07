package scheduler;

public interface TokenBucketInterface {
	
	/**
	 * Called from a Client. Posts a task to be forwarded by the bucket to the Node.
	 * 
	 * @param t
	 * @return true if the forwarding was correct, false otherwise
	 * 
	 * @throws InterruptedException
	 */
	boolean postTask(Task t) throws InterruptedException;

	/**
	 * Called from a Node. It returns the last duration of the computation.
	 * 
	 * @param millis
	 */
	void receiveLastDuration(int millis, int id);

}
