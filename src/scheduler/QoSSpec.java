package scheduler;

public class QoSSpec {
	
	public static final int PREMIUM = 3;
	public static final int ADVANCED = 2;
	public static final int BASIC = 1;
	
	private int TBrate; // In tokens/sec
	private double TBcapacity;
	
	private int WFQpriority;
	private int qos;
	
	public QoSSpec(int classOfService) {
		if (classOfService == PREMIUM) {
			qos = PREMIUM;
			TBrate = 800;
			TBcapacity = 10000;
			WFQpriority = 10;
		} else if (classOfService == ADVANCED) {
			qos = ADVANCED;

			TBrate = 300;
			TBcapacity = 150;
			WFQpriority = 3;
		} else {
			qos = BASIC;

			// Basic
			TBrate = 100;
			TBcapacity = 125;
			WFQpriority = 1;
		}
	}

	public int getTBrate() {
		return TBrate;
	}

	public double getTBcapacity() {
		return TBcapacity;
	}

	public int getWFQpriority() {
		return WFQpriority;
	}

	public int getQoS() {
		return qos;
	}
	
}
