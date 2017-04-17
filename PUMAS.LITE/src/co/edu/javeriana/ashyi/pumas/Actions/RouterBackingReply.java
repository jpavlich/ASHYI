package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class RouterBackingReply<T> extends MessagePassingAction<T>{
	
	public RouterBackingReply(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datosLlegan = (Message)this.getMensaje();
		System.out.println("In Guard ExecutionRouterGuard");

		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentRouterData datosEstado = (AgentRouterData) myAgent.getDatos();

		// agente ocupado
		datosEstado.setOcupado(true);
		
		datosEstado.setConsultasRecibidas(datosEstado.getConsultasRecibidas() + 1);

		List datos = (List) datosLlegan.getMessage();

		// revisar q la actividad no se repita
		for (int i = 0; i < ((List) datos.get(0)).size(); i++) {
			if (!datosEstado.getActividadesRefuerzoSeleccionar().contains(((List) datos.get(0)).get(i)))
				datosEstado.getActividadesRefuerzoSeleccionar().add(((List) datos.get(0)).get(i));
		}

		// esperar a que lleguen todas las consultas
		if (datosEstado.getConsultasRecibidas() == datosEstado.getAgenteFI().size()) {
			// resetear
			datosEstado.setConsultasRecibidas(0);

			List datosEnviar = new ArrayList<>();

			datosEnviar.add(datosEstado.getActividadesRefuerzoSeleccionar());
			datosEnviar.add(datos.get(1));// id actividad

			// vaciar lista
			datosEstado.setActividadesRefuerzoSeleccionar(new ArrayList<>());

			// enviar solicitudes a los FI
			Agent agentI = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());// intermediary
			Message data = new Message(datosEnviar, datosLlegan.getReceiverAgent(),datosLlegan.getSenderAgent(), datosLlegan.getAliasAgentAux());
			//Event evento = new Event(ExecutionIntermediaryGuard.class, data);
			IntermediaryInformacionSendRBackingR evento = new IntermediaryInformacionSendRBackingR(data);
			agentI.executeAction(evento);
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
