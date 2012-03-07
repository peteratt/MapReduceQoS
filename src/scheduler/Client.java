package scheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client implements ClientInterface, Runnable {

	private int clientId;

	private Queue<Job> jobList;
	private Job currentJob;

	private TokenBucket bucket;
	private QoSSpec qos;

	public Client(int id, QoSSpec spec) {
		this.clientId = id;
		this.qos = spec;
		jobList = new ConcurrentLinkedQueue<Job>();
		fillJobList();
		bucket = new TokenBucket(spec.getTBrate(), spec.getTBcapacity());
	}

	private void fillJobList() {
		// Generates a random number of jobs
		int numberOfJobs = Simulator.generateNumberOfJobs();

		for (int i = 0; i < numberOfJobs; i++) {
			// Adds random jobs to the list
			jobList.add(Simulator.generateRandomJob(this));
		}
	}

	@Override
	public void notifyTask(Task t) {
		currentJob.taskCompleted(t);
		bucket.receiveLastDuration(t.getDuration(), clientId);
	}

	@Override
	public void run() {
		try {
			bucket.init(clientId);
			// A client iterates over the list of jobs it has
			while (jobList.iterator().hasNext()) {
				// Retrieves next job
				currentJob = jobList.poll();
				Simulator.printStartJob(currentJob.getJobId(), qos.getQoS());

				boolean notFailed = true;

				while (notFailed && (currentJob.hasUnsubmittedTasks())) {
					Task nextTaskToPost = currentJob.retrieveTask();
					notFailed = bucket.postTask(nextTaskToPost);
				}
				if (notFailed) {
					System.out.println("Job done succesfully in " + clientId);
					Simulator.printEndJob(currentJob.getJobId(), qos.getQoS());
				} else {
					System.out.println("Job failed in " + clientId);
				}
			}
			System.out.println("ALL JOBS DONE FOR CLIENT " + clientId);
			bucket.clientEnded();
			Simulator.postJobsEnded();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getClientId() {
		return clientId;
	}

	public QoSSpec getQos() {
		return qos;
	}

}
