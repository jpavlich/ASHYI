package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.GrafosUsuario;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.Usuario;

/**
 * @author ASHYI
 * @see Usuario
 */
public class UsuarioImpl implements Usuario, Serializable{
	
	private Integer idUsuario;
	private String idUsuarioSakai;
	private String rol;
	private String nivel;
	
	private List<CaracteristicasUsuario> caracteristicas;
	//private List<ActividadesUsuario> actividades;
	private List<ItemsUsuario> items;
	private List<GrafosUsuario> grafos;
	
	/**
	 * Constructor base
	 */
	public UsuarioImpl() {}
		
	/**
	 * @param idUsuarioSakai
	 * @param rol: estudiante, profesor, turista, ...
	 */
	public UsuarioImpl(String idUsuarioSakai, String rol) {
		super();
		this.idUsuarioSakai = idUsuarioSakai;
		this.rol = rol;
		this.caracteristicas = new ArrayList<CaracteristicasUsuario>();
		//this.actividades = new ArrayList<ActividadesUsuario>();
		this.items = new ArrayList<ItemsUsuario>();
	}
	
	/**
	 * @param idUsuarioSakai
	 * @param rol: estudiante, profesor, turista, ...
	 * @param nivel: Universitario, Secundaria, etc
	 */
	public UsuarioImpl(String idUsuarioSakai, String rol, String nivel) {
		super();
		this.idUsuarioSakai = idUsuarioSakai;
		this.rol = rol;
		this.caracteristicas = new ArrayList<CaracteristicasUsuario>();
		//this.actividades = new ArrayList<ActividadesUsuario>();
		this.items = new ArrayList<ItemsUsuario>();
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getIdUsuario()
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setIdUsuario(java.lang.Integer)
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getIdUsuarioSakai()
	 */
	public String getIdUsuarioSakai() {
		return idUsuarioSakai;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setIdUsuarioSakai(java.lang.String)
	 */
	public void setIdUsuarioSakai(String idUsuarioSakai) {
		this.idUsuarioSakai = idUsuarioSakai;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getRol()
	 */
	public String getRol() {
		return rol;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setRol(java.lang.String)
	 */
	public void setRol(String rol) {
		this.rol = rol;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getCaracteristicas()
	 */
	public List<CaracteristicasUsuario> getCaracteristicas() {
		return caracteristicas;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setCaracteristicas(java.util.List)
	 */
	public void setCaracteristicas(List<CaracteristicasUsuario> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#addCaracteristicas(org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario)
	 */
	public void addCaracteristicas(CaracteristicasUsuario carac)
	{
		getCaracteristicas().add(carac);
	}
	
//	public List<ActividadesUsuario> getActividades() {
//		return actividades;
//	}
//
//	public void setActividades(List<ActividadesUsuario> actividades) {
//		this.actividades = actividades;
//	}
	
//	public void addActividades(ActividadesUsuario actividad)
//	{
//		getActividades().add(actividad);
//	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getNivel()
	 */
	public String getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setNivel(java.lang.String)
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getItems()
	 */
	public List<ItemsUsuario> getItems() {
		return items;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setItems(java.util.List)
	 */
	public void setItems(List<ItemsUsuario> items) {
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#addItems(org.sakaiproject.lessonbuildertool.model.ItemsUsuario)
	 */
	public void addItems(ItemsUsuario Items) {
		getItems().add(Items);		
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#getGrafos()
	 */
	public List<GrafosUsuario> getGrafos() {
		return grafos;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Usuario#setGrafos(java.util.List)
	 */
	public void setGrafos(List<GrafosUsuario> grafos) {
		this.grafos = grafos;
	}
	
	
}
