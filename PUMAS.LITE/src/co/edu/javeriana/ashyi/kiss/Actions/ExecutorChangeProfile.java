package co.edu.javeriana.ashyi.kiss.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeChangeProfile;

public class ExecutorChangeProfile<T> extends MessagePassingAction<T>{
	
	public ExecutorChangeProfile(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datosLlegan = (Message)this.getMensaje();
		sentoToProfileChange(datosLlegan);
		return null;
	}
	
	/**
	 * Desarrollo del evento "Cambios en el perfil" de un usuario ejecutor
	 * Enviar evento al agente ejecutor correspondiente
	 * @param datosLlegan datos del evento
	 */
	private void sentoToProfileChange(Message datosLlegan) {

		System.out.println("Enviando a agente representante");

		String aliasRep = this.getAgent().getID().replace("Executor", "RepresentativeAgent");
		Agent agente = this.getAgent().getAdmLocal().getAgent(aliasRep);

		Message data = new Message(datosLlegan.getMessage(),datosLlegan.getSenderAgent(), this.getAgent().getID());
		RepresentativeChangeProfile evento = new RepresentativeChangeProfile(data);
		agente.executeAction(evento);
	}

}
