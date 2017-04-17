package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
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
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
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
 * Productor de listado de objetivos de un aunidad didactica
 * 
 */
public class ObjetivosEditProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "ObjetivosEdit";

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

	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}


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
				System.out.println("ActividadPicker permission exception " + e);
				return;
			}
		}

		UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		.decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		UIForm form = UIForm.make(tofill, "objetivoC");

		SimplePage currentPage = ashyiBean.getCurrentPage();

		ashyiBean.setCurrentPageId(currentPage.getPageId());

		SimplePageItem i = ashyiToolDao.findItem(params.getItemId());	

		//profesor
		if (ashyiBean.canEditPage()) {

			SimplePage page = ashyiBean.getCurrentPage();
			SimplePageItem pageItem = null;
			if (page != null) {
				pageItem = ashyiBean.getCurrentPageItem(params.getItemId());
			}

			//disabled retroalimentacion del estudiante

			//2 post condiciones
			
			String [] objetivosActividad=ashyiToolDao.getObjetivosActividad(3, ashyiBean.getCurrentPage().getTitle());
			List<Integer> idObjetivosActividad = ashyiToolDao.getIdObjetivosActividad(3, ashyiBean.getCurrentPage().getTitle());
			int index = 0;
			//listado de objetivos de la unidad
			for (String entry: objetivosActividad) {
				UIBranchContainer row = UIBranchContainer.make(form, "pageObj:");	
				GeneralViewParameters viewAct = new GeneralViewParameters(
						ObjetivoEditProducer.VIEW_ID);
				viewAct.setIdUnidadDidactica(ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad());				
				viewAct.setObjetivo(String.valueOf(idObjetivosActividad.get(index)));
				viewAct.setReturnView(this.VIEW_ID);
				UILink link = UIInternalLink
						.make(row, "link-textC", entry, viewAct);
//				link.decorate(new UIFreeAttributeDecorator("target",
//						"_blank"));
//				UIOutput.make(row, "link-textC", entry);
				index++;
			}	
			
//			UISelect selectFechaIni = UISelect.makeMultiple(form, "fechasIniciales", null , "#{ashyiBean.fechasInicialesObjetivosUD}" , null);
//			index = 0;
//			UIBranchContainer row = UIBranchContainer.make(form, "pageFechaIni:");
//			for (int j=0;j<objetivosActividad.length; j++) {
//				UIOutput.make(form, "initial:", "#{ashyiBean.inicialF}");
//				index++;
//			}
//			
////			UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
////			decorate(new UIFreeAttributeDecorator("title", entry));
//			
//			UISelect selectFechaFin = UISelect.makeMultiple(form, "fechasFinales", null , "#{ashyiBean.fechasFinalesObjetivosUD}" , null);
//			index = 0;
//			UIBranchContainer row2 = UIBranchContainer.make(form, "pageFechaFin:");
//			for (int j=0;j<objetivosActividad.length; j++) {
//				UIInput.make(form, "final:", "#{ashyiBean.finalF}");
//				index++;
//			}

			UIOutput.make(tofill, "objetivosPost-label", messageLocator.getMessage("objetivos.listaDeObjetivos"));
			UIOutput.make(tofill, "time-label", messageLocator.getMessage("objetivos.tiempo-objetivos"));
			UIOutput.make(tofill, "time-initial-label", messageLocator.getMessage("objetivos.tiempo-inicial-objetivos"));
			UIOutput.make(tofill, "time-final-label", messageLocator.getMessage("objetivos.tiempo-final-objetivos"));
//			UICommand.make(form, "submit", messageLocator.getMessage("respuesta.almacenar"), "#{ashyiBean.updateObjetivosUnidad}");
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
		togo.add(new NavigationCase("success", new SimpleViewParameters(FlowChartProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(ObjetivosEditProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(FlowChartProducer.VIEW_ID)));
		return togo;
	}
}
