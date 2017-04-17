package co.edu.javeriana.ashyi.kiss;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class Admin {
	public static final Admin INSTANCE = new Admin();
	private Map<String, Agent> agents = new ConcurrentHashMap<String, Agent>();
	
	private Admin() {
		
	}


	public Agent getAgent(String id) {
		Agent agent = agents.get(id);
		if (agent == null) {
			throw new RuntimeException("Agente " + id + " no existe");
		}
		return agent;
	}

	public boolean doesAgentExist(String name) {
		return agents.containsKey(name);
	}


	public void registerAgent(Agent agent) {
		agents.put(agent.getID(), agent);

		
	}
}
