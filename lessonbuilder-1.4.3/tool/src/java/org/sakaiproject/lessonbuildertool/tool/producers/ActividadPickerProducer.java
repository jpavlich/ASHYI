package org.sakaiproject.lessonbuildertool.tool.producers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.tool.beans.AshyiBean;
import org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.MemoryService;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
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
 * 
 * @author ASHYI
 * Productor de actividad
 * 
 */
public class ActividadPickerProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	/**
	 * ID que identifica al productor, por el que se encuentra el archivo html correspondiente
	 */
	public static final String VIEW_ID = "ActividadPicker";

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
			System.out.println("ActividadPicker permission exception " + e);
			return;
		    }
		}

                UIOutput.make(tofill, "html").decorate(new UIFreeAttributeDecorator("lang", localeGetter.get().getLanguage()))
		    .decorate(new UIFreeAttributeDecorator("xml:lang", localeGetter.get().getLanguage()));

		Long itemId = ((GeneralViewParameters) viewparams).getItemId();

		ashyiBean.setItemId(itemId);

		//profesor
		if (ashyiBean.canEditPage()) {
		        
			SimplePage page = ashyiBean.getCurrentPage();
			SimplePageItem pageItem = null;
			if (page != null) {
				pageItem = ashyiBean.getCurrentPageItem(params.getItemId());
			}
			
			String assignId = null; // default, normally current

			// if itemid is null, we'll append to current page, so it's ok
			if (itemId != null && itemId != -1) {
			    SimplePageItem currentItem = ashyiToolDao.findItem(itemId);
			    if (currentItem == null)
				return;
			    // trying to hack on item not on this page
			    if (currentItem.getPageId() != page.getPageId())
				return;
			    assignId = currentItem.getSakaiId();
			}
		

			UIForm form = UIForm.make(tofill, "actividadC");
		
			SimplePage currentPage = ashyiBean.getCurrentPage();
			
			ashyiBean.setCurrentPageId(currentPage.getPageId());
			if(params.author!=null)
				UIOutput.make(tofill, "actividad-label",params.author);
			
			UIOutput.make(tofill, "actividad-label", messageLocator.getMessage("actividad.nueva-actividad"));			
			UIOutput.make(tofill, "actividad-tipo", messageLocator.getMessage("actividad.tipo-actividad"));
			UIOutput.make(tofill, "actividad-recurso", messageLocator.getMessage("actividad.recurso-actividad"));
			UIOutput.make(tofill, "descripcion-label", messageLocator.getMessage("actividad.descripcion-actividad"));
			UIOutput.make(tofill, "goal-label", messageLocator.getMessage("actividad.goal-actividad"));
			UIOutput.make(tofill, "dedicacionActvidad-label", messageLocator.getMessage("actividad.dedicacion-actividad"));
			UIOutput.make(tofill, "time-label", messageLocator.getMessage("actividad.tiempo-actividad"));
			UIOutput.make(tofill, "time-initial-label", messageLocator.getMessage("actividad.tiempo-inicial-actividad"));
			UIOutput.make(tofill, "time-final-label", messageLocator.getMessage("actividad.tiempo-final-actividad"));
//			UIOutput.make(tofill, "charct-label", messageLocator.getMessage("actividad.charct-actividad"));
//			UIOutput.make(tofill, "estilo-label", messageLocator.getMessage("actividad.estilo"));
			UIOutput.make(tofill, "habilidades-label", messageLocator.getMessage("actividad.habilidades"));
			UIOutput.make(tofill, "competencias-label", messageLocator.getMessage("actividad.competencias"));
//			UIOutput.make(tofill, "personalidad-label", messageLocator.getMessage("actividad.personalidad"));
			UIOutput.make(tofill, "sa-label", messageLocator.getMessage("actividad.sa"));
//			UIOutput.make(tofill, "recursos-label", messageLocator.getMessage("actividad.recursos"));
//			UIOutput.make(tofill, "niveles-label", messageLocator.getMessage("actividad.niveles"));
//			UIOutput.make(tofill, "nivelesPC-label", messageLocator.getMessage("actividad.nivelespc"));			
			UIOutput.make(tofill, "precondiciones-label", messageLocator.getMessage("actividad.pre"));
			UIOutput.make(tofill, "contexto-label", messageLocator.getMessage("actividad.contexto"));
			UIOutput.make(tofill, "postcondiciones-label", messageLocator.getMessage("actividad.post"));
			UIOutput.make(tofill, "actividadIF-label", messageLocator.getMessage("actividad.inicialFinal"));
			
			int idActividad = params.getIdActividad();
			
			//actividad nueva
			if(idActividad == -1)
			{
				
				// Rich Text Input
				UIInput.make(form, "nombre:", "#{ashyiBean.nomberA}");
				//UIInput.make(form, "tipo:", "#{ashyiBean.tipoA}");
//				UIInput.make(form, "initial:", "#{ashyiBean.inicialF}");
//				UIInput.make(form, "final:", "#{ashyiBean.finalF}");
				UIInput.make(form, "descripcion:", "#{ashyiBean.descripcionA}");
				//UIInput.make(form, "goal:", "#{ashyiBean.objetivoA}");
				//3 porque son los objetivos de unidad didactica
				String nombreUnidad = ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getName();
				String[] listaObj = ashyiBean.getObjetivosActividad(3,nombreUnidad);
				
				UISelect select = UISelect.makeMultiple(form, "objetivos", listaObj , "#{ashyiBean.objetivosSeleccionados}" , null);
				int index = 0;
				for (String entry: listaObj) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "page:");
					
					UISelectChoice.make(row, "select", select.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-text", entry);
					index++;
				}	
				
				GeneralViewParameters viewObj = new GeneralViewParameters(NuevoObjetivoProducer.VIEW_ID);
				viewObj.setSendingPage(Long.valueOf(3));
				viewObj.setIdActividad(ashyiToolDao.getactividad(ashyiBean.getCurrentPage().getTitle(), 3).getIdActividad());
				viewObj.setItemId(ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getId());
				
				UIInternalLink.make(form, "nuevoObjetivo-link", viewObj);
				
				//String[] listaItems = ashyiBean.getHyCActividadSegunTipo(2,nombreUnidad);			
				
				UIInput.make(form, "dedicacionactividad:", "#{ashyiBean.dedicacionActividad}");
	//			UIInput.make(form, "nivele:", "#{ashyiBean.nivele}");
	//			UIInput.make(form, "nivelh:", "#{ashyiBean.nivelh}");
	//			UIInput.make(form, "nivelc:", "#{ashyiBean.nivelc}");
	//			UIInput.make(form, "nivelp:", "#{ashyiBean.nivelp}");
	//			UIInput.make(form, "nivelsa:", "#{ashyiBean.nivelsa}");
	//			UIInput.make(form, "nivelhpc:", "#{ashyiBean.nivelhpc}");
	//			UIInput.make(form, "nivelcpc:", "#{ashyiBean.nivelcpc}");
				
				//UIOutput.make(tofill, "coso","#{ashyiBean.nivele}");
				
				//UIInput.make(form, "listac:", "#{ashyiBean.caracteristicaSeleccionada}");			
				
				String tipoActividad = params.getTipoActividad();
				if(tipoActividad.contains("template-activityNode"))
				{
					String[] tiposA = new String [1];
					tiposA[0]=tipoActividad;
					UISelect.make(tofill, "listaTipos", tiposA,"#{ashyiBean.tipoA}","  "  );
				}
				else
				{
					String[] tiposA = ashyiBean.getTiposActividad();
					UISelect.make(tofill, "listaTipos", tiposA,"#{ashyiBean.tipoA}","  "  );	
				}
				
				String nombreCurso = ashyiBean.getCurrentSite().getTitle();
				String[] listaR = ashyiBean.getRecursosActividad(1,nombreCurso);
				UISelect.make(tofill, "listaRecursos", listaR,"#{ashyiBean.recursoA}","  "  );	
	
				UISelect.make(tofill, "listaContexto", ashyiBean.getContextos(),"#{ashyiBean.contextoS}","  "  );
				
				//String[] listaIF = ashyiBean.getOpcionesIniciaFinal();
				
				UISelect.make(tofill, "actividadIF_opciones", ashyiBean.getOpcionesIniciaFinal(),"#{ashyiBean.opcionIniciaFinal}","  "  );
//				UISelect selectIF = UISelect.makeMultiple(form, "actividadIF", listaIF , "#{ashyiBean.opcionIniciaFinal}" , null);
//				index = 0;
//				for (String entry: listaIF) {
//					
//					UIBranchContainer row = UIBranchContainer.make(form, "pageIF:");
//					
//					UISelectChoice.make(row, "actividadIF_opciones", selectIF.getFullID(), index).
//					decorate(new UIFreeAttributeDecorator("title", entry));
//					UIOutput.make(row, "link-textIF", entry);
//					index++;
//				}
				
				//UISelect.make(tofill, "listaHabilidades", ashyiBean.getHabilidades(),"#{ashyiBean.habilidadS}"," "  );
				String[] listaHabilidades = ashyiBean.getHabilidades();
				UISelect selectH = UISelect.makeMultiple(form, "habilidades", listaHabilidades , "#{ashyiBean.habilidadesSeleccionadas}" , null);
				
				index = 0;
				for (String entry: listaHabilidades) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageH:");
					
					UISelectChoice.make(row, "listaHabilidades", selectH.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textH", entry);
					index++;
				}
				
				//UISelect.make(tofill, "listaCompetencias", ashyiBean.getCompetencias(),"#{ashyiBean.competenciaS}"," "  );
				String[] listaCompetencias = ashyiBean.getCompetencias();
				UISelect selectC = UISelect.makeMultiple(form, "competencias", listaCompetencias , "#{ashyiBean.competenciasSeleccionadas}" , null);
				index = 0;
				for (String entry: listaCompetencias) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageC:");
					
					UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textC", entry);
					index++;
				}
				
				//UISelect.make(tofill, "listaCompetenciasPC", ashyiBean.getCompetencias(),"#{ashyiBean.competenciaSPC}"," "  );
				UISelect selectCP = UISelect.makeMultiple(form, "competenciasPC", listaCompetencias , "#{ashyiBean.competenciasPCSeleccionadas}" , null);
				index = 0;
				for (String entry: listaCompetencias) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageCP:");
					
					UISelectChoice.make(row, "listaCompetenciasPC", selectCP.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textCP", entry);
					index++;
				}
							
				//UISelect.make(tofill, "listaHabilidadesPC", ashyiBean.getHabilidades(),"#{ashyiBean.habilidadSPC}"," "  );
				UISelect selectHP = UISelect.makeMultiple(form, "habilidadesPC", listaHabilidades , "#{ashyiBean.habilidadesPCSeleccionadas}" , null);
				index = 0;
				for (String entry: listaHabilidades) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageHP:");
					
					UISelectChoice.make(row, "listaHabilidadesPC", selectHP.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textHP", entry);
					index++;
				}
				
	//			UISelect.make(tofill, "listaPersonalidad", ashyiBean.getPersonalidades(),"#{ashyiBean.personalidadS}"," "  );
				UISelect.make(tofill, "listaSA", ashyiBean.getSA(),"#{ashyiBean.saS}"," "  );
	//			UISelect.make(tofill, "listaEstilos", ashyiBean.getEstilos(),"#{ashyiBean.estiloS}"," "  );
		
	
				UIInput.make(form, "item-id", "#{ashyiBean.itemId}");
	
				UICommand.make(form, "submit", messageLocator.getMessage("actividad.crear"), "#{ashyiBean.addActividad}");
				
				UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
				
			}
			else //editar una actividad
			{
				Actividad a = ashyiToolDao.getActividad(idActividad);
				
				ashyiBean.setIdC(String.valueOf(a.getIdActividad()));
				
				UIInput.make(form, "idActividad", "#{ashyiBean.idC}");
				
				a.setCaracteristicas(ashyiToolDao.getCaracteristicasActividad(idActividad));
//				List<CaracteristicaActividad> lC = ashyiToolDao.getCaracteristicasActividad(idActividad);
				a.setObjetivo(ashyiToolDao.getObjetivosActividad(idActividad));
//				List<ObjetivosActividad> lO = ashyiToolDao.getObjetivosActividad(idActividad);
				a.setRecursos(ashyiToolDao.getRecursosActividad(idActividad));
//				List<RecursosActividad> lR = ashyiToolDao.getRecursosActividad(idActividad);
				// Rich Text Input
				UIInput.make(form, "nombre:", "#{ashyiBean.nomberA}", a.getNombre());
				//UIInput.make(form, "tipo:", "#{ashyiBean.tipoA}");
//				UIInput.make(form, "initial:", "#{ashyiBean.inicialF}", a.getFecha_inicial());
//				UIInput.make(form, "final:", "#{ashyiBean.finalF}", a.getFecha_final());
				UIInput.make(form, "descripcion:", "#{ashyiBean.descripcionA}", a.getDescripcion());
				//UIInput.make(form, "goal:", "#{ashyiBean.objetivoA}");
				//3 porque son los objetivos de unidad didactica
				String nombreUnidad = ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getName();
				String[] listaObj = ashyiBean.getObjetivosActividad(3,nombreUnidad);				
				
				
				String[] listaObjActividad = new String[a.getObjetivo().size()];
				int iObj = 0;
				List<ObjetivosActividad> objs = a.getObjetivo();
				for(ObjetivosActividad obj:objs)
				{
					listaObjActividad[iObj] = obj.getIdObjetivo().getNombre();
					iObj++;
				}
				
				UISelect select = UISelect.makeMultiple(form, "objetivos", listaObj , "#{ashyiBean.objetivosSeleccionados}" , listaObjActividad);
				int index = 0;
				for (String entry: listaObj) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "page:");
					
					UISelectChoice.make(row, "select", select.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-text", entry);
					index++;
				}	
				
				GeneralViewParameters viewObj = new GeneralViewParameters(NuevoObjetivoProducer.VIEW_ID);
				viewObj.setSendingPage(Long.valueOf(3));
				viewObj.setItemId(ashyiBean.getCurrentPageItem(ashyiBean.getItemId()).getId());
				
				UIInternalLink.make(form, "nuevoObjetivo-link", viewObj);
				
				//String[] listaItems = ashyiBean.getHyCActividadSegunTipo(2,nombreUnidad);			
				
				UIInput.make(form, "dedicacionactividad:", "#{ashyiBean.dedicacionActividad}", String.valueOf(a.getDedicacion()));		
				
				String tipoActividad = ((GeneralViewParameters) viewparams).getTipoActividad();
				if(tipoActividad.contains("template-activityNode"))
				{
					String[] tiposA = new String [1];
					tiposA[0]=tipoActividad;
					UISelect.make(tofill, "listaTipos", tiposA,"#{ashyiBean.tipoA}","  "  );
					String info = ((GeneralViewParameters) viewparams).getPosicionActividad();
					updateActividadTieneActividad(info);
				}
				else
				{
					String[] tiposA = ashyiBean.getTiposActividad();
					UISelect.make(tofill, "listaTipos", tiposA,"#{ashyiBean.tipoA}", a.getIdTipo().getNombre());	
				}
				
				List<CaracteristicaActividad> caracteristicas = a.getCaracteristicas();
				int hpre = 0, hpost = 0, cpre = 0,cpost = 0;
				
				String cxt = "", rAct = "", sas = "";
				
				List<RecursosActividad> rA = a.getRecursos();
				for(RecursosActividad r: rA)
				{					
					if(r.getIdRecurso().getIdItemSakai().isEmpty())
					{
						rAct = r.getIdRecurso().getNombre();
					}
				}
				
				for(CaracteristicaActividad c:caracteristicas)
				{
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 8)
						cxt = c.getIdCaracteristica().getNombre(); 
					
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 9)
						sas = c.getIdCaracteristica().getNombre(); 
						
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 5)
					{
						if(c.isPrecondicion())
						{
							hpre++;
						}
						if(c.isPostcondicion())
						{
							hpost++;
						}
					}
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 6)
					{
						if(c.isPrecondicion())
						{
							cpre++;
						}
						if(c.isPostcondicion())
						{
							cpost++;
						}
					}
				}		
				
				String[] listaHabilidadesActividadPre = new String[hpre];
				String[] listaHabilidadesActividadPost = new String[hpost];
				String[] listaCompetenciasActividadPre = new String[cpre];
				String[] listaCompetenciasActividadPost = new String[cpost];
				hpre = 0; hpost = 0; cpre = 0;cpost = 0;
				
				for(CaracteristicaActividad c:caracteristicas)
				{						
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 5)
					{
						if(c.isPrecondicion())
						{
							listaHabilidadesActividadPre[hpre] = c.getIdCaracteristica().getNombre();
							hpre++;
						}
						if(c.isPostcondicion())
						{
							listaHabilidadesActividadPost[hpost] = c.getIdCaracteristica().getNombre();
							hpost++;
						}
					}
					if(c.getIdCaracteristica().getIdItem().getIdItem() == 6)
					{
						if(c.isPrecondicion())
						{
							listaCompetenciasActividadPre[cpre] = c.getIdCaracteristica().getNombre();
							cpre++;
						}
						if(c.isPostcondicion())
						{
							listaCompetenciasActividadPost[cpost] = c.getIdCaracteristica().getNombre();
							cpost++;
						}
					}
				}	
				
				
				String nombreCurso = ashyiBean.getCurrentSite().getTitle();
				String[] listaR = ashyiBean.getRecursosActividad(1,nombreCurso);
				UISelect.make(tofill, "listaRecursos", listaR,"#{ashyiBean.recursoA}",rAct);	
	
				UISelect.make(tofill, "listaContexto", ashyiBean.getContextos(),"#{ashyiBean.contextoS}",cxt);
				
				String[] listaIF = ashyiBean.getOpcionesIniciaFinal();
				
				UISelect selectIF = UISelect.makeMultiple(form, "actividadIF", listaIF , "#{ashyiBean.opcionIniciaFinal}" , listaIF);
				index = 0;
				for (String entry: listaIF) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageIF:");
					
					UISelectChoice.make(row, "actividadIF_opciones", selectIF.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textIF", entry);
					index++;
				}
				
				String[] listaHabilidades = ashyiBean.getHabilidades();
				
				UISelect selectH = UISelect.makeMultiple(form, "habilidades", listaHabilidades , "#{ashyiBean.habilidadesSeleccionadas}" , listaHabilidadesActividadPre);
				
				index = 0;
				for (String entry: listaHabilidades) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageH:");
					
					UISelectChoice.make(row, "listaHabilidades", selectH.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textH", entry);
					index++;
				}
				
				//UISelect.make(tofill, "listaCompetencias", ashyiBean.getCompetencias(),"#{ashyiBean.competenciaS}"," "  );
				String[] listaCompetencias = ashyiBean.getCompetencias();
				UISelect selectC = UISelect.makeMultiple(form, "competencias", listaCompetencias , "#{ashyiBean.competenciasSeleccionadas}" , listaCompetenciasActividadPre);
				index = 0;
				for (String entry: listaCompetencias) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageC:");
					
					UISelectChoice.make(row, "listaCompetencias", selectC.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textC", entry);
					index++;
				}
				
				//UISelect.make(tofill, "listaCompetenciasPC", ashyiBean.getCompetencias(),"#{ashyiBean.competenciaSPC}"," "  );
				UISelect selectCP = UISelect.makeMultiple(form, "competenciasPC", listaCompetencias , "#{ashyiBean.competenciasPCSeleccionadas}" , listaCompetenciasActividadPost);
				index = 0;
				for (String entry: listaCompetencias) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageCP:");
					
					UISelectChoice.make(row, "listaCompetenciasPC", selectCP.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textCP", entry);
					index++;
				}
							
				//UISelect.make(tofill, "listaHabilidadesPC", ashyiBean.getHabilidades(),"#{ashyiBean.habilidadSPC}"," "  );
				UISelect selectHP = UISelect.makeMultiple(form, "habilidadesPC", listaHabilidades , "#{ashyiBean.habilidadesPCSeleccionadas}" , listaHabilidadesActividadPost);
				index = 0;
				for (String entry: listaHabilidades) {
					
					UIBranchContainer row = UIBranchContainer.make(form, "pageHP:");
					
					UISelectChoice.make(row, "listaHabilidadesPC", selectHP.getFullID(), index).
					decorate(new UIFreeAttributeDecorator("title", entry));
					UIOutput.make(row, "link-textHP", entry);
					index++;
				}
				
	//			UISelect.make(tofill, "listaPersonalidad", ashyiBean.getPersonalidades(),"#{ashyiBean.personalidadS}"," "  );
				UISelect.make(tofill, "listaSA", ashyiBean.getSA(),"#{ashyiBean.saS}",sas  );
	//			UISelect.make(tofill, "listaEstilos", ashyiBean.getEstilos(),"#{ashyiBean.estiloS}"," "  );
		
	
				UIInput.make(form, "item-id", "#{ashyiBean.itemId}");
	
				UICommand.make(form, "submit", messageLocator.getMessage("actividad.actualizar"), "#{ashyiBean.updateActividad}");
				
				UICommand.make(form, "delete", messageLocator.getMessage("actividad.eliminar"), "#{ashyiBean.deleteActividad}");
				
				UICommand.make(form, "cancel", messageLocator.getMessage("ashyiBean.cancel"), "#{ashyiBean.cancel}");
			}
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
		togo.add(new NavigationCase("failure", new SimpleViewParameters(ActividadPickerProducer.VIEW_ID)));
		
		togo.add(new NavigationCase("cancel", params));
//		togo.add(new NavigationCase("succesExamen", new SimpleViewParameters(QuizPickerProducer.VIEW_ID)));
//		togo.add(new NavigationCase("succesMultimedia", new SimpleViewParameters(ResourcePickerProducer.VIEW_ID)));
//		togo.add(new NavigationCase("succesTarea", new SimpleViewParameters(AssignmentPickerProducer.VIEW_ID)));
//		togo.add(new NavigationCase("succesRecurso", new SimpleViewParameters(ResourcePickerProducer.VIEW_ID)));
		togo.add(new NavigationCase("succesRecurso", new SimpleViewParameters(RegistroRecursoProducer.VIEW_ID)));
		togo.add(new NavigationCase("succesForo", new SimpleViewParameters(ForumPickerProducer.VIEW_ID)));
		return togo;
	}
		
	/**
	 * Actualizar json para las relaciones entre actividades
	 * @param info String con los datos de la actividad a editar
	 */
	public void updateActividadTieneActividad(String info)
	{
		JSONParser parser=new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(info);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String idSiguienteNivel= (String) jOb.get("id");
		String top= (String) jOb.get("top");
		String left= (String) jOb.get("left");
		String estiloSiguienteNivel="";
		estiloSiguienteNivel="\"top\":\""+top+"\",\"left\":\""+left+"\"";

		if(idSiguienteNivel.contains("Duplicater_"));
		{
			//crear actividad falsa
		}

		boolean result=ashyiBean.updateActividadTieneActividad(idSiguienteNivel, estiloSiguienteNivel, true);
		if(result)
			System.out.println("ATA updated");
		else
			System.out.println("Error updating ATA");
	}
}
