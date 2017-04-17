package co.edu.javeriana.ashyi.kiss.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Admin;
import co.edu.javeriana.ashyi.kiss.Actions.Action;
import co.edu.javeriana.ashyi.kiss.Data.Data;

public abstract class Agent {

	private String ID;
	private Data datos;
	private List<Class> acciones;
		
	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public Agent(String id, Data datos, List<Class> acciones) {
		super();
		this.ID = id;
		this.datos = datos;
		this.acciones = acciones;
		getAdmLocal().registerAgent(this);
	}

	public Admin getAdmLocal() {
		return Admin.INSTANCE;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public Data getDatos() {
		return datos;
	}

	public void setDatos(Data datos) {
		this.datos = datos;
	}

	public List<Class> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<Class> acciones) {
		this.acciones = acciones;
	}

	public void start() {
		// do nothing		
	}
	
	public void executeAction(Action action)
	{
		if(getAcciones().contains(action.getClass()))
		{
			action.setAgent(this);				
			action.execute();
		}
	}
	
}
