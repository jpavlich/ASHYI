package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
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
 * Productor de nuevo objetivo
 * 
 */
public class NuevoObjetivoProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "NuevoObjetivo";
	/**
	 * Bean de sakai
	 */
	private SimplePageBean simplePageBean;
	/**
	 * Bean de ASHYI, puente de comunicacion
	 */
	private AshyiBean ashyiBean;
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
	 * @param dao objeto de acceso a datos de Sakai
	 */
	public void setSimplePageToolDao(Object dao) {
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

		SimplePage currentPage = ashyiBean.getCurrentPage();
		
		ashyiBean.setCurrentPageId(currentPage.getPageId());
		
		UIOutput.make(tofill, "nuevoObjetivo-dialog").decorate(new UIFreeAttributeDecorator("title", messageLocator.getMessage("ashyiBean.nuevoObjetivo")));
		UIForm form = UIForm.make(tofill, "nuevoObjetivo-form");

		UIOutput.make(form, "subpagegoalVerbo-label", messageLocator.getMessage("registro.objetivoVerbo"));
		UIOutput.make(form, "subpagegoalComplemento-label", messageLocator.getMessage("registro.objetivoComplemento"));
				
		if(params.getSendingPage() == 1)	
			//UIInput.make(form, "subpage-goal", "#{ashyiBean.subpageGoal}");
			UISelect.make(tofill, "listaVerbos", ashyiBean.getVerbosCurso(),"#{ashyiBean.subpageGoal}","  "  );
		else if(params.getSendingPage() == 2)	
			//UIInput.make(form, "subpage-goal", "#{ashyiBean.subpageGoalUnidad}");
			UISelect.make(tofill, "listaVerbos", ashyiBean.getVerbosCurso(),"#{ashyiBean.subpageGoalUnidad}","  "  );
		else if(params.getSendingPage() == 3)	
			//UIInput.make(form, "subpage-goal", "#{ashyiBean.subpageGoalUnidad}");
			UISelect.make(tofill, "listaVerbos", ashyiBean.getVerbosCurso(),"#{ashyiBean.subpageGoalActividad}","  "  );
		
		String[] listaComplementos = ashyiBean.getComplementosCurso();
		UISelect select = UISelect.makeMultiple(form, "objetivos", listaComplementos , "#{ashyiBean.objetivosSeleccionados}" , null);
		int index = 0;
		for (String entry: listaComplementos) {
			
			UIBranchContainer row = UIBranchContainer.make(form, "page:");
			
			UISelectChoice.make(row, "listaComplementos", select.getFullID(), index).
			decorate(new UIFreeAttributeDecorator("title", entry));
			UIOutput.make(row, "link-text", entry);
			index++;
		}	
		
		ashyiBean.setIdActividad(String.valueOf(params.getIdActividad()));
		UIInput.make(form, "idActividad", "#{ashyiBean.idActividad}");
		
		UICommand.make(form, "createnuevoObjetivo-subpage", messageLocator.getMessage("simplepage.create"), "#{ashyiBean.createnuevoObjetivo}");
		UICommand.make(form, "cancelnuevoObjetivo-subpage", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		
		//UIOutput.make(form, "item-id", String.valueOf(ashyiBean.getItemId()));
		//UIInput.make(form, String.valueOf(ashyiBean.getItemId()), "#{ashyiBean.itemId}");
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
		GeneralViewParameters params = new GeneralViewParameters(ShowPageProducer.VIEW_ID);
		params.setIdItemPAshyi(0);
		togo.add(new NavigationCase("success", params));
		togo.add(new NavigationCase("successU", new SimpleViewParameters(RegistroObjetivoUnidadProducer.VIEW_ID)));
		togo.add(new NavigationCase("successActividad", new SimpleViewParameters(ActividadPickerProducer.VIEW_ID)));		
		togo.add(new NavigationCase("failure", new SimpleViewParameters(NuevoObjetivoProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", params));
		return togo;
	}	
}
