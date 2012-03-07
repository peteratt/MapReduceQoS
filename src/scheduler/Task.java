package scheduler;

import java.util.Random;

public class Task {

	// task id
	private int taskId;
	// Default task duration time interval boundaries
	private static int minDuration = 20;
	private static int maxDuration = 120;

	// Belongs to a Job
	private Job job;

	// Task duration (randomly generated) (ms)
	private int dur;
	private int customPriority = 0;

	public int getTaskId() {
		return taskId;
	}
	
	public static void setDuration(int aveDuration) {
		minDuration = (int) Math.round(0.5 * aveDuration);
		maxDuration = (int) Math.round(1.5 * aveDuration);
	}

	/**
	 * Sets up a new task with the default interval for the task completion
	 * time.
	 * 
	 * @param taskId
	 *            The task ID.
	 */
	public Task(int taskId, Job job) {
		this(taskId, minDuration, maxDuration);
		this.job = job;
	}

	/**
	 * Sets up a new task with an specified interval for the minimum and maximum
	 * duration it may take to compute it. The actual duration is a random value
	 * that lies within the endpoints of the supplied boundaries.
	 * 
	 * @param taskId
	 *            The task ID.
	 * @param mindur
	 * @param maxdur
	 */
	public Task(int taskId, int mindur, int maxdur) {
		Random rng = new Random();
		this.taskId = taskId;
		dur = rng.nextInt(maxdur - mindur) + mindur;
	}

	/**
	 * Return the job to which this task belongs to.
	 * 
	 * @return The associated job.
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * Runs the task. Effectively delays execution for the randomly generated
	 * time value.
	 */
	public void runTask() {
		try {
			Thread.sleep(dur);
		} catch (InterruptedException e) {
		}
	}

	public int getDuration() {
		return dur;
	}

	public boolean degradePriority() {
		int priority = job.getQos().getWFQpriority();
		int newPriority = 0;
		
		switch (priority) {
		case QoSSpec.PREMIUM:
			newPriority = QoSSpec.ADVANCED;
			break;
			
		case QoSSpec.ADVANCED:
			newPriority = QoSSpec.BASIC;
			break;

		default:
			break;
		}
		
		if (newPriority > 0) {
			this.customPriority = newPriority;
			System.out.println("Task with new priority=" + newPriority + " idClient=" + job.getClient().getClientId());
			return true;
		}
		System.out.println("Dropped Task! idClient=" + job.getClient().getClientId());
		return false;
	}

	public int getPriority() {
		if (customPriority > 0) {
			return customPriority;
		} else {
			return job.getQos().getWFQpriority();
		}
	}

}
