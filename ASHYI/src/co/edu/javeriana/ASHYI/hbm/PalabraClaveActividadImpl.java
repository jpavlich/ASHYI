package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.PalabraClave;
import co.edu.javeriana.ASHYI.model.PalabraClaveActividad;

/**
 * @author ASHYI
 * @see PalabraClaveActividad
 */

public class PalabraClaveActividadImpl implements PalabraClaveActividad, Serializable{
	
	private PalabraClave idPalabraClave;
	private Actividad idActividad;
	
	/**
	 * Constructor base
	 */
	public PalabraClaveActividadImpl() {
	}

	/**
	 * @param PalabraClave a relacionar
	 * @param Actividad asociada
	 */
	public PalabraClaveActividadImpl(PalabraClave idPalabraClave,
			Actividad idActividad) {
		super();
		this.idPalabraClave = idPalabraClave;
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad#getIdPalabraClave()
	 */
	public PalabraClave getIdPalabraClave() {
		return idPalabraClave;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad#setIdPalabraClave(org.sakaiproject.lessonbuildertool.model.PalabraClave)
	 */
	public void setIdPalabraClave(PalabraClave idPalabraClave) {
		this.idPalabraClave = idPalabraClave;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}		
}