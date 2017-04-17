package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class RepresentativeSendBackingQuest<T> extends MessagePassingAction<T>{
	
	public RepresentativeSendBackingQuest(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datos = (Message)this.getMensaje();
		
		Agent agIntermediary = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());
		
		Message data = new Message(datos.getMessage(), datos.getReceiverAgent(),datos.getSenderAgent(), myAgent.getID());
		IntermediarySendBackingQuest evento = new IntermediarySendBackingQuest(data);
		agIntermediary.executeAction(evento);
		return null;
	}
}
