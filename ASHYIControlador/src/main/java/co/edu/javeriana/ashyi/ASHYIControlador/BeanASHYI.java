package co.edu.javeriana.ashyi.ASHYIControlador;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.kiss.Admin;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Actions.Action;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorVerifyContextChange;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorVerifyContextCheck;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorCalculateDistanceG;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorEditGraph;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorEditGraphRemedialA;
import co.edu.javeriana.ashyi.kiss.Actions.ExecutorChangeProfile;
import co.edu.javeriana.ashyi.kiss.Actions.MainCreateGRA;
import co.edu.javeriana.ashyi.kiss.Actions.MainEnableDIsableActivities;
import co.edu.javeriana.ashyi.kiss.Actions.MainPassContextCheck;
import co.edu.javeriana.ashyi.kiss.Actions.MainCreateG;
import co.edu.javeriana.ashyi.kiss.Actions.MainCreatePumas;
import co.edu.javeriana.ashyi.kiss.Actions.MainCreateUser;
import co.edu.javeriana.ashyi.kiss.Actions.MainCreateUserPlan;
import co.edu.javeriana.ashyi.kiss.Actions.MainPassCalculateDistanceG;
import co.edu.javeriana.ashyi.kiss.Actions.MainEditGraph;
import co.edu.javeriana.ashyi.kiss.Actions.MainPassGraph;
import co.edu.javeriana.ashyi.kiss.Actions.MainChangeProfile;
import co.edu.javeriana.ashyi.kiss.Actions.MainCheckRoute;
import co.edu.javeriana.ashyi.kiss.Actions.MainPassRouteG;
import co.edu.javeriana.ashyi.kiss.Agents.ASHYIAgent;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;
import co.edu.javeriana.ashyi.kiss.Data.ExecutorASHYIAgentData;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;
import co.edu.javeriana.ashyi.kiss.Data.PlanASHYIAgentData;
import co.edu.javeriana.ashyi.pumas.Data.AgentContextData;

public class BeanASHYI {

	private String foo = "Default Foo";
	private int bar = 0;
	private Admin contenedor = Admin.INSTANCE;
	private String aliasCx;

	/**
	 * Constructor - Creacion contenerdor BESA
	 */
	public BeanASHYI() {
		System.out.println("Contenedor ASHYI BESA.KISS creado");
		foo = "Created container";
	}

	/**
	 * Creacion de agente principal (main) 
	 * Agente de comunicacion con interface grafica
	 * @param aliasMain
	 */
	public void crearAgenteMain(String aliasMain) {
		aliasCx = aliasMain;
		// creando agente principal de besa kiss
		if (!existeAgente(aliasCx)) {
			String nombre = crearAgente(aliasCx, 1, "");
			System.out.println("Agente " + nombre + " creado");
		}
	}

	/**
	 * Creacion de agentes (editor - planificador)
	 * @param alias de agente
	 * @param tipo (2: editor - 3: ejecutor)
	 * @param actividad a asociar
	 * @return
	 */
	public String crearAgente(String alias, int tipo, String title) {
		ASHYIAgent agent = null;
		if (tipo == 1)// MAIN
		{
			List<String> agents = new ArrayList<String>();
			MainAgentData agentData = new MainAgentData(agents);
			List<Class> acciones = new ArrayList<Class>();
			acciones.add(MainPassContextCheck.class);
			acciones.add(MainCreateUser.class);
			acciones.add(MainCreateUserPlan.class);
			acciones.add(MainCheckRoute.class);
			acciones.add(MainPassRouteG.class);
			acciones.add(MainEnableDIsableActivities.class);
			acciones.add(MainCreateG.class);
			acciones.add(MainCreateGRA.class);
			acciones.add(MainCreatePumas.class);
			acciones.add(MainPassCalculateDistanceG.class);
			acciones.add(MainEditGraph.class);
			acciones.add(MainPassGraph.class);
			acciones.add(MainChangeProfile.class);

			agent = new ASHYIAgent(alias, agentData, acciones);
			agent.start();

			executeAction("", alias, "PUMAS", null);
		}
		if (tipo == 2)// plan
		{
			PlanASHYIAgentData agentData = new PlanASHYIAgentData();
			agentData.setActividadRecursiva(title);
			List<Class> acciones = new ArrayList<Class>();
			acciones.add(ExecutorCalculateDistanceG.class);
			acciones.add(ExecutorChangeProfile.class);
			acciones.add(ExecutorVerifyContextChange.class);
			acciones.add(ExecutorVerifyContextCheck.class);
			acciones.add(ExecutorEditGraph.class);
			acciones.add(ExecutorEditGraphRemedialA.class);
			//acciones.add(ASHYIPlanLoadStateGuard.class);

			agent = new ASHYIAgent(alias, agentData, acciones);
			agent.start();
		}
		if (tipo == 3)// executor
		{
			ExecutorASHYIAgentData agentData = new ExecutorASHYIAgentData();
			agentData.setActividadRecursiva(title);
			List<Class> acciones = new ArrayList<Class>();
			acciones.add(ExecutorCalculateDistanceG.class);
			acciones.add(ExecutorChangeProfile.class);
			acciones.add(ExecutorVerifyContextChange.class);
			acciones.add(ExecutorVerifyContextCheck.class);
			acciones.add(ExecutorEditGraph.class);
			acciones.add(ExecutorEditGraphRemedialA.class);
			//acciones.add(ASHYILoadStateGuard.class);

			agent = new ASHYIAgent(alias, agentData, acciones);
			agent.start();
		}

		return agent.getID();
	}

	/**
	 * Obtener handler de un agente consultado
	 * @param alias del agente a buscar
	 * @return
	 */
	public Agent obtenerAgente(String alias) {
		System.out.println("Buscando alias");
		Agent handler = null;
		if (contenedor.doesAgentExist(alias)) {
			System.out.println("Agente existe");
			handler = contenedor.getAgent(alias);
		} else {
			System.out.println("Agente no existe");
		}
		return handler;
	}

	/**
	 * Saber si el agente del alias dado, existe en el contenedor
	 * @param alias del agente a buscar
	 * @return existe o no
	 */
	public boolean existeAgente(String alias) {
		return contenedor.doesAgentExist(alias);
	}

	/**
	 * @return foo
	 */
	public String getFoo() {
		return (this.foo);
	}

	/**
	 * @param foo
	 */
	public void setFoo(String foo) {
		this.foo = foo;
	}

	/**
	 * @return bar
	 */
	public int getBar() {
		return (this.bar);
	}

	/**
	 * @param bar
	 */
	public void setBar(int bar) {
		this.bar = bar;
	}

	/**
	 * Retornar el alias de un agente que se busca
	 * @param alias del agente a buscar
	 * @return alias del agente si existe, si no, ""
	 */
	public String getAgent(String alias) {
		System.out.println("buscando handler en besa");
		Agent handler = obtenerAgente(alias);
		if (handler != null) {
			System.out.println("Agente " + handler.getID() + " encontrado");
			return handler.getID();
		}

		return "";
	}

	/**
	 * Obtener el contenedor de agentes
	 * @return contenedor
	 */
	public Admin getContenedor() {
		return contenedor;
	}

	/**
	 * Cambiar el contenedor de agentes
	 * @param contenedor
	 */
	public void setContenedor(Admin contenedor) {
		this.contenedor = contenedor;
	}

	/**
	 * Enviar evento a agente
	 * @param myAlias alias de agente que envia el evento
	 * @param alias de agente que recibe el evento
	 * @param tipo de evento
	 * @param datos a enviar al evento
	 * @return
	 */
	public boolean executeAction(String myAlias, String alias, String tipo, Object datos) {
		
		boolean rta = false;
		Message data = new Message();
		Agent agente = null;
		Action evento = null;
		// crear agentes pumas
		if (tipo.equalsIgnoreCase("PUMAS")) {
			agente = obtenerAgente(alias);
			data = new Message( datos, alias, myAlias);
			evento = new MainCreatePumas(data);
		}
		// crear agentes pumas de usuario ejecutor
		if (tipo.equalsIgnoreCase("USER")) {
			agente = obtenerAgente(aliasCx);
			data = new Message(datos, alias, myAlias);
			//evento = new Event(ASHYIMainCreationGuarddata);
			evento = new MainCreateUser(data);
		}
		// crear agentes pumas de usuario plan
		if (tipo.equalsIgnoreCase("USERPLAN")) {
			agente = obtenerAgente(aliasCx);
			data = new Message(datos, alias, myAlias);
			//evento = new Event(ASHYIMainCreationGuarddata);
			evento = new MainCreateUserPlan(data);
		}
		// solicitar actividades iniciales de grafo
		if (tipo.equalsIgnoreCase("CreateG")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(), myAlias);
			evento = new MainCreateG(data);
		}
		// solicitar actividades de refuerzo
		if (tipo.equalsIgnoreCase("CreateGRA")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(), myAlias);
			evento = new MainCreateGRA(data);
		}
		// edición del grafo
		if (tipo.equalsIgnoreCase("EditG")) {
			alias = "CreateG-Agent";
			agente = obtenerAgente(alias);
			data = new Message(null, agente.getID(), myAlias);
			evento = new MainEditGraph(data);
		}
		// enviar grafo a agente de editor
		if (tipo.equalsIgnoreCase("Graph")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(), myAlias);
			evento = new MainPassGraph(data);
		}
		// calcular ruta de usuario ejecutor
		if (tipo.equalsIgnoreCase("RouteG")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(), myAlias);
			//evento = new Event(ASHYIMainExecutionGuarddata);
			evento = new MainPassRouteG(data);
		}
		// calcular ruta de usuario ejecutor
		if (tipo.equalsIgnoreCase("RouteCheck")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(),myAlias);
			//evento = new Event(ASHYIMainExecutionGuarddata);
			evento = new MainCheckRoute(data);
		}
		// cambio de contexto
		if (tipo.equalsIgnoreCase("ContextCheck")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(), myAlias);
			//evento = new Event(ASHYIMainExecutionGuarddata);
			evento = new MainPassContextCheck(data);
		}
		// cargar datos agente
//		if (tipo.equalsIgnoreCase("LoadState")) {
//			agente = obtenerAgente(alias);
//			data = new Message(datos, agente.getID(),
//					myAlias, ASHYIMainExecution.class);
//			evento = new Event(ASHYIMainExecutiondata);
//		}
		// cambio de perfil por realizar una actividad
		if (tipo.equalsIgnoreCase("PROFILE_CHANGE")) {
			agente = obtenerAgente(alias);
			data = new Message(datos, agente.getID(),myAlias);
			evento = new MainChangeProfile(data);
		}
		// habilida deshabilita actividad
		if (tipo.equalsIgnoreCase("ACTIVITY_DH")) {
			agente = obtenerAgente(myAlias);
			data = new Message(datos, agente.getID(),"");
			evento = new MainEnableDIsableActivities(data);
		}

		agente.executeAction(evento);

		return rta;
	}

	/**
	 * Retorna el datos de un agente en particular
	 * @param alias del agente a buscar
	 * @return datos del agente solicitado
	 */
	public Data getDatosAgente(String alias) {

		Data datos = this.obtenerAgente(alias).getDatos();

		return datos;
	}

	/**
	 * Obtener actividades que debe ejecutar un agente editor
	 * @param alias del agente solicitado
	 * @param actividadR: id de actividad recursiva que contiene actividades a ejecutar 
	 * @return actividades que debe ejecutar un agente editor
	 */
	public List<Object> getActividadesEditor(String alias, int actividadR) {
		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(alias);
		if (datos.getActividades().size() >= 0) {
			return datos.getActividadesMapa(actividadR);
		}
		return new ArrayList<Object>();
	}

	/**
	 * Verificar si existen actividades de refuerzo
	 * @param alias del agente planificador a buscar
	 * @return si existen actividades de refuerzo
	 */
	public boolean getdatosActividadesRefuerzoEditor(String alias) {
		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(alias);
		return datos.isHayActividadesR();
	}

	/**
	 * Obtener actividades de refuerzo que debe ejecutar un agente editor
	 * @param alias del agente solicitado
	 * @param actividadR: id de actividad recursiva que contiene actividades a ejecutar 
	 * @return actividades de refuerzo que debe ejecutar un agente editor
	 */
	public List<Object> getActividadesRefuerzoEditor(String alias,
			int actividadR) {
		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(alias);

		if (datos.getActividadesRefuerzo().size() > 0) {
			return datos.getActividadesRefuerzoMapa(actividadR);
		}
		return new ArrayList<Object>();
	}

	/**
	 * verificar si el grafo de una actividad en especifico tiene nodos
	 * @param alias del agente planificador a verificar
	 * @param actividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @return si el grafo de una actividad en especifico tiene nodos o no
	 */
	public boolean getdatosGrafo(String alias, int actividad) {
		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(alias);

		if (datos.getGrafosMapa(actividad).getNumEdges() == 0)
			return false;
		else
			return true;
	}

	/**
	 * Verificar si ya se ha calculado el grafo para una actividad recursiva
	 * @param alias del agente a verificar
	 * @param actividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @return si ya se ha calculado el grafo para una actividad recursiva
	 */
	public boolean getEstadoCalculo(String alias, int actividad) {

		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) getDatosAgente(alias);

		return datos.isEstaCalculadoMapa(actividad);
	}

	/**
	 * Verificar si ya se ha ingresado las caracteristicas de un usuario en especifico
	 * @param alias del agente a verificar
	 * @return si ya se ha ingresado las caracteristicas de un usuario en especifico
	 */
	public int getdatosCaracteristicas(String alias) {
		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) getDatosAgente(alias);

		return datos.getCaracteristicasHechas();
	}

	/**
	 * 
	 * @param aliass del agente a verificar
	 * @param actividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @return actividades del grafo de una actividad recursiva de un usuario editor
	 */
	public List getActividadesEjecutor(String alias, int actividad) {
		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) getDatosAgente(alias);
		if (datos.getActividades().size() >= 0) {
			return datos.getActividadesMapa(actividad);
		}
		return new ArrayList();
	}

	/**
	 * Cambiar el datos del calculo del grafo de un usuario editor
	 * @param alias del agennte involucrado
	 * @param b se ha calculado o no
	 * @param actividad: id de actividad recursiva que contiene actividades a ejecutar
	 */
	public void setdatosCalculo(String alias, boolean b, int actividad) {

		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) getDatosAgente(alias);

		datos.setEstaCalculadoMapa(b, actividad);

	}

	/**
	 * @param aliasAg alias del agente involucrado
	 * @param idActividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @return grafo de una actividad recursiva sociado a un usuario editor
	 */
	public Graph getGrafoUsuario(String aliasAg, int idActividad) {

		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(aliasAg);

		if (datos.getGrafosMapa(idActividad).getNumEdges() != 0)
			return datos.getGrafosMapa(idActividad);
		else
			return new Graph<Object>();
	}

	/**
	 * cambiar grafo de una actividad recursiva sociado a un usuario editor
	 * @param aliasAg alias del agente involucrado
	 * @param idActividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @param grafo grafo a editar
	 */
	public void setGrafoUsuarioAg(String aliasAg, int idActividad, Graph grafo) {
		PlanASHYIAgentData datos = (PlanASHYIAgentData) getDatosAgente(aliasAg);

		datos.setGrafos(grafo, idActividad);

		System.out.println("datos de agente " + aliasAg + " cambiado");
	}

	/**
	 * 
	 * @param agenteInterface alias del agente involucrado
	 * @param contexto lista de caracteristicas que conforman el contexto
	 * @return si se el contexto de un usuario editor ha cambiado
	 */
	public boolean isCambioContexto(String agenteInterface, List contexto) {
		String agenteCxt = agenteInterface.replace("Executor", "ContextAgent");
		AgentContextData datos = (AgentContextData) getDatosAgente(agenteCxt);
		if (!datos.getContexto().containsAll(contexto)) {
			ExecutorASHYIAgentData datosAI = (ExecutorASHYIAgentData) getDatosAgente(agenteInterface);
			datosAI.setEstadoContexto(false);
			return true;
		} else {
			ExecutorASHYIAgentData datosAI = (ExecutorASHYIAgentData) getDatosAgente(agenteInterface);
			datosAI.setEstadoContexto(true);
			return false;
		}
	}

	/**
	 * @param agenteInterface alias del agente involucrado
	 * @return si se ha verificado el contexto de un usuario editor
	 */
	public boolean getdatosContexto(String agenteInterface) {
		ExecutorASHYIAgentData datosAI = (ExecutorASHYIAgentData) getDatosAgente(agenteInterface);
		return datosAI.isEstadoContexto();
	}

	/**
	 *  cambiar grafo de una actividad recursiva sociado a un usuario planificador
	 * @param aliasAg alias del agente involucrado
	 * @param idActividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @param grafo grafo a editar
	 */
	public void setGrafoAgMain(String aliasAg, int idActividad,
			Graph<Integer> grafo) {

		MainAgentData datos = (MainAgentData) getDatosAgente(aliasAg);

		datos.setGrafoMapa(grafo, idActividad);

		System.out.println("datos de agente " + aliasAg + " cambiado");
	}

	/**
	 *  cambiar las actividades del grafo de una actividad recursiva sociado a un usuario editor
	 * @param agenteInterface alias del agente involucrado
	 * @param idActividad: id de actividad recursiva que contiene actividades a ejecutar
	 * @param itemsPlan: id items plan del grafo del editor
	 */
	public void setdatosActividadesUsuario(String agenteInterface,
			int idActividad, List<Integer> itemsPlan) {

		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) getDatosAgente(agenteInterface);

		datos.setActividadesMapa(itemsPlan, idActividad);

		System.out.println("Actividades de datos de agente " + agenteInterface+ " cambiado");
	}
}
