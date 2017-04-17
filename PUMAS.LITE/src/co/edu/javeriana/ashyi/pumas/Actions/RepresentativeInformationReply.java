package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Actions.ExecutorEditGraph;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Message;

public class RepresentativeInformationReply<T> extends MessagePassingAction<T>{
	
	public RepresentativeInformationReply(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
		Message datos = (Message)this.getMensaje();
		System.out.println("Enviando agente ejecutor");
		// agente ejecutor
		Agent handlerEditor = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());
		Message data = new Message(datos.getMessage(), "", "");
		ExecutorEditGraph evento = new ExecutorEditGraph(data);
		handlerEditor.executeAction(evento);
		return null;
	}

}
