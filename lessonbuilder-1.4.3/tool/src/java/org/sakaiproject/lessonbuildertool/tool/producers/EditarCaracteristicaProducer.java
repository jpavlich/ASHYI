package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.lessonbuildertool.CaracteristicaImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
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
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;

/**
 * @author ASHYI
 * Productor de editor de caracteristicas
 * 
 *  (Estilo de aprendizaje, contexto, situacion de aprendizaje, habilidad y/o competencia)
 * 
 */
public class EditarCaracteristicaProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter{
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "EditarCaracteristica";
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
			System.out.println("Caracteristica permission exception " + e);
			return;
		    }
		}

                UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		ashyiBean.setItemId(itemId);
		SimplePage page = ashyiBean.getCurrentPage();
		
		UIBranchContainer toolBar = UIBranchContainer.make(tofill, "tool-bar:");
		createToolBarLink(EditPageProducer.VIEW_ID, toolBar, "add-text", "simplepage.text", page, "simplepage.text.tooltip").setItemId(null);
			
		UIForm form = UIForm.make(tofill, "listaC");
		
		UIOutput.make(tofill, "tofill-id", tofill.ID);
		
		UIInput.make(form, "tofill-id", "#{ashyiBean.tofillAct}");
		
		//Aspectos de una caracteristica
		UIOutput.make(tofill, "dominio.lista", messageLocator.getMessage("dominio.lista"));
		UIOutput.make(tofill, "nombreListaC", messageLocator.getMessage("dominio.listac"));			
		UIOutput.make(tofill, "nombreListaCx", messageLocator.getMessage("dominio.listacx"));
		
		//UIOutput.make(tofill, "tipos-label", messageLocator.getMessage("tipo.tipos"));
		UIOutput.make(tofill, "estilo-label", messageLocator.getMessage("actividad.estilo"));
		UIOutput.make(tofill, "habilidades-label", messageLocator.getMessage("actividad.habilidades"));
		UIOutput.make(tofill, "competencias-label", messageLocator.getMessage("actividad.competencias"));
		UIOutput.make(tofill, "personalidad-label", messageLocator.getMessage("actividad.personalidad"));
		UIOutput.make(tofill, "sa-label", messageLocator.getMessage("actividad.sa"));
				
		String[] listaH = ashyiBean.getHabilidades();
		
		UISelect select =  UISelect.makeMultiple(tofill, "habilidades", listaH,"#{ashyiBean.habilidadesSeleccionadas}",listaH);
		int index = 0;
		for (String entry: listaH) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageH:");
			UISelectChoice.make(row, "listaHabilidades", select.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setReturnView(VIEW_ID);			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setHabilidadS(entry);
			hE=ashyiBean.editarHabilidad();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarH-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}	
		
		String[] listaC = ashyiBean.getCompetencias();
		
		UISelect selectC =  UISelect.makeMultiple(tofill, "competencias", listaC,"#{ashyiBean.competenciasSeleccionadas}",listaC);
		index = 0;
		for (String entry: listaC) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageC:");
			UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setSendingPage(ashyiBean.getCurrentPageId());			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setCompetenciaS(entry);
			hE=ashyiBean.editarCompetencia();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarC-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}	
		
		String[] listaP = ashyiBean.getPersonalidades();
		
		UISelect selectP =  UISelect.makeMultiple(tofill, "personalidades", listaP,"#{ashyiBean.personalidadesSeleccionadas}",listaP);
		index = 0;
		for (String entry: listaP) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageP:");
			UISelectChoice.make(row, "listaPersonalidad", selectP.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setSendingPage(ashyiBean.getCurrentPageId());			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setPersonalidadS(entry);
			hE=ashyiBean.editarPersonalidad();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarP-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}	
		
		String[] listaSA = ashyiBean.getSA();
		
		UISelect selectSA =  UISelect.makeMultiple(tofill, "SA", listaSA,"#{ashyiBean.saSeleccionadas}",listaSA);
		index = 0;
		for (String entry: listaSA) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageSA:");
			UISelectChoice.make(row, "listaSA", selectSA.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setSendingPage(ashyiBean.getCurrentPageId());			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setSaS(entry);
			hE=ashyiBean.editarSas();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarSA-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}	
		
		String[] listaE = ashyiBean.getEstilos();
		
		UISelect selectE =  UISelect.makeMultiple(tofill, "estilos", listaE,"#{ashyiBean.saSeleccionadas}",listaE);
		index = 0;
		for (String entry: listaE) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageE:");
			UISelectChoice.make(row, "listaEstilos", selectE.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setSendingPage(ashyiBean.getCurrentPageId());			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setEstiloS(entry);
			hE=ashyiBean.editarEstilo();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarE-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}	
		
		String[] listaCx = ashyiBean.getContextos();
		
		UISelect selectCx =  UISelect.makeMultiple(tofill, "contextos", listaCx,"#{ashyiBean.contextosSeleccionados}",listaCx);
		index = 0;
		for (String entry: listaCx) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "pageCx:");
			UISelectChoice.make(row, "listaContextos", selectCx.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));	
			
			GeneralViewParameters viewObj = new GeneralViewParameters(CaracteristicaProducer.VIEW_ID);
			viewObj.setSendingPage(ashyiBean.getCurrentPageId());			
			Caracteristica hE = new CaracteristicaImpl();
			ashyiBean.setContextoS(entry);
			hE=ashyiBean.editarContexto();
			viewObj.setIdActividad(hE.getIdCaracteristica());
			
			UIInternalLink.make(row, "editarCx-link",entry, viewObj).decorate(new UIFreeAttributeDecorator("title",entry));
			index++;
		}
				
		UICommand.make(form, "delete", messageLocator.getMessage("dominio.eliminar"), "#{ashyiBean.eliminarDominio}");
		UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		
		//if(edit.)
		SimplePageItem pageItem = null;
		if (ashyiBean.getCurrentPage() != null) {
			pageItem = ashyiBean.getCurrentPageItem(params.getItemId());
		}		
		
		UIInput.make(form, "item-id", "#{ashyiBean.itemId}");
		
		
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
		togo.add(new NavigationCase("failure", new SimpleViewParameters(EditarCaracteristicaProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));	
		
		return togo;
	}	
	
	private GeneralViewParameters createToolBarLink(String viewID, UIContainer tofill, String ID, String message, SimplePage currentPage, String tooltip) {
		GeneralViewParameters params = new GeneralViewParameters();
		params.setSendingPage(currentPage.getPageId());
		createStandardToolBarLink(viewID, tofill, ID, message, params, tooltip);
		return params;
	}
	
	private void createStandardToolBarLink(String viewID, UIContainer tofill, String ID, String message, SimpleViewParameters params, String tooltip) {
		params.viewID = viewID;
		UILink link = UIInternalLink.make(tofill, ID, messageLocator.getMessage(message), params);
		link.decorate(new UITooltipDecorator(messageLocator.getMessage(tooltip)));
	}	
	
	public String myUrl() {
	    // previously we computed something, but this will give us the official one
	        return ServerConfigurationService.getServerUrl();
	}
	
}
