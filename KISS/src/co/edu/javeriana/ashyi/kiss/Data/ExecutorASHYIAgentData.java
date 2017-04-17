package co.edu.javeriana.ashyi.kiss.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.ashyi.Graph.Graph;

/**
 * @author ASHYI
 * Datos del agente ejecutor
 * @see Data
 */
public class ExecutorASHYIAgentData extends Data {
	
	/**
	 * Numero de caracteristicas cumplidas por el agente
	 */
	private int caracteristicasHechas;
	/**
	 * Lista de actividades atomica por actividad recursiva
	 */
	private Map<Integer, List> actividades;
	/**
	 * Estado de el calculo de grafo de actividades atomica por actividad recursiva
	 */
	private Map<Integer, Boolean> estaCalculado;
	/**
	 * Grafo de actividades atomica por actividad recursiva
	 */
	private Map<Integer, Graph> grafoEjecutar;
	/**
	 * Nombre de alguna actividad de refuerzo realizada
	 */
	private String actividadRecursiva;
	/**
	 * Si el contexto del agente ha cambiado o no
	 */
	private boolean estadoContexto;
		
	/**
	 * Constructor base
	 */
	public ExecutorASHYIAgentData() {
		actividades = new LinkedHashMap<Integer, List>();
		estaCalculado = new LinkedHashMap<Integer, Boolean>();
		grafoEjecutar = new LinkedHashMap<Integer, Graph>();
		caracteristicasHechas = 0;
		actividadRecursiva = "";
		estadoContexto = false;
	}

	/**
	 * Constructor
	 * @param actividades Mapa de actividades atomica por actividad recursiva
	 */
	public ExecutorASHYIAgentData(Map<Integer, List> actividades) {
		super();
		this.actividades = actividades;
		estaCalculado = new LinkedHashMap<Integer, Boolean>();
		actividadRecursiva = "";
		estadoContexto = false;
	}

	/**
	 * @return Mapa de actividades atomicas por actividad recursiva
	 */
	public Map<Integer, List> getActividades() {
		return actividades;		
	}
	
	/**
	 * @param idActividad id actividad recursiva
	 * @return actividades atomicas asociadas a la actividad recursiva dada
	 */
	public List getActividadesMapa(int idActividad) {
		return actividades.get(idActividad);		
	}

	/**
	 * @param actividades Mapa de actividades atomicas por actividad recursiva
	 */
	public void setActividades(Map<Integer, List> actividades) {
		this.actividades = actividades;
	}
	
	/**
	 * @param actividades actividades atomicas por actividad recursiva
	 * @param idActividad id de actividad recursiva asociada
	 */
	public void setActividadesMapa(List actividades, int idActividad) {
		if(!this.actividades.containsKey(idActividad))
			this.actividades.put(idActividad, actividades);
	}

	/**
	 * @return Mapa de estados de calculo por actividad recursiva
	 */
	public Map<Integer, Boolean> isEstaCalculado() {
		return estaCalculado;
	}
	
	/**
	 * @param idActividad id actividad recursiva asociada al calculo del grafo a buscar
	 * @return si esta calculado o no
	 */
	public Boolean isEstaCalculadoMapa(int idActividad) {
		if(!estaCalculado.isEmpty())
		{
			if(estaCalculado.containsKey(idActividad))
			{
				return estaCalculado.get(idActividad);
			}
		}
		return false;
	}

	/**
	 * @param estaCalculado  Mapa de estados de calculo por actividad recursiva
	 */
	public void setEstaCalculado(Map<Integer, Boolean> estaCalculado) {
		this.estaCalculado = estaCalculado;
	}
	
	/**
	 * @param estado estado del calculo del grafo en una actividad recursiva
	 * @param idActividad id actividad recursiva asociada
	 */
	public void setEstaCalculadoMapa(boolean estado, int idActividad) {
		if(!this.estaCalculado.containsKey(idActividad))
			this.estaCalculado.put(idActividad, estado);
	}

	/**
	 * @return caracteristicasHechas
	 */
	public int getCaracteristicasHechas() {
		return caracteristicasHechas;
	}

	/**
	 * @param caracteristicasHechas por el agente
	 */
	public void setCaracteristicasHechas(int caracteristicasHechas) {
		this.caracteristicasHechas = caracteristicasHechas;
	}

	/**
	 * @return Mapa de grafos de actividades a ejecutar por actividad recursiva
	 */
	public Map<Integer, Graph> getGrafoEjecutar() {
		return grafoEjecutar;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return grafo de la actividad recursiva asociada
	 */
	public Graph getGrafoEjecutarMapa(int idActividad) {
		if(!grafoEjecutar.isEmpty())
		{
			if(grafoEjecutar.containsKey(idActividad))
				return grafoEjecutar.get(idActividad);
		}
		
		return new Graph<>();
	}

	/**
	 * @param grafoEjecutar Mapa de grafos de actividades a ejecutar por actividad recursiva
	 */
	public void setGrafoEjecutar(Map<Integer, Graph> grafoEjecutar) {
		this.grafoEjecutar = grafoEjecutar;
	}
	
	/**
	 * @param actividades grafo de actividades a ejecutar en una actividad recursiva
	 * @param idActividad id actividad recursiva involucrada
	 */
	public void setGrafoEjecutarMapa(Graph actividades, int idActividad) {
		if(!this.grafoEjecutar.containsKey(idActividad))
			this.grafoEjecutar.put(idActividad, actividades);
	}

	/**
	 * @return nombre de actividad de refuerzo asociada
	 */
	public String getActividadRecursiva() {
		return actividadRecursiva;
	}

	/**
	 * @param actividadRecursiva nombre de actividad de refuerzo asociada
	 */
	public void setActividadRecursiva(String actividadRecursiva) {
		this.actividadRecursiva = actividadRecursiva;
	}

	/**
	 * @return cambia o no el contetxo del agente
	 */
	public boolean isEstadoContexto() {
		return estadoContexto;
	}

	/**
	 * @param estadoContexto cambia o no el contetxo del agente
	 */
	public void setEstadoContexto(boolean estadoContexto) {
		this.estadoContexto = estadoContexto;
	}

	/**
	 * @return Mapa de estados de calculo de grafos de las actividades de refuerzo
	 */
	public Map<Integer, Boolean> getEstaCalculado() {
		return estaCalculado;
	}
	
}
