package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentIntermediaryData;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class IntermediarySendBackingQuest<T> extends MessagePassingAction<T>{
	
	public IntermediarySendBackingQuest(Message mensaje) {
		super(mensaje);
	}
	
	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentIntermediaryData datosEstado = (AgentIntermediaryData) myAgent.getDatos();

		Message datosLlegan = datos;
		
		Message data;

		List<Object> consultaE = (List<Object>) datosLlegan.getMessage();

		List<Object> datosEnviar = (List<Object>) consultaE.get(0);

		datosEstado.setNivelActividad((String) (datosEnviar.get(0)));
		datosEstado.setTipoConsulta((int) consultaE.get(1));
		datosEstado.setTipoBusqueda((int) consultaE.get(2));

		// AgentBESA handlerRouter =
		// this.getAgent().getAdmLocal().getAgent("RouterAgent");
		Agent agRouter = encontrarAgenteRouter(datosEstado.getTipoBusqueda(),datosEstado.getAgentesEnrutadores());

		List<List<?>> lista = new ArrayList<List<?>>();

		List<String> niveles = new ArrayList<String>();

		niveles.add(datosEstado.getNivelActividad());

		lista.add(niveles);

		List<Integer> tipos = new ArrayList<Integer>();

		tipos.add(datosEstado.getTipoConsulta());
		tipos.add((Integer) datosEnviar.get(1));// idActividad

		lista.add(tipos);

		myAgent.setDatos(datosEstado);

		System.out.println("A agente router");

		data = new Message(lista, datosLlegan.getSenderAgent(),myAgent.getID(), datosLlegan.getAliasAgentAux());
		RouterBackingInformation evento = new RouterBackingInformation(data);
		agRouter.executeAction(evento);
		return null;
	}

	/**
	 * Obtener el handler del agente enrutador 
	 * @param tipoBusqueda tipo de consultas manejadas por el agente
	 * @param listaEnrutadores lista de agentes enrutadores
	 * @return handler del agente enrutador
	 * @throws ExceptionManager
	 */
	public Agent encontrarAgenteRouter(int tipoBusqueda, List listaEnrutadores) {

		Agent agRouter = null;

		for (int i = 0; i < listaEnrutadores.size(); i++) {
			agRouter = this.getAgent().getAdmLocal().getAgent((String) listaEnrutadores.get(i));
			AgentRouterData datos = (AgentRouterData) agRouter.getDatos();
			// si el enrutador es del mismo tipo
			if (datos.getTipoBusqueda() == tipoBusqueda) {
				// si esta libre
				if (!datos.isOcupado())
					return agRouter;
			}
		}

		return null;
	}
	

}
