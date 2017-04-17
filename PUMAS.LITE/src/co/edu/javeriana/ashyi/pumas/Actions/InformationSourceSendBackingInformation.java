package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Recurso;
import co.edu.javeriana.ASHYI.model.RecursosActividad;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;

public class InformationSourceSendBackingInformation<T> extends MessagePassingAction<T>{
	
	public InformationSourceSendBackingInformation(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datosM = (Message)this.getMensaje();
		
		System.out.println("In Guard ExecutionInformationSourceGuard");

		Message datosLlegan = datosM;

		String tipoActividades = "";
		Consulta cAshyi = new Consulta();

		List preActividades = new ArrayList<Actividad>();
		List<List<?>> lista = (List<List<?>>) datosLlegan.getMessage();

		if (lista.size() > 0) {

			// List<Actividad> actividades = (List<Actividad>) lista.get(0);

			// List<Recurso> queryRecursos = (List<Recurso>) lista.get(2);

			String nivel = ((List<String>) lista.get(0)).get(0);

			int tipoC = ((List<Integer>) lista.get(1)).get(0);
			int idActividad = ((List<Integer>) lista.get(1)).get(1);

			double goalMatch = 0;
			double hwMatch = 0;
			double levelMatch = 0;
			if (tipoC == 5)// refuerzo
			{
				List<Actividad> actividades = getActividadesRefuerzo(cAshyi);
				for (int i = 0; i < actividades.size(); i++) {
					levelMatch = compareLevel(actividades.get(i), nivel);
					if (levelMatch > 0) {
						preActividades.add(actividades.get(i).getIdActividad());
					}
				}
				tipoActividades = "Editor-GRA";
				
				Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());

				List aliasExecutor = new ArrayList<Integer>();
				aliasExecutor = lista.get(2);// alias de ejecutor

				List datos = new ArrayList<>();

				datos.add(preActividades);
				datos.add(idActividad);

				// enviar actividades
				Agent agentE = this.getAgent().getAdmLocal().getAgent(datosLlegan.getSenderAgent());
				// Message data = new
				// Message(tipoActividades,preActividades,
				// "",myHandler.getID(),ASHYIExecutionGuard.class);
				Message data = new Message(datos, datosLlegan.getReceiverAgent(),(String) aliasExecutor.get(0), datosLlegan.getAliasAgentAux());
				//Event evento = new Event(ExecutionRouterGuard.class, data);
				RouterBackingReply evento = new RouterBackingReply(data);
				agentE.executeAction(evento);
			}
		}
		return null;
	}
	
	/**
	 * Consulta actividades de refuerzo
	 * @param cAshyi Puente de consulta
	 * @return lista de actividades de refuerzo
	 */
	public List<Actividad> getActividadesRefuerzo(Consulta cAshyi) {
		List<Actividad> actividades = new ArrayList<Actividad>();
		// Consulta cAshyi = new Consulta();
		// consultar actividades segun consulta
		actividades = cAshyi.getActividadesRefuerzo();
		System.out.println("Refuerzo: " + actividades.size());
		return actividades;
	}
	
	/**
	 * Consulta actividades segun parametros especificos i.e. lista de objetivos a cumplir
	 * @param list lista de parametros
	 * @param cAshyi Puente de consulta
	 * @return lista de actividades que cumplen con la consulta realizada
	 */
	public List<Actividad> getActividades(List<List> list, Consulta cAshyi) {
		List<Actividad> actividades = new ArrayList<Actividad>();
		// Consulta cAshyi = new Consulta();
		// consultar actividades segun consulta
		actividades = cAshyi.getActividades(list);
		return actividades;
	}

	/**
	 * Compara el nivel de una actividad
	 * @param actividad Actividad a verificar
	 * @param nivel Nivel a comparar
	 * @return 1 es compatible, 0 si no lo es
	 */
	private double compareLevel(Actividad actividad, String nivel) {

		if (actividad.getNivel() != null)
			if (actividad.getNivel().equals(nivel)) {
				return 1;
			}

		return 0;
	}

	/**
	 * Busca si los recursos listados corresponden a las actividades que se tienen
	 * @param queryR Lista de recursos
	 * @param recursos Lista de recursos de actividad
	 * @return 1 es compatible, 0 si no lo es
	 */
	public double compareResources(List<Recurso> queryR, List<RecursosActividad> recursos) {
		if (queryR.size() > 0) {
			double[] general = new double[queryR.size()];
			double[] actividad = new double[queryR.size()];

			int i = 0;

			for (Recurso objQ : queryR) {
				general[i] = 1;
				for (RecursosActividad obj : recursos) {
					if (objQ.equals(obj.getIdRecurso())) {
						actividad[i] = 1;
					} else {
						actividad[i] = 0;
					}
				}

				i++;
			}

			// Metodos m = new Metodos();
			// return m.jaccard(general, actividad, 0, queryR.size());
			return 1;
		} else
			return 0;
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
