package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class RepresentativeCheckContext<T> extends MessagePassingAction<T>{
	
	public RepresentativeCheckContext(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datos = (Message)this.getMensaje();
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());

		// agente intermediario
		Agent agCoxt = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());

		System.out.println("Enviando a agente Contexto");
		String aliasCxt = this.getAgent().getID().replace("RepresentativeAgent", "ContextAgent");
		agCoxt = this.getAgent().getAdmLocal().getAgent(aliasCxt);
		Message data = new Message(datos.getMessage(), datos.getReceiverAgent(),datos.getSenderAgent(), this.getAgent().getID());
		//Event evento = new Event(ExecutionContextGuard.class, data);
		ContextCheckContext evento = new ContextCheckContext(data);
		agCoxt.executeAction(evento);
		return null;
	}
}
