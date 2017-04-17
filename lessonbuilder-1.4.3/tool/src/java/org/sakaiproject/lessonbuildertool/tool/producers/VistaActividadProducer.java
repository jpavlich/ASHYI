package org.sakaiproject.lessonbuildertool.tool.producers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.content.cover.ContentTypeImageService;
import org.sakaiproject.lessonbuildertool.RespuestaItemsUsuarioImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Usuario;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean.Status;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIColourDecorator;
import uk.org.ponder.rsf.components.decorators.UIDisabledDecorator;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UIStyleDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
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
 * Productor de vista del progreso de una actividad atomica
 * Muestra y permite las entregas a una actividad y su caificacion 
 * 
 */
public class VistaActividadProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "VistaActividad";
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

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		ashyiBean.setItemId(itemId);
//		UIForm form = UIForm.make(tofill, "actividadC");
//		UIForm form2 = UIForm.make(tofill, "actividadC2");
		UIForm uploadform = UIForm.make(tofill, "uploadform" );
		
		SimplePage currentPage = ashyiBean.getCurrentPage();
		
		ashyiBean.setCurrentPageId(currentPage.getPageId());
		
		int idActividad = params.getIdActividad();
		Actividad a = ashyiToolDao.getActividad(idActividad);
		
		int idTP = params.getIdItemPAshyi();
		int idTU = params.getIdItemUAshyi();
		int idRUI = params.getIdItemRAshyi();
		
		ashyiBean.setIdTP(String.valueOf(idTP));
		ashyiBean.setIdTU(String.valueOf(idTU));
		ashyiBean.setIdActividad(String.valueOf(idActividad));
		ashyiBean.setIdRIU(String.valueOf(idRUI));
		
		UIInput.make(uploadform, "item-id", "#{ashyiBean.itemId}");
		UIInput.make(uploadform, "idTP", "#{ashyiBean.idTP}");
		UIInput.make(uploadform, "idTU", "#{ashyiBean.idTU}");
		UIInput.make(uploadform, "idRIU", "#{ashyiBean.idRIU}");
		UIInput.make(uploadform, "idActividad", "#{ashyiBean.idActividad}");
		
		UIInput.make(uploadform, "nombre:", "#{ashyiBean.nomberA}", a.getNombre());
		
		UIInput.make(uploadform, "descripcion:", "#{ashyiBean.descripcionA}", a.getDescripcion());
		
		System.out.println("En VistaActividadProducer con idActividad: "
		+idActividad+",  idTP: "+idTP+", idTU: "+idTU+" idR: "+ idRUI);
		
		ItemsUsuario iPlan = ashyiToolDao.getItemUsuario(idTP, idTU);
		
		SimplePageItem i = ashyiToolDao.findItem(params.getItemId());	

		
//		String[] urlP = params.getSource().split("\\.");
		//si no se ha realizado, la actividad esta disponible
		if(iPlan==null)
			System.out.println("El itemPlan aca en vistaActividadProducer es: ItemsUsuario=null");
		else
			System.out.println("El itemPlan aca en vistaActividadProducer es: "+iPlan.isRealizada());
		if( !iPlan.isRealizada())
		{
			UIOutput.make(tofill, "recurso-abrir", messageLocator.getMessage("actividad.disponible"));
		}
		else
		{
			UIOutput.make(tofill, "recurso-abrir", messageLocator.getMessage("actividad.Nodisponible"));
		}	
		
		//profesor
		if (ashyiBean.canEditPage()) {
		        
			SimplePage page = ashyiBean.getCurrentPage();
			SimplePageItem pageItem = null;
			if (page != null) {
				pageItem = ashyiBean.getCurrentPageItem(params.getItemId());
			}
			
			List<RespuestaItemsUsuario> respuestas = ashyiBean.getRespuestasItemUsuarioList(idTP, idTU);
			
			//si se han registrado respuestas para esta actividad
			if(respuestas != null)
			{
				System.out.println("Respuestas a mostrar: "+respuestas.size());
				//2 post condiciones
				String[] listaRtas = ashyiBean.getRespuestasItemUsuario(idTP, idTU);
				
				UISelect selectR = UISelect.makeMultiple(uploadform, "repuestasS", listaRtas , "#{ashyiBean.personalidadesSeleccionadas}" , null);
				for (int j = 0; j < listaRtas.length; j++) {
					
					UIBranchContainer row = UIBranchContainer.make(uploadform, "repuestas:");
					
					GeneralViewParameters viewObj = new GeneralViewParameters(VistaActividadProducer.VIEW_ID);
					viewObj.setReturnView(VIEW_ID);	
					viewObj.setIdItemRAshyi(respuestas.get(j).getIdRespuesta());
					viewObj.setIdItemPAshyi(idTP);
					viewObj.setIdItemUAshyi(idTU);
					viewObj.setIdActividad(idActividad);
					viewObj.setItemId(params.getItemId());
					
					UIInternalLink.make(row, "listaRepuestas","Entregada en: "+listaRtas[j], viewObj).decorate(new UIFreeAttributeDecorator("title", "Entregada en: "+listaRtas[j]));
					UIOutput.make(row, "listaRepuestas-text", "Entregada en: "+listaRtas[j]);
					
					//UIOutput.make(row, "link-textactividad", listaRtas[index]);
					
					//UIOutput.make(row, "link-textR", entry);j
				}	
			}
			else
			{
				UIOutput.make(tofill, "repuestasS", messageLocator.getMessage("actividad.linkRespuestasE"));
			}
			
			//disabled retroalimentacion del estudiante
			if(idRUI != -1)
			{
				UIOutput.make(tofill, "recursoUpload-label", messageLocator.getMessage("actividad.respuesta"));
				UIOutput.make(tofill, "retroalimentacionE-label", messageLocator.getMessage("actividad.retroalimentacionE"));
				UIOutput.make(tofill, "retroalimentacionP-label", messageLocator.getMessage("actividad.retroalimentacionP"));
				UIOutput.make(tofill, "recurso-labelS", messageLocator.getMessage("actividad.linkRecursoS"));
				
				RespuestaItemsUsuario rIU = ashyiToolDao.getRespuestaItemUsuario(idTP, idTU, idRUI);
				UIInput.make(uploadform, "retroalimentacionE:", "#{ashyiBean.retroalimentacionE}",rIU.getRetroalimentacion_usuario())
				.decorate(new UIDisabledDecorator(true));
				
				UIInput.make(uploadform, "retroalimentacionP:", "#{ashyiBean.retroalimentacionP}", rIU.getRetroalimentacion_profesor());
				
				String urlP = this.httpServletRequest.getRequestURL().toString();
				String[] cad = urlP.split("/");
				
				String url = "";
				url += cad[0];
				url += "/";
				url += cad[1];
				url += "/";
				url += cad[2];
				url += "/";
				url += messageLocator.getMessage("ashyibean.pathArchivosC");
				url += "/";
				url += rIU.getArchivo();
				
				//String urlEncode = httpServletResponse.encodeURL(url);
							
				UILink link = UILink
							.make(uploadform, "recursoUpload-link", "Revisar respuesta",url);
				
				if(rIU.getArchivo() == null)
				{
					//no ver porque no se ha subido
				    disableLink(link, messageLocator);
				    UIOutput.make(uploadform, "recursoError", messageLocator.getMessage("actividad.respuestaError"));
				}
				else
					UIOutput.make(uploadform, "recursoError", messageLocator.getMessage("actividad.respuestaEncontrar"));
				
				UICommand.make(uploadform, "submit", messageLocator.getMessage("respuesta.almacenar"), "#{ashyiBean.updateItemUsuarioPlan}");
			}
			else
			{	
//				UIOutput.make(tofill, "recursoUpload-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "retroalimentacionE-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "retroalimentacionP-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "recurso-labelS", messageLocator.getMessage("actividad.infoNo"));
				UICommand.make(uploadform, "submit", messageLocator.getMessage("respuesta.almacenar"), "#{ashyiBean.updateItemUsuarioPlanIncompleto}");
			}
			
			UIInput.make(uploadform, "nota:", "#{ashyiBean.notaA}",String.valueOf(iPlan.getNota()));
			
			//2 post condiciones
			String[] listaCompetencias = ashyiBean.getCompHabPostItemUsuario(iPlan, 2);
			String[] listaCLlenas = ashyiBean.getCaracLlenasItemUsuario(iPlan);
			UISelect selectC = UISelect.makeMultiple(uploadform, "competencias", listaCompetencias , "#{ashyiBean.competenciasSeleccionadas}" , listaCLlenas);
			int index = 0;
			for (String entry: listaCompetencias) {
				
				UIBranchContainer row = UIBranchContainer.make(uploadform, "pageC:");
				
				UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
				decorate(new UIFreeAttributeDecorator("title", entry));
				UIOutput.make(row, "link-textC", entry);
				index++;
			}
			
			UICommand.make(uploadform, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		}
		else // estudiante
		{
			UICommand boton = UICommand.make(uploadform, "submit", messageLocator.getMessage("respuesta.almacenarE"), "#{ashyiBean.updateItemUsuarioEjecutor}");
			UICommand.make(uploadform, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		
			//disabled retroalimentacion del profesor
			if(idRUI >= 0)
			{
				UIOutput.make(tofill, "recursoUpload-label", messageLocator.getMessage("actividad.respuesta"));
				UIOutput.make(tofill, "retroalimentacionE-label", messageLocator.getMessage("actividad.retroalimentacionE"));
				UIOutput.make(tofill, "retroalimentacionP-label", messageLocator.getMessage("actividad.retroalimentacionP"));
				UIOutput.make(tofill, "recurso-labelS", messageLocator.getMessage("actividad.linkRecursoS"));
			}
			
			if(idRUI < 0)
			{
//				UIOutput.make(tofill, "recursoUpload-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "retroalimentacionE-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "retroalimentacionP-label", messageLocator.getMessage("actividad.infoNo"));
//				UIOutput.make(tofill, "recurso-labelS", messageLocator.getMessage("actividad.infoNo"));
				
				GeneralViewParameters viewObj = new GeneralViewParameters(VistaActividadProducer.VIEW_ID);
				viewObj.setReturnView(VIEW_ID);	
				viewObj.setIdItemRAshyi(0);//nueva respuesta
				viewObj.setIdItemPAshyi(idTP);
				viewObj.setIdItemUAshyi(idTU);
				viewObj.setIdActividad(idActividad);
				viewObj.setItemId(params.getItemId());
				
				if(!iPlan.isRealizada())
				{
					UILink link = UIInternalLink.make(uploadform, "nuevaRta-link",messageLocator.getMessage("actividad.newRta"), viewObj);
					link.decorate(new UIFreeAttributeDecorator("title", messageLocator.getMessage("actividad.newRta")));
					
					if(!(iPlan.getIdItemPlan().getIdActividad().isEs_refuerzo()) && (iPlan.getIdItemPlan().getFecha_inicial() != null))
						if(iPlan.getIdItemPlan().getFecha_final().compareTo(new Date()) < 0)
						{
							//disableLink(link, messageLocator);
							disableLink(link, messageLocator);
							boton.decorate(new UIDisabledDecorator(true));
						}
				}
				else
				{
					boton.decorate(new UIDisabledDecorator(true));				
				}
			}
			
			//recurso asocaido
			if(idRUI > 0)
			{	
				UIOutput.make(uploadform, "recurso");
				
				RespuestaItemsUsuario rIU = ashyiToolDao.getRespuestaItemUsuario(idTP, idTU, idRUI);
				
				UIInput.make(uploadform, "retroalimentacionP:", "#{ashyiBean.retroalimentacionP}", rIU.getRetroalimentacion_profesor())
				.decorate(new UIDisabledDecorator(true));
								
				String urlP = this.httpServletRequest.getRequestURL().toString();
				String[] cad = urlP.split("/");
				
				String url = "";
				url += cad[0];
				url += "/";
				url += cad[1];
				url += "/";
				url += cad[2];
				url += "/";
				url += messageLocator.getMessage("ashyibean.pathArchivosC");
				url += "/";
				url += rIU.getArchivo();
				
				//String urlEncode = httpServletResponse.encodeURL(url);
								
				UILink link = UILink
							.make(uploadform, "recursoUpload-link", "Revisar respuesta",url);		
				
				if(rIU.getArchivo() == null)
				{
					//no ver porque no se ha subido
				    disableLink(link, messageLocator);
				    UIOutput.make(tofill, "recursoError", messageLocator.getMessage("actividad.respuestaError"));
				}
				else
					UIOutput.make(tofill, "recursoError", messageLocator.getMessage("actividad.respuestaEncontrar"));
				
				if(!iPlan.isRealizada())
				{
				    UICommand.make(uploadform, "submitFile", "ashyiBean.uploadExportFile");
				    UIInput rE = UIInput.make(uploadform, "retroalimentacionE:", "#{ashyiBean.retroalimentacionE}", rIU.getRetroalimentacion_usuario());
				    //no ver porque no se ha subido
				    //disableLink(link, messageLocator);
				    if(!(iPlan.getIdItemPlan().getIdActividad().isEs_refuerzo())  && (iPlan.getIdItemPlan().getFecha_inicial() != null))
						if(iPlan.getIdItemPlan().getFecha_final().compareTo(new Date()) < 0)
					    {
					    	boton.decorate(new UIDisabledDecorator(true));
					    }
				}
				else
				{
					UIInput rE = UIInput.make(uploadform, "retroalimentacionE:", "#{ashyiBean.retroalimentacionE}", rIU.getRetroalimentacion_usuario());
					rE.decorate(new UIDisabledDecorator(true));
					boton.decorate(new UIDisabledDecorator(true));				
				}
			}
			if(idRUI == 0)
			{
				UIOutput.make(uploadform, "recurso");
				RespuestaItemsUsuario riU = new RespuestaItemsUsuarioImpl();
				
				ItemPlan iP = ashyiToolDao.getItemPlan(idTP);
				riU.setIdItemPlan(iP);
				
				Usuario us = ashyiToolDao.getUsuario(idTU);
				riU.setIdUsuario(us);
				
				riU.setFecha(new Date());
				//almacenar respuesta
				ashyiBean.saveCarateristica(riU);
				
				riU.setIdRespuesta(ashyiToolDao.getUltimaRespuesta());
				
				idRUI = riU.getIdRespuesta();
				
				ashyiBean.setIdRIU(String.valueOf(riU.getIdRespuesta()));
				UIInput.make(uploadform, "idRIUR", "#{ashyiBean.idRIU}");
			    UICommand.make(uploadform, "submitFile", "ashyiBean.uploadExportFile");
			    
			    UIInput.make(uploadform, "retroalimentacionP:", "#{ashyiBean.retroalimentacionP}", null)
				.decorate(new UIDisabledDecorator(true));
			    
			    UIInput.make(uploadform, "retroalimentacionE:", "#{ashyiBean.retroalimentacionE}", null);
			   
			    String urlP = this.httpServletRequest.getRequestURL().toString();
				String[] cad = urlP.split("/");
				
				String url = "";
							
				UILink link = UILink
							.make(uploadform, "recursoUpload-link", "Revisar respuesta",url);
			    
				//no ver porque no se ha subido
			    disableLink(link, messageLocator);
			    UIOutput.make(tofill, "recursoError", messageLocator.getMessage("actividad.respuestaError"));
			}
					
//			FilePickerViewParameters viewAct = new FilePickerViewParameters(
//					ResourcePickerProducer.VIEW_ID);
//			viewAct.setSourcePage("VistaActividadProducer");
//			viewAct.setIdItemPAshyi(idTP);
//			viewAct.setIdItemUAshyi(idTU);
//			viewAct.setIdActividad(idActividad);
//			UILink link = UIInternalLink
//					.make(form, "recursoUpload-link", "Cargar respuesta", viewAct);
//			link.decorate(new UIFreeAttributeDecorator("target",
//					"_blank"));
						
						
						
			UIInput.make(uploadform, "nota:", "#{ashyiBean.notaA}", String.valueOf(iPlan.getNota()))
				.decorate(new UIDisabledDecorator(true));
			
			List<RespuestaItemsUsuario> respuestas = ashyiBean.getRespuestasItemUsuarioList(idTP, idTU);
			
			if(respuestas != null)
			{	
				System.out.println("Respuestas a mostrar: "+respuestas.size());
				//2 post condiciones
				String[] listaRtas = ashyiBean.getRespuestasItemUsuario(idTP, idTU);
				
				//UISelect selectR = UISelect.makeMultiple(uploadform, "repuestasS", listaRtas , "#{ashyiBean.personalidadesSeleccionadas}" , null);
				for (int j = 0; j < listaRtas.length; j++) {
					
					UIBranchContainer row = UIBranchContainer.make(uploadform, "repuestas:");
					
					GeneralViewParameters viewObj = new GeneralViewParameters(VistaActividadProducer.VIEW_ID);
					viewObj.setReturnView(VIEW_ID);	
					viewObj.setIdItemRAshyi(respuestas.get(j).getIdRespuesta());
					viewObj.setIdItemPAshyi(idTP);
					viewObj.setIdItemUAshyi(idTU);
					viewObj.setIdActividad(idActividad);
					viewObj.setItemId(params.getItemId());
					
					UIInternalLink.make(row, "listaRepuestas","Entregada en: "+listaRtas[j], viewObj).decorate(new UIFreeAttributeDecorator("title", "Entregada en: "+listaRtas[j]));
					UIOutput.make(row, "listaRepuestas-text", "Entregada en: "+listaRtas[j]);
					//UIOutput.make(row, "link-textactividad", listaRtas[index]);
					
					//UIOutput.make(row, "link-textR", entry);j
				}	
			}
			else
			{
				UIOutput.make(tofill, "repuestasS", messageLocator.getMessage("actividad.linkRespuestasE"));
			}
				
			//2 post condiciones
			String[] listaCompetencias = ashyiBean.getCompHabPostItemUsuario(iPlan, 2);
			String[] listaCLlenas = ashyiBean.getCaracLlenasItemUsuario(iPlan);
			UISelect selectC = UISelect.makeMultiple(uploadform, "competencias", listaCompetencias , "#{ashyiBean.competenciasSeleccionadas}" , listaCLlenas);
			int index = 0;
			for (String entry: listaCompetencias) {
				
				UIBranchContainer row = UIBranchContainer.make(uploadform, "pageC:");
				
				UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
				decorate(new UIFreeAttributeDecorator("title", entry)).decorate(new UIDisabledDecorator(true));
				UIOutput.make(row, "link-textC", entry)
				.decorate(new UIDisabledDecorator(true));
				index++;
			}	
			
		}
					
			UIOutput.make(tofill, "actividad-label", messageLocator.getMessage("actividad.nueva-actividad"));
			UIOutput.make(tofill, "descripcion-label", messageLocator.getMessage("actividad.descripcion-actividad"));
			
			
			UIOutput.make(tofill, "competenciasPost-label", messageLocator.getMessage("actividad.compHabResultado"));
			UIOutput.make(tofill, "nota-label", messageLocator.getMessage("actividad.nota"));
			UIOutput.make(tofill, "recurso-label", messageLocator.getMessage("actividad.linkRecurso"));
			
			UIOutput.make(tofill, "respuestas-label", messageLocator.getMessage("actividad.linkRespuestasL"));
				
			
//			UILink.make(tofill,"recurso-link",i.getName(),"http://lessonbuilder.sakaiproject.org/"+ i.getId() + "/")
//			.decorate(new UIFreeAttributeDecorator("title",messageLocator.getMessage("simplepage.copylink2").replace("{}",i.getName())));			
			
			if(iPlan.getIdItemPlan().getIdRecurso() != null)
			{
				String url2 = params.getSource();
				
				///access/lessonbuilder/item/65/group/93e96f13-6db8-4e58-8e5e-9d84fc1e86cc/recursosActividad1.txt
				if(i.getType() == 1 )
				{
					System.out.println("!!!!!!!!!!!!!!!!!!params: "+params.getSource());
					System.out.println("!!!!!!!!!!!!!!!!!!params: "+params.getItemId());
					String trozos[] = params.getSource().split("/");					
										
					//si el recurso no pertenece al curso actual
					if(idRUI < 0)
						if(!trozos[6].equalsIgnoreCase(ashyiBean.getCurrentSiteId()))
						{
							//habilitar el recurso para abrir
							url2 = ashyiBean.getServerName()+"access/content/";
							for(int ix = 5; ix < trozos.length; ix++)
								if(ix == 5)
									url2 = url2 + trozos[ix];
								else
									url2 = url2 + "/"+trozos[ix];
						}
				}
				System.out.println("NAme: "+i.getName()
				+"  params: "+url2);
				UILink.make(tofill,"link",i.getName(), url2)
				.decorate(new UIFreeAttributeDecorator("title",messageLocator.getMessage("simplepage.copylink2").replace("{}",i.getName())));
			}			
					
			ShowPageProducer sp = new ShowPageProducer();
			

			boolean navButton = "button".equals(i.getFormat())
					&& !i.isRequired();
			
			boolean notDone = false;
			Status status = Status.NOT_REQUIRED;
			if (!navButton) {
				status = sp.handleStatusImage(tofill, i);
				if (status == Status.REQUIRED) {
					notDone = true;
				}
			}
			
			// imagen relacionada al recurso de la actividad
			boolean isInline = (i.getType() == SimplePageItem.BLTI && "inline"
					.equals(i.getFormat()));
			UIOutput linktd = UIOutput.make(uploadform, "item-td");
			UIBranchContainer linkdiv = null;
			if (!isInline) {
				linkdiv = UIBranchContainer.make(uploadform, "link-div:");
				UIOutput itemicon = UIOutput.make(linkdiv, "item-icon");
				switch (i.getType()) {
				case SimplePageItem.FORUM:
					itemicon.decorate(new UIFreeAttributeDecorator(
							"src", "/library/image/silk/comments.png"));
					break;
				case SimplePageItem.ASSIGNMENT:
					itemicon.decorate(new UIFreeAttributeDecorator(
							"src", "/library/image/silk/page_edit.png"));
					break;
				case SimplePageItem.ASSESSMENT:
					itemicon.decorate(new UIFreeAttributeDecorator(
							"src", "/library/image/silk/pencil.png"));
					break;
				case SimplePageItem.BLTI:
					itemicon.decorate(new UIFreeAttributeDecorator(
							"src",
							"/library/image/silk/application_go.png"));
					break;
				case SimplePageItem.PAGE:
					itemicon.decorate(new UIFreeAttributeDecorator(
							"src", "/library/image/silk/book_open.png"));
					break;
				case SimplePageItem.RESOURCE:
					String mimeType = i.getHtml();

					if (mimeType == null || mimeType.equals("")) {
						String s = i.getSakaiId();
						int j = s.lastIndexOf(".");
						if (j >= 0)
							s = s.substring(j + 1);
						mimeType = ContentTypeImageService
								.getContentType(s);
						// System.out.println("type " + s + ">" +
						// mimeType);
					}

					String src = null;
					if (src == null) {
						String image = ContentTypeImageService
								.getContentTypeImage(mimeType);
						if (image != null)
							src = "/library/image/" + image;
					}

					if (src != null) {
						itemicon.decorate(new UIFreeAttributeDecorator(
								"src", src));
					}
					break;
				}
			}
			
			
			
//			showRefresh = !makeLink(tofill, "link", i,ashyiBean.canEditPage(), currentPage, notDone, status, sp)|| showRefresh;
			
		
			//UIInput file = UIInput.make(form, "recursoUpload:", "#{ashyiBean.recursoUpload}");
				
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
//		params = new GeneralViewParameters(VistaActividadProducer.VIEW_ID);
//		RespuestaItemsUsuario rIU = ashyiToolDao.getRespuestaItemUsuario(ashyiToolDao.getUltimaRespuesta());
//		params.setIdItemPAshyi(rIU.getIdItemPlan().getIdItemPlan());
//		params.setIdActividad(rIU.getIdItemPlan().getIdActividad().getIdActividad());
//		params.setIdItemUAshyi(rIU.getIdUsuario().getIdUsuario());
//		params.setIdItemRAshyi(rIU.getIdRespuesta());
//		togo.add(new NavigationCase("successR", params));
		//togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(VistaActividadProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", params));
		return togo;
	}			
	
	/**
	 * Deshabilitar un enlace
	 * @param link enlace a deshabilitar
	 * @param messageLocator mensaje de deshabilitar
	 */
	private static void disableLink(UIComponent link,
			MessageLocator messageLocator) {
		link.decorate(new UIFreeAttributeDecorator("onclick", "return false"));
		link.decorate(new UIDisabledDecorator());
		link.decorate(new UIStyleDecorator("disabled"));
		link.decorate(new UIFreeAttributeDecorator("style",
				"color:#999 !important"));
		link.decorate(new UITooltipDecorator(messageLocator
				.getMessage("simplepage.complete_required")));
	}
	
   /**
    * Cambio de color 
	 * @param todecorate componente a cambiar
	 */
	private void decorateRandomly(UIComponent todecorate) {
	    int colour = (int)(0xffffff);
	    Color c = new Color(colour);
	    c = c.brighter().brighter().brighter();
	    todecorate.decorators = new DecoratorList(new UIColourDecorator(null, c));
	  }
}
