package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.PlanASHYIAgentData;

public class ExecutorEditGraphRemedialA<T> extends MessagePassingAction<T>{
	
	public ExecutorEditGraphRemedialA(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		
		Message mensaje = (Message)this.getMensaje();
		System.out.println("Set actividades refuerzo en estado: " + this.getAgent().getID());

		List datosEnviar = (List) mensaje.getMessage();

		PlanASHYIAgentData datos = (PlanASHYIAgentData) myAgent.getDatos();
		datos.setActividadesRefuerzoMapa((List) datosEnviar.get(0), (int) datosEnviar.get(1));
		
		return null;
	}
	
}
