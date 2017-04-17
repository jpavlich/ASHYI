package co.edu.javeriana.ashyi.kiss.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.ashyi.Graph.Graph;

/**
 * @author ASHYI
 * Datos del agente planificador
 * @see Data
 */
public class PlanASHYIAgentData extends Data {
	
	/**
	 * Grafos asociados al agente planificador
	 * Uno por cada actividad recursiva asociada
	 */
	private Map<Integer, Graph> grafos;
	/**
	 * Mapa de listas de actividades atomicas asociadas a una actividad recursiva 
	 */
	private Map<Integer, List> actividades;
	/**
	 * Mapa de listas de actividades de refuerzo asociadas a una actividad recursiva 
	 */
	private Map<Integer, List> actividadesRefuerzo;
	/**
	 * tiene o no actividades de refuerzo
	 */
	private boolean hayActividadesR;
	/**
	 * tipo de actividad recursiva
	 */
	private String actividadRecursiva;
	
	/**
	 * Constructor
	 * @param grafo Mapa de grafos asociados al agente planificador, uno por cada actividad recursiva asociada
	 * @param ambientes asociados al agente
	 */
	public PlanASHYIAgentData(Map<Integer, Graph> grafo) {
		super();
		this.grafos = grafo;
		this.hayActividadesR = false;
		actividadRecursiva = "";
	}
	
	/**
	 * Constructor base
	 */
	public PlanASHYIAgentData() {
		super();
		this.grafos = new LinkedHashMap<Integer, Graph>();
		this.actividades = new LinkedHashMap<Integer, List>();
		this.actividadesRefuerzo = new LinkedHashMap<Integer, List>();
		this.hayActividadesR = false;
		actividadRecursiva = "";
	}

	/**
	 * @return Mapa de grafos asociados al agente planificador, uno por cada actividad recursiva asociada
	 */
	public Map<Integer, Graph> getGrafos() {
		return grafos;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return grafo de items de la actividad recursiva consultada
	 */
	public Graph getGrafosMapa(int idActividad) {
		if(!grafos.isEmpty())
		{
			if(grafos.containsKey(idActividad))
				return grafos.get(idActividad);
		}
		
		return new Graph<>();
	}

	/**
	 * @param grafos Mapa de grafos asociados al agente planificador, uno por cada actividad recursiva asociada
	 */ 
	public void setGrafos(Map<Integer, Graph> grafos) {
		this.grafos = grafos;
	}
	
	/**
	 * @param grafo de items de la actividad recursiva
	 * @param idActividad id actividad recursiva a cambiar
	 */
	public void setGrafos(Graph grafo, int idActividad) {
		if(!this.grafos.containsKey(idActividad))
			this.grafos.put(idActividad, grafo);
	}

	/**
	 * @return Mapa de listas de actividades atomicas asociadas a una actividad recursiva
	 */
	public Map<Integer, List> getActividades() {
		return actividades;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return lista de actividades atomicas asociadas a la actividad recursiva consultada
	 */
	public List getActividadesMapa(int idActividad) {
		if(!actividades.isEmpty())
		{
			if(actividades.containsKey(idActividad))
				return actividades.get(idActividad);
		}
		
		return new ArrayList<>();
	}

	/**
	 * @param actividades Mapa de listas de actividades atomicas asociadas a una actividad recursiva
	 */
	public void setActividades(Map<Integer, List> actividades) {
		this.actividades = actividades;
	}
	
	/**
	 * @param actividades lista de actividades asocaidas a una actividad recursiva
	 * @param idActividad id actividad recursiva a cambiar
	 */
	public void setActividadesMapa(List actividades, int idActividad) {
		if(!this.actividades.containsKey(idActividad))
			this.actividades.put(idActividad, actividades);
	}

	/**
	 * @return  Mapa de listas de actividades de refuerzo asociadas a una actividad recursiva
	 */
	public Map<Integer, List> getActividadesRefuerzo() {
		return actividadesRefuerzo;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return Lista de actividades de refuerzo asociadas a una actividad recursiva
	 */
	public List getActividadesRefuerzoMapa(int idActividad) {
		if(!actividadesRefuerzo.isEmpty())
		{
			if(actividadesRefuerzo.containsKey(idActividad))
				return actividadesRefuerzo.get(idActividad);
		}
		
		return new ArrayList<>();
	}

	/**
	 * @param actividadesRefuerzo Mapa de listas de actividades de refuerzo asociadas a una actividad recursiva
	 */
	public void setActividadesRefuerzo(Map<Integer, List> actividadesRefuerzo) {
		this.actividadesRefuerzo = actividadesRefuerzo;
	}
	
	/**
	 * @param actividades Lista de actividades de refuerzo asociadas a una actividad recursiva
	 * @param idActividad id actividad recursiva a consultar
	 */
	public void setActividadesRefuerzoMapa(List actividades, int idActividad) {
		if(!this.actividadesRefuerzo.containsKey(idActividad))
			this.actividadesRefuerzo.put(idActividad, actividades);
	}

	/**
	 * @return existen o no actividades de refuerzo
	 */
	public boolean isHayActividadesR() {
		return hayActividadesR;
	}

	/**
	 * @param hayActividadesR existen o no actividades de refuerzo
	 */
	public void setHayActividadesR(boolean hayActividadesR) {
		this.hayActividadesR = hayActividadesR;
	}

	/**
	 * @return tipo de actividad recursiva
	 */
	public String getActividadRecursiva() {
		return actividadRecursiva;
	}

	/**
	 * @param actividadRecursiva tipo de actividad recursiva
	 */
	public void setActividadRecursiva(String actividadRecursiva) {
		this.actividadRecursiva = actividadRecursiva;
	}

}