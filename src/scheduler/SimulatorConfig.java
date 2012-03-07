package scheduler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.thoughtworks.xstream.XStream;

public class SimulatorConfig {

	// XStream for object (de)serialization
	private static XStream xstream;

	/**
	 * Writes configuration file. Only for testing purposes. Creates two
	 * simulations with the same clients and nodes.
	 */
	private static void writeConfigurationFile(String simulationName,
			int numberOfClients, int numberOfNodes, int clientQos) {
		Simulation simulation = null;
		List<Client> clients = new ArrayList<Client>();
		List<Node> nodes = new ArrayList<Node>();
		QoSSpec qos = null;
		Client client = null;
		Node node = null;
		
		Random randomGenerator = new Random();
		
		if (clientQos == 0) {
			int randomQos;
			for (int i = 0; i < numberOfClients; i++) {
				randomQos = randomGenerator.nextInt(3) + 1;
				qos = new QoSSpec(randomQos);
				client = new Client(i, qos);
				clients.add(client);
			}
		} else {
			for (int i = 0; i < numberOfClients; i++) {
				qos = new QoSSpec(clientQos);
				client = new Client(i, qos);
				clients.add(client);
			}
		}
		for (int i = 0; i < numberOfNodes; i++) {
			node = new Node(i);
			nodes.add(node);
		}

		simulation = new Simulation(1, clients, nodes);

		try {
			FileOutputStream fs = new FileOutputStream("./config.xml");
			xstream.toXML(simulation, fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configures the XStream object
	 */
	private static void configureXStream() {
		xstream = new XStream();
		xstream.alias("simulation", Simulation.class);
		xstream.alias("client", Client.class);
		xstream.alias("qos", QoSSpec.class);
		xstream.alias("nodes", Node.class);
		xstream.alias("nodequeue", NodeQueue.class);
		xstream.alias("joblist",
				java.util.concurrent.ConcurrentLinkedQueue.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		configureXStream();
		// args[0] - filename
		// args[1] - number of clients
		// args[2] - number of nodes
		// args[3] - QoS specs (0 for random)
		if (args.length > 4) {
			if (args[4].contains("duration=")) {
				Task.setDuration(Integer.parseInt(args[4].split("=")[1]));
			}
			if (args[4].contains("nTasks=")) {
				Simulator.setMeanTasks(Integer.parseInt(args[4].split("=")[1]));
			}
		}
		writeConfigurationFile(args[0], Integer.parseInt(args[1]),
				Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		
	}

}
