package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;
import co.edu.javeriana.ashyi.pumas.Actions.InformationSourceSendBackingInformation;
import co.edu.javeriana.ashyi.pumas.Actions.InformationSourceSendInformation;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediaryInformacionSendRBacking;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediarySendBackingQuest;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediaryInformacionSendRBackingR;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediaryInitialQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RouterBackingInformation;
import co.edu.javeriana.ashyi.pumas.Actions.RouterBackingReply;
import co.edu.javeriana.ashyi.pumas.Actions.RouterReplyInformationR;
import co.edu.javeriana.ashyi.pumas.Actions.RouterSendInformation;
import co.edu.javeriana.ashyi.pumas.Actions.RouterReplyInformation;
import co.edu.javeriana.ashyi.pumas.Agents.InformationSourceAgent;
import co.edu.javeriana.ashyi.pumas.Agents.IntermediaryAgent;
import co.edu.javeriana.ashyi.pumas.Agents.RouterAgent;
import co.edu.javeriana.ashyi.pumas.Data.AgentInformationSourceData;
import co.edu.javeriana.ashyi.pumas.Data.AgentIntermediaryData;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class MainCreatePumas<T> extends MessagePassingAction<T>{
	
	public MainCreatePumas(Message mensaje) {
		super(mensaje);
	}
	public T execute() {
		Message datos = (Message)this.getMensaje();
		createPumasAgents();
		return null;
	}
	/**
	 * Creacion de agentes fuente de onformacion, enrutador e intermediario para el sistema
	 */
	private void createPumasAgents() {

		MainAgentData myData = (MainAgentData) this.getAgent().getAdmLocal().getAgent(this.getAgent().getID()).getDatos();

		// agente fuente de informacion
		AgentInformationSourceData dataD = new AgentInformationSourceData();
		dataD.setTipoBusqueda(1);// sql
		List<Class> acciones = new ArrayList<Class>();
		acciones.add(InformationSourceSendInformation.class);
		acciones.add(InformationSourceSendBackingInformation.class);
		InformationSourceAgent agentD = new InformationSourceAgent("InformationSourceAgent-1", dataD, acciones);
		agentD.start();

		myData.getAgentes().add("RouterAgent-1");
		// agente router
		AgentRouterData dataR = new AgentRouterData();
		dataR.setTipoBusqueda(1);// sql
		dataR.getAgenteFI().add("InformationSourceAgent-1");
		acciones = new ArrayList<Class>();
		acciones.add(RouterBackingReply.class);
		acciones.add(RouterBackingInformation.class);
		acciones.add(RouterReplyInformationR.class);
		acciones.add(RouterSendInformation.class);
		acciones.add(RouterReplyInformation.class);
		RouterAgent agentR = new RouterAgent("RouterAgent-1", dataR, acciones);
		agentR.start();

		myData.getAgentes().add("IntermediaryAgent");
		// agente intermediary
		AgentIntermediaryData dataI = new AgentIntermediaryData();
		dataI.getAgentesEnrutadores().add("RouterAgent-1");// primer agente informacion
		acciones = new ArrayList<Class>();
		acciones.add(IntermediarySendBackingQuest.class);		
		acciones.add(IntermediaryInformacionSendRBacking.class);
		acciones.add(IntermediaryInformacionSendRBackingR.class);
		acciones.add(IntermediaryInitialQuest.class);
		IntermediaryAgent agentI = new IntermediaryAgent("IntermediaryAgent", dataI, acciones);
		agentI.start();
	}

}
