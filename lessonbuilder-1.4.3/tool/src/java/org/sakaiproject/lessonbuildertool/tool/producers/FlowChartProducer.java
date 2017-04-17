/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *
 * Author: Eric Jeney, jeney@rutgers.edu
 *
 * Copyright (c) 2010 Rutgers, the State University of New Jersey
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");                                                                
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.lessonbuildertool.GrafosUsuarioImpl;
import org.sakaiproject.lessonbuildertool.ItemPlanImpl;
import org.sakaiproject.lessonbuildertool.ObjetivosActividadImpl;
import org.sakaiproject.lessonbuildertool.RecursoImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageItemImpl;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.GrafoRelaciones;
import org.sakaiproject.lessonbuildertool.model.GrafosUsuario;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Usuario;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;





//
//import src.java.org.sakaiproject.lessonbuildertool.tool.beans.BufferedWriter;
//import src.java.org.sakaiproject.lessonbuildertool.tool.beans.FileWriter;
import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.GraphEdge;


/**
 * @author ashiy
 * Clase que maneja la creaci&oacuten de la p&aacutegina que presenta el grafo de actividades de forma gr&aacutefica.
 */
public class FlowChartProducer implements ViewComponentProducer,
NavigationCaseReporter, ViewParamsReporter {
	
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "FlowChart";
	/**
	 * Controla operaciones con Sakai.
	 */
	private SimplePageBean simplePageBean;
	/**
	 * Controla operaciones con Ashyi.
	 */
	private AshyiBean ashyiBean;
	/**
	 * Controla operaciones con la base de datos de Sakai y algunas con la base de datos de Ashyi.
	 */
	private SimplePageToolDao simplePageToolDao;
	/**
	 * Controla operaciones con la base de datos de Ashyi.
	 */
	private AshyiToolDao ashyiToolDao;
	/**
	 * Atributo que viene con el producer.
	 */
	private LessonEntity actividadEntity;

	/**
	 * ItemPlans organizados por objetivos de actividad.
	 */
	LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> itemPlansPorObjetivo;
	/**
	 * ItemPlans remediales organizados por objetivos de actividad.
	 */
	LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> refuerzosPorObjetivo;
	/**
	 * Objetivos de la unidad did&aacutectica.
	 */
	ArrayList<ObjetivosActividad> objetivos;
	/**
	 * Grafo con los identificadores de los itemPlans de la unidad did&aacutectica.
	 */
	Graph<Integer> grafoActividades;
	/**
	 * Grafo con los identificadores de los itemPlans del estudiante.
	 */
	Graph<Integer> grafoEstudiante;
	/**
	 * Atributo que viene con el producer.
	 */
	private static LessonEntity bltiEntity;
	/**
	 * Acceso a los datos de cabecera HTTP
	 */
	private HttpServletRequest httpServletRequest;
	/**
	 * Atributo que viene con el producer.
	 */
	private static Log log = LogFactory.getLog(ShowPageProducer.class);
	/**
	 * MemoryService is the interface for the Sakai Memory service.
	 * This tracks memory users (cachers), runs a periodic garbage collection to keep memory available, and can be asked to report memory usage.
	 */
	private static MemoryService memoryService = (MemoryService) ComponentManager
			.get(MemoryService.class);
	/**
	 * Atributo que viene con el producer.
	 */
	private static Cache urlCache = memoryService
			.newCache("org.sakaiproject.lessonbuildertool.tool.producers.ShowPageProducer.url.cache");
	/**
	 * Core interface supporting lookup of localised messages. Very similar to
	 * Spring's MessageSource
	 */
	public MessageLocator messageLocator;
    
	/**
	 * Necessary since Locale is a final concrete class and cannot be proxied.
	 */
	public LocaleGetter localeGetter;
	/**
	 * Cantidad de refuerzos.
	 */
	private int sizeRefuerzos=0;


	/**
	 * Asigna el simplePageBean.
	 * @param simplePageBean
	 */
	public void setSimplePageBean(SimplePageBean simplePageBean) {
		this.simplePageBean = simplePageBean;
	}
	/**
	 * Asigna el ashyiBean.
	 * @param ashyiBean
	 */
	public void setAshyiBean(AshyiBean ashyiBean) {
		this.ashyiBean = ashyiBean;
	}
	/**
	 * Asigna el ashyiToolDao.
	 * @param dao
	 */
	public void setAshyiToolDao(Object dao) {
		ashyiToolDao = (AshyiToolDao) dao;
	}
	/**
	 * Asigna el simplePageToolDao.
	 * @param dao
	 */
	public void setSimplePageToolDao(Object dao) {
		simplePageToolDao = (SimplePageToolDao) dao;
	}
	
	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.view.ViewIDReporter#getViewID()
	 */
	public String getViewID() {
		return VIEW_ID;
	}
	
	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.view.ComponentProducer#fillComponents(uk.org.ponder.rsf.components.UIContainer, uk.org.ponder.rsf.viewstate.ViewParameters, uk.org.ponder.rsf.view.ComponentChecker)
	 */
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		GeneralViewParameters params = (GeneralViewParameters) viewparams;
		if (((GeneralViewParameters) viewparams).getSendingPage() != -1) {
			// will fail if page not in this site
			// security then depends upon making sure that we only deal with
			// this page
			try {
				ashyiBean.updatePageObject(((GeneralViewParameters) viewparams)
						.getSendingPage());
			} catch (Exception e) {
				System.out.println("FlowChart permission exception " + e);
				return;
			}
		}

		UIOutput.make(tofill, "html")
		.decorate(
				new UIFreeAttributeDecorator("lang", localeGetter.get()
						.getLanguage()))
						.decorate(
								new UIFreeAttributeDecorator("xml:lang", localeGetter
										.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		ashyiBean.setItemId(itemId);
		
		//if (ashyiBean.canEditPage())
		{

			SimplePage page = ashyiBean.getCurrentPage();
			SimplePageItem pageItem = null;
			if (page != null) {
				pageItem = ashyiBean.getCurrentPageItem(params.getItemId());
			}
			// createToolBar(tofill, page, (pageItem.getType() ==
			// SimplePageItem.STUDENT_CONTENT));

			String assignId = null; // default, normally current

			// if itemid is null, we'll append to current page, so it's ok
			if (itemId != null && itemId != -1) {
				SimplePageItem currentItem = ashyiToolDao.findItem(itemId);
				if (currentItem == null)
					return;
				// trying to hack on item not on this page
				if (currentItem.getPageId() != page.getPageId())
					return;
				assignId = currentItem.getSakaiId();
			}

			// UIForm form = UIForm.make(tofill, "flowChartForm");
			SimplePage currentPage = ashyiBean.getCurrentPage();
			ashyiBean.setCurrentPageId(currentPage.getPageId());

			UIInput.make(tofill, "agenteInterface", "#{ashyiBean.agenteInterface}");

			if(ashyiBean.getCurrentUserType().equals("Student"))
			{
				int idActividad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad();
				Usuario idUsuario=ashyiToolDao.getUsuario(ashyiToolDao.getUltimoUsuario(ashyiBean.getCurrentUserId()));
				List<GrafosUsuario> lista=ashyiToolDao.getGrafoUsuario(idUsuario, idActividad);
				if(!lista.isEmpty())
				{
					GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
					for(GrafosUsuario gU : lista)				
						if(gU.isActivo())
						{	
							grafoActivo = gU;
							break;
						}	
					//Consultar si hay un instructor con un grafo para la unidad didactica
					List<GrafosUsuario> listaInstructores = ashyiBean.getInstructorsGraphs(ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad());
					
					if(!listaInstructores.isEmpty())
					{
						grafoEstudiante=cargarGrafoExistente(grafoActivo.getIdGrafo().getIdGrafo());
						grafoActividades=cargarGrafoExistente(listaInstructores.get(0).getIdGrafo().getIdGrafo());
						graphToJsonStudent();
					}
				}
				else
				{
					//mostrar mensaje de error
					System.out.println("===================No existe grafo estudiante y general=====================");
					UIOutput.make(tofill, "errorEstudiante",
							messageLocator.getMessage("flowchart.errorEstudiante"));

				}
			}
			else if(ashyiBean.getCurrentUserType().equals("Instructor"))
			{
				ashyiBean.setAgenteInterface(ashyiBean.addAgent("Editor-"+ashyiBean.getCurrentUserName(), 2, ashyiBean.getCurrentPage().getTitle()));
				//si es la primera vez, pedir actividades a pumas
				System.out.println("Estado del agete: "+ ashyiBean.getAgenteInterface());
				if(ashyiBean.getCurrentPage().getParent() != null)
				{
					int idActividad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad();
					Usuario idUsuario=ashyiToolDao.getUsuario(ashyiToolDao.getUltimoUsuario(ashyiBean.getCurrentUserId()));
					List<GrafosUsuario> lista=ashyiToolDao.getGrafoUsuario(idUsuario, idActividad);
					if(!lista.isEmpty())
					{
						System.out.println("El grafo ya existe");
						//crear grafo a partir de bd
						grafoActividades=cargarGrafoExistente(lista.get(0).getIdGrafo().getIdGrafo());
						System.out.println("Grafo graph del profesor cargado de la base de datos");
						//setear grafo al agente
						ashyiBean.setGrafoUsuarioAg(ashyiBean.getAgenteInterface(), grafoActividades);
						graphToJsonInstructor();
					}
					else
					{
						//Consultar si hay otro instructor con un grafo para la unidad didactica
						List<GrafosUsuario> listaInstructores = ashyiBean.getInstructorsGraphs(ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad());
						
						if(listaInstructores != null)
						{
							if(!listaInstructores.isEmpty())
							{
								System.out.println("El grafo ya existe para otro instructor");
								//crear grafo a partir de bd
								grafoActividades=cargarGrafoExistente(listaInstructores.get(0).getIdGrafo().getIdGrafo());
								System.out.println("Grafo graph del profesor cargado de la base de datos");
								//setear grafo al agente
								ashyiBean.setGrafoUsuarioAg(ashyiBean.getAgenteInterface(), grafoActividades);
								graphToJsonInstructor();
							}
						}
						else
						{
							//3 unidad
							//int idActividad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad();
							if(ashyiBean.getBeanBesa().getActividadesEditor(ashyiBean.getAgenteInterface(), idActividad ).size() == 0)
							{					
								List<List> objActividad = ashyiBean.getIdsObjetivosActividad(3, ashyiBean.getCurrentPage().getTitle());
								List<Object> datosEnviar = new ArrayList<Object>();
								datosEnviar.add(objActividad);
								datosEnviar.add(ashyiBean.getNivelActividades());
								datosEnviar.add(idActividad);
	
								ashyiBean.executeAction(ashyiBean.getAgenteInterface(), ashyiBean.getAgenteComunicacion(), "CreateG", datosEnviar);
								System.out.println("ASHYI, enviado Objetivos de actividad!");
							}	
							int espera = 0;
							UIOutput m = UIOutput.make(tofill, "esperarActividades",messageLocator.getMessage("esperarActividades.editor"));
							while(ashyiBean.getBeanBesa().getActividadesEditor(ashyiBean.getAgenteInterface(),idActividad).size() == 0)
							{
								System.out.println("Esperando Actividades");
								esperar(2);// dormir el hilo
								espera++;
								if (espera > 180000)// si espera mas de 6 minutos, es porque
									// no hay actividades todavia
									break;
							}	
							if (espera <= 180000)// sale sin el break, hay actividades
							{
								espera = 0;
								m.setValue(messageLocator.getMessage("esperarActividades.fuera"));
								ashyiBean.setActividadesMostrar(ashyiBean.getBeanBesa().getActividadesEditor(ashyiBean.getAgenteInterface(),idActividad));
	
								//si hay actividades de refuerzo
								if(ashyiBean.getActividadesRefuerzo(ashyiBean.getNivelActividades()))
								{
									if(ashyiBean.getBeanBesa().getActividadesRefuerzoEditor(ashyiBean.getAgenteInterface(), idActividad).size() == 0)
									{
										List<Object> datosEnviar = new ArrayList<Object>();
										datosEnviar.add(ashyiBean.getNivelActividades());
										datosEnviar.add(idActividad);
										ashyiBean.executeAction(ashyiBean.getAgenteInterface(), ashyiBean.getAgenteComunicacion(), "CreateGRA", datosEnviar);
									}
									while(ashyiBean.getBeanBesa().getActividadesRefuerzoEditor(ashyiBean.getAgenteInterface(), idActividad).size() == 0)
									{
										System.out.println("Esperando Actividades de refuerzo");
									}
									ashyiBean.setActividadesRefuerzoMostrar(ashyiBean.getBeanBesa().getActividadesRefuerzoEditor(ashyiBean.getAgenteInterface(), idActividad));					
								}
								else
								{
									ashyiBean.setActividadesRefuerzoMostrar(new ArrayList<Object>());
								}
	
								//mostrar las actividades segun el dispositivo (grafica o lista)
	
								onReceiveActivities(ashyiBean.getActividadesMostrar(), ashyiBean.getActividadesRefuerzoMostrar(), idActividad);
	
								//ashyiBean.loadExistingFlowChartComplete();
								//ashyiBean.loadExistingActivities();
								//enviar cambios del profesor
							}
						}
					}
				}
			}

			// llenar flowchart con las actividades que ya existen
			UIOutput.make(tofill, "existingFlowChartLabel", "",
					"#{ashyiBean.existingFlowChart}");
			UIOutput.make(tofill, "existingActivities", "",
					"#{ashyiBean.existingActivities}");

			UIForm form2 = UIForm.make(tofill, "connectionsForm");
			UIInput.make(form2, "connectionText",
					"#{ashyiBean.lastConnection}", "");
			UICommand x = UICommand.make(form2, "saveConnection",
					"Save Connection", "#{ashyiBean.saveConnection}");

			UIForm form3 = UIForm.make(tofill, "activitiesForm");
			UIInput.make(form3, "itemPlansText",
					"#{ashyiBean.itemPlansStyle}", "");
			UICommand y = UICommand.make(form3, "itemPlansUpdate",
					"Update Item Plan", "#{ashyiBean.updateItemPlan}");

			FlowChartProducer flowChartProducer = this;
			UIForm form4 = UIForm.make(tofill, "editActivityForm");
			UIInput.make(form4, "currentActivityToEdit",
					"#{ashyiBean.currentActivityToEdit}", "");
			
			GeneralViewParameters viewAct;
			if(ashyiBean.getCurrentUserType().equals("Instructor"))
			{
				viewAct = new GeneralViewParameters(ActividadPickerProducer.VIEW_ID);
				viewAct.setSendingPage(Long.valueOf(3));
				viewAct.setTipoActividad("1");
			}
			else
			{
				viewAct = new GeneralViewParameters(VistaActividadProducer.VIEW_ID);
				viewAct.setReturnView(VIEW_ID);	
				viewAct.setIdItemUAshyi(0);
				viewAct.setIdItemPAshyi(0);
				//viewAct.setIdActividad(actividad.getIdActividad());
				viewAct.setSendingPage(currentPage.getPageId());
				SimplePageItem i = new SimplePageItemImpl();
				viewAct.setSource(i.getItemURL(
						simplePageBean.getCurrentSiteId(),
						currentPage.getOwner()));
				viewAct.setItemId((long)0);
				//viewAct.setItemId(ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getId());
			}
			UIInternalLink.make(form4, "nuevaActividad-link",
					"Nueva Actividad", viewAct);
			UIForm formC = UIForm.make(tofill, "formC");
			UICommand.make(formC, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		}
	}
	
	/**
	 * Carga el grafo existente en la base de datos.
	 * @param idGrafo
	 * @return Graph - el grafo creado a partir de la informaci&oacuten existente.
	 */
	private Graph cargarGrafoExistente(int idGrafo) {
		List<GrafoRelaciones> relaciones=ashyiToolDao.getGrafoRelaciones(idGrafo);
		Graph<Integer> grafo=new Graph<Integer>();
		System.out.println("En cargar grafo existente numero de relaciones: "+relaciones.size());
		for (int i = 0; i < relaciones.size(); i++) {
			GrafoRelaciones rel=relaciones.get(i);
			grafo.addEdge(rel.getIdItemPlan_Origen(), rel.getIdItemPlan_Destino(), 1.0, rel.getOrden());
			//System.out.println("Edge: "+rel.getIdItemPlan_Origen()+" y "+rel.getIdItemPlan_Destino());
		}
		return grafo;
	}
	
	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.viewstate.ViewParamsReporter#getViewParameters()
	 */
	public ViewParameters getViewParameters() {
		return new GeneralViewParameters();
	}

	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter#reportNavigationCases()
	 */
	public List reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>();
		togo.add(new NavigationCase("success", new SimpleViewParameters(
				ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(
				ActividadPickerProducer.VIEW_ID)));
		GeneralViewParameters params = new GeneralViewParameters(ShowPageProducer.VIEW_ID);
		params.setIdItemPAshyi(0);
		togo.add(new NavigationCase("cancel", params));		
		togo.add(new NavigationCase("successGraphEdition", new SimpleViewParameters(
				ObjetivosEditProducer.VIEW_ID)));
		return togo;
	}

	/**
	 * Retorna la URL oficial de la p&aacutegina.
	 * @return String - la URL.
	 */
	public String myUrl() {
		// previously we computed something, but this will give us the official
		// one
		return ServerConfigurationService.getServerUrl();
	}
	

	/**
	 * Se ejecuta cuando no existe un grafo para la unidad did&aacutectica y se reciben actividades para crearlo.
	 * @param lista - actividades normales.
	 * @param refuerzoList - actividades remediales.
	 * @param idActividad - identificador de la unidad did&aacutectica.
	 */
	public void onReceiveActivities(List<Actividad> lista, List<Actividad> refuerzoList, int idActividad) {
		List<Integer> idItemsUltimoObjetivo = new ArrayList<Integer>();
		ObjetivosActividad objetivoActual, objetivoActualR, ultimoObjetivo=new ObjetivosActividadImpl();
		this.itemPlansPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		//this.refuerzos=new ArrayList<ItemPlan>();
		refuerzosPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		this.objetivos=new ArrayList<ObjetivosActividad>();
		Actividad inicial=ashyiToolDao.getActividadInicial();
		//System.out.println("Actividad inicial: "+inicial.getNombre()+" id: "+inicial.getIdActividad());
		objetivoActual = ashyiBean.getObjetivosActividad(inicial.getNombre(), inicial.getNivel_recursividad()).get(0);
		System.out.println("Objetivo inicial: "+objetivoActual.getIdObjetivo().getNombre()+" id: "+objetivoActual.getIdObjetivo().getIdObjetivo());
		LinkedList<Actividad> list=new LinkedList<Actividad>();
		list.add(inicial);
		for (int i = 0; i < lista.size(); i++) {
			list.add(lista.get(i));
		}
		//==================================a&ntildeadir las actividades originales al mapa==================================
		if (list != null && !list.isEmpty()) {
			objetivoActual = ashyiBean.getObjetivosActividad(list.get(0).getNombre(), list.get(0).getNivel_recursividad()).get(0);
			objetivos.add(objetivoActual);
			int orden=1;
			for (int i = 0; i < list.size(); i++) {
				Actividad auxActividad = list.get(i);
				ObjetivosActividad ObjetivoAc = ashyiBean.getObjetivosActividad(auxActividad.getNombre(),auxActividad.getNivel_recursividad()).get(0);
				if (ObjetivoAc.getIdObjetivo().getIdObjetivo() != 
						objetivoActual.getIdObjetivo().getIdObjetivo())
				{
					objetivoActual = ObjetivoAc;
					objetivos.add(ObjetivoAc);
					orden+=2;
				}

				List<RecursosActividad> actividadRecurso = ashyiToolDao.actividadesRecurso(simplePageToolDao.getItems(), auxActividad);

				if(actividadRecurso.size() > 0)
				{
					for(RecursosActividad ac : actividadRecurso)
					{
						Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
						auxActividad = ac.getIdActividad();
						Recurso auxRecurso = ac.getIdRecurso();
						//nuevo item plan activo
						boolean estaItem = ashyiBean.itemEsta(ud, auxActividad, auxRecurso, 1, false, orden);
						ItemPlan itemPlan = new ItemPlanImpl();
						itemPlan.setOrden(orden);
						if(!estaItem)
						{
							itemPlan = ashyiBean.addItemPlan(ud, auxActividad, auxRecurso, true, orden);
						}
						else
						{
							itemPlan = ashyiBean.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
						}
						if(!itemPlansPorObjetivo.containsKey(objetivoActual))
							itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
						itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
					}
				}else
				{				
					Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);

					//nuevo item plan activo sin recurso
					boolean estaItem = ashyiBean.itemEsta(ud, auxActividad, new RecursoImpl(), 2, false, orden);
					ItemPlan itemPlan = new ItemPlanImpl();
					if(!estaItem)
					{
						itemPlan = ashyiBean.addItemPlan(ud, auxActividad, true, orden);
					}
					else
					{
						itemPlan = ashyiBean.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
					}
					if(!itemPlansPorObjetivo.containsKey(objetivoActual))
						itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
					itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
				}
			}
			ultimoObjetivo=objetivoActual;		
		}

		int numObjetivos=itemPlansPorObjetivo.keySet().size();
		//System.out.println("!!!!!!!!!!!!!!!1 : "+numObjetivos);
		//==================================a&ntildeadir las de refuerzo al mapa==================================
		int orden = 2;
		if(refuerzoList.size()>0)
		{			
			for(int j = 0; j <objetivos.size()-1; j++)
			{
				objetivoActual = objetivos.get(j);
				objetivoActualR = ashyiBean.getObjetivosActividad(refuerzoList.get(0).getNombre(), refuerzoList.get(0).getNivel_recursividad()).get(0);
				for (int i = 0; i < refuerzoList.size(); i++) {
					Actividad auxActividad = refuerzoList.get(i);
					List<RecursosActividad> actividadRecurso = ashyiToolDao.actividadesRecurso(simplePageToolDao.getItems(), auxActividad);

					if(actividadRecurso.size() > 0)
					{
						for(RecursosActividad ac : actividadRecurso)
						{
							Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
							auxActividad = ac.getIdActividad();
							Recurso auxRecurso = ac.getIdRecurso();
							//nuevo item plan activo
							//1, con recurso y true de refuerzo
							boolean estaItem = ashyiBean.itemEsta(ud, auxActividad, auxRecurso, 1, true, orden);
							ItemPlan itemPlan = new ItemPlanImpl();
							if(!estaItem)
							{
								itemPlan = ashyiBean.addItemPlan(ud, auxActividad, auxRecurso, true, orden);
							}
							else
							{
								itemPlan = ashyiBean.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
							}
							if(!refuerzosPorObjetivo.containsKey(objetivoActual))
								refuerzosPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
							refuerzosPorObjetivo.get(objetivoActual).add(itemPlan);
						}
						//las de refuerzo
					}else
					{	
						Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
						//nuevo item plan activo sin recurso
						//2, sin recurso y true de refuerzo
						boolean estaItem = ashyiBean.itemEsta(ud, auxActividad, new RecursoImpl(), 2, true, orden);
						ItemPlan itemPlan = new ItemPlanImpl();
						if(!estaItem)
						{
							itemPlan = ashyiBean.addItemPlan(ud, auxActividad, true, orden);
							itemPlan.setIdItemPlan(ashyiBean.getUltimoItemPlan());		
						}
						else
						{
							itemPlan = ashyiBean.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
						}
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

		Iterator <ItemPlan> it2=this.itemPlansPorObjetivo.get(ultimoObjetivo).iterator();
		while(it2.hasNext())
			idItemsUltimoObjetivo.add(it2.next().getIdItemPlan());

		List<Object> datosEnviar = new ArrayList<Object>();

		datosEnviar.add(grafoActividades);
		datosEnviar.add(idItemsUltimoObjetivo);
		datosEnviar.add(idActividad);
//		Set<GraphEdge> h = grafoActividades.getEdges();
//		for(GraphEdge idItem: h)
//		{
//			System.out.println("!!!!!!!!!!!!!! 3FlC: "+idItem+ " orden: "+idItem.getOrder());
//		}

		//=======================================enviar grafo a gente editor======================= 
		ashyiBean.executeAction(ashyiBean.getAgenteInterface(), ashyiBean.getAgenteComunicacion(), "Graph", datosEnviar);

		// Obtener el grafo para pintar
		graphToJsonInstructor();
	}

	/**
	 * Genera el objeto JSON de actividades y conexiones para el usuario tipo instructor.
	 */
	private void graphToJsonInstructor() {
		loadActivitiesInstructor();
		loadFlowChart();
	}
	/**
	 * Genera el objeto JSON de actividades y conexiones para el usuario tipo estudiante.
	 */
	private void graphToJsonStudent() {
		loadActivitiesStudent();
		loadFlowChartStudent();
	}

	/**
	 * Crea el objeto JSON de conexiones a partir del grafo de la unidad did&aacutectica.
	 */
	private void loadFlowChart() {
		StringBuilder fcString = new StringBuilder();
		fcString.append("{\"user\":\"p\",\"connections\":[");
		Set<GraphEdge> set=grafoActividades.getEdges();
		Iterator <GraphEdge> itEdge=set.iterator();
		while (itEdge.hasNext()) {
			GraphEdge edge=itEdge.next();
			fcString.append("{\"source\":\"");
			fcString.append(edge.getOrigin());
			fcString.append("\",\"target\":\"");
			fcString.append(edge.getDestination());
			fcString.append("\",\"order\":\"");
			fcString.append(edge.getOrder());
			fcString.append("\"},");
		}
		if (fcString.charAt(fcString.length() - 1) == ',')
			fcString.deleteCharAt(fcString.length() - 1);
		fcString.append("]}");
		ashyiBean.setExistingFlowChart(fcString.toString());
		//System.out.println(ashyiBean.getExistingFlowChart());
	}
	/**
	 * Crea el objeto JSON de conexiones a partir del grafo del estudiante.
	 */
	private void loadFlowChartStudent() {
		StringBuilder fcString = new StringBuilder();
		fcString.append("{\"user\":\"s\",\"connections\":[");
		Set<GraphEdge> set=grafoEstudiante.getEdges();
		Iterator <GraphEdge> itEdge=set.iterator();
		while (itEdge.hasNext()) {
			GraphEdge edge=itEdge.next();
			fcString.append("{\"source\":\"");
			fcString.append(edge.getOrigin());
			fcString.append("\",\"target\":\"");
			fcString.append(edge.getDestination());
			fcString.append("\",\"order\":\"");
			fcString.append(edge.getOrder());
			fcString.append("\"},");
		}
		if (fcString.charAt(fcString.length() - 1) == ',')
			fcString.deleteCharAt(fcString.length() - 1);
		fcString.append("]}");
		ashyiBean.setExistingFlowChart(fcString.toString());
		//System.out.println(ashyiBean.getExistingFlowChart());
	}
	/**
	 * Crea el objeto JSON de actividades para el usuario de tipo instructor, este objeto se crea a partir del grafo de la unidad did&aacutectica.
	 * Esta es la funci&oacuten que se usa actualmente.
	 */
	private void loadActivitiesInstructor() {
		Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
		List<ItemPlan> itemPlans=ashyiToolDao.getItemPlansUD(ud);
		StringBuilder fcString = new StringBuilder();
		fcString.append("{\"user\":\"p\",\"activitiesByObj\":[");

		List<ItemPlan> actividades=new ArrayList<ItemPlan>();
		List<ItemPlan> refuerzos=new ArrayList<ItemPlan>();
		for(ItemPlan ip: itemPlans)
		{
			if(ip.getOrden()%2==1)
				actividades.add(ip);
			else
				refuerzos.add(ip);
		}
		int orden=-1;
		for (int i = 0; i < actividades.size(); i++) {
			ItemPlan ip=actividades.get(i);
			if(ip.getOrden()!=orden)
			{
				if(i>0)
				{
					if (fcString.charAt(fcString.length() - 1) == ',')
						fcString.deleteCharAt(fcString.length() - 1);
					fcString.append("]");

					fcString.append(",\"activitiesRefuerzo\":[");
					orden++;
					for (int j = 0; j < refuerzos.size(); j++)
					{
						if(refuerzos.get(j).getOrden()==orden)
						{
							ItemPlan ref=refuerzos.get(j);
							fcString.append("{\"id\":\"");
							fcString.append(ref.getIdItemPlan());
							fcString.append("\",\"name\":\"");
							fcString.append(ref.getIdActividad().getNombre());
							fcString.append("\",\"idActividad\":\"");
							fcString.append(ref.getIdActividad().getIdActividad());
							fcString.append("\",\"type\":\"");
							fcString.append(ref.getIdActividad().getIdTipo().getNombre());
							fcString.append("\",\"status\":\"");
							if(ref.isEstaActivo())
								fcString.append("1");
							else
								fcString.append("0");
							fcString.append("\"},");
						}
					}
					if (fcString.charAt(fcString.length() - 1) == ',')
						fcString.deleteCharAt(fcString.length() - 1);
					fcString.append("]},");
				}

				orden=ip.getOrden();
				Integer idObjetivo=ashyiToolDao.getObjetivosActividad(ip.getIdActividad().getIdActividad())
						.get(0).getIdObjetivo().getIdObjetivo();
				Objetivo obj=ashyiToolDao.getObjetivoPorId(idObjetivo);
				if(!obj.getNombre().equalsIgnoreCase("Does not exist"))
				{
					fcString.append("{\"id\":\"");
					fcString.append(idObjetivo);
					fcString.append("\",\"name\":\"");
					fcString.append(obj.getNombre());
					fcString.append("\",\"activities\":[");
				}
				//System.out.println("Cambiando de objetivo");
			}
			fcString.append("{\"id\":\"");
			fcString.append(ip.getIdItemPlan());
			fcString.append("\",\"name\":\"");
			fcString.append(ip.getIdActividad().getNombre());
			fcString.append("\",\"idActividad\":\"");
			fcString.append(ip.getIdActividad().getIdActividad());
			fcString.append("\",\"type\":\"");
			if(ip.getIdActividad().getNombre().equalsIgnoreCase("Actividad de inicio")
					&&ip.getIdActividad().getNivel().equalsIgnoreCase("InicioUD"))
				fcString.append("actividadInicio");
			else
				fcString.append(ip.getIdActividad().getIdTipo().getNombre());
			fcString.append("\",\"status\":\"");
			if(ip.isEstaActivo())
				fcString.append("1");
			else
				fcString.append("0");
			fcString.append("\"},");
		}
		if (fcString.charAt(fcString.length() - 1) == ',')
			fcString.deleteCharAt(fcString.length() - 1);
		fcString.append("]}]}");
		System.out.println(fcString.toString());
		ashyiBean.setExistingActivities(fcString.toString());
	}
	/**
	 * Crea el objeto JSON de actividades para el usuario de tipo instructor, este objeto se crea a partir del grafo de la unidad did&aacutectica.
	 * Esta es una funci&oacuten que no se usa actualmente pues est&aacute desactualizada.
	 */
	private void loadActivitiesInstructor2() {
		Map<Integer, LinkedHashSet<Integer> > itemsPorOrden=new LinkedHashMap<Integer, LinkedHashSet<Integer >>();
		Set<GraphEdge> set=new LinkedHashSet<GraphEdge>();
		set=grafoActividades.getEdges();
		Iterator <GraphEdge> itEdge=set.iterator();
		Set<Integer> agregados=new LinkedHashSet<Integer>();

		//organizar actividades por orden
		while(itEdge.hasNext())
		{
			GraphEdge<Integer> edge=itEdge.next();
			int orden=edge.getOrder();
			if(!itemsPorOrden.containsKey(orden))
				itemsPorOrden.put(orden, new LinkedHashSet<Integer>());
			itemsPorOrden.get(orden).add(edge.getOrigin());
			agregados.add(edge.getOrigin());
		}


		int ordenMayor=grafoActividades.getOrdenMayor()+1;
		Iterator <Integer> vertices=grafoActividades.getVertices().iterator();
		itemsPorOrden.put(ordenMayor, new LinkedHashSet<Integer>());
		while(vertices.hasNext())
		{
			Integer vertice=vertices.next();
			if(!agregados.contains(vertice))
			{
				itemsPorOrden.get(ordenMayor).add(vertice);
				//System.out.println("Vertice agregado a orden "+ordenMayor+": "+vertice);
			}
		}

		Iterator<Integer> it=itemsPorOrden.keySet().iterator();
		while(it.hasNext())
		{
			int orden=it.next();
			Iterator<Integer> it2=itemsPorOrden.get(orden).iterator();
		}

		//armar json de actividades por objetivo
		StringBuilder fcString = new StringBuilder();
		fcString.append("{\"user\":\"p\",\"activitiesByObj\":[");

		String [] objetivosActividad=ashyiToolDao.getObjetivosActividad(3, ashyiBean.getCurrentPage().getTitle());

		for (int i = 1; i <= ordenMayor; i+=2) {
			Iterator <Integer> iteratorItemsObj=itemsPorOrden.get(i).iterator();
			int j=0;
			while(iteratorItemsObj.hasNext()) {
				ItemPlan itemPlan=ashyiToolDao.getItemPlan(iteratorItemsObj.next());
				if(j==0)
				{
					Integer idObjetivo=ashyiToolDao.getObjetivosActividad(itemPlan.getIdActividad().getIdActividad())
							.get(0).getIdObjetivo().getIdObjetivo();

					Objetivo obj=ashyiToolDao.getObjetivoPorId(idObjetivo);
					if(!obj.getNombre().equalsIgnoreCase("Does not exist"))
					{

						fcString.append("{\"id\":\"");
						fcString.append(idObjetivo);
						fcString.append("\",\"name\":\"");
						fcString.append(obj.getNombre());
						fcString.append("\",\"activities\":[");
						j=1;
					}
				}
				fcString.append("{\"id\":\"");
				fcString.append(itemPlan.getIdItemPlan());
				fcString.append("\",\"name\":\"");
				fcString.append(itemPlan.getIdActividad().getNombre());
				fcString.append("\",\"idActividad\":\"");
				fcString.append(itemPlan.getIdActividad().getIdActividad());
				fcString.append("\",\"type\":\"");
				fcString.append(itemPlan.getIdActividad().getIdTipo().getNombre());
				fcString.append("\"},");
			}
			j=0;
			if (fcString.charAt(fcString.length() - 1) == ',')
				fcString.deleteCharAt(fcString.length() - 1);

			//actividades de refuerzo
			fcString.append("]");
			if(i<ordenMayor)
			{
				fcString.append(",\"activitiesRefuerzo\":[");
				iteratorItemsObj=itemsPorOrden.get(i+1).iterator();
				//System.out.println("Items por orden "+(i+1)+ ": "+itemsPorOrden.get(i+1).size());
				while(iteratorItemsObj.hasNext()) {
					ItemPlan itemPlan=ashyiToolDao.getItemPlan(iteratorItemsObj.next());
					fcString.append("{\"id\":\"");
					fcString.append(itemPlan.getIdItemPlan());
					fcString.append("\",\"name\":\"");
					fcString.append(itemPlan.getIdActividad().getNombre());
					fcString.append("\",\"idActividad\":\"");
					fcString.append(itemPlan.getIdActividad().getIdActividad());
					fcString.append("\",\"type\":\"");
					fcString.append(itemPlan.getIdActividad().getIdTipo().getNombre());
					fcString.append("\"},");
				}
				if (fcString.charAt(fcString.length() - 1) == ',')
					fcString.deleteCharAt(fcString.length() - 1);
				fcString.append("]");
			}
			fcString.append("},");
		}
		if (fcString.charAt(fcString.length() - 1) == ',')
			fcString.deleteCharAt(fcString.length() - 1);
		fcString.append("]}");
		//System.out.println(fcString.toString());
		ashyiBean.setExistingActivities(fcString.toString());
	}

	/**
	 * Genera el objeto JSON de actividades para el usuario de tipo estudiante.
	 */
	private void loadActivitiesStudent() {
		Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
		Usuario idUsuario=ashyiToolDao.getUsuario(ashyiToolDao.getUltimoUsuario(ashyiBean.getCurrentUserId()));
		List<ItemPlan> itemPlans=ashyiToolDao.getItemPlansUD(ud);
		StringBuilder fcString = new StringBuilder();
		fcString.append("{\"user\":\"s\",\"activitiesByObj\":[");

		List<ItemPlan> actividades=new ArrayList<ItemPlan>();
		List<ItemPlan> refuerzos=new ArrayList<ItemPlan>();
		for(ItemPlan ip: itemPlans)
		{
			if(ip.getOrden()%2==1)
				actividades.add(ip);
			else
				refuerzos.add(ip);
		}
		int orden=-1;
		
		for (int i = 0; i < actividades.size(); i++) {
			ItemPlan ip=actividades.get(i);
			if(ip.getOrden()!=orden)
			{
				if(i>0)
				{
					if (fcString.charAt(fcString.length() - 1) == ',')
						fcString.deleteCharAt(fcString.length() - 1);
					fcString.append("]");

					fcString.append(",\"activitiesRefuerzo\":[");
					orden++;
					for (int j = 0; j < refuerzos.size(); j++)
					{
						if(refuerzos.get(j).getOrden()==orden)
						{
							ItemPlan ref=refuerzos.get(j);
							fcString.append("{\"id\":\"");
							fcString.append(ref.getIdItemPlan());
							fcString.append("\",\"name\":\"");
							fcString.append(ref.getIdActividad().getNombre());
							fcString.append("\",\"idActividad\":\"");
							fcString.append(ref.getIdActividad().getIdActividad());
							fcString.append("\",\"type\":\"");
							fcString.append(ref.getIdActividad().getIdTipo().getNombre());
							fcString.append("\",\"status\":\"");
							if(ref.isEstaActivo())
								fcString.append("1");
							else
								fcString.append("0");
							fcString.append("\",\"student\":\"");
							Integer status=ashyiToolDao.getEstadoItemsUsuario(ref.getIdItemPlan(), idUsuario.getIdUsuario());
							if(grafoEstudiante.getGraph().containsKey(ref.getIdItemPlan()))
							{
								fcString.append("1");
								fcString.append("\",\"IdItemUAshyi\":\"");
								fcString.append(idUsuario.getIdUsuario());
								fcString.append("\",\"IdItemPAshyi\":\"");
								fcString.append(ref.getIdItemPlan());
								fcString.append("\",\"ItemId\":\"");
								if(ref.getIdRecurso()!=null)
									fcString.append(ref.getIdRecurso().getIdItemSakai());
								else
									fcString.append("0");
							}
							else
							{
								fcString.append("0");
								status=5;
							}
							fcString.append("\",\"activityStatus\":\"");
							fcString.append(status);
							fcString.append("\"},");
						}
					}
					if (fcString.charAt(fcString.length() - 1) == ',')
						fcString.deleteCharAt(fcString.length() - 1);
					fcString.append("]},");
				}

				orden=ip.getOrden();
				Integer idObjetivo=ashyiToolDao.getObjetivosActividad(ip.getIdActividad().getIdActividad())
						.get(0).getIdObjetivo().getIdObjetivo();
				Objetivo obj=ashyiToolDao.getObjetivoPorId(idObjetivo);
				if(!obj.getNombre().equalsIgnoreCase("Does not exist"))
				{
					fcString.append("{\"id\":\"");
					fcString.append(idObjetivo);
					fcString.append("\",\"name\":\"");
					fcString.append(obj.getNombre());
					fcString.append("\",\"activities\":[");
				}
				//System.out.println("Cambiando de objetivo");
			}
			fcString.append("{\"id\":\"");
			fcString.append(ip.getIdItemPlan());
			fcString.append("\",\"name\":\"");
			fcString.append(ip.getIdActividad().getNombre());
			fcString.append("\",\"idActividad\":\"");
			fcString.append(ip.getIdActividad().getIdActividad());
			fcString.append("\",\"type\":\"");
			if(ip.getIdActividad().getNombre().equalsIgnoreCase("Actividad de inicio")
					&&ip.getIdActividad().getNivel().equalsIgnoreCase("InicioUD"))
				fcString.append("actividadInicio");
			else
				fcString.append(ip.getIdActividad().getIdTipo().getNombre());
			fcString.append("\",\"status\":\"");
			if(ip.isEstaActivo())
				fcString.append("1");
			else
				fcString.append("0");
			fcString.append("\",\"student\":\"");
			Integer status=ashyiToolDao.getEstadoItemsUsuario(ip.getIdItemPlan(), idUsuario.getIdUsuario());
			if(grafoEstudiante.getGraph().containsKey(ip.getIdItemPlan()))
			{
				fcString.append("1");
				fcString.append("\",\"IdItemUAshyi\":\"");
				fcString.append(idUsuario.getIdUsuario());
				fcString.append("\",\"IdItemPAshyi\":\"");
				fcString.append(ip.getIdItemPlan());
				fcString.append("\",\"ItemId\":\"");
				if(ip.getIdRecurso()!=null)
					fcString.append(ip.getIdRecurso().getIdItemSakai());
				else
					fcString.append("0");
			}
			else
			{
				fcString.append("0");
				status=5;
			}
			fcString.append("\",\"activityStatus\":\"");
			fcString.append(status);
			fcString.append("\"},");
		}
		if (fcString.charAt(fcString.length() - 1) == ',')
			fcString.deleteCharAt(fcString.length() - 1);
		fcString.append("]}]}");
		System.out.println(fcString.toString());
		ashyiBean.setExistingActivities(fcString.toString());
	}

	/**
	 * Espera una cantidad de segundos pasada por par&aacutemetro.
	 * @param segundos
	 */
	public void esperar(int segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (Exception e) {
			// Mensaje en caso de que falle
		}
	}

}
