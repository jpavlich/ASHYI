package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.ItemsUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentContextData;

public class ContextCheckContext<T> extends MessagePassingAction<T>{
	
	public ContextCheckContext(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		System.out.println("In Guard ExecutionContextGuard");

		List datosL = (List) datos.getMessage();
		// consultar si hay cambio de contexto
		
		AgentContextData datosAg = (AgentContextData) this.getAgent().getDatos();
		
		List<String> contextoAg = datosAg.getContexto();

		List contextoLlega = (List) datosL.get(0);
		Graph grafoUnidad = (Graph) datosL.get(1);
		List itemsUsuario = (List) datosL.get(2);
		List itemsMain = new ArrayList<>();

		// los ids de los nodos del garfo
		itemsMain.addAll(grafoUnidad.getVertices());

		int idActividad = (int) contextoLlega.get(2);

		// eliminar idActividad
		contextoLlega.remove(2);

		String aliasAg = this.getAgent().getID();

		System.out.println("Verificar si hay cambios de contexto");

		// si hay cambios de contexto
		if (!contextoAg.containsAll(contextoLlega)) {
			List<Integer> ids = new ArrayList<Integer>();
			Consulta c = new Consulta();
			for (int i = 0; i < itemsUsuario.size(); i++) {
				Usuario us = c.getUsuario(getIdUsuarioFormAlias(this.getAgent().getID()
						.replace("ContextAgent-", "")));
				int aU = 0;
				if(itemsUsuario.get(i) instanceof String)
					aU = Integer.valueOf((String) itemsUsuario.get(i));
				if(itemsUsuario.get(i) instanceof Integer)
					aU = (int) itemsUsuario.get(i);
				//System.out.println("Busccando items de usuario");
				ItemsUsuario iU = c.getItemUsuario(aU, us.getIdUsuario());
				System.out.println("Item: " + iU.getIdItemPlan().getIdItemPlan());
				ItemPlan ip = c.getItemPlan(iU.getIdItemPlan().getIdItemPlan());
				Actividad ac = c.getActividad(ip.getIdActividad().getIdActividad());
				if (!iU.isRealizada() && !ac.getNivel().equals("InicioUD")) {
					System.out.println("Item no realizado");
					// si la actividad no tiene el contexto solicitado
					if (!c.isContextoActividad(aU, contextoLlega)) {
						ids.add(aU);
					}
				}
			}

			List datosEnviar = new ArrayList<>();

			// ids a cambiar
			if (!ids.isEmpty()) {
				Map<Integer, List<Integer>> itemsCambio = new LinkedHashMap<Integer, List<Integer>>();
				// encontrar item alterno en items del curso
				for (int i = 0; i < ids.size(); i++) {
					itemsCambio.put(ids.get(i), c.objetivosCxtActividades(ids.get(i), itemsMain, contextoLlega));
				}

				if (itemsCambio.isEmpty()) {
					// no items disponibles
					datosAg.setContexto(contextoLlega);
				} else {
					// enviar cambios y actualizar estado
					datosAg.setContexto(contextoLlega);
					datosEnviar.add(itemsCambio);
					datosEnviar.add(idActividad);
					datosEnviar.add(grafoUnidad);
				}
			} else {
				// actualizar estado
				datosAg.setContexto(contextoLlega);
			}

			System.out.println("Enviando respuesta a agente Representante");
			// enviar a representante
			Agent agenteS = this.getAgent().getAdmLocal().getAgent(datos.getAliasAgentAux());

			Message data = new Message(datosEnviar,datos.getReceiverAgent(), datos.getSenderAgent(), this.getAgent().getID());
			//Event evento = new Event(ExecutionRepresentativeGuard.class, data);
			RepresentativeChangeContext evento = new RepresentativeChangeContext(data);
			agenteS.executeAction(evento);
		}
		return null;
	}
	
	/**
	 * Obtener id de usuario a partir del alias del agente
	 * @param aliasAg alias del agente contexto
	 * @return id de usuario
	 */
	public String getIdUsuarioFormAlias(String aliasAg) {
		String newAlias = "";
		String[] subStrings = aliasAg.split("-");
		for (int i = 0; i < subStrings.length; i++) {
			// System.out.println(subStrings[i]);
			if (i > 0 && i < subStrings.length - 1) {
				newAlias += subStrings[i];
				if (i < subStrings.length - 2)
					newAlias += "-";
			}
		}

		return newAlias;
	}

}
