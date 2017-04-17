package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.Methods.MetodosFuncionDistancia;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.ExecutorASHYIAgentData;

public class ExecutorVerifyContextChange<T> extends MessagePassingAction<T>{
	
	//private Message datos;
	
	public ExecutorVerifyContextChange(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datosLlegan = (Message)this.getMensaje();
		
		List datosL = (List) datosLlegan.getMessage();

		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) myAgent.getDatos();
		// si hay cambios de actividades
		System.out.println("Verificar cambios para items de usuarios");
		if (!((Map<Integer, List<Integer>>) datosL.get(0)).isEmpty()) {
			System.out.println("Si hay cambios");
			changeActivities(datos, (Map<Integer, List<Integer>>) datosL.get(0), (int) datosL.get(1),
					(Graph) datosL.get(2), (List<CaracteristicasUsuario>) datosL.get(3));
		}

		datos.setEstadoContexto(true);
		return null;
	}
	
	/**
	 * Busca actividades alternas para objetivos, seg[un cambios de contexto
	 * @param datos datos que llegan del evento
	 * @param mapItemsCambio mapa de items para buscar el cambio
	 * @param idActividad id actividad recursiva
	 * @param grafo del usuario
	 * @param perfil del usuario
	 */
	public void changeActivities(ExecutorASHYIAgentData datos, Map<Integer, List<Integer>> mapItemsCambio,
			int idActividad, Graph grafo, List<CaracteristicasUsuario> perfil) {

		List actividades = datos.getActividadesMapa(idActividad);

		MetodosFuncionDistancia m = new MetodosFuncionDistancia();
		Consulta c = new Consulta();

		// consultar las caracteristicas del sistema
		List<List<Caracteristica>> caracteristicas = c.getCaracteristicas();
		//false --> sin personalidad, true. tipo con personalidad
		Map<Integer, List<Caracteristica>> mapa = getCaracteristicasActividad(c, grafo, 1, false);

		System.out.println("Verificar distancia nueva");

		for (int i = 0; i < actividades.size(); i++) {
			if (mapItemsCambio.containsKey(actividades.get(i))) {
				List<Integer> idsCambio = mapItemsCambio.get(actividades.get(i));
				double distMenor = 100;
				int idMenor = 0;
				double usuario[] = construirusuario(perfil, c);
				for (int j = 0; j < idsCambio.size(); j++) {
					ItemPlan item = c.getItemPlan(idsCambio.get(j));
					double actividad[] = construirActividad(item, caracteristicas, mapa);
					double distancia = (double) m.calculateDistance(usuario, actividad);

					if (distancia < distMenor) {
						distMenor = distancia;
						idMenor = idsCambio.get(j);
					}
				}
				actividades.set(i, idMenor);
			}
		}

		System.out.println("Actividades a cambiar");
		// setactividades de agente
		datos.setActividadesMapa(actividades, idActividad);

		// cambiar base de datos
		String idUsuario = getIdUsuarioFormAlias(this.getAgent().getID().replace("Executor-", ""));
		c.cambiarItemsUsuario(actividades, idUsuario, idActividad);
	}
	
	/**
	 * Con base en los datos de la actividad, construir un arreglo con los valores del mismo 
	 * @param item ItemPlan asociado
	 * @param caracteristicas lista de caracteristicas de la actividad
	 * @param mapa Lista de caracteristicas completas del sistema
	 * @return arreglo de las caracteristicas de la actividad
	 */
	private double[] construirActividad(ItemPlan item, List<List<Caracteristica>> caracteristicas,
			Map<Integer, List<Caracteristica>> mapa) {
		// Consulta consAHYI = new Consulta();
		double[] actividad = new double[55];

		// 1 pre condiciones y 2 consulta tipo
		// List<Caracteristica> caracteristicasActividad =
		// getCaracteristicasAc(item, consAHYI,1,2);
		List<Caracteristica> caracteristicasActividad = mapa.get(item.getIdItemPlan());

		int pos = 0;
		// construir el vector de actividad seg�n el orden de las
		// caracteristicas
		for (int i = 0; i < caracteristicas.size(); i++) {
			for (Caracteristica cG : caracteristicas.get(i)) {
				int encuentra = 0;
				for (Caracteristica cA : caracteristicasActividad) {
					if (cG.getIdCaracteristica() == cA.getIdCaracteristica()) {
						actividad[pos] = 1;
						encuentra++;
						break;
					}
				}
				if (encuentra == 0)
					actividad[pos] = 0;
				pos++;
			}
		}
		return actividad;
	}
	
	/**
	 * Extraer el ID de usuario del alias completo del agente 
	 * @param aliasAg Alias del agente
	 * @return ID de usuario
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
	
	/**
	 * Encontrar las caracteristicas pre o post condicion de una actividad 
	 * @param item ItemPlan a buscar
	 * @param consAHYI variable para la consulta / puente @see Consulta
	 * @param tipo tipo de consulta (pre (1) o post(2))
	 * @param queConsulta tipo de consulta (TipoActividad (2))
	 * @param correlacion false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return caracteristicas pre o post condicion de una actividad
	 */
	public List<Caracteristica> getCaracteristicasAc(ItemPlan item, Consulta consAHYI, int tipo, int queConsulta, boolean correlacion) {
		Actividad a = item.getIdActividad();
		return consAHYI.getCaracteristicasActividad(a, tipo, queConsulta, correlacion);
	}

	
	/**
	 * Obtener las caracteristicas de las actividades del grafo de una actividad recursiva
	 * @param c variable para la consulta / puente @see Consulta
	 * @param grafo Grafo de actividades de la actividad recursiva
	 * @param tipo Tipo de consulta (pre 1 - post 2)
	 * @param correlacion false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return mapa con las carateristicas de las actividades
	 */
	public Map<Integer, List<Caracteristica>> getCaracteristicasActividad(Consulta c, Graph<Integer> grafo, int tipo, boolean correlacion) {
		Map<Integer, List<Caracteristica>> mapa = new LinkedHashMap<Integer, List<Caracteristica>>();
		for (int vertice : grafo.getVertices()) {
			ItemPlan item = c.getItemPlan(vertice);
			// 1 pre condiciones y 2 consulta tipo
			List<Caracteristica> caracteristicasActividad = getCaracteristicasAc(item, c, tipo, 2, correlacion);
			// for(Caracteristica d: caracteristicasActividad)
			// {
			// System.out.println("CAr: "+vertice+"  : "+d.getIdCaracteristica());
			// }
			mapa.put(vertice, caracteristicasActividad);
		}
		return mapa;
	}
	
	/**
	 * Con base en los datos del perfil de usuario ejecutor, construir un arreglo con los valores del mismo
	 * @param perfil caracteristicas del usaurio ejecutor
	 * @param consAHYI variable para la consulta / puente @see Consulta
	 * @return arreglo de las caracteristicas del usuario ejecutor
	 */
	private double[] construirusuario(List<CaracteristicasUsuario> perfil, Consulta consAHYI) {
		double[] usuario = new double[55];
		// consultar las caracteristicas del sistema
		// Consulta consAHYI = new Consulta();
		List<List<Caracteristica>> caracteristicas = consAHYI.getCaracteristicas();

		int pos = 0;
		// organizar el vector de usuario seg�n el orden de las
		// caracteristicas
		for (int i = 0; i < caracteristicas.size(); i++)
			for (Caracteristica cG : caracteristicas.get(i)) {
				for (CaracteristicasUsuario cU : perfil) {
					if (cG.getIdCaracteristica() == cU.getIdCaracteristica().getIdCaracteristica()) {
						usuario[pos] = cU.getNivel();
						break;
					} else
						usuario[pos] = 0;
				}
				pos++;
			}

		return usuario;
	}
}
