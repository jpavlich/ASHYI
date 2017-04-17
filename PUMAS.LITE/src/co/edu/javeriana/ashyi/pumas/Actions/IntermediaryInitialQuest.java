package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentIntermediaryData;
import co.edu.javeriana.ashyi.pumas.Data.AgentRouterData;

public class IntermediaryInitialQuest<T> extends MessagePassingAction<T>{
	
	public IntermediaryInitialQuest(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		AgentIntermediaryData datosEstado = (AgentIntermediaryData) myAgent.getDatos();

		Message datosLlegan = datos;

		datosEstado.setConsulta((List) ((List) datosLlegan.getMessage()).get(0));
		datosEstado.setNivelActividad((String) ((List) datosLlegan.getMessage()).get(1));
		datosEstado.setTipoConsulta((int) ((List) datosLlegan.getMessage()).get(3));
		datosEstado.setTipoBusqueda((int) ((List) datosLlegan.getMessage()).get(4));

		int idActividad = (int) ((List) datosLlegan.getMessage()).get(2);
		Message data;
		// encontrar el agente enrutador de acuerdo al tipo de busqueda
		// q est� disponible
		Agent handlerRouter = encontrarAgenteRouter(datosEstado.getTipoBusqueda(), datosEstado.getAgentesEnrutadores());

		if (handlerRouter != null) {
			// datosEstado.setActividades(getActividades(datosEstado.getConsulta(),
			// datosEstado.getTipoConsulta()));

			List<List<?>> lista = new ArrayList<List<?>>();

			// lista.add(datosEstado.getActividades());

			lista.add(datosEstado.getConsulta());

			List<String> niveles = new ArrayList<String>();

			niveles.add(datosEstado.getNivelActividad());

			lista.add(niveles);

			List<Integer> tipos = new ArrayList<Integer>();

			tipos.add(datosEstado.getTipoConsulta());
			tipos.add(datosEstado.getTipoBusqueda());
			tipos.add(idActividad);

			lista.add(tipos);

			myAgent.setDatos(datosEstado);

			System.out.println("A agente router");

			data = new Message(lista, datosLlegan.getSenderAgent(), myAgent.getID(), datosLlegan.getAliasAgentAux());
			//Event evento = new Event(ExecutionRouterGuard.class, data);
			RouterSendInformation evento = new RouterSendInformation(data);
			handlerRouter.executeAction(evento);
		}
		return null;
	}

	/**
	 * Obtener el handler del agente enrutador 
	 * @param tipoBusqueda tipo de consultas manejadas por el agente
	 * @param listaEnrutadores lista de agentes enrutadores
	 * @return handler del agente enrutador
	 * @throws ExceptionManager
	 */
	public Agent encontrarAgenteRouter(int tipoBusqueda, List listaEnrutadores){

		Agent agRouter = null;

		for (int i = 0; i < listaEnrutadores.size(); i++) {
			agRouter = this.getAgent().getAdmLocal().getAgent((String) listaEnrutadores.get(i));
			AgentRouterData datosR = (AgentRouterData) agRouter.getDatos();
			// si el enrutador es del mismo tipo
			if (datosR.getTipoBusqueda() == tipoBusqueda) {
				// si esta libre
				if (!datosR.isOcupado())
					return agRouter;
			}
		}

		return null;
	}

}
