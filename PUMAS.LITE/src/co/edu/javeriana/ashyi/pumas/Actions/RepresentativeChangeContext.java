package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorVerifyContextChange;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRepresentativeData;

public class RepresentativeChangeContext<T> extends MessagePassingAction<T>{
	
	public RepresentativeChangeContext(
			co.edu.javeriana.ashyi.kiss.Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datos = (Message)this.getMensaje();
	
		Message datosLlegan = (Message) datos;
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());
		AgentRepresentativeData dx = (AgentRepresentativeData) myAgent.getDatos();

		// agente intermediario
		Agent agentS = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());
		
		List datosEnviar = (List) datosLlegan.getMessage();

		datosEnviar.add(dx.getCaracteristicasUsuario());

		System.out.println("Enviando a agente Ejecutor");
		agentS = this.getAgent().getAdmLocal().getAgent(datosLlegan.getSenderAgent());
		Message data = new Message(datosEnviar, datosLlegan.getReceiverAgent(), this.getAgent().getID());
		//Event evento = new Event(ASHYIExecutionGuard.class, data);
		ExecutorVerifyContextChange evento = new ExecutorVerifyContextChange(data);
		agentS.executeAction(evento);

		
		return null;
	}

}
