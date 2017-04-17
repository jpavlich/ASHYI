package co.edu.javeriana.ashyi.pumas.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Agente Enrutador: agente encargado de recibir las consultas del agente Intermediario 
 * y de buscar la fuente de informacion que pueda satisfacer parcial o totalmente la consulta.
 */
public class RouterAgent extends Agent {

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public RouterAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}
 
}