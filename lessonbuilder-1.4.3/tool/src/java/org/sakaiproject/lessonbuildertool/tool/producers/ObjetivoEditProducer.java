package org.sakaiproject.lessonbuildertool.tool.producers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
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
 * Productor de editor de objetivo
 * 
 */
public class ObjetivoEditProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "ObjetivoEdit";
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
		System.out.println("UD y objetivo");
		System.out.println(params.getIdUnidadDidactica()+", "+params.getObjetivo());

		UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		.decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		UIForm form = UIForm.make(tofill, "objetivoC");

		SimplePage currentPage = ashyiBean.getCurrentPage();

		ashyiBean.setCurrentPageId(currentPage.getPageId());
		Objetivo obj = ashyiToolDao.getObjetivoPorId(Integer.valueOf(String.valueOf(params.getObjetivo())));
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
			
			//Fecha inicial y final del objetivo
			ashyiBean.setObjetivoUD(params.getObjetivo());
			UIInput.make(form, "objId", "#{ashyiBean.objetivoUD}");
			
			UIOutput.make(form, "link-textC", obj.getNombre());
			//UIInput.make(form, "initial:", "#{ashyiBean.fechaInicialObjetivoUD}");		
			//UIInput.make(form, "final:", "#{ashyiBean.fechaFinalObjetivoUD}");
			//UIInput.make(form, "fecha_F", "#{ashyiBean.fechaFinalObjetivoUD}");
			//UIInput.make(form, "fecha_I", "#{ashyiBean.fechaInicialObjetivoUD}");
			
			UIInput.make(form, "demo2", "#{ashyiBean.fechaFinalObjetivoUD}");
			UIInput.make(form, "demo3", "#{ashyiBean.fechaInicialObjetivoUD}");
			
			//fechas anteriores
			List<Date> fechas = verificarFechas(obj.getIdObjetivo());
			if(!fechas.isEmpty())
			{
				UIOutput.make(tofill, "time-initial-label", messageLocator.getMessage("objetivos.tiempo-inicialAnterior-objetivos"));
				UIOutput.make(tofill, "time-final-label", messageLocator.getMessage("objetivos.tiempo-finalAnterior-objetivos"));
				
				UIOutput.make(tofill, "time-initialAnterior-label", messageLocator.getMessage("objetivos.tiempo-inicial-objetivos"));
				UIOutput.make(tofill, "time-finalAnterior-label", messageLocator.getMessage("objetivos.tiempo-final-objetivos"));
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				try {
					Date date1 = formatter.parse(fechas.get(0).toString().split("\\.")[0]);
					Date date2 = formatter.parse(fechas.get(1).toString().split("\\.")[0]);
					
					formatter.applyPattern("dd-MMM-yyyy hh:mm:ss a");
					String fechaMostrar1 = formatter.format(date1);
					String fechaMostrar2 = formatter.format(date2);
					
					UIOutput.make(tofill, "fecha_IAnterior", fechaMostrar1);
					UIOutput.make(tofill, "fecha_FAnterior", fechaMostrar2);
					
//					UIOutput.make(tofill, "fecha_IAnterior", fechas.get(0).toString());
//					UIOutput.make(tofill, "fecha_FAnterior", fechas.get(1).toString());
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
			{
				UIOutput.make(tofill, "time-initial-label", messageLocator.getMessage("objetivos.tiempo-inicial-objetivos"));
				UIOutput.make(tofill, "time-final-label", messageLocator.getMessage("objetivos.tiempo-final-objetivos"));
			}

			UIOutput.make(tofill, "time-label", messageLocator.getMessage("objetivos.tiempo-objetivos"));
			
			UICommand.make(form, "submit", messageLocator.getMessage("objetivo.almacenar"), "#{ashyiBean.updateObjetivoUnidad}");
		}
		UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
	}
	
	/**
	 * Consultar las fechas deinicio y fimal de un objetivo
	 * @param idObjetivo id del objetivo a consultar
	 * @return lista de las fechas asociadas al objetivo
	 */
	public List<Date> verificarFechas(int idObjetivo)
	{
		Actividad ud=ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
				
		List<ItemPlan> items=ashyiToolDao.getItemPlansUD(ud);
				
		List<Date> fechas = new ArrayList<Date>();
		
		for (int i = 0; i < items.size(); i++) {
			ItemPlan item=items.get(i);
			Actividad ac=item.getIdActividad();
			ObjetivosActividad oa=ashyiToolDao.getObjetivosActividad(ac.getIdActividad()).get(0);
			Objetivo obj=oa.getIdObjetivo();
//			String [] spl=getFechaInicialObjetivoUD().split("-");
//			GregorianCalendar gc=new GregorianCalendar(Integer.parseInt(spl[0]), Integer.parseInt(spl[1])-1, 
//					Integer.parseInt(spl[2]));
//			Date fechaIni=new Date(gc.getTimeInMillis());
//			
//			spl=getFechaFinalObjetivoUD().split("-");
//			gc=new GregorianCalendar(Integer.parseInt(spl[0]), Integer.parseInt(spl[1])-1, 
//					Integer.parseInt(spl[2]));
//			Date fechaFin=new Date(gc.getTimeInMillis());
			System.out.println("Buscando objetivo: "+Integer.valueOf(idObjetivo)+" en el obj del item: "+obj.getIdObjetivo()+" del item: "+item.getIdItemPlan());
			if(obj.getIdObjetivo() == idObjetivo)
			{
				if(item.getFecha_inicial() != null && item.getFecha_final() != null )
				{
					fechas.add(item.getFecha_inicial());
					fechas.add(item.getFecha_final());
					break;
				}
			}
		}
		return fechas;
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
		togo.add(new NavigationCase("success", new SimpleViewParameters(ObjetivosEditProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(ObjetivoEditProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ObjetivosEditProducer.VIEW_ID)));
		
		GeneralViewParameters params2 = new GeneralViewParameters(ShowPageProducer.VIEW_ID);
		params2.setSourcePage("vistaActividadCloseWindow");
		//togo.add(new NavigationCase("successObjetivoEdit", params2));
		return togo;
	}	
}