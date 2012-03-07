package scheduler;

public interface NodeInterface {
	
	/**
	 * Called from the NodeList. Adds a task to a queue.
	 * 
	 * @param source
	 * @param t
	 */
	public void addTask(Task t);

}
