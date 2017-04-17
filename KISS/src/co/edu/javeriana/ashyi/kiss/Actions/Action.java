package co.edu.javeriana.ashyi.kiss.Actions;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public abstract class Action<T> {
	
	private Agent agent;
		
	public abstract T execute();
	
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
}
