package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Control
 * Representa el control sobre las caracteristcias de dominio a administrar
 * @author ASHYI
 * @see ControlImpl
 */

import java.util.Date;

public interface Control {
	
		
	/**
	 * @return idcontrol_ashyi
	 */
	public int getIdcontrol_ashyi();
	/**
	 * @param idcontrol_ashyi
	 */
	public void setIdcontrol_ashyi(int idcontrol_ashyi);
	/**
	 * @return editar_dominio: si es posible o no
	 */
	public int getEditar_dominio();
	/**
	 * @param editar_dominio si es posible editar o no
	 */
	public void setEditar_dominio(int editar_dominio);
	/**
	 * @return quien es el editor 
	 */
	public String getEditor();
	/**
	 * @param editor quien es el editor 
	 */
	public void setEditor(String editor);
	/**
	 * @return fecha de edicion
	 */
	public Date getFecha_edicion();
	/**
	 * @param fecha_edicion fecha de edicion
	 */
	public void setFecha_edicion(Date fecha_edicion);	
	
}