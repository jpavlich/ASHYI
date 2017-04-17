
package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.Usuario;


/**
 * @author ASHYI
 * @see ItemsUsuario
 */

public class ItemsUsuarioImpl implements ItemsUsuario, Serializable{
	
	private ItemPlan idItemPlan;
	private Usuario idUsuario;
	private boolean realizada;
	private boolean estaActivo;
	private double nota;
	private double calificacion_usuario;
//	private float tiempo_duracion;
//	private Date fecha_inicial;
//	private Date fecha_final;
	
	private List<RespuestaItemsUsuario> respuestas = new ArrayList<RespuestaItemsUsuario>();
	
	/**
	 *  Constructor base
	 */
	public ItemsUsuarioImpl()
	{
		//tiempo_duracion = 0;
	}

	/**
	 * @param ItemPlan
	 * @param Usuario
	 * @param realizada o no
	 * @param estado: activo o no para el usuario en particular
	 * @param nota
	 */
	public ItemsUsuarioImpl(ItemPlan idItemPlan, Usuario idUsuario, boolean realizada, boolean estado,
			double nota) {
		super();
		this.idItemPlan = idItemPlan;
		this.idUsuario = idUsuario;
		this.realizada = realizada;
		this.estaActivo = estado;
		this.nota = nota;
		//this.tiempo_duracion = 0;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#getIdItemPlan()
	 */
	public ItemPlan getIdItemPlan() {
		return idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setIdItemPlan(org.sakaiproject.lessonbuildertool.model.ItemPlan)
	 */
	public void setIdItemPlan(ItemPlan idItemPlan) {
		this.idItemPlan = idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setIdUsuario(org.sakaiproject.lessonbuildertool.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#isRealizada()
	 */
	public boolean isRealizada() {
		return realizada;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setRealizada(boolean)
	 */
	public void setRealizada(boolean realizada) {
		this.realizada = realizada;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#getNota()
	 */
	public double getNota() {
		return nota;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setNota(double)
	 */
	public void setNota(double nota) {
		this.nota = nota;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#getCalificacion_usuario()
	 */
	public double getCalificacion_usuario() {
		return calificacion_usuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setCalificacion_usuario(double)
	 */
	public void setCalificacion_usuario(double calificacion_usuario) {
		this.calificacion_usuario = calificacion_usuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#isEstaActivo()
	 */
	public boolean isEstaActivo() {
		return estaActivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemsUsuario#setEstaActivo(boolean)
	 */
	public void setEstaActivo(boolean estado) {
		this.estaActivo = estado;
	}
	
//	public float getTiempo_duracion() {
//		return tiempo_duracion;
//	}
//
//	public void setTiempo_duracion(float tiempo_duracion) {
//		this.tiempo_duracion = tiempo_duracion;
//	}
//
//	public Date getFecha_inicial() {
//		return fecha_inicial;
//	}
//
//	public void setFecha_inicial(Date fecha_inicial) {
//		this.fecha_inicial = fecha_inicial;
//	}
//
//	public Date getFecha_final() {
//		return fecha_final;
//	}
//
//	public void setFecha_final(Date fecha_final) {
//		this.fecha_final = fecha_final;
//	}

	/**
	 * @return
	 */
	public List<RespuestaItemsUsuario> getRespuestas() {
		return respuestas;
	}

	/**
	 * @param respuestas
	 */
	public void setRespuestas(List<RespuestaItemsUsuario> respuestas) {
		this.respuestas = respuestas;
	}	
}