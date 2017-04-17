package co.edu.javeriana.ashyi.pumas.Data;

import java.util.List;

import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.ItemsUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Datos de agente representante
 */
public class AgentRepresentativeData extends Data {
	
	/**
	 * Lista de caracteristicas que conforman el perfil de usuario que repreesnta el agente
	 */
	private List<CaracteristicasUsuario> caracteristicasUsuario;
	/**
	 * Usuario a quien representa el agente
	 */
	private Usuario usuario; 
	/**
	 * Lista de items a ejecutar por el usuario en un momento determinado
	 */
	private List<ItemsUsuario> actividadesUsuario;
	
	/**
	 * Constructor base
	 */
	public AgentRepresentativeData()
	{
	}
	
	/**
	 * Constructor
	 * @param caracteristicasUsuario Lista de caracteristicas que conforman el perfil de usuario que repreesnta el agente
	 * @param usuario Usuario a quien representa el agente
	 * @param actividadesUsuario
	 */
	public AgentRepresentativeData(
			List<CaracteristicasUsuario> caracteristicasUsuario,
			Usuario usuario, List<ItemsUsuario> actividadesUsuario) {
		super();
		this.caracteristicasUsuario = caracteristicasUsuario;
		this.usuario = usuario;
		this.actividadesUsuario = actividadesUsuario;
	}

	/**
	 * @return Lista de caracteristicas que conforman el perfil de usuario que repreesnta el agente
	 */
	public List<CaracteristicasUsuario> getCaracteristicasUsuario() {
		return caracteristicasUsuario;
	}

	/**
	 * @param caracteristicasUsuario Lista de caracteristicas que conforman el perfil de usuario que repreesnta el agente
	 */
	public void setCaracteristicasUsuario(
			List<CaracteristicasUsuario> caracteristicasUsuario) {
		this.caracteristicasUsuario = caracteristicasUsuario;
	}

	/**
	 * @return Usuario a quien representa el agente
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario Usuario a quien representa el agente
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return Lista de items a ejecutar por el usuario en un momento determinado
	 */
	public List<ItemsUsuario> getActividadesUsuario() {
		return actividadesUsuario;
	}

	/**
	 * @param actividadesUsuario Lista de items a ejecutar por el usuario en un momento determinado
	 */
	public void setActividadesUsuario(List<ItemsUsuario> actividadesUsuario) {
		this.actividadesUsuario = actividadesUsuario;
	}
	
	
	
}
