package co.edu.javeriana.ashyi.kiss.Actions;

import antlr.debug.Event;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class MainChangeProfile<T> extends MessagePassingAction<T>{
	
	public MainChangeProfile(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datos = (Message)this.getMensaje();
		sentoToProfileChange(datos);
		return null;
	}
	
	/**
	 * Desarrollo del evento "Cambios en el perfil" de un usuario ejecutor
	 * Enviar evento al agente ejecutor correspondiente
	 * @param datosLlegan datos del evento
	 */
	private void sentoToProfileChange(Message datosLlegan) {

		System.out.println("Enviando agente ejecutor");
		// agente ejecutor
		Agent agente = this.getAgent().getAdmLocal().getAgent(datosLlegan.getSenderAgent());

		Message data = new Message( datosLlegan.getMessage(), datosLlegan.getSenderAgent(), this.getAgent().getID());
		ExecutorChangeProfile evento = new ExecutorChangeProfile(data);
		agente.executeAction(evento);
	}

}