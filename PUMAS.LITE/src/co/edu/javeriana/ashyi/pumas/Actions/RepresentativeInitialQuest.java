package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class RepresentativeInitialQuest<T> extends MessagePassingAction<T>{
	
	public RepresentativeInitialQuest(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		System.out.println("In Guard ExecutionRepresentativeGuard");

		Message datosLlegan = datos;
		
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());
		
		Message data = new Message( datosLlegan.getMessage(),datosLlegan.getReceiverAgent(), datosLlegan.getSenderAgent(), myAgent.getID());
		//Event evento = new Event(ExecutionIntermediaryGuard.class, data);
		IntermediaryInitialQuest evento = new IntermediaryInitialQuest(data);
		// agente intermediario
		Agent handler = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());
		handler.executeAction(evento);
		
		return null;
	}

}
