package co.edu.javeriana.ashyi.pumas.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;


/**
 * Agente Contexto: agente encargado de estar al tanto de las caracteristicas del entorno de ejecucion del usuario y sus respectivos cambios
 * @author ASHYI
 *
 */
public class ContextAgent extends Agent{

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public ContextAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}
 

}