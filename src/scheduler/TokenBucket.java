package scheduler;

public class TokenBucket implements TokenBucketInterface {

	private int tokenRate;
	private double capacity;
	private boolean clientActive;

	/**
	 * Tokens needed for forwarding a task to a node
	 */
	private double forwardingTokensNeeded;

	/**
	 * Tokens currently in bucket
	 */
	private double tokenCount;
	private boolean maxDurationViolated;

	public TokenBucket(int rate, double capacity) {

		this.tokenRate = rate;
		this.capacity = capacity;

		maxDurationViolated = false;

		forwardingTokensNeeded = 1;
		tokenCount = 0.0;

	}

	@Override
	public boolean postTask(Task t) throws InterruptedException {
		Simulator.printTaskPosted(t.getTaskId(), t.getJob().getClient().getQos().getQoS());
		synchronized (this) {
			// Tries to remove the needed tokens
			while (tokenCount < forwardingTokensNeeded)
				wait();
			if (maxDurationViolated) { // Degrade the priority
				Simulator.printTaskNotConformant(t.getTaskId(), t.getJob().getClient().getQos().getQoS());
				if (!t.degradePriority()) {
					maxDurationViolated = false;
					return false; // Not able to forward it
				}
			}
		}
		removeTokens();
		// Sends the Task to a Node
		NodeList.forwardTask(t);
		return true;
	}

	@Override
	public synchronized void receiveLastDuration(int millis, int id) {
		forwardingTokensNeeded = Math.ceil(forwardingTokensNeeded + (double) millis) / 2;

		System.out.println("ClientId=" + id + ": task return with duration " + millis
				+ ", new forwarding tokens needed " + forwardingTokensNeeded);
		
		if (forwardingTokensNeeded > capacity) {
			maxDurationViolated = true;
			forwardingTokensNeeded = capacity;
		}
	}

	public synchronized void addToken() {
		double diff = capacity - tokenCount;
		if (diff >= 1) {
			tokenCount += 1.0;
		} else if (diff > 0 && diff < 1) {
			tokenCount = capacity;
		}

		if (tokenCount > forwardingTokensNeeded) {
			notifyAll();
		}
		if (tokenCount == capacity) {
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	private synchronized void removeTokens() {
		tokenCount -= forwardingTokensNeeded;
		notifyAll();
	}

	public void init(int id) {
		this.clientActive = true;
		TokenGenerator gen = new TokenGenerator(id);
		Thread genThread = new Thread(gen);
		genThread.setName("tokenGenerator-" + id);
		genThread.start();
	}

	public int getTokenRate() {
		return tokenRate;
	}

	public double getTokenCount() {
		return tokenCount;
	}
	
	public void clientEnded() {
		clientActive = false;
	}
	
	public class TokenGenerator implements Runnable {

		int id;
		long sleepTime;

		public TokenGenerator(int id) {
			this.id = id;
			this.sleepTime = Math.round((1 / (double)tokenRate) * 1000);
		}

		@Override
		public void run() {
			try {
				while (clientActive) {
					addToken();
					// System.out.println("Ntokens of " + id + "=" + tokenCount);
					Thread.sleep(sleepTime);
				}
				System.out.println("Client ended");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


}
