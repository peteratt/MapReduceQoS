package scheduler;

public interface ClientInterface {
	
	/**
	 * Called from a Node. Notifies a client that a Task has been correctly
	 * computed.
	 * 
	 * @param t
	 */
	public void notifyTask(Task t);
	
}
