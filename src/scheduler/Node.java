package scheduler;

import java.util.ArrayList;
import java.util.List;

public class Node implements NodeInterface {

	private List<NodeQueue> queues;

	private int id;

	private boolean emptyQueues = true;

	private Thread executor;

	public Node(int id) {
		queues = new ArrayList<NodeQueue>();
		this.id = id;
	}

	@Override
	public synchronized void addTask(Task t) {

		int taskWeight = t.getPriority();
		boolean found = false;

		// Search for an existing queue for the given priority
		for (NodeQueue nq : queues) {
			if (taskWeight == nq.getWeight()) {
				found = true;
				nq.queueTask(t);
				break;
			}
		}

		// Create a new one if it didn't previously exist
		if (!found) {
			NodeQueue newQueue = new NodeQueue(taskWeight);
			newQueue.queueTask(t);
			queues.add(newQueue);
		}

		notifyAll();
	}

	public int getNodeId() {
		return id;
	}

	private synchronized void runNextTask() {
		// Check the existing queues sequentially
		emptyQueues = true;
		for (NodeQueue nq : queues) {
			for (int n = 0; n < nq.getWeight(); n++) {
				Task next = nq.retrieveTaskFromQueue();

				// If the queue is empty, go on to the next queue.
				if (next == null)
					break;

				emptyQueues = false;
				next.runTask();
				next.getJob().getClient().notifyTask(next);
				
			}
		}
		// Suspend execution until a new task arrives in case we
		// didn't find any in this round.
		if (emptyQueues) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public void run() {
		executor = new Thread(new NodeExecutor());
		executor.setName("nodeThread-" + id);
		executor.start();
	}

	private class NodeExecutor implements Runnable {

		@Override
		public void run() {

			while (true) {
				runNextTask();
			}
		}

	}

}
