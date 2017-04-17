package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;

/**
 * @author ASHYI
 * @see CaracteristicasUsuario
 */
public class CaracteristicasUsuarioImpl implements CaracteristicasUsuario, Serializable{
	
	private int idCaracteristicaUsuario;
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

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#getIdCaracteristicaUsuario()
	 */
	public int getIdCaracteristicaUsuario() {
		return idCaracteristicaUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#setIdCaracteristicaUsuario(int)
	 */
	public void setIdCaracteristicaUsuario(int idCaracteristicaUsuario) {
		this.idCaracteristicaUsuario = idCaracteristicaUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#setIdUsuario(co.edu.javeriana.ASHYI.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#getNivel()
	 */
	public int getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#setNivel(int)
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasUsuario#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

}