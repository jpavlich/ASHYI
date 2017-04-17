package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class RouterSendInformation<T> extends MessagePassingAction<T>{
	
	public RouterSendInformation(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		System.out.println("In Guard ExecutionRouterGuard");

		Agent myHandler = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentRouterData datosEstado = (AgentRouterData) myHandler.getDatos();

		// agente ocupado
		datosEstado.setOcupado(true);

		Message datosLlegan = datos;
		
		List<List<?>> lista = (List<List<?>>) datosLlegan.getMessage();
		List aliasExecutor = new ArrayList<Integer>();
		aliasExecutor.add(datosLlegan.getReceiverAgent());

		lista.add(aliasExecutor);

		for (int i = 0; i < datosEstado.getAgenteFI().size(); i++) {
			// enviar solicitudes a los FI
			Agent agEditor = this.getAgent().getAdmLocal().getAgent((String) datosEstado.getAgenteFI().get(i));
			Message data = new Message(lista, datosLlegan.getSenderAgent(), myHandler.getID(),datosLlegan.getAliasAgentAux());
			InformationSourceSendInformation evento = new InformationSourceSendInformation(data);
			agEditor.executeAction(evento);
		}
		datosEstado.setOcupado(false);
		return null;
	}
	
	/**
	 * Obtener id de usuario a partir del alias del agente
	 * @param aliasAg alias del agente contexto
	 * @return id de usuario
	 */
	public String getIdUsuarioFormAlias(String aliasAg) {
		String newAlias = "";
		String[] subStrings = aliasAg.split("-");
		for (int i = 0; i < subStrings.length; i++) {
			// System.out.println(subStrings[i]);
			if (i > 0 && i < subStrings.length - 1) {
				newAlias += subStrings[i];
				if (i < subStrings.length - 2)
					newAlias += "-";
			}
		}

		return newAlias;
	}

}
