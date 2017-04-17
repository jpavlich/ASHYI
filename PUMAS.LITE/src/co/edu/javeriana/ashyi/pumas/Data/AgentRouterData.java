package co.edu.javeriana.ashyi.pumas.Data;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Datos del agente enrutador
 */
public class AgentRouterData extends Data {	
	
	/**
	 * Lista de actividades de respuesta  en un momento dado
	 */
	private List<Actividad> actividades;
	/**
	 * Tipo de busqueda a manejar por el grupo de agentes
	 */
	private int tipoBusqueda;
	/**
	 * Lista de alias de agentes fuente de informacion asociados
	 */
	private List agenteFI;
	/**
	 * Bandera para verificar si el agente esta disponible o no
	 */
	private boolean ocupado;
	/**
	 * Numero de consultas realizadas en un momento dado
	 */
	private int consultasRecibidas;
	/**
	 * Lista de actividades que se seleccionan en una consulta
	 */
	private List actividadeSeleccionar;
	/**
	 * Lista de actividades de refuerzo que se seleccionan en una consulta
	 */
	private List actividadesRefuerzoSeleccionar;
		
	/**
	 * Constructor
	 * @param actividades Lista de actividades de respuesta  en un momento dado
	 */
	public AgentRouterData(List<Actividad> actividades) {
		super();
		this.actividades = actividades;
		this.tipoBusqueda = 0;
		this.agenteFI = new ArrayList<>();
		this.actividadeSeleccionar = new ArrayList<>();
		this.actividadesRefuerzoSeleccionar = new ArrayList<>();
		this.ocupado = false;
		this.consultasRecibidas = 0;
	}
	
	/**
	 * Constructor base
	 */
	public AgentRouterData() {
		this.actividades = new ArrayList<>();
		this.tipoBusqueda = 0;
		this.agenteFI = new ArrayList<>();
		this.ocupado = false;
		this.consultasRecibidas = 0;
		this.actividadeSeleccionar = new ArrayList<>();
		this.actividadesRefuerzoSeleccionar = new ArrayList<>();
	}

	/**
	 * @return Lista de actividades de respuesta  en un momento dado
	 */
	public List<Actividad> getActividades() {
		return actividades;
	}

	/**
	 * @param actividades Lista de actividades de respuesta  en un momento dado
	 */
	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	/**
	 * @return Tipo de busqueda a manejar por el grupo de agentes
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda Tipo de busqueda a manejar por el grupo de agentes
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * @return Lista de alias de agentes fuente de informacion asociados
	 */
	public List getAgenteFI() {
		return agenteFI;
	}

	/**
	 * @param agenteFI Lista de alias de agentes fuente de informacion asociados
	 */
	public void setAgenteFI(List agenteFI) {
		this.agenteFI = agenteFI;
	}

	/**
	 * @return Bandera para verificar si el agente esta disponible o no
	 */
	public boolean isOcupado() {
		return ocupado;
	}

	/**
	 * @param estado Bandera para verificar si el agente esta disponible o no
	 */
	public void setOcupado(boolean estado) {
		this.ocupado = estado;
	}

	/**
	 * @return Numero de consultas realizadas en un momento dado
	 */
	public int getConsultasRecibidas() {
		return consultasRecibidas;
	}

	/**
	 * @param consultasRecibidas Numero de consultas realizadas en un momento dado
	 */
	public void setConsultasRecibidas(int consultasRecibidas) {
		this.consultasRecibidas = consultasRecibidas;
	}

	/**
	 * @return Lista de actividades que se seleccionan en una consulta
	 */
	public List getActividadeSeleccionar() {
		return actividadeSeleccionar;
	}

	/**
	 * @param actividadeSeleccionar Lista de actividades que se seleccionan en una consulta
	 */
	public void setActividadeSeleccionar(List actividadeSeleccionar) {
		this.actividadeSeleccionar = actividadeSeleccionar;
	}

	/**
	 * @return Lista de actividades de refuerzo que se seleccionan en una consulta
	 */
	public List getActividadesRefuerzoSeleccionar() {
		return actividadesRefuerzoSeleccionar;
	}

	/**
	 * @param actividadesRefuerzoSeleccionar Lista de actividades de refuerzo que se seleccionan en una consulta
	 */
	public void setActividadesRefuerzoSeleccionar(
			List actividadesRefuerzoSeleccionar) {
		this.actividadesRefuerzoSeleccionar = actividadesRefuerzoSeleccionar;
	}
	
}
