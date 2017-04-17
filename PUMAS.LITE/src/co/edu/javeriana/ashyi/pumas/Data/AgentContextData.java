package co.edu.javeriana.ashyi.pumas.Data;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.model.Usuario;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Datos del agente contexto
 */
public class AgentContextData extends Data {
			
	/**
	 * Usuario que representa el agente 
	 */
	private Usuario usuario; 
	//private List<ActividadesUsuario> actividadesUsuario;
	/**
	 * Lista de actividades que conforman el contexto de un usuario 
	 */
	private List contexto;
	
	/**
	 * Constructor base
	 */
	public AgentContextData()
	{
		this.contexto = new ArrayList<String>();
	}

	/**
	 * Constructor
	 * @param usuario Usuario que representa el agente
	 * @param contexto Lista de actividades que conforman el contexto de un usuario
	 */
	public AgentContextData(Usuario usuario, List<String> contexto) {
		super();
		this.usuario = usuario;
		this.contexto = contexto;
	}

	/**
	 * @return Usuario que representa el agente 
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario Usuario que representa el agente 
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return Lista de actividades que conforman el contexto de un usuario
	 */
	public List getContexto() {
		return contexto;
	}

	/**
	 * @param contexto Lista de actividades que conforman el contexto de un usuario
	 */
	public void setContexto(List contexto) {
		this.contexto = contexto;
	}
	
}
