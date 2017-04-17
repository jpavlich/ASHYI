package co.edu.javeriana.ashyi.kiss.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import co.edu.javeriana.ashyi.Graph.Graph;

/**
 * @author ASHYI
 * Datos para agente principal (Main)
 */
public class MainAgentData extends Data {
	
	/**
	 * Lista de agentes asociados al agente principal de la actividad 
	 */
	private List<String> agentes;
	/**
	 * Lista de items por actividad recursiva
	 */
	private Map<Integer, List> idItems;
	/**
	 * estado del calculo el grafo de cada actividad recursiva 
	 */
	private Map<Integer, Boolean> estadoActividades;
	/**
	 * grafo asociado a cada actividad recursiva
	 */
	private Map<Integer, Graph<Integer>> grafo;

	/**
	 * Constructor base
	 */
	public MainAgentData() {
		super();
		this.agentes = Collections.synchronizedList(new ArrayList<String>());
		this.idItems = Collections.synchronizedMap(new LinkedHashMap<Integer, List>());
		this.grafo = Collections.synchronizedMap(new LinkedHashMap<Integer, Graph<Integer>>());
		this.estadoActividades = Collections.synchronizedMap(new LinkedHashMap<Integer, Boolean>());
	}	
	
	/**
	 * Constructor
	 * @param agentes lista de alias de agentes
	 */
	public MainAgentData(List<String> agentes) {
		super();
		this.agentes = agentes;
		this.grafo = new LinkedHashMap<Integer, Graph<Integer>>();
		this.idItems = new LinkedHashMap<Integer, List>();
		this.estadoActividades = new LinkedHashMap<Integer, Boolean>();
	}	


	/**
	 * @return lista de alias de agentes asociados al agente principal
	 */
	public List<String> getAgentes() {
		return agentes;
	}

	/**
	 * @param agentes lista de alias de agentes asociados al agente principal
	 */
	public void setAgentes(List<String> agentes) {
		this.agentes = agentes;
	}

	/**
	 * @return mapa de items que conforman los grafos de cada actividad recursiva
	 */
	public Map<Integer, Graph<Integer>> getGrafo() {
		return grafo;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return grafo de la actividad recursiva consultada
	 */
	public Graph getGrafoMapa(int idActividad) {
		if(!grafo.isEmpty())
		{
			if(grafo.containsKey(idActividad))
				return grafo.get(idActividad);
		}
		
		return new Graph<>();
	}

	/**
	 * @param grafo mapa de items que conforman los grafos de cada actividad recursiva
	 */
	public void setGrafo(Map<Integer, Graph<Integer>> grafo) {
		this.grafo = grafo;
	}
	
	/**
	 * @param grafo grafo de actividades de la actividad recursiva
	 * @param idActividad id de la actividad revursiva a moddificar
	 */
	public void setGrafoMapa(Graph grafo, int idActividad) {
		if(!this.grafo.containsKey(idActividad))
			this.grafo.put(idActividad, grafo);
	}

	/**
	 * @return mapa de items que conforman los grafos de cada actividad recursiva
	 */
	public Map<Integer, List> getIdItems() {
		return idItems;
	}
	
	/**
	 * @param idActividad id actividad recursiva a consultar
	 * @return lista de id items que conforman el grafo de la actividad recursiva consultada 
	 */
	public List getIdItemsMapa(int idActividad) {
		if(!idItems.isEmpty())
		{
			if(idItems.containsKey(idActividad))
				return idItems.get(idActividad);
		}
		
		return new ArrayList<>();
	}

	/**
	 * @param idItems mapa de items que conforman los grafos de cada actividad recursiva
	 */
	public void setIdItems(Map<Integer, List> idItems) {
		this.idItems = idItems;
	}
	
	/**
	 * Cambiar items de una actividad recursiva
	 * @param items id items del grafo de una actividad recursiva
	 * @param idActividad id actividad de una actividad recursiva a modificar
	 */
	public void setIdItemsMapa(List items, int idActividad) {
		if(!this.idItems.containsKey(idActividad))
			this.idItems.put(idActividad, items);
	}

	/**
	 * @return mapa de el estado del calculo del grafo de una actividad recursiva
	 */
	public Map<Integer, Boolean> getEstadoActividades() {
		return estadoActividades;
	}

	/**
	 * @param estadoActividades mapa de el estado del calculo del grafo de una actividad recursiva
	 */
	public void setEstadoActividades(Map<Integer, Boolean> estadoActividades) {
		this.estadoActividades = estadoActividades;
	}
	
	/**
	 * @param cambio estado de calculo
	 * @param idActividad id actividad recursiva a cambiar
	 */
	public void setEstadoActividadesMapa(Boolean cambio, int idActividad) {
		
			this.estadoActividades.put(idActividad, cambio);
	}
	
	/**
	 * @param idActividad id actividad recursiva involucrada
	 * @return estado del calculo del grafo de la actividad recursiva involucrada
	 */
	public Boolean getEstadoActividadesMapa(int idActividad) {
		if(!estadoActividades.isEmpty())
		{
			if(estadoActividades.containsKey(idActividad))
				return estadoActividades.get(idActividad);
		}
		
		return false;
	}
}