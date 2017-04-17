package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeInitialQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingReply;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeChangeContext;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeCheckContext;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeInformationReply;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeChangeProfile;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeRequestProfile;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingReplyR;
import co.edu.javeriana.ashyi.pumas.Agents.RepresentativeAgent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRepresentativeData;

public class MainCreateUserPlan<T> extends MessagePassingAction<T>{
	
	public MainCreateUserPlan(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datos = (Message)this.getMensaje();
		createUserPlanPumasAgents((String) datos.getMessage());
		return null;
	}
	
	/**
	 * Enviar evento para creacion de los agentes de pumas para un usuario planificador
	 * @param aliasUser alias del agente del usuario 
	 */
	private void createUserPlanPumasAgents(String aliasUser) {

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
		acciones.add(RepresentativeSendBackingReplyR.class);
		RepresentativeAgent agent = new RepresentativeAgent("RepresentativeAgent-" + aliasUser, dataP, acciones);
		agent.start();

	}

}
