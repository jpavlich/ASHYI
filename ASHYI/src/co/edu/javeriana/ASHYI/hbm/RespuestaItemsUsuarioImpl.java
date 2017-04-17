
package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;
import java.util.Date;

import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.RespuestaItemsUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;

/**
 * @author ASHYI
 * @see RespuestaItemsUsuario
 */

public class RespuestaItemsUsuarioImpl implements RespuestaItemsUsuario, Serializable{
	
	private ItemPlan idItemPlan;
	private Usuario idUsuario;
	private Integer idRespuesta;
	private String retroalimentacion_usuario;
	private String retroalimentacion_profesor;
	private String archivo;
	private Date fecha;
	
	/**
	 * Constructor base
	 */
	public RespuestaItemsUsuarioImpl()
	{
		retroalimentacion_usuario = "";
		retroalimentacion_profesor = "";
		fecha = new Date();
	}

	/**
	 * @param ItemPlan asociado a la respuesta
	 * @param Usuario quien realiza la respuesta 
	 * @param idRespuesta
	 */
	public RespuestaItemsUsuarioImpl(ItemPlan idItemPlan, Usuario idUsuario,
			Integer idRespuesta) {
		super();
		this.idItemPlan = idItemPlan;
		this.idUsuario = idUsuario;
		this.idRespuesta = idRespuesta;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getIdItemPlan()
	 */
	public ItemPlan getIdItemPlan() {
		return idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setIdItemPlan(org.sakaiproject.lessonbuildertool.model.ItemPlan)
	 */
	public void setIdItemPlan(ItemPlan idItemPlan) {
		this.idItemPlan = idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setIdUsuario(org.sakaiproject.lessonbuildertool.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getRetroalimentacion_usuario()
	 */
	public String getRetroalimentacion_usuario() {
		return retroalimentacion_usuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setRetroalimentacion_usuario(java.lang.String)
	 */
	public void setRetroalimentacion_usuario(String retroalimentacion_usuario) {
		this.retroalimentacion_usuario = retroalimentacion_usuario;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getRetroalimentacion_profesor()
	 */
	public String getRetroalimentacion_profesor() {
		return retroalimentacion_profesor;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setRetroalimentacion_profesor(java.lang.String)
	 */
	public void setRetroalimentacion_profesor(String retroalimentacion_profesor) {
		this.retroalimentacion_profesor = retroalimentacion_profesor;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getIdRespuesta()
	 */
	public Integer getIdRespuesta() {
		return idRespuesta;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setIdRespuesta(java.lang.Integer)
	 */
	public void setIdRespuesta(Integer idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getArchivo()
	 */
	public String getArchivo() {
		return archivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setArchivo(java.lang.String)
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#getFecha()
	 */
	public Date getFecha() {
		return fecha;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario#setFecha(java.util.Date)
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}