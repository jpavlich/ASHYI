package org.sakaiproject.lessonbuildertool.tool.producers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.service.BltiInterface;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.FilePickerViewParameters;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.util.ResourceLoader;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
/**
 * @author ASHYI
 * 
 * Productor de caracteristica del sistema 
 * (Estilo de aprendizaje, contexto, situacion de aprendizaje, habilidad y/o competencia)
 */
public class CaracteristicaProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "Caracteristica";

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
	//private LessonEntity actividadEntity;
	//private LessonEntity assignmentEntity;
	private static LessonEntity bltiEntity;
	/**
	 * Acceso a los datos de cabecera HTTP
	 */
	private HttpServletRequest httpServletRequest;
	
	private static Log log = LogFactory.getLog(ShowPageProducer.class);
	/**
	 * MemoryService is the interface for the Sakai Memory service.
	 * This tracks memory users (cachers), runs a periodic garbage collection to keep memory available, and can be asked to report memory usage.
	 */
	private static MemoryService memoryService = (MemoryService)ComponentManager.get(MemoryService.class);
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
	 * @param simplePageBean Bean de comunicacion con Sakai
	 */
	public void setSimplePageBean(SimplePageBean simplePageBean) {
		this.simplePageBean = simplePageBean;
	}
	/**
     * Cambiar bean de ASHYI 
     * @param ashyiBean Bean de comunicacion con ASHYI
     */
	public void setAshyiBean(AshyiBean ashyiBean) {
			this.ashyiBean = ashyiBean;
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
//
//    public void setActividadEntity(LessonEntity l) {
//    		actividadEntity = l;
//	}
    
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
		if (((GeneralViewParameters) viewparams).getSendingPage() != -1) {
		    // will fail if page not in this site
		    // security then depends upon making sure that we only deal with this page
		    try {
			ashyiBean.updatePageObject(((GeneralViewParameters) viewparams).getSendingPage());
		    } catch (Exception e) {
			System.out.println("Caracteristica permission exception " + e);
			return;
		    }
		}

                UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = params.getItemId();

		ashyiBean.setItemId(itemId);

			
			UIForm form = UIForm.make(tofill, "caracteristicaC");
			SimplePage currentPage = ashyiBean.getCurrentPage();
			
			ashyiBean.setCurrentPageId(currentPage.getPageId());
			
			//nueva caracteristica
			if(params.getIdActividad()!= -1)
			{
				//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!: "+params.getItemId().intValue());
				List<Caracteristica> list = ashyiToolDao.getCaracteristica(params.getIdActividad());
				
				Caracteristica caracteristicaEditar = list.get(0);
								
				UIOutput.make(tofill, "caracteristica-nombre", messageLocator.getMessage("caracteristica.nombre"));			
				UIOutput.make(tofill, "caracteristica-tipo", messageLocator.getMessage("caracteristica.tipo"));
				UIOutput.make(tofill, "caracteristica-item", messageLocator.getMessage("caracteristica.item"));
				
				//UIOutput.make(tofill, "nombre:", caracteristicaEditar.getNombre());
				//UIInput.make(form, "tipo:", "#{ashyiBean.tipoC}");
								
				// Rich Text Input
				UIInput.make(form, String.valueOf(caracteristicaEditar.getIdCaracteristica()), "#{ashyiBean.idC}");
				
				UIInput.make(tofill, "nombre:", "#{ashyiBean.nombreC}",caracteristicaEditar.getNombre());
				//UIInput.make(form, "tipo:", "#{ashyiBean.tipoC}");
				
				UISelect.make(tofill, "listaTipos", ashyiBean.getTiposCaracteristica(),"#{ashyiBean.tipoC}", caracteristicaEditar.getIdItem().getNombre()  );
								
				ashyiBean.setIdC(String.valueOf(caracteristicaEditar.getIdCaracteristica()));
				UIInput.make(form, "item-id", "#{ashyiBean.idC}");
	
				UICommand.make(form, "submit", messageLocator.getMessage("caracteristica.editar"), "#{ashyiBean.editarCaracteristica}");
				UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
			}
			else //editar caracteristica
			{
			
				UIOutput.make(tofill, "caracteristica-nombre", messageLocator.getMessage("caracteristica.nombre"));			
				UIOutput.make(tofill, "caracteristica-tipo", messageLocator.getMessage("caracteristica.tipo"));
				UIOutput.make(tofill, "caracteristica-item", messageLocator.getMessage("caracteristica.item"));
				
				// Rich Text Input
				UIInput.make(form, "nombre:", "#{ashyiBean.nombreC}");
				//UIInput.make(form, "tipo:", "#{ashyiBean.tipoC}");
				UIInput.make(form, "test:", "#{ashyiBean.testC}");
				
				UISelect.make(tofill, "listaTipos", ashyiBean.getTiposCaracteristica(),"#{ashyiBean.tipoC}"," "  );
	//			String id=g.selection.ID;
				
				UIInput.make(form, "item-id", "#{ashyiBean.itemId}");
	
				UICommand.make(form, "submit", messageLocator.getMessage("caracteristica.crear"), "#{ashyiBean.addCaracteristica}");
				UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
			}
		
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
		togo.add(new NavigationCase("failure", new SimpleViewParameters(CaracteristicaProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", params));
		return togo;
	}
	
	/**
	 * Retorna la url de la pagina actual
	 * @return url string
	 */
	public String myUrl() {
	    // previously we computed something, but this will give us the official one
	        return ServerConfigurationService.getServerUrl();
	}
}
