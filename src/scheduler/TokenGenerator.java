package scheduler;

public class TokenGenerator implements Runnable {

	int id;
	private TokenBucket tokenBucket;

	public TokenGenerator(int id, TokenBucket bucket) {
		this.id = id;
		this.tokenBucket = bucket;
	}

	@Override
	public void run() {
		try {
			while (true) {
				tokenBucket.addToken();
				System.out.println("Ntokens of " + id + "=" + tokenBucket.getTokenCount());
				Thread.sleep(tokenBucket.getTokenRate());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
