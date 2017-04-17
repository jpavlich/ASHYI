package co.edu.javeriana.ashyi.pumas.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentIntermediaryData;

public class IntermediaryInformacionSendRBacking<T> extends MessagePassingAction<T>{
	
	public IntermediaryInformacionSendRBacking(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentIntermediaryData datosEstado = (AgentIntermediaryData) myAgent.getDatos();

		Message datosLlegan = datos;

		// enviar a ejecutor
		Agent agentE = this.getAgent().getAdmLocal().getAgent(datosLlegan.getAliasAgentAux());// representante
		Message data = new Message(datosLlegan.getMessage(), datosLlegan.getSenderAgent(),myAgent.getID());
		//Event evento = new Event(ExecutionRepresentativeGuard.class, data);
		RepresentativeSendBackingReply evento = new RepresentativeSendBackingReply(data);
		agentE.executeAction(evento);
		return null;
	}

}

