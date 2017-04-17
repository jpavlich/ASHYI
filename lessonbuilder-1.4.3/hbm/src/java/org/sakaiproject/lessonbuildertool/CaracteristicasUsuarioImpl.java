package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Usuario;

/**
 * @author ASHYI
 * @see CaracteristicasUsuario
 */

public class CaracteristicasUsuarioImpl implements CaracteristicasUsuario, Serializable{
	
	//private int idCaracteristicaUsuario;
	private Usuario idUsuario;
	private Caracteristica idCaracteristica;
	private int nivel;
	
	/**
	 * Constructor base
	 */
	public CaracteristicasUsuarioImpl()
	{}
	
	/**
	 * @param Usuario
	 * @param caracteristica
	 * @param nivel (nivel de la caracteristica para el usuario)
	 */
	public CaracteristicasUsuarioImpl(Usuario Usuario,
			Caracteristica caracteristica, int nivel) {
		super();
		this.idUsuario = Usuario;
		this.idCaracteristica = caracteristica;
		this.nivel = nivel;
	}	

//	public int getIdCaracteristicaUsuario() {
//		return idCaracteristicaUsuario;
//	}
//
//	public void setIdCaracteristicaUsuario(int idCaracteristicaUsuario) {
//		this.idCaracteristicaUsuario = idCaracteristicaUsuario;
//	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#setIdUsuario(org.sakaiproject.lessonbuildertool.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#getNivel()
	 */
	public int getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#setNivel(int)
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario#setIdCaracteristica(org.sakaiproject.lessonbuildertool.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

}