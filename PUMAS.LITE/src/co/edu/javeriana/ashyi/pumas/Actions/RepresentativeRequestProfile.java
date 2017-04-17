package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MainPassCalculateDistanceG;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRepresentativeData;

public class RepresentativeRequestProfile<T> extends MessagePassingAction<T>{
	
	public RepresentativeRequestProfile(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		
		Message datos = (Message)this.getMensaje();
		
		System.out.println("In Guard RepresentativeProfileRequest");

		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());
		AgentRepresentativeData datosI = (AgentRepresentativeData) myAgent.getDatos();

		// agente intermediario
		Agent agIntermediary = this.getAgent().getAdmLocal().getAgent(datos.getReceiverAgent());

		
		List datosN = (List) datos.getMessage();
		String aliasAg = (String) datosN.get(0);
		String newAlias = "";
		String[] subStrings = aliasAg.split("-");
		for (int i = 2; i < subStrings.length; i++) {
			// System.out.println(subStrings[i]);
			if (i > 0 && i < subStrings.length - 1) {
				newAlias += subStrings[i];
				if (i < subStrings.length - 2)
					newAlias += "-";
			}
		}

		Consulta cAshyi = new Consulta();

		List perfil = getCaracteristicasUsuario(newAlias, cAshyi);
		datosI.setUsuario(getUsuario(newAlias, cAshyi));
		datosI.setCaracteristicasUsuario(perfil);
		System.out.println("Enviar datos a calculo de distancia a " + datos.getSenderAgent() + " "
				+ perfil.size());

		List datosEnviar = new ArrayList<>();
		datosEnviar.add(perfil);
		datosEnviar.add((int) datosN.get(1));// idActividad
		datosEnviar.add((int) datosN.get(2));// tipoRuta

		agIntermediary = this.getAgent().getAdmLocal().getAgent(datos.getSenderAgent());
		Message data = new Message(datosEnviar, datos.getReceiverAgent(),datos.getSenderAgent());
		//Event evento = new Event(ASHYIMainExecutionGuard.class, data);
		MainPassCalculateDistanceG evento = new MainPassCalculateDistanceG(data);
		agIntermediary.executeAction(evento);
		return null;
	}
	
	/**
	 * Obtener el usuario de un agente
	 * @param newAlias alias del agente
	 * @param cAshyi Puente de consulta
	 * @return usuario consultado
	 */
	private Usuario getUsuario(String newAlias, Consulta cAshyi) {
		return cAshyi.getUsuario(newAlias);
	}
	
	/**
	 * Obtener las caracteristicas del perfil de usuario
	 * @param aliasAg alias del agente
	 * @param cAshyi Puente de consulta
	 * @return lista de caracteristicas del usuario consultado
	 */
	public List getCaracteristicasUsuario(String aliasAg, Consulta cAshyi) {
		List<CaracteristicasUsuario> caracteristicas = cAshyi.getCaracteristicasUsuario(aliasAg);

		List<CaracteristicasUsuario> estilos = new ArrayList<CaracteristicasUsuario>();
		List<CaracteristicasUsuario> personalidades = new ArrayList<CaracteristicasUsuario>();
		List<CaracteristicasUsuario> habilidades = new ArrayList<CaracteristicasUsuario>();
		List<CaracteristicasUsuario> competencias = new ArrayList<CaracteristicasUsuario>();

		// for(CaracteristicasUsuario cU : caracteristicas)
		// {
		// if(cU.getIdCaracteristica().getIdItem().getIdItem() == 4)
		// estilos.add(cU);
		// if(cU.getIdCaracteristica().getIdItem().getIdItem() == 7)
		// personalidades.add(cU);
		// if(cU.getIdCaracteristica().getIdItem().getIdItem() == 5)
		// habilidades.add(cU);
		// if(cU.getIdCaracteristica().getIdItem().getIdItem() == 6)
		// competencias.add(cU);
		// }

		// List<List> listaDatos = new ArrayList<List>();
		//
		// listaDatos.add(estilos);
		// listaDatos.add(personalidades);
		// listaDatos.add(habilidades);
		// listaDatos.add(competencias);

		return caracteristicas;
	}

}
