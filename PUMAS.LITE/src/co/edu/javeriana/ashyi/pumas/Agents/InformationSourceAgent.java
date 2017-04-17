package co.edu.javeriana.ashyi.pumas.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Agente fuente de informacion: agente que sabe ejecutar la consulta en una respectiva fuente de informacion
 */
public class InformationSourceAgent extends Agent{

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public InformationSourceAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}
 

}