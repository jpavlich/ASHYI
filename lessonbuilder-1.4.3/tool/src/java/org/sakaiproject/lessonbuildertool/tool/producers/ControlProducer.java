package org.sakaiproject.lessonbuildertool.tool.producers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.lessonbuildertool.SimplePage;
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
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIOutput;
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
 * Productor de Control de cambios en las caracteristicas de dominio
 * 
 */
public class ControlProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "Control";
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
	 * @param dao objeto de acceso a datos de Sakai
	 */
	public void setSimplePageToolDao(Object dao) {
		simplePageToolDao = (SimplePageToolDao) dao;
	}
    
//    public void setAssignmentEntity(LessonEntity l) {
//		assignmentEntity = l;
//	}

	public String getViewID() {
		return VIEW_ID;
	}

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
			
		UIForm form = UIForm.make(tofill, "controlC");
		SimplePage currentPage = ashyiBean.getCurrentPage();
		
		UIOutput.make(tofill, "coso", String.valueOf(ashyiBean.getCurrentUserId()));
		
		ashyiBean.setCurrentPageId(currentPage.getPageId());
		
		UIOutput.make(tofill, "control-advertencia", messageLocator.getMessage("control-advertencia"));
					
		UIInput.make(form, "item-id", "#{ashyiBean.itemId}");
		UIInput.make(form, "nombre:", "#{ashyiBean.nombre}");

		UICommand.make(form, "submit", messageLocator.getMessage("ashyiBean"), "#{ashyiBean.addControl}");
		UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
	
	}

	public ViewParameters getViewParameters() {
		return new GeneralViewParameters();
	}

	public List reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>();
		togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		togo.add(new NavigationCase("failure", new SimpleViewParameters(ControlProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
		return togo;
	}
	
	
	public String myUrl() {
	    // previously we computed something, but this will give us the official one
	        return ServerConfigurationService.getServerUrl();
	}
	
	private boolean UrlOk(String url) {
		Boolean cached = (Boolean) urlCache.get(url);
		if (cached != null)
		    return (boolean) cached;

		if (url.startsWith("http:") || url.startsWith("https:")) {
		    // actual URL, check it out

		    try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestMethod("HEAD");
			con.setConnectTimeout(30 * 1000);
			boolean ret = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
			urlCache.put(url, (Boolean) ret);
			return ret;
		    } catch (java.net.SocketTimeoutException e) {
			log.error("Internationalization url lookup timed out for " + url + ": Please check lessonbuilder.helpfolder. It appears that the host specified is not responding.");
			urlCache.put(url, (Boolean) false);
			return false;
		    } catch (ProtocolException e) {
			urlCache.put(url, (Boolean) false);
			return false;
		    } catch (IOException e) {
			urlCache.put(url, (Boolean) false);
			return false;
		    }
		} else {
		    // remove the leading /sakai-lessonbuildertool-tool, since getresource is
		    // relative to the top of the servlet
		    int i = url.indexOf("/", 1);
		    url = url.substring(i);
		    try {
			// inside the war file, check the file system. That avoid issues
			// with odd deployments behind load balancers, where the user's URL may not
			// work from one of the front ends
			if (httpServletRequest.getSession().getServletContext().getResource(url) == null) {
			    urlCache.put(url, (Boolean) false);
			    return false;
			} else {
			    urlCache.put(url, (Boolean) true);
			    return true;
			}
		    } catch (Exception e) {  // probably malfformed url
			log.error("Internationalization url lookup failed for " + url + ": " + e);
			urlCache.put(url, (Boolean) true);
			return true;
		    }

		}
	}
}
