package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Actions.ContextCheckContext;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingReply;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeChangeContext;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeCheckContext;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeInformationReply;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeInitialQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeChangeProfile;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeRequestProfile;
import co.edu.javeriana.ashyi.pumas.Agents.ContextAgent;
import co.edu.javeriana.ashyi.pumas.Agents.RepresentativeAgent;
import co.edu.javeriana.ashyi.pumas.Data.AgentContextData;
import co.edu.javeriana.ashyi.pumas.Data.AgentRepresentativeData;

public class MainCreateUser<T> extends MessagePassingAction<T>{
	
	public MainCreateUser(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		createUserPumasAgents(datos);
		return null;
	}
	
	/**
	 *  Creacion de agentes Representante y de Contexto para los usuarios ejecutor
	 * @param datos datos que llegan del evento con alias del agente ejecutor
	 */
	private void createUserPumasAgents(Message datos) {

		List contexto = (List) datos.getMessage();

		String aliasUser = (String) contexto.get(3);

		// eliminar id actividad
		contexto.remove(2);
		// eliminar alias agente
		contexto.remove(2);
		if (!this.getAgent().getAdmLocal().doesAgentExist("RepresentativeAgent-" + aliasUser)) {
			// agente representante --> por usuario
			AgentRepresentativeData dataP = new AgentRepresentativeData();
			List<Class> acciones = new ArrayList<Class>();
			acciones.add(RepresentativeInitialQuest.class);
			acciones.add(RepresentativeSendBackingQuest.class);
			acciones.add(RepresentativeSendBackingReply.class);
			acciones.add(RepresentativeChangeContext.class);
			acciones.add(RepresentativeCheckContext.class);
			acciones.add(RepresentativeInformationReply.class);
			acciones.add(RepresentativeChangeProfile.class);
			acciones.add(RepresentativeRequestProfile.class);
			RepresentativeAgent agent = new RepresentativeAgent("RepresentativeAgent-" + aliasUser, dataP, acciones);
			agent.start();
			System.out.println("Agente "+agent.getID()+" creado");
		}

		if (!this.getAgent().getAdmLocal().doesAgentExist("ContextAgent-" + aliasUser)) {
			// agente contexto
			List<Class> acciones = new ArrayList<Class>();
			AgentContextData dataC = new AgentContextData();
			dataC.setContexto(contexto);
			acciones.add(ContextCheckContext.class);
			ContextAgent agentC = new ContextAgent("ContextAgent-" + aliasUser, dataC, acciones);
			agentC.start();
			System.out.println("Agente "+agentC.getID()+" creado");
		}

	}

}
