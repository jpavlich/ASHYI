package co.edu.javeriana.ashyi.pumas.Actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.ASHYI.hbm.CaracteristicasUsuarioImpl;
import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.MainCheckRoute;
import co.edu.javeriana.ashyi.kiss.Actions.MessagePassingAction;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.pumas.Data.AgentRepresentativeData;

public class RepresentativeChangeProfile<T> extends MessagePassingAction<T>{
	
	public RepresentativeChangeProfile(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		Message datosLlegan = (Message)this.getMensaje();
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(getAgent().getID());

		// agente intermediario
		Agent handler = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());
		System.out.println("Cambiando perfil de agente: " + this.getAgent().getID());
		cambiarPerfilUsuario(datosLlegan, (AgentRepresentativeData) myAgent.getDatos());

		List datos = (List) datosLlegan.getMessage();

		int idActividad = (int) datos.get(1);

		// cambio de grafo
		handler = this.getAgent().getAdmLocal().getAgent(datosLlegan.getReceiverAgent());
		Message data = new Message(idActividad, datosLlegan.getSenderAgent(), this.getAgent().getID());
		MainCheckRoute evento = new MainCheckRoute(data);
		handler.executeAction(evento);
		return null;
	}

	/**
	 * Desarrolla evento "Cambio de perfil", segun actividades realizadas, cambia el perfil del usuario
	 * @param datosLlegan datos del evento
	 * @param myState estadodel agente Representante
	 */
	private void cambiarPerfilUsuario(Message datosLlegan, AgentRepresentativeData datosR) {
		List<CaracteristicasUsuario> perfil = datosR.getCaracteristicasUsuario();
		Consulta cAshyi = new Consulta();

		List datos = (List) datosLlegan.getMessage();

		int idTP = (int) datos.get(0);

		String useriD = getIdUsuarioFormAlias(this.getAgent().getID().replace("RepresentativeAgent-", ""));

		if (datosR.getUsuario() == null) {
			datosR.setUsuario(cAshyi.getUsuario(useriD));
		}
		System.out.println("Usuario del agente: " + datosR.getUsuario().getIdUsuario());

		if (perfil == null) {
			perfil = getCaracteristicasUsuario(useriD, cAshyi);
		}
		
		System.out.println("Perfil del agente: " + perfil.size());

		List<CaracteristicasItemsUsuario> cAct = cAshyi.getCaracteristicasItemsUsuario(cAshyi.getItemUsuario(idTP,
				datosR.getUsuario().getIdUsuario()));

		//System.out.println("Cract items usu del agente: " + cAct.size());

		for (CaracteristicasItemsUsuario cIU : cAct) {

			//System.out.println("CIU: " + cIU.getIdCaracteristica().getIdCaracteristica() + " est: " + cIU.isEstado());
			if (cIU.isEstado())// si la tiene
			{
				//System.out.println("la obtuvo");
				int index = -1;
				for (int j = 0; j < perfil.size(); j++)
					if (perfil.get(j).getIdCaracteristica().getIdCaracteristica() == cIU.getIdCaracteristica()
							.getIdCaracteristica()) {
						cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),cIU.getIdCaracteristica().getIdCaracteristica(),"Caracteristica actual",cIU.getIdItemPlan().getIdItemPlan(),new Date());

						index = j;
					}

				if (index < perfil.size() && index != -1)// si la tiene
				{
					cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),perfil.get(index).getIdCaracteristica().getIdCaracteristica(),"Competencia/habilidad obtenida. ya la tenía",cIU.getIdItemPlan().getIdItemPlan(),new Date());

					//System.out.println("si la tiene");
					// break;
				} else {
					//System.out.println("no la tiene");
					// almacenar caracteristica usuario
					CaracteristicasUsuario cU = new CaracteristicasUsuarioImpl(datosR.getUsuario(),
							cIU.getIdCaracteristica(), 1);
					cAshyi.saveObject(cU);
					perfil.add(cU);
					cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),cU.getIdCaracteristica().getIdCaracteristica(),"Competencia/habilidad obtenida. no la tenía, la obtuvo",cIU.getIdItemPlan().getIdItemPlan(),new Date());
				}
			} else// si no la cumplio
			{
				//System.out.println("no la cumplio");
				int index = -1;
				for (int j = 0; j < perfil.size(); j++)
					if (perfil.get(j).getIdCaracteristica().getIdCaracteristica() == cIU.getIdCaracteristica()
							.getIdCaracteristica()) {
						cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),cIU.getIdCaracteristica().getIdCaracteristica(),"Caracteristica actual",cIU.getIdItemPlan().getIdItemPlan(),new Date());
						index = j;
						
					}

				if (index < perfil.size() && index != -1)// si la tiene
				{
					//System.out.println("si la tiene, hay q quitarla");
					// eliminar del perfil
					cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),perfil.get(index).getIdCaracteristica().getIdCaracteristica(),"Competencia/habilidad no obtenida. La tenía, no la obtuvo",cIU.getIdItemPlan().getIdItemPlan(),new Date());
					cAshyi.deleteObject(perfil.get(index));
					perfil.remove(index);
				}else{
					CaracteristicasUsuario cU = new CaracteristicasUsuarioImpl(datosR.getUsuario(),
							cIU.getIdCaracteristica(), 1);
					cAshyi.generarLogCaracteristicas(datosR.getUsuario().getIdUsuario(),cU.getIdCaracteristica().getIdCaracteristica(),"Competencia/habilidad obtenida. no la tenía,no la obtuvo",cIU.getIdItemPlan().getIdItemPlan(),new Date());
				}
			}
		}

		// actualizar perfil de agente
		datosR.setCaracteristicasUsuario(perfil);
	}

	
	/**
	 * Obtener el id del usuario a partir del alias del agente
	 * @param aliasAg alias del agente
	 * @return id del usuario
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
