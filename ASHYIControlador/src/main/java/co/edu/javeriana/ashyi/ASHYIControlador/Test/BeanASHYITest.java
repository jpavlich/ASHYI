package co.edu.javeriana.ashyi.ASHYIControlador.Test;
/*
 * Para ejecutarlo hay tres opciones:
 * 1. Se deja el main como test y se le quita la anotacion a los metodos que se llaman en dicho test
 * 2. Se ejecutan los cinco ultimos metodos como uno solo para que no sobreescriba las lista de actividades (llamando al metodo Ecrear
 *    y eliminando los comentarios de los metodos que se invocan )
 * 3. Se ejecuta cada procedimiento de forma independiente, teniendo en cuenta que las listas de actividades se pierden cada i
 *    inicializacion de cada test
 */
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import co.edu.javeriana.ASHYI.hbm.ObjetivosActividadImpl;
import co.edu.javeriana.ASHYI.hbm.RecursoImpl;
import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;
import co.edu.javeriana.ASHYI.model.Recurso;
import co.edu.javeriana.ASHYI.model.RecursosActividad;
import co.edu.javeriana.ashyi.ASHYIControlador.BeanASHYI;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.GraphEdge;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BeanASHYITest {

	private String MainAgentAlias;
	private String EditorAgentAlias;
	private String ExecutorAgentAlias;
	private String AgentAliasEx;
	private String AgentAliasEd;
	private static BeanASHYI bean;
	
	private String nivelR;
	private int idActividadR;
	private String nombreActividadR;
	private static Consulta c;
	private List<Actividad> actividades;
	private List<Actividad> actividadesR;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bean = new BeanASHYI();
		c = new Consulta();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Datos a llenar de acuerdo a la base de datos
	 * EditorAgentAlias y AgentAliasEd esta relacionado con el nombre del profesor (usuario planificador) y su ID
	 * ExecutorAgentAlias y AgentAliasEx esta relacionado con el nombre del estudiante (usuario ejecutor) y su ID
	 * 
	 * nivelR es el nivel de las actividades atomicas manejadas
	 * idActividadR id de la actividad recursiva para calcular el grafo
	 * nombreActividadR nombre de la actividad recursiva para calcular el grafo
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MainAgentAlias = "Main-ASHYIAgent-Aprender a aprender en la red 1 1 PUJ";
		EditorAgentAlias = "Editor-Monica-eb8d9f67-dbf6-4332-9ad6-cdca89df7c96-Instructor";
		AgentAliasEd = "Monica-eb8d9f67-dbf6-4332-9ad6-cdca89df7c96-Instructor";
		ExecutorAgentAlias = "Executor-Estudiante-bbfbbd34-c9c6-48ca-bac8-d9385034a311-Student";
		AgentAliasEx = "Estudiante-bbfbbd34-c9c6-48ca-bac8-d9385034a311-Student";
		
		nivelR = "Universitario";
		idActividadR = 3;
		nombreActividadR = "Unidad 1";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Verifica si es correcta la creacion del agente
	 * Principal
	 * 
	 * Y si es correcta la creacion de los agentes de PUMAS.Lite
	 * Router
	 * Intermediary
	 * Information Source
	
	 */	
	@Test
	public void AcrearAgentesSistema() {
		//fail("Not yet implemented");
		// Aniadir agente editor si es profesor
		// crear agente editor
		crearAgentePrincipal();		
		crearAgenteAEnrutador();
		crearAgenteBIntermediario();
		crearAgenteCFuenteInformacion();

	}
	
	//@Test
	public void crearAgenteCFuenteInformacion() {
		Assert.assertTrue("No Existe Information Source", bean.existeAgente("InformationSourceAgent-1"));
		System.out.println("Creacion de agente InformationSourceAgent-1");
	}

	//@Test
	public void crearAgenteBIntermediario() {
		Assert.assertTrue("No Existe Intermediary", bean.existeAgente("IntermediaryAgent"));
		System.out.println("Creacion de agente IntermediaryAgent");
	}

	//@Test
	public void crearAgenteAEnrutador() {
		Assert.assertTrue("No Existe Router", bean.existeAgente("RouterAgent-1"));
		System.out.println("Creacion de agente RouterAgent-1");
	}
	
	public void crearAgentePrincipal() {
		System.out.println("Creacion de agente principal");
		
		bean.crearAgenteMain(MainAgentAlias);
		
		Assert.assertTrue("No Existe Main", bean.existeAgente(MainAgentAlias));
	}

	/**
	 * Verifica si es correcta la creacion de los agentes
	 * Editor
	 * Ejecutor
	 * 
	 * Y si es correcta la creacion de los agentes de PUMAS.Lite
	 * Representative
	 * Context
	 */
	@Test
	public void BcrearAgentesUsuarios() {
		crearAgentePlanificador();
		
		crearAgenteRepresentante(AgentAliasEd);
		crearAgenteEjecutor();
				
		//creacion de agentes de pumas.lite para ejecutor
		accionUser();
				
		crearAgenteRepresentante(AgentAliasEx);
		
	}
	
	private void accionUser() {
		//creacion de agentes de pumas.lite para ejecutor
		List contexto = new ArrayList();
		//dentro del campus
		contexto.add("campus");
		//ingreso desde pc de escritorio
		contexto.add("desktop");
		//3 es el id de una unidad didactica con objetivos asociados
		contexto.add(idActividadR);
		contexto.add(AgentAliasEx);
		bean.executeAction(ExecutorAgentAlias, "", "USER",contexto);
		Assert.assertTrue("No Existe Context", bean.existeAgente("ContextAgent-"+ AgentAliasEx));
	}

	
	public void crearAgenteRepresentante(String alias) {
		
		//creacion de agentes de pumas.lite para ejecutor

		bean.executeAction("", "", "USERPLAN",AgentAliasEd);	
		Assert.assertTrue("No Existe Representante de editor", bean.existeAgente("RepresentativeAgent-" +alias));		
		
		System.out.println("Creacion de agente Representante "+alias);
	}
	//@Test
	public void crearAgenteEjecutor() {
		
		bean.crearAgente( ExecutorAgentAlias, 3, "Aprender a aprender en la red 1 1 PUJ");
		
		Assert.assertTrue("No Existe Executor", bean.existeAgente(ExecutorAgentAlias));			
		
		System.out.println("Creacion de agente ejecutor");
	}
	//@Test
	public void crearAgentePlanificador() {
		System.out.println("Creacion de agente planificador");
		
		bean.crearAgente(EditorAgentAlias, 2, "Aprender a aprender en la red 1 1 PUJ");
		
		Assert.assertTrue("No Existe Editor", bean.existeAgente(EditorAgentAlias));
	}

	/**
	 * Verifica que se realiza la consulta de actividades de una actividad recursiva (unidad didactica)
	 */
	@Test
	public void CconsultaActividades()
	{	
		System.out.println("Consulta de actividades segun objetivos");
		
		List<List> objActividad = consultarObjetivos();
		
		List<Object> datosEnviar = new ArrayList<Object>();
		datosEnviar.add(objActividad);
		datosEnviar.add(nivelR);
		datosEnviar.add(3);

		List ActividadesConsultadas = crearG(datosEnviar);	
			
		
		actividades = new ArrayList<Actividad>();
		for(int i = 0; i < ActividadesConsultadas.size(); i++)
			actividades.add(c.getActividad((Integer) ActividadesConsultadas.get(i)));			
	}
	
	public void consultaActividades()
	{	
		System.out.println("Consulta de actividades segun objetivos");
		
		List<List> objActividad = consultarObjetivos();
		
		List<Object> datosEnviar = new ArrayList<Object>();
		datosEnviar.add(objActividad);
		datosEnviar.add(nivelR);
		datosEnviar.add(3);

		List ActividadesConsultadas = crearG(datosEnviar);	
			
		
		actividades = new ArrayList<Actividad>();
		for(int i = 0; i < ActividadesConsultadas.size(); i++)
			actividades.add(c.getActividad((Integer) ActividadesConsultadas.get(i)));			
	}
	
	private List crearG(List<Object> datosEnviar) {
		bean.executeAction(EditorAgentAlias,MainAgentAlias, "CreateG", datosEnviar);
		
		int espera = 0;
		while(bean.getActividadesEditor(EditorAgentAlias,idActividadR).size() == 0)
		{
			System.out.println("Esperando Actividades "+bean.getActividadesEditor(EditorAgentAlias,3).size());
			esperar(10);// dormir el hilo
			espera++;
			if (espera > 18000)// si espera mas de 6 minutos, es porque
				// no hay actividades todavia
				break;
		}
		
		Assert.assertTrue("No actividades disponibles", espera < 18000);
		
		//3 es el id de una unidad didactica con objetivos asociados
		List ActividadesConsultadas = bean.getActividadesEditor(EditorAgentAlias, idActividadR);
		System.out.println(ActividadesConsultadas.size()+" actividades encontradas");
		Assert.assertTrue("No actividades consultadas", ActividadesConsultadas.size() != 0);
	
		return ActividadesConsultadas;
	}

	private List<List> consultarObjetivos() {
		List<List> objActividad = c.getIdsObjetivosActividad(idActividadR, nombreActividadR);
		System.out.println("Objetivos "+objActividad.size());
		Assert.assertTrue("Objetivos no encontrados", objActividad.size() != 0);
		return objActividad;
	}

	/**
	 * Consulta las actividades de refuerzo del sistema
	 * @param c Consulta, puente de comunicacion
	 * @return lista de actividades de refuerzo
	 */
	@Test
	public void DconsultaActividadesRefuerzo() {
		
		System.out.println("Consulta de actividades de refuerzo");
		
		List<Object> datosEnviar = new ArrayList<Object>();
		datosEnviar.add(nivelR);
		datosEnviar.add(idActividadR);
		
		List ActividadesRConsultadas = crearGRA(datosEnviar);		
		
		actividadesR = new ArrayList<Actividad>();
		for(int i = 0; i < ActividadesRConsultadas.size(); i++)
			actividadesR.add(c.getActividad((Integer) ActividadesRConsultadas.get(i)));
		
	}
	
	public void consultaActividadesRefuerzo() {
		
		System.out.println("Consulta de actividades de refuerzo");
		
		List<Object> datosEnviar = new ArrayList<Object>();
		datosEnviar.add(nivelR);
		datosEnviar.add(idActividadR);
		
		List ActividadesRConsultadas = crearGRA(datosEnviar);		
		
		actividadesR = new ArrayList<Actividad>();
		for(int i = 0; i < ActividadesRConsultadas.size(); i++)
			actividadesR.add(c.getActividad((Integer) ActividadesRConsultadas.get(i)));
		
	}

	private List crearGRA(List datosEnviar) {
		bean.executeAction(EditorAgentAlias, MainAgentAlias, "CreateGRA", datosEnviar);
		
		while(bean.getActividadesRefuerzoEditor(EditorAgentAlias, idActividadR).size() == 0)
		{
			System.out.println("Esperando Actividades de refuerzo");
		}
		
		List ActividadesRConsultadas = bean.getActividadesRefuerzoEditor(EditorAgentAlias, idActividadR);
		System.out.println(ActividadesRConsultadas.size()+" actividades de refuerzo encontradas");
		Assert.assertTrue("No actividades de refuerzo consultadas", ActividadesRConsultadas.size() != 0);
		return ActividadesRConsultadas;
	}

	/**
	 * Metodo inicial para comprobar en orden las funciones
	 */
	//@Test
	public void main()
	{		
		//BESA.kiss: creacion de agente principal
		//PUMAS.Lite: agentes del sistema
		AcrearAgentesSistema();
		//BESA.kiss: creacion de agentes editor y ejecutor
		//PUMAS.Lite: agentes de cada usuario
		BcrearAgentesUsuarios();
		//Consulta de actividades 
		//Paso de mensajes de BESA.kiss a PUMAS.Lite y el camino contrario 
		CconsultaActividades();
		//Consulta de actividades 
		//Paso de mensajes de BESA.kiss a PUMAS.Lite y el camino contrario 
		DconsultaActividadesRefuerzo();
		//BESA.kiss: Creacion de grafo
		EcrearGrafoGeneral();
		//Creacion de ruta a estudiante
		//Paso de mensajes de BESA.kiss a PUMAS.Lite y el camino contrario
		FcrearRutaEstudiante();
		//Cambio de contexto de estudiante
		//Paso de mensajes de BESA.kiss a PUMAS.Lite y el camino contrario
		GcambioContexto();
	}
	
	/**
	 * Verifica si el agente ejecutor tiene un cambio de contexto
	 */
	@Test
	public void GcambioContexto() {
		//creacion de agentes de pumas.lite para ejecutor
		List contexto = new ArrayList();
		//dentro del campus
		contexto.add("outside");
		//ingreso desde pc de escritorio
		contexto.add("mobile");
		
		System.out.println("Verificando cambio de contexto: "+ExecutorAgentAlias+" cont: "+contexto.size() + " es: "+bean.isCambioContexto(ExecutorAgentAlias, contexto));
		//si hay cambio de contexto
		Assert.assertTrue("No hay cambio de contexto", bean.isCambioContexto(ExecutorAgentAlias, contexto));
		
		if(bean.isCambioContexto(ExecutorAgentAlias, contexto))
		{		
			contexto.add(idActividadR);
			bean.executeAction(ExecutorAgentAlias,MainAgentAlias, "ContextCheck",contexto);
		}
	}
	
	public void cambioContexto() {
		//creacion de agentes de pumas.lite para ejecutor
		List contexto = new ArrayList();
		//dentro del campus
		contexto.add("outside");
		//ingreso desde pc de escritorio
		contexto.add("mobile");
		
		System.out.println("Verificando cambio de contexto: "+ExecutorAgentAlias+" cont: "+contexto.size() + " es: "+bean.isCambioContexto(ExecutorAgentAlias, contexto));
		//si hay cambio de contexto
		Assert.assertTrue("No hay cambio de contexto", bean.isCambioContexto(ExecutorAgentAlias, contexto));
		
		if(bean.isCambioContexto(ExecutorAgentAlias, contexto))
		{		
			contexto.add(idActividadR);
			bean.executeAction(ExecutorAgentAlias,MainAgentAlias, "ContextCheck",contexto);
		}
	}

	/**
	 * Crea la ruta para el usuario ejecutor, con base en el grafo general
	 */
	@Test
	public void FcrearRutaEstudiante() {
		
		bean.executeAction(ExecutorAgentAlias,MainAgentAlias, "RouteG",idActividadR);
		
		while ((!bean.getEstadoCalculo(ExecutorAgentAlias, idActividadR)))
				System.out.println("Esperando calculo");
		
		List ActividadesGrafoConsultadas = bean.getActividadesEjecutor(ExecutorAgentAlias, idActividadR);
		System.out.println(ActividadesGrafoConsultadas.size()+" actividades de grafo encontradas");
		Assert.assertTrue("No actividades de grafo consultadas", ActividadesGrafoConsultadas.size() != 0);		
		
	}
	
	public void crearRutaEstudiante() {
		
		bean.executeAction(ExecutorAgentAlias,MainAgentAlias, "RouteG",idActividadR);
		
		while ((!bean.getEstadoCalculo(ExecutorAgentAlias, idActividadR)))
				System.out.println("Esperando calculo");
		
		List ActividadesGrafoConsultadas = bean.getActividadesEjecutor(ExecutorAgentAlias, idActividadR);
		System.out.println(ActividadesGrafoConsultadas.size()+" actividades de grafo encontradas");
		Assert.assertTrue("No actividades de grafo consultadas", ActividadesGrafoConsultadas.size() != 0);		
		
	}

	/**
	 * Crea el grafo general de la actividad recursiva y lo almacena en el agente editor
	 * @param lista actividades de clase
	 * @param refuerzoList actividades de refuerzo
	 * @param idActividad id actividad recursiva
	 * @param c puente de consulta
	 */
	@Test
	public void EcrearGrafoGeneral() {
//		consultaActividades();
//		consultaActividadesRefuerzo();
		onReceiveActivities(actividades, actividadesR, idActividadR, c);
//		crearRutaEstudiante();
//		cambioContexto();
	}

	/**
	 * Dormir el hilo
	 * @param segundos tiempo a dormir el hilo
	 */
	public void esperar(int segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (Exception e) {
			// Mensaje en caso de que falle
		}
	}
	
	/**
	 * Crea el grafo general de la actividad recursiva y lo almacena en el agente editor
	 * @param lista actividades de clase
	 * @param refuerzoList actividades de refuerzo
	 * @param idActividad id actividad recursiva
	 * @param c puente de consulta
	 */
	public void onReceiveActivities(List<Actividad> lista, List<Actividad> refuerzoList, int idActividad, Consulta c) {
		
		// ItemPlans organizados por objetivos de actividad
		Map<ObjetivosActividad, ArrayList<ItemPlan>> itemPlansPorObjetivo;
		Map<ObjetivosActividad, ArrayList<ItemPlan>> refuerzosPorObjetivo;
		//ArrayList<ItemPlan> refuerzos;
		ArrayList<ObjetivosActividad> objetivos;
		// Grafo de itemPlan
		Graph<Integer> grafoActividades;
		
		List<Integer> idItemsUltimoObjetivo = new ArrayList<Integer>();
		ObjetivosActividad objetivoActual, objetivoActualR, ultimoObjetivo=new ObjetivosActividadImpl();
		itemPlansPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		//this.refuerzos=new ArrayList<ItemPlan>();
		refuerzosPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		objetivos=new ArrayList<ObjetivosActividad>();
		Actividad inicial=c.getActividadInicial();
		
		Assert.assertTrue("Actividad inicial no encontrada", inicial.getIdActividad() > 0);
		
		//System.out.println("Actividad inicial: "+inicial.getNombre()+" id: "+inicial.getIdActividad());
		objetivoActual = c.getObjetivosActividad(inicial.getNombre(), inicial.getNivel_recursividad()).get(0);
		System.out.println("Objetivo inicial: "+objetivoActual.getIdObjetivo().getNombre()+" id: "+objetivoActual.getIdObjetivo().getIdObjetivo());
		LinkedList<Actividad> list=new LinkedList<Actividad>();
		list.add(inicial);
		for (int i = 0; i < lista.size(); i++) {
			list.add(lista.get(i));
		}
		
		Assert.assertTrue("Objetivos no encontrados", list.size() != 0);
		//==================================aniadir las actividades originales al mapa==================================
		if (list != null && !list.isEmpty()) {
			objetivoActual = c.getObjetivosActividad(list.get(0).getNombre(), list.get(0).getNivel_recursividad()).get(0);
			objetivos.add(objetivoActual);
			int orden=1;
			for (int i = 0; i < list.size(); i++) {
				Actividad auxActividad = list.get(i);
				ObjetivosActividad ObjetivoAc = c.getObjetivosActividad(auxActividad.getNombre(),auxActividad.getNivel_recursividad()).get(0);
				if (ObjetivoAc.getIdObjetivo().getIdObjetivo() != 
						objetivoActual.getIdObjetivo().getIdObjetivo())
				{
					objetivoActual = ObjetivoAc;
					objetivos.add(ObjetivoAc);
					orden+=2;
				}

				List<RecursosActividad> actividadRecurso = c.actividadesRecurso(c.getItems(), auxActividad);

				if(actividadRecurso.size() > 0)
				{
					for(RecursosActividad ac : actividadRecurso)
					{
						Actividad ud=c.getActividad(idActividadR);
						auxActividad = ac.getIdActividad();
						Recurso auxRecurso = ac.getIdRecurso();
						//nuevo item plan activo
						ItemPlan itemPlan = c.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
						if(!itemPlansPorObjetivo.containsKey(objetivoActual))
							itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
						itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
					}
				}else
				{				
					Actividad ud=c.getActividad(idActividadR);

					//nuevo item plan activo sin recurso
					ItemPlan itemPlan = c.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
					if(!itemPlansPorObjetivo.containsKey(objetivoActual))
						itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
					itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
				}
			}
			ultimoObjetivo=objetivoActual;		
		}

		int numObjetivos=itemPlansPorObjetivo.keySet().size();
		
		Assert.assertTrue("Objetivos no desarrollados en grafo", numObjetivos != 0);
		
		System.out.println("!!!!!!!!!!!!!!!1 : "+numObjetivos);
		//==================================aniadir las de refuerzo al mapa==================================
		int orden = 2;
		
		Assert.assertTrue("Refuerzoz no encontrados", refuerzoList.size() > 0);
		
		if(refuerzoList.size()>0)
		{			
			for(int j = 0; j <objetivos.size()-1; j++)
			{
				objetivoActual = objetivos.get(j);
				objetivoActualR = c.getObjetivosActividad(refuerzoList.get(0).getNombre(), refuerzoList.get(0).getNivel_recursividad()).get(0);
				for (int i = 0; i < refuerzoList.size(); i++) {
					Actividad auxActividad = refuerzoList.get(i);
					List<RecursosActividad> actividadRecurso = c.actividadesRecurso(c.getItems(), auxActividad);

					if(actividadRecurso.size() > 0)
					{
						for(RecursosActividad ac : actividadRecurso)
						{
							Actividad ud=c.getActividad(idActividadR);
							auxActividad = ac.getIdActividad();
							Recurso auxRecurso = ac.getIdRecurso();
							//nuevo item plan activo
							//1, con recurso y true de refuerzo
							ItemPlan itemPlan = c.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
							if(!refuerzosPorObjetivo.containsKey(objetivoActual))
								refuerzosPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
							refuerzosPorObjetivo.get(objetivoActual).add(itemPlan);
						}
						//las de refuerzo
					}else
					{	
						Actividad ud=c.getActividad(idActividadR);
						//nuevo item plan activo sin recurso
						//2, sin recurso y true de refuerzo
						ItemPlan itemPlan = c.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
						
						if(!refuerzosPorObjetivo.containsKey(objetivoActual))
							refuerzosPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
						refuerzosPorObjetivo.get(objetivoActual).add(itemPlan);
					}					
				}
				orden+=2;
			}
			//cerrar ciclo de objetivos
		}

		//==================================crear grafo de verdad==================================
		grafoActividades = new Graph<Integer>();
		Iterator<ObjetivosActividad> it = objetivos.iterator();
		ObjetivosActividad objaux=it.next();
		ObjetivosActividad objauxR=objaux;

		int numObj=1;
		Iterator<ObjetivosActividad> itaux = objetivos.iterator();
		while(itaux.hasNext())
		{
			System.out.println("Objetivo "+numObj);
			ObjetivosActividad auxobj=itaux.next();
			ArrayList<ItemPlan> itemsPlanNivelAnterior=itemPlansPorObjetivo.get(auxobj);
			for (int i = 0; i < itemsPlanNivelAnterior.size(); i++) {
				if(itemsPlanNivelAnterior.get(i).getIdItemPlan() == null)
				{
					itemsPlanNivelAnterior.remove(i);
					i--;
				}
				System.out.print(itemsPlanNivelAnterior.get(i).getIdItemPlan()+" ");
			}
			numObj++;
		}

		ArrayList<ItemPlan> itemsPlanNivelAnterior = itemPlansPorObjetivo.get(objaux);
		int k=0;
		//sizeRefuerzos=refuerzos.size();
		int times=1;
		orden=1;
		while (it.hasNext()) {
			objauxR=objaux;
			objaux=it.next();
			ArrayList<ItemPlan> itemsPlanNivelActual = itemPlansPorObjetivo.get(objaux);

			//conectar originales con siguiente nivel y con refuerzo, luego refuerzo con siguiente nivel
			boolean refuerzosConectados=false;
			for (int i = 0; i < itemsPlanNivelAnterior.size(); i++) {
				ItemPlan itemPlanNivelAnterior = itemsPlanNivelAnterior.get(i);
				for (int j = 0; j < itemsPlanNivelActual.size(); j++) {
					ItemPlan actual=itemsPlanNivelActual.get(j);
					grafoActividades.addEdge(itemPlanNivelAnterior.getIdItemPlan(),
							actual.getIdItemPlan(), 4.0, orden);
				}

				//conectar originales con refuerzo y refuerzo con siguiente nivel				
				if(!refuerzosPorObjetivo.isEmpty()&&refuerzosPorObjetivo.get(objauxR) != null)
				{
					ArrayList<ItemPlan> itemsPlanRzNivelActual = refuerzosPorObjetivo.get(objauxR);

					for (int m = 0; m < itemsPlanRzNivelActual.size(); m++) {
						ItemPlan refuerzo=itemsPlanRzNivelActual.get(m);
						grafoActividades.addEdge(itemPlanNivelAnterior.getIdItemPlan(),refuerzo.getIdItemPlan(), 4.0, orden);

						if(!refuerzosConectados)
						{
							for (int l = 0; l < itemsPlanNivelActual.size(); l++) {
								ItemPlan actual=itemsPlanNivelActual.get(l);
								//System.out.println("Refuerzo origen: "+refuerzo.getIdItemPlan()+", actividad destino "+actual.getIdItemPlan());
								grafoActividades.addEdge(refuerzo.getIdItemPlan(),
										actual.getIdItemPlan(), 4.0, orden+1);
							}
							
						}
					}
					refuerzosConectados=true;
				}	
			}
			refuerzosConectados=false;
			itemsPlanNivelAnterior=itemsPlanNivelActual;
			orden+=2;
		}

		Iterator <ItemPlan> it2=itemPlansPorObjetivo.get(ultimoObjetivo).iterator();
		while(it2.hasNext())
			idItemsUltimoObjetivo.add(it2.next().getIdItemPlan());

		List<Object> datosEnviar = new ArrayList<Object>();
		
		Assert.assertTrue("Grafo contiene valores nulos", grafoActividades.getNumEdges() > 0);

		datosEnviar.add(grafoActividades);
		datosEnviar.add(idItemsUltimoObjetivo);
		datosEnviar.add(idActividad);
		Set<GraphEdge> h = grafoActividades.getEdges();
		
		//144 para los datos del curso piloto en su primera unidad	
		//Assert.assertTrue("Grafo mal creado", h.size() == 144);
		
		//menor que 1, porque puede tener solo la actividad inicial
		Assert.assertTrue("Grafo mal creado", h.size() > 1);
		
		Assert.assertTrue("Grafo contiene valores nulos", !h.contains(null));
		
		System.out.println("Grafo con "+h.size()+" edges");
		
		for(GraphEdge idItem: h)
		{
			System.out.println("!!!!!!!!!!!!!! 3FlC: "+idItem+ " orden: "+idItem.getOrder());
		}

		//=======================================enviar grafo a gente editor======================= 
		bean.executeAction(EditorAgentAlias, MainAgentAlias, "Graph", datosEnviar);

	}
}
