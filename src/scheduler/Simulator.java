package scheduler;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.thoughtworks.xstream.XStream;

public class Simulator {

	// XStream for object (de)serialization
	private static XStream xstream;

	// existing simulations
	private static List<Simulation> simulations;

	// mean of the gaussian distribution to get the number of jobs
	private static final int MEAN_JOBS = 15;
	// variance of the gaussian distribution to get the number of jobs
	private static final int VARIANCE_JOBS = 2;
	// mean of the gaussian distribution to get the number of tasks
	private static int meanTasks = 20;
	// variance of the gaussian distribution to get the number of tasks
	private static final int VARIANCE_TASKS = 5;

	// job id. Increased each time a job is created
	private static int jobId;
	// task id. Increased each time a task is created
	private static int taskId;

	private static BufferedWriter outExp1;

	private static int clientsEnded;

	private static int totalClients;

	private static BufferedWriter outExp2;

	/**
	 * Initializes the simulator
	 */
	public static void init() {
		jobId = 0;
		taskId = 0;
		clientsEnded = 0;

		simulations = new ArrayList<Simulation>();

		configureXStream();
		readConfigurationFile();
		
		try {
			outExp1 = new BufferedWriter(new FileWriter("outExp1_" + now() + ".log"));
			outExp2 = new BufferedWriter(new FileWriter("outExp2_" + now() + ".log"));

		} catch (IOException e) {
		}
	}

	/**
	 * Starts the simulator
	 */
	public static void start() {
		// Para cada simulación en simulations, recuperar la lista de
		// clientes (getClients), nodos (getNodes) y arrancarlos todos.

		List<Client> clientsInThisSimulation;
		List<Node> nodesInThisSimulation;
		Simulation currentSimulation;

		while (simulations.iterator().hasNext()) {
			currentSimulation = simulations.remove(0);
			System.out.println("simulation id:"
					+ currentSimulation.getSimulationId());
			clientsInThisSimulation = currentSimulation.getClients();
			totalClients = clientsInThisSimulation.size();
			nodesInThisSimulation = currentSimulation.getNodes();
			
			NodeList.init(nodesInThisSimulation);
			// este fragmento es para comprobar que el fichero de configuración
			// se ha importado correctamente. Debería imprimir por consola el id
			// de las dos simulaciones y el id de cada cliente en la simulación.
			// Hay que tener en cuenta que son simulaciones idénticas, con
			// distinto id.
			for (Node node : nodesInThisSimulation) {
				System.out.println("node id:" + node.getNodeId());
				node.run();
			}
			while (clientsInThisSimulation.iterator().hasNext()) {
				Client client = clientsInThisSimulation.remove(0);
				System.out.println("client id:" + client.getClientId());
				Thread clientThread = new Thread(client);
				clientThread.setName("clientThread-" + client.getClientId());
				clientThread.start();
			}
		}

	}

	public static void printResults() {

	}

	/**
	 * Generates task
	 * 
	 * @param job
	 *            the generated task is part of this job
	 * @param multiplier 
	 * @return the generated task
	 */
	public static Task generateRandomTask(Job job) {
		Task newTask = new Task(taskId, job);

		taskId++;

		return newTask;
	}

	/**
	 * Generates a random "number of tasks"
	 * 
	 * @return number of tasks
	 */
	public static int generateNumberOfTasks() {
		Random random = new Random();

		int numberOfTasks = (int) Math.abs(meanTasks + random.nextGaussian()
				* VARIANCE_TASKS);

		return numberOfTasks;
	}

	/**
	 * Generates a random "number of jobs"
	 * @return number of jobs
	 */
	public static int generateNumberOfJobs() {
		Random random = new Random();

		int numberOfJobs = (int) Math.abs(MEAN_JOBS + random.nextGaussian()
				* VARIANCE_JOBS);

		return numberOfJobs;
	}

	/**
	 * Generates job
	 * 
	 * @param client
	 *            owner of the job
	 * @param multiplier 
	 * @return the generated job
	 */
	public static Job generateRandomJob(Client client) {
		Job newJob = new Job(jobId, client);
		jobId++;

		return newJob;
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
	 * Reads parameters from the configuration file and builds a Simulator
	 * object with those parameters
	 */
	private static void readConfigurationFile() {
		try {
			FileInputStream fs = new FileInputStream("./config.xml");
			Simulation simulation = new Simulation(0, null, null);
			xstream.fromXML(fs, simulation);
			simulations.add(simulation);
			// System.out.println(getSimulationId()); // just to check if the
			// simulator has been successfully created
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// METODO DE PRUEBA, NO TIENE NINGUNA FINALIDAD!
	public int getSimulationId() {
		Simulation simulation = simulations.get(0);
		return simulation.getSimulationId();
	}

	public static void printStartJob(int jobId, int qoS) {
		try {
			outExp1.write("start " + now() + " " + jobId + " " + qoS + "\n");
		} catch (IOException e) {
		}
	}
	
	public static void printEndJob(int jobId, int qoS) {
		try {
			outExp1.write("end " + now() + " " + jobId + " " + qoS + "\n");
		} catch (IOException e) {
		}
	}

	private static long now() {
		return System.currentTimeMillis();
	}

	public static void postJobsEnded() {
		clientsEnded++;
		if (clientsEnded == totalClients) {
			try {
				outExp1.close();
			} catch (IOException e) {
			}
			System.exit(0);
		}
	}

	public static void setMeanTasks(int newMean) {
		meanTasks = newMean;
	}

	public static void printTaskNotConformant(int taskId, int qoS) {
		try {
			outExp2.write("nonConf " + taskId + " " + qoS + "\n");
		} catch (IOException e) {
		}
	}

	public static void printTaskPosted(int taskId, int qoS) {
		try {
			outExp2.write("posted " + taskId + " " + qoS + "\n");
		} catch (IOException e) {
		}
	}

}
