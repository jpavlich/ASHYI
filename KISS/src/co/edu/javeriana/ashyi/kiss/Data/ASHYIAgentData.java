package co.edu.javeriana.ashyi.kiss.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ASHYI
 * Datos para agente ASHYI - Ejecutor
 * @see Data
 */
public class ASHYIAgentData extends Data {
	
	/**
	 * Lista de actividades del agente
	 */
	private List actividades;
		
	/**
	 * Constructor base
	 */
	public ASHYIAgentData() {
		actividades = new ArrayList<>();
	}

	/**
	 * @param actividades actividades del agente
	 */
	public ASHYIAgentData(List actividades) {
		super();
		this.actividades = actividades;
	}

	/**
	 * @return actividades actividades del agente
	 */
	public List getActividades() {
		return actividades;
	}

	/**
	 * @param actividades actividades del agente
	 */
	public void setActividades(List actividades) {
		this.actividades = actividades;
	}
	
	
}
