package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Actions.ExecutorEditGraph;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Message;

public class RepresentativeSendBackingReply<T> extends MessagePassingAction<T>{
	
	public RepresentativeSendBackingReply(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
	
		Message datos = (Message)this.getMensaje();
		
		// enviar actividades
		Agent agEditor = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());
		Message data = new Message(datos.getMessage(), "", "");
		//Event evento = new Event(ASHYIExecutionGuard.class, data);
		ExecutorEditGraph evento = new ExecutorEditGraph(data);
		agEditor.executeAction(evento);
		
		return null;
	}

}
