package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.ExecutorASHYIAgentData;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeCheckContext;

public class ExecutorVerifyContextCheck<T> extends MessagePassingAction<T>{
	
	public ExecutorVerifyContextCheck(Message mensaje) {
		super(mensaje);
	}

	//private Message datos;
	
	public T execute() {
		Message datosLlegan = (Message)this.getMensaje();
		
		senToContextCheck(datosLlegan);
		return null;
	}
	
	/**
	 * Enviar contexto de agente a agente representante
	 * Se realiza por un cambio en el contexto del usuario
	 * @param datosLlegan datos que llegan del evento
	 */
	private void senToContextCheck(Message datosLlegan) {

		System.out.println("Enviando a agente representante");

		String aliasRep = this.getAgent().getID().replace("Executor", "RepresentativeAgent");
		Agent agente = this.getAgent().getAdmLocal().getAgent(aliasRep);
		
		List datos = (List) datosLlegan.getMessage();
		List contextoLlega = (List) datos.get(0);

		ExecutorASHYIAgentData misDatos = (ExecutorASHYIAgentData) this.getAgent().getDatos();
		
		// actividades de ejecutor
		datos.add(misDatos.getActividadesMapa((int) contextoLlega.get(2)));

		Message data = new Message(datos, datosLlegan.getSenderAgent(), this.getAgent().getID());
		//Event evento = new Event(ExecutionRepresentativeGuard.class, data);
		RepresentativeCheckContext evento = new RepresentativeCheckContext(data);
		agente.executeAction(evento);

	}
}
