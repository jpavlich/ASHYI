package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.ItemPlanImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
/**
 * @author ASHYI
 * Productor de editor de actividades
 * Lista las actividades disponibles en la unidad didactica
 * para habilitarlas/deshabilitarlas e ingresar a las caravteristicas de cada una
 */
public class EditarActividadesProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "EditarActividades";
	/**
	 * Bean de sakai
	 */
	private SimplePageBean simplePageBean;
	/**
	 * Bean de ASHYI, puente de comunicacion
	 */
	private AshyiBean ashyiBean;
	/**
	 * Acceso a datos de sakai
	 */
	private SimplePageToolDao simplePageToolDao;
	/**
	 *  Acceso a datos de ASHYI
	 */
	private AshyiToolDao ashyiToolDao;
	/**
	 * Acceso a los datos de cabecera HTTP
	 */
	private HttpServletRequest httpServletRequest;
	/**
	 * Acceso a los datos de cabecera HTTP
	 */
	private HttpServletResponse httpServletResponse;
    // have to do it here because we need it in urlCache. It has to happen before Spring initialization
	/**
	 * MemoryService is the interface for the Sakai Memory service.
	 * This tracks memory users (cachers), runs a periodic garbage collection to keep memory available, and can be asked to report memory usage.
	 */
	private static MemoryService memoryService = (MemoryService)ComponentManager.get(MemoryService.class);
	/**
	 * The interface to a family of evolvers for text controls, with the same
	 * binding structure as UIInput
	 */
	public TextInputEvolver richTextEvolver;
	//private LessonEntity assignmentEntity;
	
	private static Log log = LogFactory.getLog(ShowPageProducer.class);
	private static Cache urlCache = memoryService.newCache("org.sakaiproject.lessonbuildertool.tool.producers.ShowPageProducer.url.cache");
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
	 * Cambiar el servlet http
	 * @param httpServletResponse servlet http
	 */
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}
	

	/**
	 * Cambiar el servlet http
	 * @param httpServletRequest servlet http
	 */
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	
	/**
     * Cambiar bean de ASHYI 
     * @param ashyiBean Bean de comunicacion con ASHYI
     */
    public void setAshyiBean(AshyiBean ashyiBean) {
		this.ashyiBean = ashyiBean;
	}
    /**
	 * @param simplePageBean Bean de comunicacion con Sakai
	 */
	public void setSimplePageBean(SimplePageBean simplePageBean) {
		this.simplePageBean = simplePageBean;
	}
	
	public SimplePageToolDao getSimplePageToolDao() {
		return simplePageToolDao;
	}
	/**
	 * @param dao objeto de acceso a datos de Sakai
	 */
	public void setSimplePageToolDao(SimplePageToolDao simplePageToolDao) {
		this.simplePageToolDao = simplePageToolDao;
	}
	public SimplePageBean getSimplePageBean() {
		return simplePageBean;
	}
	/**
	 * @param dao objeto de acceso a datos de ASHYI
	 */
	public void setAshyiToolDao(Object dao) {
		ashyiToolDao = (AshyiToolDao) dao;
	}
	/**
	 * @param dao objeto de acceso a datos de Sakai
	 */
	public void setSimplePageToolDao(Object dao) {
		simplePageToolDao = (SimplePageToolDao) dao;
	}
    
//    public void setAssignmentEntity(LessonEntity l) {
//		assignmentEntity = l;
//	}

	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.view.ViewIDReporter#getViewID()
	 */
	public String getViewID() {
		return VIEW_ID;
	}

	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.view.ComponentProducer#fillComponents(uk.org.ponder.rsf.components.UIContainer, uk.org.ponder.rsf.viewstate.ViewParameters, uk.org.ponder.rsf.view.ComponentChecker)
	 */
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		GeneralViewParameters params = (GeneralViewParameters) viewparams;
		
       UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
			    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();
		
		ashyiBean.setItemId(itemId);
		UIOutput.make(tofill, "editarActividades").decorate(new UIFreeAttributeDecorator("title", messageLocator.getMessage("ashyiBean.listaActividades")));
		UIForm form = UIForm.make(tofill, "editarActividades-form");
		
		//desHabilitar actividad
		if(params.getIdItemPAshyi() != -1)
		{
			boolean rta = ashyiBean.desHabilitarActividad(params.getIdActividad(), params.getIdItemPAshyi());
			if(!rta)
				UIOutput.make(form, "error",messageLocator.getMessage("ashyiBean.errorHD"));
			else
			{
				//guardar en agente
				Actividad unidad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
//				List datos = new ArrayList();
//				datos.add(params.getIdActividad());
//				datos.add(unidad.getIdActividad());
				ashyiBean.getBeanBesa();
				ashyiBean.executeAction(ashyiBean.getAgenteComunicacion(), "", "ACTIVITY_DH", unidad.getIdActividad());				
			}
		}		

		
		SimplePage currentPage = ashyiBean.getCurrentPage();
		
		ashyiBean.setCurrentPageId(currentPage.getPageId());					
					    
		Actividad unidad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
		List<ActividadTieneActividad> actividades = ashyiToolDao.getActividadTieneActividades(unidad.getIdActividad());
		String[] listaActividades = simplePageBean.getNombresActividades(actividades);
		if(listaActividades.length > 0)
		{
			//listado de actividades
			UISelect selectOU = UISelect.makeMultiple(form, "actividadesSpan", listaActividades,
					"#{simplePageBean.objetivosSeleccionados}", null);		
			UIOutput.make(form, "actividades-label",messageLocator.getMessage("ashyiBean.listaActividades"));
			
			for (int i = 0; i < listaActividades.length; i++) {

				UIBranchContainer row = UIBranchContainer.make(form, "actividad:");

				GeneralViewParameters viewObj = new GeneralViewParameters(ActividadPickerProducer.VIEW_ID);
				viewObj.setReturnView(VIEW_ID);	
				viewObj.setIdActividad(actividades.get(i).getIdActividadSiguienteNivel().getIdActividad());
				
				UIInternalLink.make(row, "actividad-link",listaActividades[i], viewObj).decorate(new UIFreeAttributeDecorator("title",listaActividades[i]));
				
				UIOutput.make(row, "link-textactividad", listaActividades[i]);
			}
			
			UISelect selectEditar = UISelect.makeMultiple(form, "botonesSpan", listaActividades,
					"#{simplePageBean.objetivosSeleccionados}", null);		
			UIOutput.make(form, "botones-label",messageLocator.getMessage("ashyiBean.listaActividadesOP"));
			
			//listado de estado (habilidata / deshabilitada)
			for (int i = 0; i < listaActividades.length; i++) {

				UIBranchContainer row = UIBranchContainer.make(form, "botones:");
				
				GeneralViewParameters viewObj = new GeneralViewParameters(EditarActividadesProducer.VIEW_ID);
				viewObj.setReturnView(VIEW_ID);
				viewObj.setIdActividad(actividades.get(i).getIdActividadSiguienteNivel().getIdActividad());
								
				
				List<ItemPlan> iPs = ashyiToolDao.getItemsPlanActividad(actividades.get(i).getIdActividadSiguienteNivel().getIdActividad());
											
				if(iPs == null)
				{
					//List<RecursosActividad> actividadRecurso = ashyiToolDao.getRecursosActividad(actividades.get(i).getIdActividadSiguienteNivel().getIdActividad());
					List<RecursosActividad> actividadRecurso = ashyiToolDao.actividadesRecurso(simplePageToolDao.getItems(), actividades.get(i).getIdActividadSiguienteNivel());
					
					if(actividadRecurso.size() > 0)
					{
						for(RecursosActividad ac : actividadRecurso)
						{
							Recurso auxRecurso = ac.getIdRecurso();
							if(auxRecurso.getIdItemSakai() != null)
							{
								ItemPlan itemPlan = ashyiBean.addItemPlan(unidad, actividades.get(i).getIdActividadSiguienteNivel(), auxRecurso, true, -1);
							}							
						}
						
						iPs = ashyiToolDao.getItemsPlanActividad(actividades.get(i).getIdActividadSiguienteNivel().getIdActividad());
					}					
				}
				
				if(iPs.get(0).isEstaActivo())			
				{
					viewObj.setIdItemPAshyi(1);	
					UIInternalLink.make(row, "habilitar-link",messageLocator.getMessage("ashyiBean.deshabilitarActividad"), viewObj).decorate(new UIFreeAttributeDecorator("title",messageLocator.getMessage("ashyiBean.deshabilitarActividad")));
					UIOutput.make(row, "link-textEactividad", messageLocator.getMessage("ashyiBean.deshabilitarActividad"));
				}
				else
				{
					viewObj.setIdItemPAshyi(2);	
					UIInternalLink.make(row, "habilitar-link",messageLocator.getMessage("ashyiBean.habilitarActividad"), viewObj).decorate(new UIFreeAttributeDecorator("title",messageLocator.getMessage("ashyiBean.habilitarActividad")));
					UIOutput.make(row, "link-textEactividad", messageLocator.getMessage("ashyiBean.habilitarActividad"));
				}			
				
				//UICommand.make(row, "habilitar", messageLocator.getMessage("ashyiBean.habilitarActividad"), "#{ashyiBean.habilitarActividad}");
			}
		}

		UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");			
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
		GeneralViewParameters params = new GeneralViewParameters(ShowPageProducer.VIEW_ID);
		params.setIdItemPAshyi(0);
		togo.add(new NavigationCase("success", params));
		//togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(EditarActividadesProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", params));
		return togo;
	}
}