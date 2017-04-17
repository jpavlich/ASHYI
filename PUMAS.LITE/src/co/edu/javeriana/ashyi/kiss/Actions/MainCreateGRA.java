package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeSendBackingQuest;
import co.edu.javeriana.ashyi.pumas.Actions.RepresentativeInitialQuest;

public class MainCreateGRA<T> extends MessagePassingAction<T>{
	
	public MainCreateGRA(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		generateGraphPlan(datos, "RepresentativeAgent",
					datos.getSenderAgent(), 5);
				
		return null;
	}
	
	/**
	 * Desarrollo del evento CreatePlan
	 * Inicia la construccion del grafo de actividades a jecutar por un usuario ejecutor
	 * @param consulta Que tipo de busqueda de actividades se desea realiza, i.e. objetivos
	 * @param aliasAgPumas Alias de agente Pumas asociado
	 * @param aliasExecutor Alias de agente Ejecutor asociado
	 * @param tipo Tipo de consulta  1 --> actividades, 5 --> actividades de refuerzo
	 */
	private void generateGraphPlan(Object consulta, String aliasAgPumas, String aliasExecutor, int tipo) {
		String[] pedazos = aliasExecutor.split("-");
		String aliasPumas = aliasAgPumas + "-";
		for (int i = 1; i < pedazos.length; i++) {
			aliasPumas += pedazos[i];
			if (i < pedazos.length - 1)
				aliasPumas += "-";

		}
		
		Agent agente = this.getAgent().getAdmLocal().getAgent(aliasPumas);
		System.out.println("Obteniendo agente pumas " + agente.getID());

		if (agente.getID() != null) {
			// pedir actividades a pumas
			Message datosLlegan = (Message) consulta;

			// 1 sql
			askActivitysToPumas(datosLlegan.getMessage(), aliasPumas,
					aliasExecutor, tipo, 1);
		}
	}
	
	/**
	 * Consulta de actividades atomicas segun criterios especificos
	 * @param consulta Puente de consulta
	 * @param aliasAg alias del agente representante
	 * @param aliasExecutor alias del agente ejecutor
	 * @param tipo tipo de actividad a consultar 1-->actividad, 5-->actividad de refuerzo
	 * @param tipoBusqueda segun el tipo de consulta 1-->sql
	 */
	private void askActivitysToPumas(Object consulta, String aliasAg,
			String aliasExecutor, int tipo, int tipoBusqueda) {
		System.out.println("Solicitando actividades a PUMAS");
		String aliasAgI = getAgentPumasALias("Intermediary");
		Agent agente = this.getAgent().getAdmLocal().getAgent(aliasAg);// handler del representante

		if (tipo == 1)// actividades
		{
			List<Object> consultaE = (List<Object>) consulta;
			consultaE.add(tipo);// objetivo
			consultaE.add(tipoBusqueda);// sql
			Message data = new Message(consultaE, aliasAgI, aliasExecutor);
			//Event evento = new Event(ExecutionRepresentativeGuard.class, data);
			RepresentativeInitialQuest evento = new RepresentativeInitialQuest(data);
			agente.executeAction(evento);
		}

		if (tipo == 5)// actividades de refuerzo
		{
			List<Object> consultaE = new ArrayList<>();
			consultaE.add(consulta);// objetivo
			consultaE.add(tipo);// refuerzo
			consultaE.add(tipoBusqueda);// sql
			Message data = new Message(consultaE, aliasAgI, aliasExecutor);
			RepresentativeSendBackingQuest evento = new RepresentativeSendBackingQuest( data);
			agente.executeAction(evento);
		}
	}

	/**
	 * Obtener el alias de un agente Pumas en especifico
	 * @param consulta Puente de consulta
	 * @return el alias de agente Pumas
	 */
	public String getAgentPumasALias(String consulta) {
		String aliasAg = "";
		Agent agente = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		
		MainAgentData datos = (MainAgentData) agente.getDatos();
		for (String agenteS : datos.getAgentes()) {
			if (agenteS.contains(consulta)) {
				aliasAg = agenteS;
				break;
			}
		}
		return aliasAg;
	}

}
