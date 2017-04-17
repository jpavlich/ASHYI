package co.edu.javeriana.ashyi.kiss.Actions;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;

public class MainEnableDIsableActivities<T> extends MessagePassingAction<T>{
	
	public MainEnableDIsableActivities(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		// enviar a ejecutor para ruta
		Message mensaje = (Message)this.getMensaje();
		
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		
		MainAgentData datos = (MainAgentData) myAgent.getDatos();
		
		Integer idActividad = (Integer) mensaje.getMessage();
		datos.setEstadoActividadesMapa(true, idActividad);
		System.out.println("Estado de actividad registrado, cambio de grafos");
		return null;
	}
	
}
