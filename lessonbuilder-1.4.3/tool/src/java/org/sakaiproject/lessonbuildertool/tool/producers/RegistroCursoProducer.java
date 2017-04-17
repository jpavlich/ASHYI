package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
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
import uk.org.ponder.rsf.components.UIInternalLink;
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
 * Productor del registro del curso como actividad macro
 * 
 */
public class RegistroCursoProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "RegistroCurso";
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
			System.out.println("Registro Curso permission exception " + e);
			return;
		    }
		}

                UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		ashyiBean.setItemId(itemId);
			
		UIForm form = UIForm.make(tofill, "registroC");
		SimplePage currentPage = ashyiBean.getCurrentPage();
				
		ashyiBean.setCurrentPageId(currentPage.getPageId());
		
		UIOutput.make(tofill, "registro-label", ashyiBean.registrarCurso());
		//UIOutput.make(tofill, "objetivos-label",  messageLocator.getMessage("registro.objetivos"));
		UIOutput.make(tofill, "recursos-label",  messageLocator.getMessage("registro.recursos"));
		UIOutput.make(tofill, "recursosLicencia-label",  messageLocator.getMessage("registro.recursosLicencia"));
		UIOutput.make(tofill, "recursosTipo-label",  messageLocator.getMessage("registro.recursosTipo"));
		UIOutput.make(tofill, "recursosTipoAcceso-label",  messageLocator.getMessage("registro.recursosTipoAcceso"));
		UIOutput.make(tofill, "recursosCxt-label",  messageLocator.getMessage("registro.recursosCxt"));
					
		//UIInput.make(form, "objetivo-nombre:", "#{ashyiBean.objetivoCurso}");
		UIInput.make(form, "recurso-nombre:", "#{ashyiBean.recursoCurso}");
		
		GeneralViewParameters viewObj = new GeneralViewParameters(NuevaPalabraClaveProducer.VIEW_ID);
		viewObj.setSendingPage(Long.valueOf(3));
		viewObj.setItemId(ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getId());
		
		UIInternalLink.make(form, "nuevaPalabra-link", viewObj);
		
		viewObj = new GeneralViewParameters(NuevoObjetivoProducer.VIEW_ID);
		viewObj.setSendingPage(Long.valueOf(1));
		viewObj.setItemId(ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getId());
		
		UIInternalLink.make(form, "nuevoObjetivo-link", viewObj);
		
		String[] naturalezaRecurso = new String[2];
		naturalezaRecurso[0] = "Logico";
		naturalezaRecurso[1] = "Fisico";
		UISelect.make(tofill, "listaTiposLicencia", naturalezaRecurso,"#{ashyiBean.tipoNaturaleza}","  "  );
		
		String[] tiposRecurso = new String[6];
		tiposRecurso[0] = "Software";
		tiposRecurso[1] = "Internet";
		tiposRecurso[2] = "Hardware";
		tiposRecurso[3] = "Infraestructura";
		tiposRecurso[4] = "Monetario";
		tiposRecurso[5] = "Servicios publicos";
		UISelect.make(tofill, "listaTiposRecurso", tiposRecurso,"#{ashyiBean.tipoRecurso}","  "  );
		
		String[] tipoAccesoRecurso = new String[3];
		tipoAccesoRecurso[0] = "Paga";
		tipoAccesoRecurso[1] = "Libre";
		tipoAccesoRecurso[2] = "No aplica";
		UISelect.make(tofill, "listaTiposAccesoRecurso", tipoAccesoRecurso,"#{ashyiBean.tipoTipoAcceso}","  "  );
		
		UISelect.make(tofill, "listaCxtRecurso", ashyiBean.getContextos(),"#{ashyiBean.contextoS}","  "  );

		UICommand.make(form, "submitObj", messageLocator.getMessage("ashyiBean.guardarObjetivo"), "#{ashyiBean.addObjetivoCurso}");
		UICommand.make(form, "submitRec", messageLocator.getMessage("ashyiBean.guardarRecurso"), "#{ashyiBean.addRecursoCurso}");
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
		togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(RegistroCursoProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		return togo;
	}
}
