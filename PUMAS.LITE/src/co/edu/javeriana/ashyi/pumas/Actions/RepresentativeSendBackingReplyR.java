package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Actions.ExecutorEditGraphRemedialA;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Message;

public class RepresentativeSendBackingReplyR<T> extends MessagePassingAction<T>{
	
	public RepresentativeSendBackingReplyR(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
	
		Message datos = (Message)this.getMensaje();
		
		// enviar actividades
		Agent agEditor = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());
		Message data = new Message(datos.getMessage(), "", "");
		//Event evento = new Event(ASHYIExecutionGuard.class, data);
		ExecutorEditGraphRemedialA evento = new ExecutorEditGraphRemedialA(data);
		agEditor.executeAction(evento);
		
		return null;
	}

}
