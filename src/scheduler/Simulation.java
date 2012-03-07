package scheduler;

import java.util.List;

public class Simulation {
	
	// simulation id
	private int simulationId;
	// clients in this simulation
	private List<Client> clients;
	// nodes in this simulation
	private List<Node> nodes;
	
	public Simulation (int simulationId, List<Client> clients, List<Node> nodes){
		this.simulationId = simulationId;
		this.clients = clients;
		this.nodes = nodes;
	}
	
	/**
	 * Returns Client with id = clientId
	 * 
	 * @param clientId		client's id
	 * @return		Client with given id
	 */
	public Client getClientWithId(int clientId){
		Client requestedClient = null;
		Client currentClient = null;
		for(int i = 0; i < clients.size();i++){
			currentClient = clients.get(i);
			if(currentClient.getClientId()==clientId){
				requestedClient = currentClient;
			}
		}
		return requestedClient;
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	public List<Client> getClients(){
		return clients;
	}
	public int getSimulationId(){
		return simulationId;
	}
}
