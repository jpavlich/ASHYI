package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;

public class MainPassContextCheck<T> extends MessagePassingAction<T>{
	
	public MainPassContextCheck(Message mensaje) {
		super(mensaje);
	}

	//private Message datos;
	
	public T execute() {
		Message datosLlegan = (Message)this.getMensaje();
		
		Agent agente = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		MainAgentData datosAg = (MainAgentData) agente.getDatos();
		sendToContextChange(datosLlegan, datosAg);
		return null;
	}
	
	/**
	 * Desarrollo del evento "Cambios en el contexto" de un usuario ejecutor
	 * Enviar evento al agente ejecutor correspondiente
	 * @param datosLlegan datos del evento
	 * @param datos datos propios del estado den agente principal
	 */
	private void sendToContextChange(Message datosLlegan, MainAgentData datos) {

		System.out.println("Enviando agente ejecutor");
		Agent agenteS = this.getAgent().getAdmLocal().getAgent(datosLlegan.getSenderAgent());

		// enviar grafo del curso
		List datosEnviar = new ArrayList<>();
		List datosLista = (List) datosLlegan.getMessage();
		datosEnviar.add(datosLista);
		datosEnviar.add(datos.getGrafoMapa((int) datosLista.get(2)));

		Message data = new Message(datosEnviar, datosLlegan.getSenderAgent(), this.getAgent().getID());
		//Event evento = new Event(ASHYIExecutionGuard.class, data);
		ExecutorVerifyContextCheck evento = new ExecutorVerifyContextCheck(data);
		agenteS.executeAction(evento);

	}
}
