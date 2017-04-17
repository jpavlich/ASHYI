package co.edu.javeriana.ashyi.pumas.Data;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Datos del agente intermediario
 */
public class AgentIntermediaryData extends Data {	
	
	/**
	 * Tipo de consulta a realizar 
	 * 1: objetivo 
	 * 2:recursos 
	 * 3: objetivo y recurso 
	 * 4: objetivo, recurso y contexto 
	 * 5: refuerzo
	 */
	private int tipoConsulta;//1: objetivo 2:recursos 3: objetivo y recurso 4: objetivo, recurso y contexto 5: refuerzo
	/**
	 * Lista de caracteristicas a consultar
	 */
	private List consulta;
	/**
	 * nivel en que se encuentra una actividad manejada temporalmente
	 */
	private String nivelActividad;
	/**
	 * tipo de busqueda a realizar
	 * 1: sql 
	 * 2: palabras clave 
	 * 3: ontologia
	 * ...
	 */
	private int tipoBusqueda;//1: sql 2: palabras clave 3: ontologia...
	/**
	 * Lista de agentes enrutadores a cargo del agente intermediario
	 */
	private List agentesEnrutadores;
		
	/**
	 * @param tipoConsulta Tipo de consulta a realizar 1: objetivo 2:recursos 3: objetivo y recurso 4: objetivo, recurso y contexto 5: refuerzo
	 * @param consulta Lista de caracteristicas a consultar
	 * @param nivelActividad nivel en que se encuentra una actividad manejada temporalmente
	 */
	public AgentIntermediaryData(int tipoConsulta, List consulta, String nivelActividad) {
		super();
		this.tipoConsulta = tipoConsulta;
		this.consulta = consulta;
		this.nivelActividad = nivelActividad;
		this.tipoBusqueda = 0;
		this.agentesEnrutadores = new ArrayList<>();
		//this.nivelActividad = "Universitario";
	}
	
	/**
	 * Constructor base
	 */
	public AgentIntermediaryData() {
		this.tipoBusqueda = 0;
		this.agentesEnrutadores = new ArrayList<>();
		this.tipoConsulta = 0;
		this.consulta = new ArrayList<>();
		this.nivelActividad = "";
	}
	/**
	 * @return Tipo de consulta a realizar 1: objetivo 2:recursos 3: objetivo y recurso 4: objetivo, recurso y contexto 5: refuerzo
	 */
	public int getTipoConsulta() {
		return tipoConsulta;
	}
	/**
	 * @param tipoConsulta Tipo de consulta a realizar 1: objetivo 2:recursos 3: objetivo y recurso 4: objetivo, recurso y contexto 5: refuerzo
	 */
	public void setTipoConsulta(int tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	/**
	 * @return Lista de caracteristicas a consultar
	 */
	public List getConsulta() {
		return consulta;
	}
	/**
	 * @param consulta Lista de caracteristicas a consultar
	 */
	public void setConsulta(List consulta) {
		this.consulta = consulta;
	}

	/**
	 * @return nivel en que se encuentra una actividad manejada temporalmente
	 */
	public String getNivelActividad() {
		return nivelActividad;
	}

	/**
	 * @param nivelActividad nivel en que se encuentra una actividad manejada temporalmente
	 */
	public void setNivelActividad(String nivelActividad) {
		this.nivelActividad = nivelActividad;
	}

	/**
	 * @return tipo de busqueda a realizar 1: sql 2: palabras clave 3: ontologia...
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda tipo de busqueda a realizar 1: sql 2: palabras clave 3: ontologia...
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * @return Lista de agentes enrutadores a cargo del agente intermediario
	 */
	public List getAgentesEnrutadores() {
		return agentesEnrutadores;
	}

	/**
	 * @param agentesEnrutadores Lista de agentes enrutadores a cargo del agente intermediario
	 */
	public void setAgentesEnrutadores(List agentesEnrutadores) {
		this.agentesEnrutadores = agentesEnrutadores;
	}
	
	
}
