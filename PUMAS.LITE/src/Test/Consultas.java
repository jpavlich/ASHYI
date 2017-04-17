package Test;
import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;
import co.edu.javeriana.ashyi.kiss.Admin;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediarySendBackingQuest;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediaryInformacionSendRBackingR;
import co.edu.javeriana.ashyi.pumas.Actions.IntermediaryInitialQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RouterBackingReply;
import co.edu.javeriana.ashyi.pumas.Actions.RouterSendInformation;
import co.edu.javeriana.ashyi.pumas.Actions.RouterReplyInformation;
import co.edu.javeriana.ashyi.pumas.Agents.IntermediaryAgent;
import co.edu.javeriana.ashyi.pumas.Agents.RouterAgent;
import co.edu.javeriana.ashyi.pumas.Data.AgentIntermediaryData;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class Consultas {
	public static Consulta c = new Consulta();

	public static void main(String[] args) {
		Admin contenedor = Admin.INSTANCE;
		System.out.println("!!!!!!!!!!!!!!!!!!!: contenedor creado");
		// agente intermediary
		List<Class> acciones = new ArrayList<Class>();
		AgentIntermediaryData dataI = new AgentIntermediaryData();
		dataI.getAgentesEnrutadores().add("RouterAgent-1");// primer agente informacion
		acciones = new ArrayList<Class>();
		acciones.add(IntermediarySendBackingQuest.class);		
		acciones.add(IntermediaryInformacionSendRBackingR.class);
		acciones.add(IntermediaryInitialQuest.class);
		IntermediaryAgent agentI = new IntermediaryAgent("IntermediaryAgent", dataI, acciones);
		agentI.start();
		System.out.println("!!!!!!!!!!!!!!!!!!!: contenedor AgentIntermediary");
		// agente router
		AgentRouterData dataR = new AgentRouterData();
		dataR.setTipoBusqueda(1);// sql
		dataR.getAgenteFI().add("InformationSourceAgent-1");
		acciones = new ArrayList<Class>();
		acciones.add(RouterBackingReply.class);
		acciones.add(RouterSendInformation.class);
		acciones.add(RouterReplyInformation.class);
		RouterAgent agentR = new RouterAgent("RouterAgent-1", dataR, acciones);
		agentR.start();
		System.out.println("!!!!!!!!!!!!!!!!!!!: contenedor AgentRouter");

		List<ObjetivosActividad> objs = c.getObjetivos("Entornos digitales", 3);

		System.out.println("!!!!!!!!!!!!!!!!!!!: " + objs.size() + "objetivos encontrados");

		Agent handler = contenedor.getAgent("IntermediaryAgent");

		AgentIntermediaryData estadoIAg = (AgentIntermediaryData) handler.getDatos();

		estadoIAg.setConsulta(objs);
		estadoIAg.setNivelActividad("Universitario");
		estadoIAg.setTipoConsulta(1);
		System.out.println("!!!!!!!!!!!!!!!!!!!: consultando");
		Message data = new Message( objs, "", "");
		IntermediaryInitialQuest evento = new IntermediaryInitialQuest(data);
		handler.executeAction(evento);
	}

}
