package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Tipo;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
/**
 * @author ASHYI
 * Productor de actividad
 * 
 */
public class TipoProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "Tipo";

	/**
	 * Bean de sakai
	 */
	private SimplePageBean simplePageBean;
	/**
	 * Acceso a datos de sakai
	 */
	private SimplePageToolDao simplePageToolDao;
	
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
		if (((GeneralViewParameters) viewparams).getSendingPage() != -1) {
		    // will fail if page not in this site
		    // security then depends upon making sure that we only deal with this page
		    try {
			simplePageBean.updatePageObject(((GeneralViewParameters) viewparams).getSendingPage());
		    } catch (Exception e) {
			System.out.println("contexto permission exception " + e);
			return;
		    }
		}

                UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		simplePageBean.setItemId(itemId);
		
		//nuevo tipo
		if(params.getItemId()!= -1)
		{
			UIForm form = UIForm.make(tofill, "tipoC");
			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!: "+params.getItemId().intValue());
			List<Tipo> list = simplePageToolDao.getTipo(params.getItemId().intValue());
			
			Tipo tipoEditar = list.get(0);
			
			UIOutput.make(tofill, "nombre:", tipoEditar.getNombre());
			//UIInput.make(form, "tipo:", "#{simplePageBean.tipoC}");
			
			//UIOutput.make(tofill, "listaTipos", String.valueOf(tipoEditar.getTipoTipo()));
			
			
			SimplePage currentPage = simplePageBean.getCurrentPage();
			
			simplePageBean.setCurrentPageId(currentPage.getPageId());
			
			UIOutput.make(tofill, "tipo-nombre", messageLocator.getMessage("tipo.nombre"));			
			UIOutput.make(tofill, "tipo-tipo", messageLocator.getMessage("tipo.tipo"));
			
			// Rich Text Input
			UIInput.make(form, "nombre:", "#{simplePageBean.nombreTipo}");
			
			UISelect.make(tofill, "listaTipos", simplePageBean.getTipoTipos(),"#{simplePageBean.tipoTipo}"," "  );
			
			UIInput.make(form, "item-id", "#{simplePageBean.itemId}");

			UICommand.make(form, "submit", messageLocator.getMessage("tipo.editar"), "#{simplePageBean.addTipo}");
			UICommand.make(form, "cancel", messageLocator.getMessage("simplepage.cancel"), "#{simplePageBean.cancel}");
		}
		else //edicion
		{

			UIForm form = UIForm.make(tofill, "tipoC");
			SimplePage currentPage = simplePageBean.getCurrentPage();
			
			simplePageBean.setCurrentPageId(currentPage.getPageId());
			
			UIOutput.make(tofill, "tipo-nombre", messageLocator.getMessage("tipo.nombre"));			
			UIOutput.make(tofill, "tipo-tipo", messageLocator.getMessage("tipo.tipo"));
			
			// Rich Text Input
			UIInput instructions = UIInput.make(form, "nombre:", "#{simplePageBean.nombreTipo}");
			
			UISelect.make(tofill, "listaTipos", simplePageBean.getTipoTipos(),"#{simplePageBean.tipoTipo}"," "  );
			
			UIInput.make(form, "item-id", "#{simplePageBean.itemId}");

			UICommand.make(form, "submit", messageLocator.getMessage("tipo.crear"), "#{simplePageBean.addTipo}");
			UICommand.make(form, "cancel", messageLocator.getMessage("simplepage.cancel"), "#{simplePageBean.cancel}");
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
		togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(TipoProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		return togo;
	}
}
