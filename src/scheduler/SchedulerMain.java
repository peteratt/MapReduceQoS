package scheduler;

public class SchedulerMain {
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("2 arguments required. Exiting");
			System.exit(1);
		}
		Simulator.init();
		Simulator.start();
		
//		GraphPlotter graph = new GraphPlotter("prueba", "titulo de prueba");
//		Hashtable<String,Double> values = new Hashtable<String,Double>();
//		values.put("valor1", 12.2);
//		values.put("valor2", 13.5);
//		graph.plotBarsGraph("valores x", "valores y", values);
//		Hashtable<String,Integer> pieValues = new Hashtable<String,Integer>();
//		graph = new GraphPlotter("prueba_pie", "titulo de prueba");
//		pieValues.put("item1",30);
//		pieValues.put("item2",20);
//		pieValues.put("item3",50);
//		graph.plotPieGraph(pieValues);
		
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
