package scheduler;

import java.util.List;
import java.util.Random;

// CUTRE, CORREGIR
public class NodeList {
	
	private static List<Node> nodeList;
	
	public static void init(List<Node> nodes) {
		nodeList = nodes;
	}

	public static void forwardTask(Task t) {
		// Selects a random node in the list
		Random r = new Random();
		int index = r.nextInt(nodeList.size());
		nodeList.get(index).addTask(t);
	}

}
