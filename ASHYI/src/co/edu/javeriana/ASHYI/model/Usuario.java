package co.edu.javeriana.ASHYI.model;

import java.util.List;

/**
 * Interface de usuario del sistema
 * Representa a un usuario ejecutor, planificador o administrador en ASHYI
 * @author ASHYI
 * @see UsuarioImpl
 */

public interface Usuario {
	
	/**
	 * @return id del usuario
	 */
	public Integer getIdUsuario();

	/**
	 * @param idUsuario id del usuario
	 */
	public void setIdUsuario(Integer idUsuario);

	/**
	 * @return id del usuario en las tablas de sakai
	 */
	public String getIdUsuarioSakai();

	/**
	 * @param idUsuarioSakai id del usuario en las tablas de sakai
	 */
	public void setIdUsuarioSakai(String idUsuarioSakai);

	/**
	 * @return rol: estudiante, profesor, turista, ...
	 */
	public String getRol();

	/**
	 * @param rol estudiante, profesor, turista, ...
	 */
	public void setRol(String rol);	
	
	/**
	 * @return caracteristicas que conforman el perfil de usuario
	 */
	public List<CaracteristicasUsuario> getCaracteristicas();

	/**
	 * @param caracteristicas caracteristicas que conforman el perfil de usuario
	 */
	public void setCaracteristicas(List<CaracteristicasUsuario> caracteristicas);
	
	/**
	 * @param caracteristica caracteristica a aniadir
	 */
	public void addCaracteristicas(CaracteristicasUsuario caracteristicas);
	
//	public List<ActividadesUsuario> getActividades();

//	public void setActividades(List<ActividadesUsuario> actividades);
//	
//	public void addActividades(ActividadesUsuario actividad);
	
	/**
	 * @return nivel: Universitario, Secundaria, etc
	 */
	public String getNivel();

	/**
	 * @param nivel Universitario, Secundaria, etc
	 */
	public void setNivel(String nivel);
		
	/**
	 * @return items a ejecutar
	 */
	public List<ItemsUsuario> getItems();
	
	/**
	 * @param items items a ejecutar
	 */
	public void setItems(List<ItemsUsuario> items);
	
	/**
	 * @param Items item de usuario a aniadir
	 */
	public void addItems(ItemsUsuario Items);
	
	/**
	 * @return grafos de actividades recursivas del usuario
	 */
	public List<GrafosUsuario> getGrafos();

	/**
	 * @param grafos grafos de actividades recursivas del usuario
	 */
	public void setGrafos(List<GrafosUsuario> grafos);
}
