package co.edu.javeriana.ashyi.pumas.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Agente intermediario: agente encargado de recibir y recopilar consultas provenientes de los agentes Usuario
 */
public class IntermediaryAgent extends Agent{

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public IntermediaryAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}



}