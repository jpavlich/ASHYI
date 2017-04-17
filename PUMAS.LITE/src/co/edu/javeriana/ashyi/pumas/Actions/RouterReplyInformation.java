package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class RouterReplyInformation<T> extends MessagePassingAction<T>{
	
	public RouterReplyInformation(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
		
		Message datos1 = (Message)this.getMensaje();
		
		System.out.println("In Guard ExecutionRouterGuard");

		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentRouterData datosEstado = (AgentRouterData) myAgent.getDatos();

		// agente ocupado
		datosEstado.setOcupado(true);

		Message datosLlegan = datos1;

		datosEstado.setConsultasRecibidas(datosEstado.getConsultasRecibidas() + 1);
		// revisar q la actividad no se repita
		List datos = (List) datosLlegan.getMessage();
		for (int i = 0; i < ((List) datos.get(0)).size(); i++) {
			if (!datosEstado.getActividadeSeleccionar().contains(((List) datos.get(0)).get(i)))
				datosEstado.getActividadeSeleccionar().add(((List) datos.get(0)).get(i));
		}

		// esperar a que lleguen todas las consultas
		if (datosEstado.getConsultasRecibidas() == datosEstado.getAgenteFI().size()) {
			// resetear
			datosEstado.setConsultasRecibidas(0);

			List datosEnviar = new ArrayList<>();

			datosEnviar.add(datosEstado.getActividadeSeleccionar());
			datosEnviar.add(datos.get(1));// id actividad

			// vaciar lista
			datosEstado.setActividadeSeleccionar(new ArrayList<>());

			// enviar solicitudes a los FI
			Agent handler = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());// intermediary
			Message data = new Message(datosEnviar, datosLlegan.getReceiverAgent(),	datosLlegan.getSenderAgent(), datosLlegan.getAliasAgentAux());
			IntermediaryInformacionSendRBacking evento = new IntermediaryInformacionSendRBacking(data);
			handler.executeAction(evento);
			datosEstado.setOcupado(false);
		}
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
