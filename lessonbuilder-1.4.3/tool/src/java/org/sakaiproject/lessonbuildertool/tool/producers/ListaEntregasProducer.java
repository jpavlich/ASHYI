package org.sakaiproject.lessonbuildertool.tool.producers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import uk.org.ponder.rsf.components.UILink;
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
public class ListaEntregasProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "ListaEntregas";
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
		
		SimplePage currentPage = ashyiBean.getCurrentPage();
		
		ashyiBean.setCurrentPageId(currentPage.getPageId());	
		
		if(params.getIdActividad() != -1)
		{	
			System.out.println("Ob: "+params.getIdActividad()+" act: "+params.getIdItemPAshyi());
			String sSistemaOperativo = System.getProperty("os.name");
			String nombreArchivo = ashyiBean.getCurrentSite().getTitle()+"-Actividades-"+ashyiBean.getCurrentPage().getTitle()+"-"+params.getIdActividad()+"-"+params.getIdItemPAshyi()+".zip";
			//System.out.println(sSistemaOperativo);
			try {
				
				List<String> nombresArchivos=ashyiBean.getRespuestasObjetivoUnidad(params.getIdActividad(), params.getIdItemPAshyi());
				List<String> usuariosArchivos=ashyiBean.getUsuariosRespuestasObjetivoUnidad(params.getIdActividad(), params.getIdItemPAshyi());
							
				//crear .zip
				if(nombresArchivos != null)
				{
					ZipOutputStream os=null;
					String path  ="";
					if(sSistemaOperativo.contains("Windows"))				
					{
						os = new ZipOutputStream(new FileOutputStream(new File(messageLocator.getMessage("ashyibean.pathArchivosW"),nombreArchivo)));
						path = messageLocator.getMessage("ashyibean.pathArchivosW")+"\\";
					}
					else
					{
						os = new ZipOutputStream(new FileOutputStream(new File(messageLocator.getMessage("ashyibean.pathArchivosL"),nombreArchivo)));//Aqui le dan el nombre y/o con la ruta del archivo salida
						path = messageLocator.getMessage("ashyibean.pathArchivosL")+"/";
					}					
					
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
					
					int i = 0;
					for(String n : nombresArchivos)
					{
						ZipEntry entrada = new ZipEntry(usuariosArchivos.get(i)+"-"+n);
						os.putNextEntry(entrada);
						FileInputStream fis = new FileInputStream(path+n);
						byte [] buffer = new byte[1024];
						int leido=0;
						while (0 < (leido=fis.read(buffer))){
						   os.write(buffer,0,leido);
						}
						fis.close();
						os.closeEntry();
						i++;
					}
					os.close();
										
					url += nombreArchivo;
					
					UILink link = UILink.make(form, "zip-link", messageLocator.getMessage("recurso.zipdescarga"),url);
					UIOutput.make(form, "registro.nuevoobjetivo", messageLocator.getMessage("recurso.zipdescarga"));
				}
				else
				{					
					UIOutput.make(tofill, "error", messageLocator.getMessage("recurso.zipdescargaVacio"));	
				}
				
				UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.back"), "#{ashyiBean.back}");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{					    
			Actividad unidad = ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3);
			List<ObjetivosActividad> objetivos = ashyiToolDao.getObjetivosActividad(unidad.getIdActividad());
			String[] listaObjetivos = simplePageBean.getObjetivosActividad( 3, unidad.getNombre());
			for(int i = 0; i < objetivos.size();i ++)
			{
				if(objetivos.get(i).getTipo()==2)
				{					
					objetivos.remove(i);
					i--;
				}
			}
			if(listaObjetivos.length > 0)
			{
				//listado de objetivos
				UISelect selectOU = UISelect.makeMultiple(form, "actividadesSpan", listaObjetivos,
						"#{simplePageBean.objetivosSeleccionados}", null);		
				UIOutput.make(form, "actividades-label",messageLocator.getMessage("recurso.listaActividades"));
				
				for (int i = 0; i < listaObjetivos.length; i++) {
	
					UIBranchContainer row = UIBranchContainer.make(form, "actividad:");
	
					GeneralViewParameters viewObj = new GeneralViewParameters(ListaEntregasProducer.VIEW_ID);
					viewObj.setReturnView(VIEW_ID);	
					viewObj.setIdActividad(objetivos.get(i).getIdObjetivo().getIdObjetivo());
					viewObj.setIdItemPAshyi(unidad.getIdActividad());					
					
					UIInternalLink.make(row, "actividad-link",listaObjetivos[i], viewObj).decorate(new UIFreeAttributeDecorator("title",listaObjetivos[i]));
					
					UIOutput.make(row, "link-textactividad", listaObjetivos[i]);
				}
										
			}
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
		//togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(ListaEntregasProducer.VIEW_ID)));
		togo.add(new NavigationCase("back", new SimpleViewParameters(ListaEntregasProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", params));
		return togo;
	}
}