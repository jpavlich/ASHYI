package org.sakaiproject.lessonbuildertool.tool.producers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.model.ActividadesUsuario;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.LogCaracteristicas;
import org.sakaiproject.lessonbuildertool.model.LogGrafos;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Usuario;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.user.api.User;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ReportesProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter  {

	
	public static final String VIEW_ID = "Reportes";
	
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
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		GeneralViewParameters params = (GeneralViewParameters) viewparams;
		
		UIForm form = UIForm.make(tofill, "formulario");
		
		if (((GeneralViewParameters) viewparams).getSendingPage() != -1) {
		    // will fail if page not in this site
		    // security then depends upon making sure that we only deal with this page
		    try {
			ashyiBean.updatePageObject(((GeneralViewParameters) viewparams).getSendingPage());
		    } catch (Exception e) {
			System.out.println("contexto permission exception " + e);
			return;
		    }
		    
		    UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));		    
		    JSONArray list = new JSONArray();
		    JSONArray list2 = new JSONArray();
		    JSONArray listData1 = new JSONArray();
		    JSONArray listData2 = new JSONArray();
		    String tabla="";
			String nuevas="";
			String actual="";
			String yaTiene="";
			String eliminadas="";
			String noObtenidas="";
		    String idUsuarios="";
		    String mismosGrafos="";
			String origenGrafos="";
			String contextoGrafos="";
			String replanificacionGrafos="";
		    String nombre="";
		    List<User>usuarioSakai=ashyiBean.getUsuariosAshyi();
		    List<Integer> usuarios=ashyiToolDao.getUsuariosLogCaracteristicas();
		   
		   for(int i=0;i<usuarios.size();i++){
			   Usuario usuario=ashyiToolDao.getUsuario(usuarios.get(i));
			   
			   for(User u:usuarioSakai){

				   if(u.getId().equals(usuario.getIdUsuarioSakai())){
					   nombre=u.getFirstName()+" "+u.getLastName();
					   break;
				   }else{
					   nombre="";
				   }
			   }
			   
			 if(!nombre.equals("")){
			   
			   idUsuarios+=nombre+",";
				List<LogCaracteristicas> caracteristicasActividadUsuario=ashyiToolDao.getLogCaracteristicasUsuario(usuario);
				int actualesActividad=0;
				int yaObtenidasActividad=0;
				int nuevasActividad=0;
				int eliminadasActividad=0;
				int noObtenidasActividad=0;

				for(LogCaracteristicas car: caracteristicasActividadUsuario){
					String caracteristicaNombre="";
					 List<Caracteristica> caracteristicas=ashyiToolDao.getCaracteristica(car.getIdCaracteristica());
					 if(caracteristicas!=null){
					caracteristicaNombre= caracteristicas.get(0).getNombre();
					 }
					if(car.getIdTipoEvento()==0){
						
						ItemPlan itemPlan=ashyiToolDao.getItemPlan(car.getIdItemPlan());

						actualesActividad++;
						actual+=nombre+","+usuario.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"actual"+","+caracteristicaNombre+","+car.getFechaCalificacion()+";";
					}else if(car.getIdTipoEvento()==1){
						ItemPlan itemPlan=ashyiToolDao.getItemPlan(car.getIdItemPlan());
						yaObtenidasActividad++;
						yaTiene+=nombre+","+usuario.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"ya obtenida"+","+caracteristicaNombre+","+car.getFechaCalificacion()+";";
					}else if(car.getIdTipoEvento()==2){
						ItemPlan itemPlan=ashyiToolDao.getItemPlan(car.getIdItemPlan());
						nuevasActividad++;
						nuevas+=nombre+","+usuario.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"nueva"+","+caracteristicaNombre+","+car.getFechaCalificacion()+";";
					}else if(car.getIdTipoEvento()==3){
						ItemPlan itemPlan=ashyiToolDao.getItemPlan(car.getIdItemPlan());
						eliminadasActividad++;
						eliminadas+=nombre+","+usuario.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"eliminada"+","+caracteristicaNombre+","+car.getFechaCalificacion()+";";
					}else if(car.getIdTipoEvento()==4){
						ItemPlan itemPlan=ashyiToolDao.getItemPlan(car.getIdItemPlan());
						noObtenidasActividad++;
						noObtenidas+=nombre+","+usuario.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"no obtenida"+","+caracteristicaNombre+","+car.getFechaCalificacion()+";";
					} 
				}
				JSONObject obj = new JSONObject();
				JSONObject obj2 = new JSONObject();
				JSONObject obj3 = new JSONObject();
				
				

			      obj.put("actual",actualesActividad);
			      obj.put("yaObtenidas",yaObtenidasActividad);
			      obj.put("nuevas",nuevasActividad);
			      obj.put("eliminadas",eliminadasActividad);
			      obj.put("noObtenidas",noObtenidasActividad);
			      obj2.put("Estudiante",nombre);
			      obj2.put("freq", obj);
                  list.add(obj2);	
                  
                  obj3.put("Estudiante",nombre);
                  obj3.put("actual",actualesActividad);
			      obj3.put("yaObtenidas",yaObtenidasActividad);
			      obj3.put("nuevas",nuevasActividad);
			      obj3.put("eliminadas",eliminadasActividad);
			      obj3.put("noObtenidas",noObtenidasActividad);
                  listData1.add(obj3);
                  
                  JSONObject objGrafos = new JSONObject();
                  
  				JSONObject obj2GRafos = new JSONObject();
  				JSONObject obj3Grafos = new JSONObject();
  				
  				int mismos=0;
  				int replanificacion=0;
  				int origen=0;
  				int contexto=0;
  				List<LogGrafos> grafosUsuario=ashyiToolDao.getLogGrafosUsuario(usuario);
  				for(LogGrafos log:grafosUsuario){
  				if(log.getIdTipoEvento()==0){
  					ItemPlan itemPlan=ashyiToolDao.getItemPlan(log.getIdItemPlan());
  					mismos++;
  					mismosGrafos+=nombre+","+log.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"mismo grafo"+","+log.getIdGrafoActivo()+","+log.getIdGrafoAnterior()+","+log.getNota()+","+log.getFecha()+";";
  				}else if(log.getIdTipoEvento()==1){
  					ItemPlan itemPlan=ashyiToolDao.getItemPlan(log.getIdItemPlan());
  					replanificacion++;
  					replanificacionGrafos+=nombre+","+log.getIdUsuario()+","+itemPlan.getIdUnidadDidacticaPadre().getNombre()+","+itemPlan.getIdActividad().getNombre()+","+"replanificación"+","+log.getIdGrafoActivo()+","+log.getIdGrafoAnterior()+","+log.getNota()+","+log.getFecha()+";";
  				}else if(log.getIdTipoEvento()==2){
  					origen++;
  					origenGrafos+=nombre+","+log.getIdUsuario()+","+"NA"+","+"NA"+","+"origen"+","+log.getIdGrafoActivo()+","+log.getIdGrafoAnterior()+","+"NA"+","+log.getFecha()+";";
  				}else{
  					contexto++;
  					contextoGrafos+=nombre+","+log.getIdUsuario()+","+"NA"+","+"NA"+","+"contexto"+","+log.getIdGrafoActivo()+","+log.getIdGrafoAnterior()+","+"NA"+","+log.getFecha()+";";
  				}
  				}
  				
  				objGrafos.put("mismoGrafo", mismos);
  				objGrafos.put("replanificacion", replanificacion);
  				objGrafos.put("origen", origen);
  				objGrafos.put("contexto", contexto);
  				obj2GRafos.put("Estudiante",nombre);
  				obj2GRafos.put("freq",objGrafos);
  				list2.add(obj2GRafos);
  				
  				obj3Grafos.put("Estudiante",nombre);
  				obj3Grafos.put("mismoGrafo", mismos);
  				obj3Grafos.put("replanificacion", replanificacion);
  				obj3Grafos.put("origen", origen);
  				obj3Grafos.put("contexto", contexto);
  				
  				listData2.add(obj3Grafos);
			 } 				
		   }
		   

		   
		   
		   String usuActivos="";
		   List<Integer> activos=ashyiToolDao.getUsuariosActivos();
		   
		   for(User u:usuarioSakai){
				for(Integer ua:activos){
					Usuario a=ashyiToolDao.getUsuario(ua);
					if(u.getId().equals(a.getIdUsuarioSakai())){
						usuActivos+=u.getFirstName()+" "+u.getLastName()+",";
					}
				}
				
			}	   
						   
						
		   
		   if(!usuActivos.equals("")){
			   usuActivos=usuActivos.substring(0, usuActivos.length()-1);
			   ashyiBean.setUsuActivos(usuActivos);
			    UIOutput.make(tofill, "usuActivos", "",
						"#{ashyiBean.usuActivos}");
		   }
		   
		    
		   
		   
		   
		   tabla=actual+nuevas+yaTiene+noObtenidas+eliminadas;
		   if(!tabla.equals("")){
		   String datosTabla=tabla.substring(0, tabla.length()-1);
		   ashyiBean.setDatosTabla(datosTabla);
		    UIOutput.make(tofill, "datosTabla", "",
					"#{ashyiBean.datosTabla}");
		   }
		   
		   String tabla2=mismosGrafos+replanificacionGrafos+origenGrafos+contextoGrafos;
		   if(!tabla2.equals("")){
			   String datosTabla2=tabla2.substring(0, tabla2.length()-1);
			   ashyiBean.setDatosTabla2(datosTabla2);
			    UIOutput.make(tofill, "datosTabla2", "",
						"#{ashyiBean.datosTabla2}");
		   }
		   
		   String idUsers=idUsuarios;
		    
               if(!idUsers.equals("")){
            	   idUsers=idUsuarios.substring(0,idUsuarios.length()-1);
		    ashyiBean.setUsuarios(idUsers);
		    UIOutput.make(tofill, "usuarios", "",
					"#{ashyiBean.usuarios}");
               }
		   
            ashyiBean.setReportesLabel(list.toString());
		    UIOutput.make(tofill, "reportesLabel", "",
					"#{ashyiBean.reportesLabel}");
		    
		    ashyiBean.setReportesLabel2(listData1.toString());
		    UIOutput.make(tofill, "reportesLabel2", "",
					"#{ashyiBean.reportesLabel2}");
		    
		    ashyiBean.setDatosGrafos(list2.toString());
			   UIOutput.make(tofill, "datosGrafos", "",
						"#{ashyiBean.datosGrafos}");
			   
			   ashyiBean.setDatosGrafos2(listData2.toString());
			   UIOutput.make(tofill, "datosGrafos2", "",
						"#{ashyiBean.datosGrafos2}");

		}		
		UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
		
	}

	
		public String getViewID() {
			return VIEW_ID;
		}

		public ViewParameters getViewParameters() {
			return new GeneralViewParameters();
		}

		public List reportNavigationCases() {
			List<NavigationCase> togo = new ArrayList<NavigationCase>();
			togo.add(new NavigationCase("success", new SimpleViewParameters(ShowPageProducer.VIEW_ID)));
			togo.add(new NavigationCase("failure", new SimpleViewParameters(this.VIEW_ID)));
			GeneralViewParameters params = new GeneralViewParameters(ShowPageProducer.VIEW_ID);			
			params.setIdItemPAshyi(0);
			togo.add(new NavigationCase("cancel", params));
			return togo;
		}

}