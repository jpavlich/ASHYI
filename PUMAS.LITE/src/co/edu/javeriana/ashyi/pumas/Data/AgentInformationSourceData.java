package co.edu.javeriana.ashyi.pumas.Data;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Datos del agente fuente de informacion
 */
public class AgentInformationSourceData extends Data {	
	
	/**
	 * Lista de caracteristicas a consultar
	 */
	private List consulta;
	/**
	 * Tipo de busqueda en la que se especializa el agente (1-->sql)
	 */
	private int tipoBusqueda;
	
	/**
	 * Constructor
	 * @param consulta Lista de caracteristicas a consultar
	 * @param tipoBusqueda Tipo de busqueda en la que se especializa el agente (1-->sql)
	 */
	public AgentInformationSourceData(List consulta, int tipoBusqueda) {
		super();
		this.consulta = consulta;
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * Constructor base
	 */
	public AgentInformationSourceData() {
		super();
		this.consulta = new ArrayList<>();
		this.tipoBusqueda = 0;
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
	 * @return Tipo de busqueda en la que se especializa el agente (1-->sql)
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda Tipo de busqueda en la que se especializa el agente (1-->sql)
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
		
}
