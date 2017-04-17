package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;
import java.util.Date;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.Usuario;

/**
 * @author ASHYI
 * @see CaracteristicasItemsUsuario
 *
 */
public class CaracteristicasItemsUsuarioImpl implements CaracteristicasItemsUsuario, Serializable{
	
	private Caracteristica idCaracteristica;
	private ItemPlan idItemPlan;
	private Usuario idUsuario;
	private boolean estado;
	private Date fecha;
	
	/**
	 *  Constructor base
	 */
	public CaracteristicasItemsUsuarioImpl()
	{}

	/**
	 * @param Caracteristica
	 * @param ItemPlan
	 * @param Usuario
	 * @param estado (realizado / no realizado)
	 */
	public CaracteristicasItemsUsuarioImpl(Caracteristica idCaracteristica,
			ItemPlan idItemPlan, Usuario idUsuario, boolean estado,Date fecha) {
		super();
		this.idCaracteristica = idCaracteristica;
		this.idItemPlan = idItemPlan;
		this.idUsuario = idUsuario;
		this.estado = estado;
		this.fecha=fecha;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#getIdItemPlan()
	 */
	public ItemPlan getIdItemPlan() {
		return idItemPlan;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#setIdItemPlan(co.edu.javeriana.ASHYI.model.ItemPlan)
	 */
	public void setIdItemPlan(ItemPlan idItemPlan) {
		this.idItemPlan = idItemPlan;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#setIdUsuario(co.edu.javeriana.ASHYI.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#isEstado()
	 */
	public boolean isEstado() {
		return estado;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario#setEstado(boolean)
	 */
	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	

	
}