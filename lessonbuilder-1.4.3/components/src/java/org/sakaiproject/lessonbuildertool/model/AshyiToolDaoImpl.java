package org.sakaiproject.lessonbuildertool.model;

/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *Dao de la aplicaci&oacuten
 *Contiene las consultas relacionadas a ASHYI para Sakai
 *Configuraci&oacuten para hibernate con base a la configuraci&oacuten de sakai
 *@author ASHYI
 *
 **********************************************************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.db.cover.SqlService;
import org.sakaiproject.event.cover.EventTrackingService;
import org.sakaiproject.lessonbuildertool.ActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasUsuarioImpl;
import org.sakaiproject.lessonbuildertool.ControlImpl;
import org.sakaiproject.lessonbuildertool.GrafoImpl;
import org.sakaiproject.lessonbuildertool.ItemImpl;
import org.sakaiproject.lessonbuildertool.ItemPlanImpl;
import org.sakaiproject.lessonbuildertool.ObjetivoImpl;
import org.sakaiproject.lessonbuildertool.ObjetivosActividadImpl;
import org.sakaiproject.lessonbuildertool.PalabraClaveImpl;
import org.sakaiproject.lessonbuildertool.RecursoImpl;
import org.sakaiproject.lessonbuildertool.RecursosActividadImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageComment;
import org.sakaiproject.lessonbuildertool.SimplePageGroup;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntry;
import org.sakaiproject.lessonbuildertool.TipoImpl;
import org.sakaiproject.lessonbuildertool.UsuarioImpl;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Control;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.PalabraClave;
import org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.Tipo;
import org.sakaiproject.lessonbuildertool.model.Usuario;

/**
 * @author ashiy
 * Implementaci&oacuten de la interface AshyiToolDao.
 */
public class AshyiToolDaoImpl extends HibernateDaoSupport implements AshyiToolDao {
	/**
	 * Atributo del AshyiToolDaoImpl.
	 */
	private static Log log = LogFactory.getLog(AshyiToolDaoImpl.class);

	/**
	 * Atributo del AshyiToolDaoImpl.
	 */
	private ToolManager toolManager;
	/**
	 * Atributo del AshyiToolDaoImpl.
	 */
	private SecurityService securityService;
	/**
	 * Atributo del AshyiToolDaoImpl.
	 */
	private static String SITE_UPD = "site.upd";
	/**
	 * Atributo del AshyiToolDaoImpl.
	 */
	private DataSource dataSource;

	/**
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getDataSource()
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}

	// part of HibernateDaoSupport; this is the only context in which it is OK
	// to modify the template configuration
	/* (non-Javadoc)
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	protected void initDao() throws Exception {
		super.initDao();
		getHibernateTemplate().setCacheQueries(true);
		log.info("initDao template " + getHibernateTemplate());
	}

	// the permissions model here is preliminary. I'm not convinced that all the code in
	// upper layers checks where it should, so the Dao is supplying an extra layer of
	// protection. As far as I can tell, any database change should be done by
	// someone with update privs, except that add or update to the log is done on
	// behalf of normal people. I've checked all the code that does save or update for
	// log entries and it looks OK.

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#canEditPage()
	 */
	public boolean canEditPage() {
		String ref = null;
		// no placement, startup testing, should be an advisor in place
		try {
			ref = "/site/" + toolManager.getCurrentPlacement().getContext();
		} catch (java.lang.NullPointerException ignore) {
			ref = "";
		}
		return securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_UPDATE, ref);
	}

	/**
	 * Retorna si se puede editar una p&aacutegina.
	 * @param pageId
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean canEditPage(long pageId) {
		boolean canEdit = canEditPage();
		// forced comments have a pageid of -1, because they are associated with
		// more than one page. But the student can't edit them anyway, so fail it
		if(!canEdit && pageId != -1L) {
			SimplePage page = getPage(pageId);
			if(UserDirectoryService.getCurrentUser().getId()
					.equals(page.getOwner())) {
				canEdit = true;
			}
		}

		return canEdit;
	}

	/**
	 * Retorna una p&aacutegina a partir de su identificador.
	 * @param pageId
	 * @return SimplePage - la p&aacutegina.
	 */
	public SimplePage getPage(long pageId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePage.class).add(Restrictions.eq("pageId", pageId));

		List l = getHibernateTemplate().findByCriteria(d);

		if (l != null && l.size() > 0) {
			return (SimplePage) l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna si se puede editar una p&aacutegina.
	 * @param owner
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean canEditPage(String owner) {
		boolean canEdit = canEditPage();
		if(owner != null && !canEdit) {
			if(owner.equals(UserDirectoryService.getCurrentUser().getId())) {
				canEdit = true;
			}
		}

		return canEdit;
	}

	/**
	 * Asigna el atributo securityService.
	 * @param service
	 */
	public void setSecurityService(SecurityService service) {
		securityService = service;
	}

	/**
	 * Asigna el atributo toolManager.
	 * @param service
	 */
	public void setToolManager(ToolManager service) {
		toolManager = service;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#findItem(long)
	 */
	public SimplePageItem findItem(long id) {

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("id", id));
		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna una acausa de una excepci&oacuten.
	 * @param t - la excepci&oacuten.
	 * @param elist - la lista de errores.
	 */
	public void getCause(Throwable t, List<String>elist) {
		while (t.getCause() != null) {
			t = t.getCause();
		}
		log.warn("error saving or updating: " + t.toString());
		elist.add(t.getLocalizedMessage());
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof Actividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof Actividad) {
				Integer id = -1;
				Actividad i = (Actividad)o;	
				id = (Integer) getHibernateTemplate().save(i);
				//getHibernateTemplate().save(o);
				//getHibernateTemplate().setFlushMode(FlushMode.COMMIT);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				//getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().commit();

				//		    	


				//    			i.setCaracteristicas(caracteristicaNueva);
				//		    	
				//		    	getHibernateTemplate().merge(i);

				//getHibernateTemplate().merge(i);
				//    			i.setIdActividad(((Actividad)ac).getIdActividad());
				//    			getHibernateTemplate().flush();
				//    			

				//getHibernateTemplate().update(i);
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
				//			updateStudentPage(o);
			} 


			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveObjetivoActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveObjetivoActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof ObjetivosActividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			System.out.println("En saveObjetivoActividad en ashyiToolDaoImpl");
			getHibernateTemplate().save(o);
			//getHibernateTemplate().setFlushMode(FlushMode.COMMIT);
			getHibernateTemplate().flush();
			//getHibernateTemplate().clear();
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveGrafoRelaciones(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveGrafoRelaciones(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		getHibernateTemplate().save(o);
		//		getHibernateTemplate().flush();
		//		getHibernateTemplate().clear();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveGrafo(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveGrafo(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		getHibernateTemplate().save(o);
		return true;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveGrafosUsuario(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveGrafosUsuario(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		getHibernateTemplate().save(o);
		//				getHibernateTemplate().flush();
		//				getHibernateTemplate().clear();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveItemPlan(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveItemPlan(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		if(requiresEditPermission && !(o instanceof ItemPlan )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof ItemPlan) {
				Integer id = -1;
				ItemPlan i = (ItemPlan)o;	
				id = (Integer) getHibernateTemplate().save(i);
				//getHibernateTemplate().setFlushMode(FlushMode.COMMIT);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/"+i.getIdItemPlan(), true));
			} 

			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimaActividad(java.lang.String)
	 */
	public Integer getUltimaActividad(String nombre)
	{
		Integer id = 0;

		Object [] fields = new Object[1];
		fields[0] = nombre;
		List<String> list = SqlService.dbRead("SELECT MAX(idActividad) AS id FROM actividad WHERE nombre = ?",fields,null);		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimaActividad()
	 */
	public Integer getUltimaActividad()
	{
		Integer id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idActividad) AS id FROM actividad");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoGrafo()
	 */
	public Integer getUltimoGrafo()
	{
		Integer id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idGrafo) AS id FROM grafo");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoGrafoCompleto()
	 */
	public Grafo getUltimoGrafoCompleto()
	{
		List list = getSession().createSQLQuery("SELECT MAX(idGrafo) AS id FROM grafo").list();		

		Integer id=0;

		if(list.size() > 0)    	
		{
			id = Integer.valueOf(String.valueOf(list.get(0)));
			DetachedCriteria d = DetachedCriteria.forClass(Grafo.class).add(Restrictions.eq("idGrafo",id));

			List<Grafo> listR = getHibernateTemplate().findByCriteria(d);

			if(listR.size() > 0)
			{
				return listR.get(0);
			}
		}

		return new GrafoImpl(0,0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoRecurso()
	 */
	public Recurso getUltimoRecurso()
	{
		List<String> list = SqlService.dbRead("SELECT MAX(idRecurso) AS id FROM recurso");		

		Integer id=0;

		if(list.size() > 0)    	
		{
			id = Integer.valueOf(list.get(0)) ;
			DetachedCriteria d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",id));

			List<Recurso> listR = getHibernateTemplate().findByCriteria(d);

			if(listR.size() > 0)
			{
				return listR.get(0);
			}
		}

		return new RecursoImpl("Does not exist","No aplica","");
	}


	//public boolean saveCaracteristicasActividades(List<CaracteristicaActividad> caracteristicas)
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveCaracteristicasActividades(org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad)
	 */
	public boolean saveCaracteristicasActividades(CaracteristicaActividad caracteristicas)
	{
		getHibernateTemplate().save(caracteristicas);
		getHibernateTemplate().flush();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveObject(java.lang.Object)
	 */
	public boolean saveObject(Object o) {

		getHibernateTemplate().save(o);
		//getHibernateTemplate().flush();
		return true;

		//		try {
		//			getHibernateTemplate().save(o);
		//			//getHibernateTemplate().flush();
		//		} catch (org.springframework.dao.DataIntegrityViolationException e) {
		//			//getCause(e);
		//			return false;
		//		} catch (org.hibernate.exception.DataException e) {
		//			//getCause(e);
		//			return false;
		//		} catch (DataAccessException e) {
		//			//getCause(e);
		//			return false;
		//		}
		//		
		//		return true;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveCaracteristica(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveCaracteristica(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof Caracteristica ) 
				&& !(o instanceof RecursosActividad ) 
				&& !(o instanceof ObjetivosActividad ) 
				&& !(o instanceof Objetivo)
				&& !(o instanceof Recurso) 
				&& !(o instanceof ActividadTieneActividad)
				&& !(o instanceof Control)
				&& !(o instanceof Item)
				&& !(o instanceof CaracteristicaRecurso)
				&& !(o instanceof Usuario)
				&& !(o instanceof CaracteristicasUsuario)
				&& !(o instanceof PalabraClave)
				&& !(o instanceof PalabraClaveActividad)
				&& !(o instanceof CaracteristicasItemsUsuario)
				&& !(o instanceof RespuestaItemsUsuario)
				&& !(o instanceof Tipo)) {
			elist.add(nowriteerr);
			return false;
		}

		try {
			getHibernateTemplate().save(o);
			//getHibernateTemplate().flush();

			if (o instanceof CaracteristicasUsuario) {
				CaracteristicasUsuario i = (CaracteristicasUsuario)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Usuario) {
				Usuario i = (Usuario)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof CaracteristicaRecurso) {
				CaracteristicaRecurso i = (CaracteristicaRecurso)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Caracteristica) {
				Caracteristica i = (Caracteristica)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof ObjetivosActividad) {
				ObjetivosActividad i = (ObjetivosActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" , true));
				//		updateStudentPage(o);
			} 

			if (o instanceof RecursosActividad) {
				RecursosActividad i = (RecursosActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecursosActividad(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Objetivo) {
				Objetivo i = (Objetivo)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdObjetivo(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof ActividadTieneActividad) {
				ActividadTieneActividad i = (ActividadTieneActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
				//		updateStudentPage(o);
			} 	 

			if (o instanceof Recurso) {
				Recurso i = (Recurso)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecurso(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Control) {
				Control i = (Control)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdcontrol_ashyi(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Tipo) {
				Tipo i = (Tipo)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdTipo(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Item) {
				Item i = (Item)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdItem(), true));
				//		updateStudentPage(o);
			}

			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#deleteItem(java.lang.Object)
	 */
	public boolean deleteItem(Object o) {
		/*
		 * If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 * permissions on it. If the item isn't SimplePageItem or SimplePage, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(!(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
				&& (o instanceof SimplePage || o instanceof SimplePageItem)) {
			return false;
		}

		if (o instanceof SimplePageItem) {
			SimplePageItem i = (SimplePageItem)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/item/" + i.getId(), true));
		} else if (o instanceof SimplePage) {
			SimplePage i = (SimplePage)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/page/" + i.getPageId(), true));
		} else if(o instanceof SimplePageComment) {
			SimplePageComment i = (SimplePageComment) o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/comment/" + i.getId(), true));
		}

		try {
			getHibernateTemplate().delete(o);
			return true;
		} catch (DataAccessException e) {
			try {

				/* If we have multiple objects of the same item, you must merge them
				 * before deleting.  If the first delete fails, we merge and try again.
				 */
				getHibernateTemplate().delete(getHibernateTemplate().merge(o));

				return true;
			}catch(DataAccessException ex) {
				ex.printStackTrace();
				log.warn("Hibernate could not delete: " + e.toString());
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#update(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean update(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
				&& !(o instanceof SimplePageLogEntry)
				&& !(o instanceof SimplePageGroup)
				&& !(o instanceof Actividad)
				&& !(o instanceof ActividadTieneActividad)
				&& !(o instanceof ActividadTieneItemPlan)
				&& !(o instanceof ItemsUsuario)
				&& !(o instanceof RespuestaItemsUsuario)
				&& !(o instanceof ItemPlan)) {
			elist.add(nowriteerr);
			return false;
		}

		if (o instanceof SimplePageItem) {
			SimplePageItem i = (SimplePageItem)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/item/" + i.getId(), true));
		} else if (o instanceof SimplePage) {
			SimplePage i = (SimplePage)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/page/" + i.getPageId(), true));
		}

		try {
			if(!(o instanceof SimplePageLogEntry)&& !(o instanceof Actividad)) {
				getHibernateTemplate().merge(o);
			}
			else {
				// Updating seems to always update the timestamp on the log correctly,
				// while merging doesn't always get it right.  However, it's possible that
				// update will fail, so we do both, in order of preference.
				try {
					getHibernateTemplate().update(o);
				}catch(DataAccessException ex) {
					log.warn("Wasn't able to update log entry, timing might be a bit off.");
					getHibernateTemplate().merge(o);
				}
			}		    
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeActividad(org.sakaiproject.lessonbuildertool.model.Tipo, org.sakaiproject.lessonbuildertool.model.Item, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public Actividad makeActividad(Tipo tipo, Item item, String name, String description, Integer nR, Integer dedicacion, String nivel) {		
		return new ActividadImpl(tipo, item, name, description, nR, dedicacion, nivel);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeActividad(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public Actividad makeActividad(Item item, String name, Integer nR,Integer dedicacion, String nivel) {
		return new ActividadImpl(item, name, nR, dedicacion, nivel);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeCaracteristica(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String)
	 */
	public CaracteristicaImpl makeCaracteristica(Item item, String name) {		
		return new CaracteristicaImpl(item, name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeTipo(java.lang.String)
	 */
	public TipoImpl makeTipo(String name) {
		return new TipoImpl(name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeItemAshyi(java.lang.String, java.lang.String)
	 */
	public Item makeItemAshyi(String name, String testC)
	{
		return new ItemImpl( name, testC);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeControl(java.lang.String, boolean)
	 */
	public ControlImpl makeControl(String usuarioId, boolean cambio) {
		Integer cambiar = 1;
		if(cambio)
			cambiar = 0;
		return new ControlImpl(usuarioId, cambiar);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemsCaracterisiticas()
	 */
	public List<Item> getItemsCaracterisiticas()
	{
		DetachedCriteria d = DetachedCriteria.forClass(Item.class);

		List<Item> list = getHibernateTemplate().findByCriteria(d);

		return  list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTiposCaracteristicas()
	 */
	public List<Item> getTiposCaracteristicas() {

		//id item > 3, para los tipos de caracteristicas y no sea 8 porque 8 es contexto

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.gt("idItem",3)).add(Restrictions.ne("idItem",8));

		List<Item> list = getHibernateTemplate().findByCriteria(d);


		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 2");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 2");


		//		if (list.size() > 0) {
		//			Integer i = 0;
		////			for(Caracteristica c : list)
		////			{
		////				listNombres[i]=c.getNombre();
		////				i++;
		////			}
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}

		//return listTipos;
		return  list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getEditarDominioAshyi()
	 */
	public Integer getEditarDominioAshyi() {
		DetachedCriteria d = DetachedCriteria.forClass(Control.class);//.add(Restrictions.eq("toolId", toolId)).add(Restrictions.isNull("parent"));

		d.addOrder(Order.asc("idcontrol_ashyi"));
		List<Control> list = getHibernateTemplate().findByCriteria(d);

		if (list.size() > 0) {

			if(list.get(list.size() - 1).getEditar_dominio() == 1)
				return 1;
		}
		else if (list.size() == 0)
		{
			return 1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTiposContexto()
	 */
	public List<Item> getTiposContexto() {

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",8));

		List<Item> list = getHibernateTemplate().findByCriteria(d);

		//		List<Tipo> listTipos = new ArrayList<Tipo>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 3");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 3");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		////			for(Caracteristica c : list)
		////			{
		////				listNombres[i]=c.getNombre();
		////				i++;
		////			}
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}
		//				
		//		return listTipos;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTiposActividad()
	 */
	public List<Tipo> getTiposActividad() {

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).addOrder(Order.asc("idTipo"));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);

		//		List<Tipo> listTipos = new ArrayList<Tipo>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 1");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 1");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}
		//				
		//		return listTipos;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getHabilidades()
	 */
	public List<Caracteristica> getHabilidades() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",5)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 12");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 12");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(12,12,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getContextos()
	 */
	public List<Caracteristica> getContextos() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",8)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 12");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 12");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(12,12,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCompetencias()
	 */
	public List<Caracteristica> getCompetencias() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",6)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 13");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 13");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(13,13,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getPersonalidades()
	 */
	public List<Caracteristica> getPersonalidades() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",7)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 11");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 11");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(11,11,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getSA()
	 */
	public List<Caracteristica> getSA() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",9)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 14");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 14");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(14,14,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getEstilos()
	 */
	public List<Caracteristica> getEstilos() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",4)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 10");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 10");
		//
		//
		//		if (list.size() > 0) {
		//			Integer i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(10,15,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTipos()
	 */
	public List<Tipo> getTipos(){

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).addOrder(Order.asc("idTipo"));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#eliminar(java.lang.Object)
	 */
	public void eliminar(Object editar) {

		getHibernateTemplate().delete(editar);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristica(java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristica(Integer id){

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idCaracteristica",id));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTipo(java.lang.Integer)
	 */
	public List<Tipo> getTipo(Integer id){

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("idTipo",id));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemId(java.lang.Integer)
	 */
	public Item getItemId(Integer id){

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",id));

		List<Item> list = getHibernateTemplate().findByCriteria(d);
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividad(java.lang.Integer)
	 */
	public Actividad getActividad(Integer id){		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("idActividad",id));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
			return new ActividadImpl();
		/*else
		{
			list.get(0).setObjetivo(getObjetivosActividad(list.get(0).getIdActividad()));
			return list.get(0);
		}*/
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemAshyi(java.lang.String)
	 */
	public Item getItemAshyi(String nombre)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("nombre",nombre));

		List<Item> listT = getHibernateTemplate().findByCriteria(d);

		if(listT.size() > 0)
		{
			return listT.get(0);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#registrarCurso(java.lang.String)
	 */
	public String registrarCurso(String titulo)
	{
		String respuesta = "El curso no se ha registrado";

		Actividad ac = getactividad(titulo,1);

		if(ac.getNombre().equals("Does not exist"))
		{
			Item i = getItemAshyi("Actividad Macro");

			if(i != null)
			{
				List<String>elist = new ArrayList<String>();

				Actividad a = new ActividadImpl(i, titulo, 1, 100, "Universitario");

				boolean respSave = saveActividad(a,elist, "Error Almacenar Actividad", true);

				if(respSave)
					respuesta = "Curso registrado";
			}
		}
		else
		{
			respuesta = "Curso registrado";
		}		

		return respuesta;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getTipo(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String)
	 */
	public Tipo getTipo(Item item, String nombre) {
		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("idItem.idItem",item.getIdItem())).add(Restrictions.eq("nombre",nombre));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getactividad(java.lang.String, java.lang.Integer)
	 */
	public Actividad getactividad(String titulo, Integer nivelRecursividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("nombre",titulo)).add(Restrictions.eq("nivel_recursividad",nivelRecursividad));

		List<Actividad> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0);
		}
		Actividad a = new ActividadImpl();
		a.setNombre("Does not exist");
		return a;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoItem()
	 */
	public Integer getUltimoItem()
	{
		Integer id = 0;
		List<String> list = SqlService.dbRead("SELECT MAX(idItem) AS id FROM item");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoObjetivo(java.lang.String)
	 */
	public Integer getUltimoObjetivo(String nombre)
	{
		Integer id = 0;

		Object [] fields = new Object[1];
		fields[0] = nombre;

		List<String> list = SqlService.dbRead("SELECT MAX(idObjetivo) AS id FROM objetivo WHERE nombre = ?",fields,null);		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoObjetivo()
	 */
	public Integer getUltimoObjetivo()
	{
		Integer id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idObjetivo AS id FROM Objetivo");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivosActividad(java.lang.Integer, java.lang.String)
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre) {

		Object [] fields = new Object[2];
		fields[0] = nivelRecursividad;
		fields[1] = nombre;

		List<String> list = SqlService.dbRead("select o.nombre from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo and oa.tipo = 1", fields, null);


		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			Integer i = 0;
			for(String c : list)
			{
				listNombres[i]=c;
				i++;
			}
		}
		return listNombres;
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivoActividad(java.lang.String, java.lang.Integer, java.lang.String)
	 */
	public Integer getObjetivoActividad(String nombre, Integer nR, String nombreAc)
	{
		Object [] fields = new Object[3];
		fields[0] = nombre;
		fields[1] = nR;
		fields[2] = nombreAc;

		List<String> list = SqlService.dbRead("select o.idObjetivo from objetivo o, objetivos_actividad oa, actividad a where o.nombre = ? and o.idObjetivo = oa.idObjetivo and oa.idActividad = a.idActividad and a.nivel_recursividad = ? and a.nombre = ?", fields, null);

		Integer id = -1;
		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			id = Integer.valueOf(list.get(0));
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemName(java.lang.Long)
	 */
	public String getItemName(Long itemId)
	{		
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("id",itemId));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0).getName();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getDependenciasActividades()
	 */
	public List<DependenciaActividad> getDependenciasActividades()
	{
		DetachedCriteria d = DetachedCriteria.forClass(DependenciaActividad.class).addOrder(Order.asc("idActividad"));
		List<DependenciaActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getDependenciaActividad(java.lang.Integer, java.lang.Integer)
	 */
	public List<DependenciaActividad> getDependenciaActividad(Integer act, Integer actDependiente)
	{
		DetachedCriteria d = DetachedCriteria.forClass(DependenciaActividad.class).add(Restrictions.eq("idActividad",act)).add(Restrictions.eq("Id_Actividad_Dependiente",actDependiente));
		List<DependenciaActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveDependenciaActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveDependenciaActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission)
	{
		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof DependenciaActividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof DependenciaActividad) {
				Integer id = -1;
				DependenciaActividad i = (DependenciaActividad)o;	
				id = (Integer) getHibernateTemplate().save(i);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				System.out.println("en saveDependenciaActividad en simplepagetooldao");
				//			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
			} 
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadTieneActividades(java.lang.Integer)
	 */
	public List<ActividadTieneActividad> getActividadTieneActividades(Integer id)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		for(int i = 0; i < list.size(); i++)
		{			
			list.get(i).setIdActividad(getActividad(list.get(i).getIdActividad().getIdActividad()));
			list.get(i).setIdActividadSiguienteNivel(getActividad(list.get(i).getIdActividadSiguienteNivel().getIdActividad()));

			if(list.get(i).getIdActividadSiguienteNivel().isEs_refuerzo())
			{
				list.remove(i);
				i--;
			}
		}
		return list;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadTieneActividad(java.lang.Integer, java.lang.Integer)
	 */
	public List<ActividadTieneActividad> getActividadTieneActividad(Integer id, Integer idSiguienteNivel)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividadSiguienteNivel.idActividad",idSiguienteNivel)).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadTieneActividadString(java.lang.Integer)
	 */
	public List<String> getActividadTieneActividadString(Integer id)
	{
		//Object [] fields = new Object[1];
		//fields[0] = id;
		//List<String> list = SqlService.dbRead("select a.idActividadSiguienteNivel.idActividad, a.estiloActividadSiguienteNivel from ACTIVIDAD_TIENE_ACTIVIDAD a where a.idActividad.idActividad = ?", fields, null);

		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		List<String> listNombres=new ArrayList<String>();

		if (list.size() > 0) {
			Integer i = 0;	
			for(ActividadTieneActividad c : list)
			{
				listNombres.add(""+c.getIdActividadSiguienteNivel().getIdActividad());
				i++;
			}
		}

		return listNombres;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getEstiloSiguienteNivelActTieneAct(java.lang.Integer, java.lang.Integer)
	 */
	public List<String> getEstiloSiguienteNivelActTieneAct(Integer id, Integer idSiguienteNivel)
	{

		//		Object [] fields = new Object[2];
		//		fields[0] = id;
		//		fields[1] = idSiguienteNivel;
		//		List<String> list = SqlService.dbRead("select a.estiloActividadSiguienteNivel from ACTIVIDAD_TIENE_ACTIVIDAD where idActividad. = ? and a.idActividadSiguienteNivel = ?", fields, null);

		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividad.idActividad",id)).add(Restrictions.eq("idActividadSiguienteNivel.idActividad",idSiguienteNivel));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		List<String> listNombres=new ArrayList<String>();

		if (list.size() > 0) {
			Integer i = 0;	
			for(ActividadTieneActividad c : list)
			{
				listNombres.add(c.getEstiloActividadSiguienteNivel());
				i++;
			}
		}

		return listNombres;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividades(java.lang.Integer)
	 */
	public List<Actividad> getActividades(Integer id){		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("idActividad",id));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#deleteObject(java.lang.Object)
	 */
	public boolean deleteObject(Object o) {
		if (o instanceof DependenciaActividad) {
			DependenciaActividad i = (DependenciaActividad)o;
		}
		if (o instanceof CaracteristicaActividad) {
			CaracteristicaActividad i = (CaracteristicaActividad)o;
		}
		if (o instanceof Actividad) {
			Actividad i = (Actividad)o;
		}
		if (o instanceof GrafoRelaciones) {
			GrafoRelaciones i = (GrafoRelaciones)o;
		}
		try {
			getHibernateTemplate().delete(o);
			return true;
		} catch (DataAccessException e) {
			try {

				/* If we have multiple objects of the same item, you must merge them
				 * before deleting.  If the first delete fails, we merge and try again.
				 */
				getHibernateTemplate().delete(getHibernateTemplate().merge(o));

				return true;
			}catch(DataAccessException ex) {
				ex.printStackTrace();
				log.warn("Hibernate could not delete: " + e.toString());
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasTipo(java.lang.String)
	 */
	public List<CaracteristicasTipo> getCaracteristicasTipo(String nombreTipo){	

		DetachedCriteria dT = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("nombre",nombreTipo));
		List<Tipo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{

			DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasTipo.class).add(Restrictions.eq("idTipo",list.get(0))).addOrder(Order.asc("linea"));
			List<CaracteristicasTipo> listC = getHibernateTemplate().findByCriteria(d);	

			return listC;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveActividadTieneActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveActividadTieneActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission)
	{
		if(requiresEditPermission && !(o instanceof ActividadTieneActividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof ActividadTieneActividad) {
				Integer id = -1;
				ActividadTieneActividad i = (ActividadTieneActividad)o;	
				id = (Integer) getHibernateTemplate().save(i);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				System.out.println("en saveActividadTieneActividad en simplepagetooldao");
				//			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
			} 
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#saveActividadTieneItemPlan(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveActividadTieneItemPlan(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission)
	{
		getHibernateTemplate().save(o);
		getHibernateTemplate().flush();
		return true;
	}

	public Recurso getRecurso(String nombre)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("nombre",nombre));
		List<Recurso> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRecursosActividad(java.lang.Integer, java.lang.String)
	 */
	public List<RecursosActividad> getRecursosActividad(Integer nivelRecursividad, String nombre)
	{	
		Actividad ac = this.getactividad(nombre,nivelRecursividad);

		DetachedCriteria d = DetachedCriteria.forClass(RecursosActividad.class).add(Restrictions.eq("idActividad.idActividad",ac.getIdActividad()));
		List<RecursosActividad> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(Integer i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listC.get(i).getIdRecurso().getIdRecurso()));
				List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

				listC.get(i).setIdRecurso(listT.get(0));
			}
			return listC;
		}
		return new ArrayList<RecursosActividad>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivo(java.lang.String)
	 */
	public Objetivo getObjetivo(String objetivo)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("nombre",objetivo));
		List<Objetivo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new ObjetivoImpl("Does not exist");
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivoPorId(java.lang.Integer)
	 */
	public Objetivo getObjetivoPorId(Integer idObjetivo)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("idObjetivo",idObjetivo));
		List<Objetivo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new ObjetivoImpl("Does not exist");
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasActividad(java.lang.Integer)
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<CaracteristicaActividad> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(Integer i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",listC.get(i).getIdCaracteristica().getIdItem().getIdItem()));
				List<Item> listT = getHibernateTemplate().findByCriteria(d);

				listC.get(i).getIdCaracteristica().setIdItem(listT.get(0));
				listC.get(i).setIdActividad(getActividad(listC.get(i).getIdActividad().getIdActividad()));
			}
			return listC;
		}
		return new ArrayList<CaracteristicaActividad>();
	}

	/**
	 * @param idObjetivo
	 * @return
	 */
	public Objetivo getObjetivo(Integer idObjetivo)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("idObjetivo",idObjetivo));
		List<Objetivo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new ObjetivoImpl("Does not exist");
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivosActividad(java.lang.Integer)
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<ObjetivosActividad> listO = getHibernateTemplate().findByCriteria(d);
		if(listO.size()>0)
		{
			for(Integer i = 0;i<listO.size();i++)
			{
				listO.get(i).setIdObjetivo(getObjetivo(listO.get(i).getIdObjetivo().getIdObjetivo()));
				listO.get(i).setIdActividad(getActividad(listO.get(i).getIdActividad().getIdActividad()));
			}
			return listO;
		}

		return new ArrayList<ObjetivosActividad>();
	}


	/**
	 * Retorna los objetivosActividad a partir del identificador de actividad y de objetivo.
	 * @param idActividad
	 * @param idObjetivo
	 * @return List<ObjetivosActividad> - los ObjetivoActividad.
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad, Integer idObjetivo)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad)).add(Restrictions.eq("idObjetivo.idObjetivo",idObjetivo));
		List<ObjetivosActividad> listO = getHibernateTemplate().findByCriteria(d);		

		if(listO.size()>0)
		{
			for(Integer i = 0;i<listO.size();i++)
			{
				listO.get(i).setIdObjetivo(getObjetivo(listO.get(i).getIdObjetivo().getIdObjetivo()));
				listO.get(i).setIdActividad(getActividad(listO.get(i).getIdActividad().getIdActividad()));
			}
			return listO;
		}

		return new ArrayList<ObjetivosActividad>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRecursosActividad(java.lang.Integer)
	 */
	public List<RecursosActividad> getRecursosActividad(Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(RecursosActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<RecursosActividad> listR = getHibernateTemplate().findByCriteria(d);

		if(listR.size()>0)
		{
			for(Integer i = 0;i<listR.size();i++)
			{
				d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listR.get(i).getIdRecurso().getIdRecurso()));
				List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

				listR.get(i).setIdRecurso(listT.get(0));
			}

			return listR;
		}
		return new ArrayList<RecursosActividad>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadCompleta(java.lang.Integer)
	 */
	public Actividad getActividadCompleta(Integer id){	

		Actividad ac = getActividad(id);

		ac.setCaracteristicas(this.getCaracteristicasActividad(ac.getIdActividad()));
		ac.setObjetivo(this.getObjetivosActividad(ac.getIdActividad()));
		ac.setRecursos(this.getRecursosActividad(ac.getIdActividad()));

		return ac;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadAltoNivel(java.lang.Integer)
	 */
	public Actividad getActividadAltoNivel(Integer id)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividadSiguienteNivel.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		Actividad ac = new ActividadImpl();
		if(list.size() > 0)
		{
			ac = this.getActividad(list.get(0).getIdActividad().getIdActividad());
		}

		return ac;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#deleteActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void deleteActividad(Actividad a)
	{		
		Object [] fields = new Object[1];
		fields[0] = a.getIdActividad();
		boolean elimina  = SqlService.dbWrite("delete from actividad where idActividad = ?", fields);

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#isItUsuario(org.sakaiproject.lessonbuildertool.model.Usuario)
	 */
	public boolean isItUsuario(Usuario usuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuarioSakai",usuario.getIdUsuarioSakai())).add(Restrictions.eq("rol",usuario.getRol())).add(Restrictions.eq("nivel", usuario.getNivel()));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUsuario(java.lang.Integer)
	 */
	public Usuario getUsuario(Integer idUsuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuario",idUsuario));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
			return new UsuarioImpl();
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoUsuario(java.lang.String)
	 */
	public Integer getUltimoUsuario(String idUsuarioSakai)
	{
		Integer id = 0;

		Object [] fields = new Object[1];
		fields[0] = idUsuarioSakai;

		List<String> list = SqlService.dbRead("SELECT MAX(idUsuario) AS id FROM usuario WHERE idUsuarioSakai = ?",fields,null);		

		if(list.size() == 0)
			id = -1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicaRecursos(org.sakaiproject.lessonbuildertool.model.Recurso, org.sakaiproject.lessonbuildertool.model.Caracteristica)
	 */
	public CaracteristicaRecurso getCaracteristicaRecursos(Recurso idRecurso, Caracteristica idCaracteristica)
	{

		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaRecurso.class).add(Restrictions.eq("idRecurso.idRecurso",idRecurso)).add(Restrictions.eq("idCaracteristica.idCaracteristica", idCaracteristica));
		List<CaracteristicaRecurso> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listC.get(0).getIdRecurso().getIdRecurso()));
			List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

			listC.get(0).setIdRecurso(listT.get(0));
			return listC.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#flush()
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getEstilosUsuario(java.lang.Integer)
	 */
	public List<CaracteristicasUsuario> getEstilosUsuario(Integer idUsuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario));
		List<CaracteristicasUsuario> listC = getHibernateTemplate().findByCriteria(d);	
		if(listC.size()>0)
		{
			for(Integer i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuario",listC.get(i).getIdUsuario().getIdUsuario()));
				List<Usuario> listT = getHibernateTemplate().findByCriteria(d);

				d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idCaracteristica",listC.get(i).getIdCaracteristica().getIdCaracteristica()));
				List<Caracteristica> listCa = getHibernateTemplate().findByCriteria(d);

				listC.get(i).setIdCaracteristica(listCa.get(0));

				listC.get(i).setIdUsuario(listT.get(0));
			}
			return listC;
		}
		return new ArrayList<CaracteristicasUsuario>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getPalabra(java.lang.String)
	 */
	public PalabraClave getPalabra(String palabraClave)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(PalabraClave.class).add(Restrictions.eq("palabra",palabraClave));
		List<PalabraClave> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new PalabraClaveImpl("Does not exist",0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimaPalabra(java.lang.String)
	 */
	public Integer getUltimaPalabra(String palabra)
	{
		Integer id = 0;

		Object [] fields = new Object[1];
		fields[0] = palabra;

		List<String> list = SqlService.dbRead("SELECT MAX(idPalabraClave) AS id FROM PalabraClave WHERE palabra = ?",fields,null);		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getPalabrasClave(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public List<PalabraClave> getPalabrasClave(Integer tipo, String nombreActividad, Integer nR)
	{
		Actividad ac = this.getactividad(nombreActividad, nR);

		DetachedCriteria dT = DetachedCriteria.forClass(PalabraClaveActividad.class).add(Restrictions.eq("idActividad.idActividad",ac.getIdActividad()));

		List<PalabraClaveActividad> listPalabraActividad =  getHibernateTemplate().findByCriteria(dT);

		List<PalabraClave> listDevolver = new ArrayList<PalabraClave>(); 

		for(Integer i = 0; i <listPalabraActividad.size() ; i++)
		{

			dT = DetachedCriteria.forClass(PalabraClave.class).add(Restrictions.eq("tipo",tipo)).add(Restrictions.eq("idPalabraClave", listPalabraActividad.get(i).getIdPalabraClave().getIdPalabraClave()));
			List<PalabraClave> list = getHibernateTemplate().findByCriteria(dT);	

			if(list.size() > 0)
			{	
				listDevolver.add(list.get(0));
			}
		}

		if(listDevolver.size() > 0)
		{	
			return listDevolver;
		}

		return new ArrayList<PalabraClave>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getIdsObjetivosActividad(java.lang.Integer)
	 */
	public List<List> getIdsObjetivosActividad(Integer idActividad)
	{
		List<ObjetivosActividad> objs = getObjetivosActividad(idActividad);

		List<List> idsObjs = new ArrayList();

		for(ObjetivosActividad obj :  objs)
		{
			List ids = new ArrayList();
			ids.add(obj.getIdObjetivo().getIdObjetivo());
			ids.add(obj.getIdActividad().getIdActividad());

			idsObjs.add(ids);
		}

		return idsObjs;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadRecurso(java.lang.Integer, java.lang.String)
	 */
	public RecursosActividad getActividadRecurso(Integer idActividad, String idRecursoSakai)
	{		
		RecursosActividad lA = new RecursosActividadImpl();

		Actividad a = this.getActividad(idActividad);

		List<RecursosActividad> rA =this.getRecursosActividad(a.getNivel_recursividad(),a.getNombre());

		Integer estar = 0;
		for(Integer i = 0;i<rA.size();i++)
		{
			if(rA.get(i).getIdRecurso().getIdItemSakai()!=null)
				if(rA.get(i).getIdRecurso().getIdItemSakai().equals(idRecursoSakai))
				{
					lA.setIdActividad(a);
					lA.setIdRecurso(rA.get(i).getIdRecurso());
					return lA;
				}

		}
		a = new ActividadImpl();
		a.setNombre("Does not exist");
		lA.setIdActividad(a);
		return lA;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#actividadesRecurso(java.util.List, org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public List<RecursosActividad> actividadesRecurso(List<SimplePageItem> items, Actividad actividad)
	{
		List<RecursosActividad> actividadesRecursos = new ArrayList<RecursosActividad>();
		for(SimplePageItem item : items)
		{					
			RecursosActividad aAgregar = getActividadRecurso(actividad.getIdActividad(), String.valueOf(item.getId()));
			if(!aAgregar.getIdActividad().getNombre().equals("Does not exist"))
			{
				actividadesRecursos.add(aAgregar);
			}
		}

		return actividadesRecursos;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasUsuario(java.lang.Integer, java.lang.Integer)
	 */
	public Integer getCaracteristicasUsuario(Integer idUsuario, Integer tipoC)
	{
		Integer id = 0;

		Object [] fields = new Object[2];
		fields[0] = idUsuario;
		fields[1] = tipoC;

		List<String> list = SqlService.dbRead("SELECT cu.idCaracteristica as id FROM caracteristicas_usuario cu, caracteristica c WHERE cu.idUsuario = ? AND c.idCaracteristica = cu.idCaracteristica AND c.idItem = ?",fields,null);		

		if(list.size() > 0)
		{
			id = list.size();
		}

		return id;
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestasUsuario(java.lang.String, java.lang.Integer)
	 */
	public boolean getRespuestasUsuario(String idUsuario, Integer encuesta)
	{
		Object [] fields = new Object[2];
		fields[0] = idUsuario;
		fields[1] = encuesta;

		List list = SqlService.dbRead("SELECT p.VOTE_OPTION as v FROM poll_vote p WHERE p.USER_ID = ? AND p.VOTE_POLL_ID = ?",fields,null);

		//si ha llenado la encuesta
		if(list.size() > 0)
		{
			for(Integer i = 0; i < list.size(); i++)
			{
				Object [] field = new Object[1];
				field[0] = Integer.valueOf((String) list.get(i));
				CaracteristicasUsuario cU = new CaracteristicasUsuarioImpl();
				List<String> listS = SqlService.dbRead("SELECT OPTION_TEXT FROM poll_option WHERE OPTION_ID = ?",field,null);

				if(listS.size() > 0)
				{
					DetachedCriteria dT = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.like("nombre",listS.get(0)));				
					List<Caracteristica> listC =  getHibernateTemplate().findByCriteria(dT);
					if(listC.size()>0)
					{
						cU.setIdCaracteristica(listC.get(0));
						cU.setIdUsuario(getUsuario(getUltimoUsuario(idUsuario)));
						cU.setNivel(1);
						//almacenar caracteristica
						saveCaracteristica(cU, new ArrayList<String>(), "Error", true);
					}
				}
			}

			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeItemPlan(org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Recurso, boolean, java.lang.Integer)
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad, Recurso recurso,
			boolean estaActivo, Integer orden) {
		return new ItemPlanImpl(ud, actividad, recurso, estaActivo, orden);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#makeItemPlan(org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Actividad, boolean, java.lang.Integer)
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad, boolean estaActivo, Integer orden) {
		return new ItemPlanImpl(ud, actividad, estaActivo, orden);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadesRefuerzo(java.lang.String)
	 */
	public boolean getActividadesRefuerzo(String nActividades)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("es_refuerzo",true)).add(Restrictions.eq("nivel",nActividades));
		List<Actividad> listActividad =  getHibernateTemplate().findByCriteria(dT);

		if(listActividad.size() > 0)
			return true;
		else 
			return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#itemEsta(org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Recurso, java.lang.Integer, boolean, java.lang.Integer)
	 */
	public boolean itemEsta(Actividad ud, Actividad auxActividad, Recurso auxRecurso, Integer tipo, boolean isRefuerzo, Integer orden)
	{
		DetachedCriteria dT = null;
		if(tipo == 1)
		{
			if(isRefuerzo)
				dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.eq("idRecurso.idRecurso",auxRecurso.getIdRecurso())).add(Restrictions.eq("orden",orden));
			else
				dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.eq("idRecurso.idRecurso",auxRecurso.getIdRecurso())).add(Restrictions.eq("orden",orden));
		}
		else if(tipo == 2)
		{
			if(isRefuerzo)
				dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.isNull("idRecurso.idRecurso")).add(Restrictions.eq("orden",orden));
			else
				dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.isNull("idRecurso.idRecurso")).add(Restrictions.eq("orden",orden));
		}

		List<ItemPlan> listItems =  getHibernateTemplate().findByCriteria(dT);

		if(listItems.size() > 0)
			return true;
		else 
		{
			if(tipo == 1)
			{
				if(isRefuerzo)
					dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.eq("idRecurso.idRecurso",auxRecurso.getIdRecurso())).add(Restrictions.eq("orden",-1));
				else
					dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.eq("idRecurso.idRecurso",auxRecurso.getIdRecurso())).add(Restrictions.eq("orden",-1));
			}
			else if(tipo == 2)
			{
				if(isRefuerzo)
					dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.isNull("idRecurso.idRecurso")).add(Restrictions.eq("orden",-1));
				else
					dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad.idActividad",auxActividad.getIdActividad())).add(Restrictions.isNull("idRecurso.idRecurso")).add(Restrictions.eq("orden",-1));
			}
			listItems =  getHibernateTemplate().findByCriteria(dT);

			if(listItems.size() > 0)
			{
				listItems.get(0).setOrden(orden);
				this.update(listItems.get(0), new ArrayList<String>(), "",true);
				return true;
			}
			else
				return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemPlan(java.lang.Integer)
	 */
	public ItemPlan getItemPlan(Integer idItem) {
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idItemPlan",idItem));
		List<ItemPlan> listC = getHibernateTemplate().findByCriteria(d);

		if(listC.size()>0)
		{
			listC.get(0).setIdActividad(getActividad(listC.get(0).getIdActividad().getIdActividad()));
			return listC.get(0);
		}
		return new ItemPlanImpl();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemPlan(org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Actividad, org.sakaiproject.lessonbuildertool.model.Recurso, java.lang.Integer, java.lang.Integer)
	 */
	public ItemPlan getItemPlan(Actividad ud, Actividad auxActividad, Recurso auxRecurso,Integer tipo, Integer orden)
	{
		DetachedCriteria dT = null;
		if(tipo == 1)
			dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad",auxActividad)).add(Restrictions.eq("idRecurso",auxRecurso)).add(Restrictions.eq("orden", orden));
		if(tipo == 2)
			dT = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",  ud.getIdActividad())).add(Restrictions.eq("idActividad",auxActividad)).add(Restrictions.isNull("idRecurso")).add(Restrictions.eq("orden", orden));
		List<ItemPlan> listItems =  getHibernateTemplate().findByCriteria(dT);

		if(listItems.size()>0)
			return listItems.get(0);
		return new ItemPlanImpl();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#isGrafoUsuario(java.lang.String, java.lang.String)
	 */
	public boolean isGrafoUsuario(String usuarioSakai, String titulo)
	{
		Integer idUsuario = getUltimoUsuario(usuarioSakai);

		Actividad a = getactividad(titulo, 3);

		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario)).add(Restrictions.eq("idActividad",a.getIdActividad()));
		List<Usuario> listU = getHibernateTemplate().findByCriteria(d);

		if(!listU.isEmpty())
		{
			return true;
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemUsuario(java.lang.Integer, java.lang.Integer)
	 */
	public ItemsUsuario getItemUsuario(Integer idTP, Integer idTU)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(ItemsUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idTU)).add(Restrictions.eq("idItemPlan.idItemPlan",idTP));
		List<ItemsUsuario> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{	
			list.get(0).setIdItemPlan(getItemPlan(list.get(0).getIdItemPlan().getIdItemPlan()));
			list.get(0).setIdUsuario(getUsuario(list.get(0).getIdUsuario().getIdUsuario()));
			return list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadTieneItemPlan(java.lang.Integer, java.lang.Integer)
	 */
	public List<ActividadTieneItemPlan> getActividadTieneItemPlan(Integer id, Integer idItemPlan)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneItemPlan.class).add(Restrictions.eq("idItemPlan.idItemPlan",idItemPlan)).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneItemPlan> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getGrafo(int)
	 */
	public Grafo getGrafo(int idGrafo) {
		DetachedCriteria d = DetachedCriteria.forClass(Grafo.class).add(Restrictions.eq("idGrafo", idGrafo));
		List<Grafo> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
			return list.get(0);
		else
			return new GrafoImpl();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getGrafoUsuario(org.sakaiproject.lessonbuildertool.model.Usuario, java.lang.Integer)
	 */
	public List<GrafosUsuario> getGrafoUsuario(Usuario idUsuario, Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario.getIdUsuario())).add(Restrictions.eq("idActividad",idActividad));
		List<GrafosUsuario> listU = getHibernateTemplate().findByCriteria(d);
		for(GrafosUsuario gu : listU)
			gu.setIdGrafo(getGrafo(gu.getIdGrafo().getIdGrafo()));
		return listU;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#deleteGrafoRelaciones(org.sakaiproject.lessonbuildertool.model.Grafo)
	 */
	public void deleteGrafoRelaciones(Grafo idGrafo)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo",idGrafo.getIdGrafo()));
		List<GrafoRelaciones> listU = getHibernateTemplate().findByCriteria(d);

		for (Integer i = 0; i < listU.size(); i++) {
			GrafoRelaciones rel=(GrafoRelaciones) listU.get(i);
			deleteObject(rel);
			flush();
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getGrafoRelaciones(java.lang.Integer)
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(Integer idGrafo)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo",idGrafo));
		List<GrafoRelaciones> listU = getHibernateTemplate().findByCriteria(d);
		return listU;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasTipoActividad(java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasTipoActividad(Integer idActividad)
	{
		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();

		List<CaracteristicasTipo> cTipo = getCaracteristicasTipo(getActividad(idActividad).getIdTipo().getNombre());

		if(cTipo.size()>0)
		{
			for(Integer i = 0; i<cTipo.size();i++)
			{
				List<Caracteristica> c = this.getCaracteristica(cTipo.get(i).getIdCaracteristica().getIdCaracteristica());
				DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",c.get(0).getIdItem().getIdItem()));
				List<Item> listT = getHibernateTemplate().findByCriteria(d);

				c.get(0).setIdItem(listT.get(0));

				cTipo.get(i).setIdCaracteristica(c.get(0));

				caracteristicas.add(cTipo.get(i).getIdCaracteristica());
			}

		}

		return caracteristicas;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasPrePostActividad(org.sakaiproject.lessonbuildertool.model.Actividad, java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasPrePostActividad(Actividad actividad, Integer tipoPrePost) {

		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();

		List<CaracteristicaActividad> cA = getCaracteristicasActividad(actividad.getIdActividad());

		for(CaracteristicaActividad c: cA)
		{
			if(tipoPrePost == 1)//precondiciones
				if(c.isPrecondicion())
					caracteristicas.add(c.getIdCaracteristica());
			if(tipoPrePost == 2)//postcondiciones
				if(c.isPostcondicion())
					caracteristicas.add(c.getIdCaracteristica());
		}

		return caracteristicas;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasActividadDistancia(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasActividadDistancia(Integer idActividad, Integer tipo, Integer queConsulta)
	{
		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();
		//consultar caracteristicas de tipo
		if(queConsulta == 2)
		{
			caracteristicas = getCaracteristicasTipoActividad(idActividad);
		}

		Actividad ac = getActividad(idActividad);

		//1 pre condiciones
		List<Caracteristica> caracteristicasPrePost = getCaracteristicasPrePostActividad(ac, tipo);

		if(caracteristicasPrePost.size()>0)
		{
			for(Integer i = 0; i<caracteristicasPrePost.size();i++)
			{				
				caracteristicas.add(caracteristicasPrePost.get(i));
			}

		}
		return caracteristicas;
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasItemUsuario(org.sakaiproject.lessonbuildertool.model.ItemsUsuario)
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemUsuario(ItemsUsuario iPlan)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasItemsUsuario.class)
				.add(Restrictions.eq("idItemPlan.idItemPlan",iPlan.getIdItemPlan().getIdItemPlan()))
				.add(Restrictions.eq("idUsuario.idUsuario",iPlan.getIdUsuario().getIdUsuario()));
		List<CaracteristicasItemsUsuario> listCIU = getHibernateTemplate().findByCriteria(d);

		for(Integer i = 0; i < listCIU.size(); i++)
		{
			listCIU.get(i).setIdItemPlan(getItemPlan(listCIU.get(i).getIdItemPlan().getIdItemPlan()));
			listCIU.get(i).setIdUsuario(getUsuario(listCIU.get(i).getIdUsuario().getIdUsuario()));
			listCIU.get(i).setIdCaracteristica(getCaracteristica(listCIU.get(i).getIdCaracteristica().getIdCaracteristica()).get(0));
		}

		return listCIU;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasLlenasItemUsuario(org.sakaiproject.lessonbuildertool.model.ItemsUsuario)
	 */
	public List<Caracteristica> getCaracteristicasLlenasItemUsuario(ItemsUsuario iPlan)
	{	
		List<CaracteristicasItemsUsuario> listCIU = getCaracteristicasItemUsuario(iPlan);
		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();

		for (Integer i = 0; i < listCIU.size(); i++) {
			if(listCIU.get(i).isEstado())
				caracteristicas.add(getCaracteristica(listCIU.get(i).getIdCaracteristica().getIdCaracteristica()).get(0));
		}
		return caracteristicas;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getCaracteristicasPorTipo(java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasPorTipo(Integer tipo) {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",tipo)).addOrder(Order.asc("idCaracteristica"));
		List<Caracteristica> listC = getHibernateTemplate().findByCriteria(d);

		if(listC.size()>0)
		{
			for(Integer i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",listC.get(i).getIdItem().getIdItem()));
				List<Item> listT = getHibernateTemplate().findByCriteria(d);

				listC.get(i).setIdItem(listT.get(0));
			}
		}

		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadInicial()
	 */
	public Actividad getActividadInicial()
	{
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("nivel","InicioUD"));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
		{
			//'17', '3', '10', 'Actividad de inicio', '4', 'Actividad de inicio', '0', NULL, NULL, '0', '2', 'InicioUD'
			Item item=getItemId(3);
			Actividad acInicio = new ActividadImpl(item, "Actividad de inicio", 4, 2, "InicioUD");
			acInicio.setDescripcion("Actividad de inicio");
			acInicio.setIdTipo(getTipo(10).get(0));
			acInicio.setEs_refuerzo(false);
			saveActividad(acInicio, new ArrayList<String>(), "", true);

			//acInicio.setIdActividad(getUltimaActividad());
			//'Inicio de unidad didactica'			

			Objetivo obj = getObjetivo("Inicio de unidad didactica");

			if(obj.getNombre().equalsIgnoreCase("Does not exist"))
			{
				obj = new ObjetivoImpl("Inicio de unidad didactica");
				saveObject(obj);

				System.out.println("id del objetivo: "+obj.getIdObjetivo());

				//obj.setIdObjetivo(getUltimoObjetivo("Inicio de unidad didactica"));
			}
			if(getObjetivosActividad(acInicio.getIdActividad(), obj.getIdObjetivo()).size()==0)
			{
				ObjetivosActividad objAct = new ObjetivosActividadImpl(1, acInicio, obj);
				saveObject(objAct);
			}
			return acInicio;
		}
		else
		{
			System.out.println("Actividad si existe");

			Objetivo obj = getObjetivo("Inicio de unidad didactica");

			if(obj.getNombre().equalsIgnoreCase("Does not exist"))
			{
				System.out.println("Objetivo no existe");
				obj = new ObjetivoImpl("Inicio de unidad didactica");
				saveObject(obj);

				System.out.println("id del objetivo: "+obj.getIdObjetivo());
			}
			else
			{
				System.out.println("Objetivo si existe");
				System.out.println("id del objetivo: "+obj.getIdObjetivo());
			}

			if(getObjetivosActividad(list.get(0).getIdActividad(), obj.getIdObjetivo()).size()==0)
			{
				ObjetivosActividad objAct = new ObjetivosActividadImpl(1, list.get(0), obj);
				saveObject(objAct);
			}
			//saveObjetivoActividad(objAct, new ArrayList<String>(), "", true);
			return list.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimoItemPlan()
	 */
	public Integer getUltimoItemPlan()
	{
		Integer id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idItemPlan) AS id FROM item_plan");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemsGrafo(java.lang.Integer, java.lang.Integer)
	 */
	public List<Integer> getItemsGrafo(Integer idUsuario, Integer idActividad)
	{
		Object [] fields = new Object[2];
		fields[0] = idUsuario;
		fields[1] = idActividad;

		List<Integer> listI = SqlService.dbRead("select iu.idItemPlan from "+
				"items_usuario iu, item_plan ip, grafo g, grafo_relaciones gr, grafos_usuario gu "+
				"where iu.idUsuario = ? "+ 
				"and gu.idActividad = ? "+
				"and gu.idUsuario = iu.idUsuario "+
				"and g.idGrafo = gu.idGrafo "+
				"and g.idGrafo = gr.idGrafo "+
				"and iu.IdItemPlan = ip.IdItemPlan "+
				"and gr.idItemPlan_Origen = ip.IdItemPlan "+
				"group by iu.idItemPlan "+
				"order by gr.idRelacionGrafo"
				,fields,null);	

		List<Integer> listS = SqlService.dbRead("select iu.idItemPlan from "+
				"items_usuario iu, item_plan ip, grafo g, grafo_relaciones gr, grafos_usuario gu "+
				"where iu.idUsuario = ? "+ 
				"and gu.idActividad = ? "+
				"and gu.idUsuario = iu.idUsuario "+
				"and g.idGrafo = gu.idGrafo "+
				"and g.idGrafo = gr.idGrafo "+
				"and iu.IdItemPlan = ip.IdItemPlan "+
				"and gr.idItemPlan_Destino = ip.IdItemPlan "+
				"group by iu.idItemPlan "+
				"order by gr.idRelacionGrafo"
				,fields,null);		

		List<Integer> list = new ArrayList<Integer>();
		if(listS.size() == 0)
			return new ArrayList<Integer>();
		else
		{
			//item inicial
			list.add(listI.get(0));
			for(int i = 0; i < listS.size(); i++)
			{				
				list.add(listS.get(i));
			}
			return list;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemsPlanActividad(java.lang.Integer)
	 */
	public List<ItemPlan> getItemsPlanActividad(Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<ItemPlan> list = getHibernateTemplate().findByCriteria(d);	
		if(!list.isEmpty())
			return list;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#estaItemEnUsuarios(int)
	 */
	public boolean estaItemEnUsuarios(int idItemPlan)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ItemsUsuario.class).add(Restrictions.eq("idItemPlan.idItemPlan",idItemPlan));
		List<ItemsUsuario> list = getHibernateTemplate().findByCriteria(d);	
		if(!list.isEmpty())
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#hayActividadesMismoObjetivo(java.lang.Integer)
	 */
	public boolean hayActividadesMismoObjetivo(Integer idActividad)
	{
		List<ObjetivosActividad> objAc = getObjetivosActividad(idActividad);

		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.ne("idActividad.idActividad",idActividad))
				.add(Restrictions.eq("idObjetivo.idObjetivo",objAc.get(0).getIdObjetivo().getIdObjetivo()))
				.add(Restrictions.eq("tipo",2));
		List<ItemsUsuario> list = getHibernateTemplate().findByCriteria(d);	
		if(!list.isEmpty())
		{
			System.out.println("Si hay actividades: "+list.size());
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestaItemUsuario(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public RespuestaItemsUsuario getRespuestaItemUsuario(Integer idTP,Integer idTU, Integer idRIU)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(RespuestaItemsUsuario.class)
				.add(Restrictions.eq("idUsuario.idUsuario",idTU))
				.add(Restrictions.eq("idItemPlan.idItemPlan",idTP))
				.add(Restrictions.eq("idRespuesta",idRIU));
		List<RespuestaItemsUsuario> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{	
			list.get(0).setIdItemPlan(getItemPlan(list.get(0).getIdItemPlan().getIdItemPlan()));
			list.get(0).setIdUsuario(getUsuario(list.get(0).getIdUsuario().getIdUsuario()));
			return list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestaItemUsuario(java.lang.Integer)
	 */
	public RespuestaItemsUsuario getRespuestaItemUsuario(Integer idRespuesta)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(RespuestaItemsUsuario.class)
				.add(Restrictions.eq("idRespuesta",idRespuesta));
		List<RespuestaItemsUsuario> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{	
			list.get(0).setIdItemPlan(getItemPlan(list.get(0).getIdItemPlan().getIdItemPlan()));
			list.get(0).setIdUsuario(getUsuario(list.get(0).getIdUsuario().getIdUsuario()));
			return list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestasItemUsuario(int, int)
	 */
	public  List<RespuestaItemsUsuario> getRespuestasItemUsuario(int idTP, int idTU)
	{
		DetachedCriteria d = DetachedCriteria.forClass(RespuestaItemsUsuario.class)
				.add(Restrictions.eq("idItemPlan.idItemPlan",idTP))
				.add(Restrictions.eq("idUsuario.idUsuario",idTU));
		List<RespuestaItemsUsuario> list = getHibernateTemplate().findByCriteria(d);	
		if(!list.isEmpty())
		{
			for(RespuestaItemsUsuario res : list)
			{
				res.setIdItemPlan(getItemPlan(res.getIdItemPlan().getIdItemPlan()));
				res.setIdUsuario(getUsuario(res.getIdUsuario().getIdUsuario()));
			}
			return list;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getUltimaRespuesta()
	 */
	public Integer getUltimaRespuesta()
	{
		Integer id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idRespuesta) AS id FROM respuesta_itemusuario");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemPlansPorUnidadDidactica(java.lang.Integer)
	 */
	public List getItemPlansPorUnidadDidactica(Integer idActividad)
	{
		Integer id = 0;

		Query query = getSession().createSQLQuery("SELECT idItemPlan FROM sakai.Actividad_Tiene_Item_Plan WHERE idActividad = :?")
				.setParameter("?", idActividad);
		//((SQLQuery) query).addScalar("id");
		List<Integer> list = query.list();		

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getItemPlansUD(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public List getItemPlansUD(Actividad ud)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idUnidadDidacticaPadre.idActividad",ud.getIdActividad()));
		d.addOrder(Order.asc("orden"));
		d.addOrder(Order.asc("idActividad.idActividad"));
		List<ItemPlan> list = getHibernateTemplate().findByCriteria(d);	
		if(!list.isEmpty())
		{
			for(ItemPlan res : list)
			{
				//System.out.println(res.getIdItemPlan()+" orden: "+res.getOrden());
				res.setIdActividad(getActividad(res.getIdActividad().getIdActividad()));
				res.setIdUnidadDidacticaPadre(getActividad(res.getIdUnidadDidacticaPadre().getIdActividad()));
			}
			return list;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getIdObjetivosActividad(int, java.lang.String)
	 */
	public List<Integer> getIdObjetivosActividad(int nivelRecursividad, String nombre) {
		Object [] fields = new Object[2];
		fields[0] = nivelRecursividad;
		fields[1] = nombre;

		List<Integer> list = SqlService.dbRead("select o.idObjetivo from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo and oa.tipo = 1", fields, null);

		if (list.size() > 0) {
			return list;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getEstadoItemPlan(java.lang.Integer)
	 */
	public Integer getEstadoItemsUsuario(Integer ip, Integer idUsuario) {
		ItemsUsuario itemsUsuario=this.getItemUsuario(ip, idUsuario);
		if(itemsUsuario!=null)
		{
			ItemPlan auxIP=this.getItemPlan(ip);
			if(itemsUsuario.isRealizada())
			{
				//Realizada y aprobada
				if(itemsUsuario.getNota()>=3.0)
					return 0;
				else //Realizada y reprobada
					return 1;
			}
			else if(!auxIP.getIdActividad().isEs_refuerzo())
			{	//Cerrada porque ya pasó
				if(auxIP.getFecha_final()!=null&&auxIP.getFecha_final().before(new Date()))
					return 2;
				else if(auxIP.getFecha_inicial()!=null&&auxIP.getFecha_inicial().after(new Date()))//No ha sido abierta aún
					return 3;
			}
			else //Se puede realizar
				return 4;
		}//No es del usuario
		return 5;
	}
		
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getIdChaeaTest(java.lang.String)
	 */
	public long getIdChaeaTest(String currentSiteId)
	{
		Object [] fields = new Object[1];
		fields[0] = currentSiteId;

		List<String> list = SqlService.dbRead("SELECT id FROM sakai.chaea_inquerito where idSite = ?", fields, null);

		if (list.size() > 0) {
			return Long.valueOf(list.get(0));
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getIdPollPersonalidad(java.lang.String)
	 */
	public int getIdPollPyH(String currentSiteId, int tipo)
	{
		Object [] fields = new Object[2];
		fields[0] = currentSiteId;
		fields[1] = 1;
		
		List<String> list = new ArrayList<String>();
		if(tipo == 1)
			list = SqlService.dbRead("SELECT poll_id FROM sakai.poll_poll where poll_site_id = ? and poll_max_options = ?", fields, null);
		if(tipo == 2)
			list = SqlService.dbRead("SELECT poll_id FROM sakai.poll_poll where poll_site_id = ? and poll_max_options > ?", fields, null);

		if (list.size() > 0) {
			return Integer.valueOf(list.get(0));
		}
		return 0;
	}
	
	public List<GrafosUsuario> getGrafosActividad(Actividad actAnterior)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idActividad",actAnterior.getIdActividad()));
		List<GrafosUsuario> listU = getHibernateTemplate().findByCriteria(d);
		for(GrafosUsuario gu : listU)
			gu.setIdGrafo(getGrafo(gu.getIdGrafo().getIdGrafo()));
		return listU;
	}

	@Override
	public List<LogCaracteristicas> getLogCaracteristicasUsuario(Usuario usuario) {
		
		DetachedCriteria d = DetachedCriteria.forClass(LogCaracteristicas.class).add(Restrictions.eq("idUsuario",usuario.getIdUsuario()));
		List<LogCaracteristicas> listCar = getHibernateTemplate().findByCriteria(d);
			
		return listCar;
		
	}

	@Override
	public List<Integer> getUsuariosLogCaracteristicas() {
		List<String> list = new ArrayList<String>();
		List<Integer> usuarios = new ArrayList<Integer>();
		list = SqlService.dbRead("SELECT distinct c.idUsuario FROM sakai.log_caracteristicas c");
		
	for(int i=0;i<list.size();i++){
		usuarios.add(Integer.valueOf(list.get(i)));
	}
	
	return usuarios;
	}

	@Override
	public List<LogGrafos> getLogGrafosUsuario(Usuario usuario) {
		DetachedCriteria d = DetachedCriteria.forClass(LogGrafos.class).add(Restrictions.eq("idUsuario",usuario.getIdUsuario()));
		List<LogGrafos> listGraf = getHibernateTemplate().findByCriteria(d);
			
		return listGraf;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestasObjetivoUnidad(int, int)
	 */
	public List<String> getRespuestasObjetivoUnidad(int idObjetivo,int idActividad)
	{
		Object [] fields = new Object[2];
		fields[0] = idActividad;
		fields[1] = idObjetivo;

		List<String> list = SqlService.dbRead("select r.archivo from respuesta_itemusuario r, item_plan ip, actividad a, objetivo o, objetivos_actividad oa where "
				+ "r.idItemPlan = ip.idItemPlan and "
				+ "ip.idUnidadDidacticaPadre = ? and "
				+ "ip.idActividad = a.idActividad and "
				+ "oa.idActividad = a.idActividad and "
				+ "oa.idObjetivo = o.idObjetivo and "
				+ "o.idObjetivo = ? and "
				+ "r.archivo IS NOT NULL "
				+ "group by r.archivo "
				+ "order by r.idRespuesta;", fields, null);

		if (list.size() > 0) {
			return list;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getRespuestasObjetivoUnidad(int, int)
	 */
	public List<String> getUsuariosRespuestasObjetivoUnidad(int idObjetivo,int idActividad)
	{
		Object [] fields = new Object[2];
		fields[0] = idActividad;
		fields[1] = idObjetivo;

		List<String> list = SqlService.dbRead("select su.LAST_NAME,su.FIRST_NAME from respuesta_itemusuario r, item_plan ip, items_usuario iu, usuario u, actividad a, objetivo o, objetivos_actividad oa, sakai_user su where "
				+ "r.idItemPlan = ip.idItemPlan and "
				+ "ip.idUnidadDidacticaPadre = ? and "
				+ "ip.idActividad = a.idActividad and "
				+ "oa.idActividad = a.idActividad and "
				+ "oa.idObjetivo = o.idObjetivo and "
				+ "o.idObjetivo = ? and "
				+ "r.archivo IS NOT NULL and "
				+ "r.idUsuario = iu.idUsuario and "
				+ "iu.idUsuario = u.idUsuario and "
				+ "u.idUsuarioSakai = su.USER_ID "
				+ "group by r.idUsuario "
				+ "order by r.idRespuesta;", fields, null);

		if (list.size() > 0) {
			return list;
		}
		return null;
	}
	
	public List<Integer> getUsuariosActivos()
	{

		List<String> list = new ArrayList<String>();
		List<Integer> usuarios = new ArrayList<Integer>();
		list = SqlService.dbRead("SELECT distinct c.idUsuario FROM sakai.caracteristicas_usuario c");
		
	for(int i=0;i<list.size();i++){
		usuarios.add(Integer.valueOf(list.get(i)));
	}
	
	return usuarios;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getIdItemsGrafo(int)
	 */
	public List<Integer> getIdItemsGrafo(int idGrafo)
	{
		List<Integer> list = new ArrayList<Integer>();

		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo", idGrafo)).addOrder(Order.asc("orden"));
		List<GrafoRelaciones> listG = getHibernateTemplate().findByCriteria(d);

		if(listG.size() > 0)
		{
			for(GrafoRelaciones iG: listG)
			{
				if(!list.contains(iG.getIdItemPlan_Origen()))
					list.add(iG.getIdItemPlan_Origen());
				if(!list.contains(iG.getIdItemPlan_Destino()))
					list.add(iG.getIdItemPlan_Destino());
			}
		}

		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getIdItemsPlan(int)
	 */
	public List<Integer> getIdItemsPlan(int idGrafo)
	{
		List<Integer> list = new ArrayList<Integer>();

		DetachedCriteria d = DetachedCriteria.forClass(Grafo.class).add(Restrictions.eq("idGrafo", idGrafo));
		List<Grafo> listG = getHibernateTemplate().findByCriteria(d);

		if(listG.size() > 0)
		{
			//list.add(listG.get(0).getIdItemPlan_Inicial());
			list.addAll(getIdItemsGrafo(idGrafo));
		}

		return list;
	}

}
