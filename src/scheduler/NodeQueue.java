package scheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeQueue {
	
	private int id;
	private int weight;
	private Queue<Task> queue;
	
	public NodeQueue(int weight) {
		queue = new ConcurrentLinkedQueue<Task>();
		this.weight = weight;
	}
	
	public Task retrieveTaskFromQueue() {
		return queue.poll();
	}
	
	public void queueTask(Task t) {
		queue.add(t);
	}
	
	public int getId() {
		return id;
	}

	public int getWeight() {
		return weight;
	}

}
