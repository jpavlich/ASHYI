package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;
import java.util.Date;

import co.edu.javeriana.ASHYI.model.Control;

/**
 * @author ASHYI
 * Tabla que controla el editor del sistema
 * detiene o permite el editor del dominio
 *
 */
public class ControlImpl implements Control, Serializable{
	
	private int idcontrol_ashyi;
	private int editar_dominio;
	private String editor;
	private Date fecha_edicion;
	
	/**
	 * Constructor base
	 */
	public ControlImpl() {}
	
	/**
	 * @param editor
	 * @param editar_dominio: si es posible o no 
	 */
	public ControlImpl(String editor, int editar_dominio) {
		super();
		this.editar_dominio = editar_dominio;
		this.editor = editor;
		this.fecha_edicion = new Date();
	}
	
	/**
	 * @param idcontrol_ashyi
	 * @param editar_dominio: si es posible o no 
	 * @param editor
	 * @param fecha_edicion
	 */
	public ControlImpl(int idcontrol_ashyi, int editar_dominio, String editor,
			Date fecha_edicion) {
		super();
		this.idcontrol_ashyi = idcontrol_ashyi;
		this.editar_dominio = editar_dominio;
		this.editor = editor;
		this.fecha_edicion = fecha_edicion;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#getIdcontrol_ashyi()
	 */
	public int getIdcontrol_ashyi() {
		return idcontrol_ashyi;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#setIdcontrol_ashyi(int)
	 */
	public void setIdcontrol_ashyi(int idcontrol_ashyi) {
		this.idcontrol_ashyi = idcontrol_ashyi;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#getEditar_dominio()
	 */
	public int getEditar_dominio() {
		return editar_dominio;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#setEditar_dominio(int)
	 */
	public void setEditar_dominio(int editar_dominio) {
		this.editar_dominio = editar_dominio;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#getEditor()
	 */
	public String getEditor() {
		return editor;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#setEditor(java.lang.String)
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#getFecha_edicion()
	 */
	public Date getFecha_edicion() {
		return fecha_edicion;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Control#setFecha_edicion(java.util.Date)
	 */
	public void setFecha_edicion(Date fecha_edicion) {
		this.fecha_edicion = fecha_edicion;
	}	
	
}