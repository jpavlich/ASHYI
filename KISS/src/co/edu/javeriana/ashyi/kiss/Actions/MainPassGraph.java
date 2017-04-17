package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.List;

import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;

public class MainPassGraph<T> extends MessagePassingAction<T>{
	
	public MainPassGraph(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		// enviar a ejecutor para ruta
		Message mensaje = (Message)this.getMensaje();
		
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		MainAgentData datos = (MainAgentData) myAgent.getDatos();
		
		List<Object> listaDatos = (List<Object>) mensaje.getMessage();
		datos.setGrafoMapa((Graph) listaDatos.get(0),
				(int) listaDatos.get(2));
		datos.setIdItemsMapa((List) listaDatos.get(1),
				(int) listaDatos.get(2));
		return null;
	}
	
}
