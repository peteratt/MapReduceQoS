package scheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Job {

	private Queue<Task> pendingTasks;
	private Queue<Task> finishedTasks;
	private int jobId;

	// Belongs to a client
	private Client client;

	public Job(int jobId, Client client) {
		this.jobId = jobId;
		this.client = client;

		pendingTasks = new ConcurrentLinkedQueue<Task>();
		finishedTasks = new ConcurrentLinkedQueue<Task>();

		fillTasks();
	}

	public int getJobId() {
		return jobId;
	}

	private void fillTasks() {
		int taskNumber = Simulator.generateNumberOfTasks();

		for (int i = 0; i < taskNumber; i++) {
			pendingTasks.add(Simulator.generateRandomTask(this));
		}
	}

	public boolean hasUnsubmittedTasks() {
		if (pendingTasks.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public Task retrieveTask() {
		return pendingTasks.poll();
	}

	public void taskCompleted(Task t) {
		finishedTasks.add(t);
	}

	public QoSSpec getQos() {
		return client.getQos();
	}
	
	public Client getClient() {
		return client;
	}

}
