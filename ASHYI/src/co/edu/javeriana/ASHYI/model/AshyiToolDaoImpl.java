package co.edu.javeriana.ASHYI.model;

/**
 *Dao de la aplicacion
 *Contiene las consultas relacionadas a ASHYI
 *Configuracion para hibernate
 *@author ASHYI
 *
 **********************************************************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import co.edu.javeriana.ASHYI.hbm.ActividadImpl;
import co.edu.javeriana.ASHYI.hbm.GrafoImpl;
import co.edu.javeriana.ASHYI.hbm.GrafosUsuarioImpl;
import co.edu.javeriana.ASHYI.hbm.ItemPlanImpl;
import co.edu.javeriana.ASHYI.hbm.ObjetivoImpl;
import co.edu.javeriana.ASHYI.hbm.ObjetivosActividadImpl;
import co.edu.javeriana.ASHYI.hbm.RecursoImpl;
import co.edu.javeriana.ASHYI.hbm.RecursosActividadImpl;
import co.edu.javeriana.ASHYI.hbm.UsuarioImpl;

public class AshyiToolDaoImpl extends HibernateDaoSupport implements AshyiToolDao {
	private static Log log = LogFactory.getLog(AshyiToolDaoImpl.class);

//	private ToolManager toolManager;
//	private SecurityService securityService;
	//private Session session = ASHYIUtil.getSessionFactory().openSession();
	//private SessionFactory sessionFactory;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public DataSource getDataSource() {
		return this.dataSource;
	}

        // part of HibernateDaoSupport; this is the only context in which it is OK
        // to modify the template configuration
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

//	public boolean canEditPage() {
//		String ref = null;
//		// no placement, startup testing, should be an advisor in place
//		try {
//			ref = "/site/" + toolManager.getCurrentPlacement().getContext();
//		} catch (java.lang.NullPointerException ignore) {
//			ref = "";
//		}
//		return securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_UPDATE, ref);
//	}
//	
//	public boolean canEditPage(long pageId) {
//		boolean canEdit = canEditPage();
//		// forced comments have a pageid of -1, because they are associated with
//		// more than one page. But the student can't edit them anyway, so fail it
//		if(!canEdit && pageId != -1L) {
//			SimplePage page = getPage(pageId);
//			if(UserDirectoryService.getCurrentUser().getId()
//					.equals(page.getOwner())) {
//				canEdit = true;
//			}
//		}
//		
//		return canEdit;
//	}
//	
//	public SimplePage getPage(long pageId) {
//		DetachedCriteria d = DetachedCriteria.forClass(SimplePage.class).add(Restrictions.eq("pageId", pageId));
//
//		List l = getHibernateTemplate().findByCriteria(d);
//
//		if (l != null && l.size() > 0) {
//			return (SimplePage) l.get(0);
//		} else {
//			return null;
//		}
//	}
//	
//	public boolean canEditPage(String owner) {
//		boolean canEdit = canEditPage();
//		if(owner != null && !canEdit) {
//			if(owner.equals(UserDirectoryService.getCurrentUser().getId())) {
//				canEdit = true;
//			}
//		}
//		
//		return canEdit;
//	}
//
//	public void setSecurityService(SecurityService service) {
//		securityService = service;
//	}
//
//	public void setToolManager(ToolManager service) {
//		toolManager = service;
//	}
//	
//	public SimplePageItem findItem(long id) {
//	    
//		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("id", id));
//		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);
//
//		if (list != null && list.size() > 0) {
//			return list.get(0);
//		} else {
//			return null;
//		}
//	}
//	
	/**
	 * Get Cause of error in data base
	 * @param t
	 * @param elist
	 */
	public void getCause(Throwable t, List<String>elist) {
		while (t.getCause() != null) {
			t = t.getCause();
		}
		log.warn("error saving or updating: " + t.toString());
		elist.add(t.getLocalizedMessage());
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#saveActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
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
//			//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimaActividad(java.lang.String)
	 */
	public Integer getUltimaActividad(String nombre)
	{
		Integer id = 0;
		
		Object [] fields = new Object[1];
		fields[0] = nombre;
		Query query = getSession().createSQLQuery("SELECT MAX(idActividad) AS id FROM actividad WHERE nombre = ?").setParameter("?", fields);
		List<String> list = query.list();	
    	
    	if(list.size() == 0)
    		id = 1;
    	else
    	{
    		id = Integer.valueOf(list.get(0)) ;
    	}
    	
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimaActividad()
	 */
	public Integer getUltimaActividad()
	{
		Integer id = 0;
		
		List<Integer> list = getSession().createSQLQuery("SELECT MAX(idActividad) AS id FROM actividad").list();		
    	
    	if(list.size() == 0)
    		id = 1;
    	else
    	{
    		id = Integer.valueOf(list.get(0)) ;
    	}
    	
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoRecurso()
	 */
	public Recurso getUltimoRecurso()
	{
		List<String> list = getSession().createSQLQuery("SELECT MAX(idRecurso) AS id FROM recurso").list();		
    	
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

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#saveCaracteristica(java.lang.Object, java.util.List, java.lang.String, boolean)
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
			&& !(o instanceof Grafo)
			&& !(o instanceof GrafoRelaciones)
			&& !(o instanceof ItemsUsuario)
			&& !(o instanceof LogCaracteristicas)
			&& !(o instanceof LogGrafos)
			&& !(o instanceof GrafosUsuario)
			&& !(o instanceof Tipo)) {
		
		elist.add(nowriteerr);
		return false;
	}
	
	try {
	    getHibernateTemplate().save(o);
	   // getHibernateTemplate().flush();
	    
	    if (o instanceof CaracteristicasUsuario) {
	    	CaracteristicasUsuario i = (CaracteristicasUsuario)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
//		updateStudentPage(o);
	    }
	    
	    if (o instanceof Usuario) {
	    	Usuario i = (Usuario)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
//		updateStudentPage(o);
	    }
	    
	    if (o instanceof CaracteristicaRecurso) {
	    	CaracteristicaRecurso i = (CaracteristicaRecurso)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
//		updateStudentPage(o);
	    }
	    
	    if (o instanceof Caracteristica) {
	    	Caracteristica i = (Caracteristica)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
//		updateStudentPage(o);
	    } 
	    
	    if (o instanceof ObjetivosActividad) {
	    	ObjetivosActividad i = (ObjetivosActividad)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdObjetivoActividad(), true));
//		updateStudentPage(o);
	    } 
	    
	    if (o instanceof RecursosActividad) {
	    	RecursosActividad i = (RecursosActividad)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecursosActividad(), true));
//		updateStudentPage(o);
	    }
	    
	    if (o instanceof Objetivo) {
	    	Objetivo i = (Objetivo)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdObjetivo(), true));
//		updateStudentPage(o);
	    } 
	    
	    if (o instanceof ActividadTieneActividad) {
	    	ActividadTieneActividad i = (ActividadTieneActividad)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
//		updateStudentPage(o);
	    } 	 
	    
	    if (o instanceof Recurso) {
	    	Recurso i = (Recurso)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecurso(), true));
//		updateStudentPage(o);
	    } 
	    
	    if (o instanceof Control) {
	    	Control i = (Control)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdcontrol_ashyi(), true));
//		updateStudentPage(o);
	    } 

	    if (o instanceof Tipo) {
	    	Tipo i = (Tipo)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdTipo(), true));
//		updateStudentPage(o);
	    } 
	    
	    if (o instanceof Item) {
	    	Item i = (Item)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdItem(), true));
//		updateStudentPage(o);
	    }
	    if (o instanceof ItemsUsuario) {
	    	ItemsUsuario i = (ItemsUsuario)o;
	    	System.out.println("Item Uusario "+i.getIdItemPlan().getIdItemPlan());
	    }	   
	    
	    if (o instanceof LogGrafos) {
	    	LogGrafos i = (LogGrafos)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
//		updateStudentPage(o);
	    }
	    
	    if (o instanceof LogCaracteristicas) {
	    	LogCaracteristicas i = (LogCaracteristicas)o;
		//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#deleteItem(java.lang.Object)
	 */
	public boolean deleteItem(Object o) {
		/*
		 * If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 * permissions on it. If the item isn't SimplePageItem or SimplePage, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
//		if(!(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
//				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
//				&& (o instanceof SimplePage || o instanceof SimplePageItem)) {
//			return false;
//		}
//
//		if (o instanceof SimplePageItem) {
//		    SimplePageItem i = (SimplePageItem)o;
//		    //getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/item/" + i.getId(), true));
//		} else if (o instanceof SimplePage) {
//		    SimplePage i = (SimplePage)o;
//		    //getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/page/" + i.getPageId(), true));
//		} else if(o instanceof SimplePageComment) {
//			SimplePageComment i = (SimplePageComment) o;
//			//getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/comment/" + i.getId(), true));
//		}

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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#update(java.lang.Object, java.util.List, java.lang.String, boolean)
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
//		if(requiresEditPermission && !(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
//				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
//		   		&& !(o instanceof SimplePageLogEntry)
//				&& !(o instanceof SimplePageGroup)
//				&& !(o instanceof Actividad)
//				&& !(o instanceof ActividadTieneActividad)) {
//			elist.add(nowriteerr);
//			return false;
//		}
//		
//		if (o instanceof SimplePageItem) {
//		    SimplePageItem i = (SimplePageItem)o;
//		    //getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/item/" + i.getId(), true));
//		} else if (o instanceof SimplePage) {
//		    SimplePage i = (SimplePage)o;
//		    //getSession().createSQLQuery(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/page/" + i.getPageId(), true));
//		}

		try {
//			if( !(o instanceof Actividad)) {
//				getHibernateTemplate().merge(o);
//			}
//			else {
				// Updating seems to always update the timestamp on the log correctly,
				// while merging doesn't always get it right.  However, it's possible that
				// update will fail, so we do both, in order of preference.
				try {
					getHibernateTemplate().update(o);
				}catch(DataAccessException ex) {
					log.warn("Wasn't able to update log entry, timing might be a bit off.");
					getHibernateTemplate().merge(o);
				}
//			}		    
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getHabilidades()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getContextos()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCompetencias()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getPersonalidades()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getSA()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getEstilos()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#eliminar(java.lang.Object)
	 */
	public void eliminar(Object editar) {
		
		getHibernateTemplate().delete(editar);
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristica(java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristica(Integer id){
		
		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idCaracteristica",id));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		if(list.size()>0)
		{
			return list;
		}
		return new ArrayList<Caracteristica>();
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getTipo(java.lang.Integer)
	 */
	public List<Tipo> getTipo(Integer id){
		
		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("idTipo",id));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemId(java.lang.Integer)
	 */
	public Item getItemId(Integer id){
		
		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",id));

		List<Item> list = getHibernateTemplate().findByCriteria(d);
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getActividad(java.lang.Integer)
	 */
	public Actividad getActividad(Integer id){		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("idActividad",id));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		
		
		if(list.size()==0)
			return new ActividadImpl();
		else
		{
			if(list.get(0).getIdTipo() != null)
				list.get(0).setIdTipo(getTipo(list.get(0).getIdTipo().getIdTipo()).get(0));
			return list.get(0);
		}		
	}
	
	/**
	 * @param nombre
	 * @return
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getactividad(java.lang.String, java.lang.Integer)
	 */
	public Actividad getactividad(String titulo, Integer nivelRecursividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("nombre",titulo)).add(Restrictions.eq("nivel_recursividad",nivelRecursividad));
		
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);
//		Actividad list = (Actividad) getSession().get(Actividad.class, 1);
		//List<Actividad> list = getSession().createQuery("from Actividad").list();
		
//		return list;
		if(list.size() > 0)
		{
			return list.get(0);
		}
		Actividad a = new ActividadImpl();
		a.setNombre("Does not exist");
		return a;
	}
		
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoItem()
	 */
	public Integer getUltimoItem()
	{
		Integer id = 0;
		List<String> list = getSession().createQuery("SELECT MAX(idItem) AS id FROM item").list();		
    	
    	if(list.size() == 0)
    		id = 1;
    	else
    	{
    		id = Integer.valueOf(list.get(0)) ;
    	}
    	
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoObjetivo(java.lang.String)
	 */
	public Integer getUltimoObjetivo(String nombre)
	{
		Integer id = 0;
		
		Object [] fields = new Object[1];
		fields[0] = nombre;
		
		List<String> list = getSession().createSQLQuery("SELECT MAX(idObjetivo) AS id FROM objetivo WHERE nombre = ?").setParameter("?",fields).list();		
    	
    	if(list.size() == 0)
    		id = 1;
    	else
    	{
    		id = Integer.valueOf(list.get(0)) ;
    	}
    	
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoObjetivo()
	 */
	public Integer getUltimoObjetivo()
	{
		Integer id = 0;
		
		
		List<Integer> list = getSession().createSQLQuery("SELECT MAX(idObjetivo) AS id FROM objetivo").list();		
    	
    	if(list.size() == 0)
    		id = 1;
    	else
    	{
    		id = Integer.valueOf(list.get(0)) ;
    	}
    	
    	return id;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivosActividad(java.lang.Integer, java.lang.String)
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre) {
		
		List listP = getSession().createQuery("SELECT distinct idObjetivo FROM objetivos_actividad").list();
		
		if(!listP.isEmpty())
		{
			for(Integer j = 0; j < listP.size(); j++ )
			{
				Object [] fields = new Object[3];
				fields[0] = nivelRecursividad;
				fields[1] = nombre;
				fields[2] = listP.get(j);
				
				List<String> list = getSession().createSQLQuery("select o.nombre from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo AND o.idObjetivo = ?").setParameter("?",fields).list();	
						
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
		}
		
		return new String[0];
	}
	
	/**
	 * @param nombre
	 * @param nR
	 * @return
	 */
	public Integer getObjetivoActividad(String nombre, Integer nR)
	{
		Object [] fields = new Object[2];
		fields[0] = nombre;
		fields[1] = nR;
		
		List<String> list = getSession().createSQLQuery("select o.idObjetivo from objetivo o, objetivos_actividad oa, actividad a where o.nombre = ? and o.idObjetivo = oa.idObjetivo and oa.idActividad = a.idActividad and a.nivel_recursividad = ?").setParameter("?",fields).list();

		Integer id = -1;
		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			id = Integer.valueOf(list.get(0));
		}
		return id;
	}
		
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#deleteObject(java.lang.Object)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasTipo(java.lang.String)
	 */
	public List<CaracteristicasTipo> getCaracteristicasTipo(String nombreTipo){	
		
		DetachedCriteria dT = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("nombre",nombreTipo));
		List<Tipo> list = getHibernateTemplate().findByCriteria(dT);	
		
		if(list.size() > 0)
		{
		
			DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasTipo.class).add(Restrictions.eq("idTipo.idTipo",list.get(0).getIdTipo())).addOrder(Order.asc("idCaracteristica.idCaracteristica"));
			List<CaracteristicasTipo> listC = getHibernateTemplate().findByCriteria(d);	
			
			if(listC.size() > 0)
			{
				for(Integer i = 0; i < listC.size(); i++)
				{
					listC.get(i).setIdCaracteristica(getCaracteristica(listC.get(i).getIdCaracteristica().getIdCaracteristica()).get(0));
				}
			}
			
			return listC;
		}

		return null;
	}
		
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getRecursosActividad(java.lang.Integer, java.lang.String)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivo(java.lang.String)
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
	
//	public Actividad getActividadRecurso(int idActividad, String idRecursoSakai)
//	{
//		Object [] fields = new Object[2];
//		fields[0] = idActividad;
//		fields[1] = idRecursoSakai;
//
//		Actividad a = new ActividadImpl();
//
//		a = this.getActividad(idActividad);
//
//		List<RecursosActividad> rA =this.getRecursosActividad(a.getNivel_recursividad(),a.getNombre());
//
//		int estar = 0;
//		for(int i = 0;i<rA.size();i++)
//		{
//			if(rA.get(i).getIdRecurso().getIdItemSakai().equals(idRecursoSakai))
//			{
//				estar++;
//			}
//
//		}
//		if(estar != 0)
//		{
//			return a;
//		}
//		a = new ActividadImpl();
//		a.setNombre("Does not exist");
//		return a;
//	}
		
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getActividadRecurso(java.lang.Integer, java.lang.String)
	 */
	public Actividad getActividadRecurso(Integer idActividad, String idRecursoSakai)
	{
		Object [] fields = new Object[2];
		fields[0] = idActividad;
		fields[1] = idRecursoSakai;
		
		Actividad a = new ActividadImpl();
		
		a = this.getActividad(idActividad);
		
		List<RecursosActividad> rA =this.getRecursosActividad(a.getNivel_recursividad(),a.getNombre());
		
		Integer estar = 0;
		for(Integer i = 0;i<rA.size();i++)
		{
			if(rA.get(i).getIdRecurso().getIdItemSakai().equals(idRecursoSakai))
			{
				estar++;
			}
			
		}
		if(estar != 0)
		{
			return a;
		}
		return a;
	}	
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasTipoActividad(java.lang.Integer)
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
		
//		List<CaracteristicaActividad> listC = getCaracteristicasActividad(idActividad);
//		
//		if(listC.size()>0)
//		{
//			for(Integer i = 0; i<listC.size();i++)
//			{				
//				caracteristicas.add(listC.get(i).getIdCaracteristica());
//			}
//			
//		}
		
		return caracteristicas;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasTipoActividad(java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasCaracteristicasActividad(Integer idActividad)
	{
		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();
		Actividad ac = getActividad(idActividad);
		
		List<Caracteristica> caractActividad = getCaracteristicasPrePostActividad(ac,1);
				
		if(caractActividad.size()>0)
		{
			for(Integer i = 0; i<caractActividad.size();i++)
			{
				List<CaracteristicaCaracteristicas> carcCaracActividad = getCaracteristicasCaracteristica(caractActividad.get(i));
				
				if(carcCaracActividad != null)
					for(Integer j = 0; j<carcCaracActividad.size();j++)
					{
						Caracteristica cAC = getCaracteristica(carcCaracActividad.get(j).getIdCaracteristica().getIdCaracteristica()).get(0);
						boolean esta = false;
						for(int p = 0; p < caracteristicas.size(); p++)
						{
							if(caracteristicas.get(p).getIdCaracteristica() == cAC.getIdCaracteristica())
							{
								esta=true;
								break;
							}
						}
						if(!esta)
							caracteristicas.add(cAC);
					}	
			}
			
		}
		return caracteristicas;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasTipoActividad(java.lang.Integer)
	 */
	public List<CaracteristicaCaracteristicas> getCaracteristicasCaracteristica(Caracteristica caracteristica) {
		
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaCaracteristicas.class).add(Restrictions.eq("idCaracteristicaRelacion.idCaracteristica",caracteristica.getIdCaracteristica()));
		List<CaracteristicaCaracteristicas> listCC = getHibernateTemplate().findByCriteria(d);
		if(listCC.size()>0)
		{
			return listCC;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasActividadDistancia(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public List<Caracteristica> getCaracteristicasActividadDistancia(Integer idActividad, Integer tipo, Integer queConsulta, boolean correlacion)
	{
		List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();
		//consultar caracteristicas de tipo
		if(queConsulta == 2)
		{
			//personalidad en el tipo
			if(correlacion)
				caracteristicas = getCaracteristicasTipoActividad(idActividad);
			else
			{
				caracteristicas.addAll(getCaracteristicasTipoActividad(idActividad));
				caracteristicas.addAll(getCaracteristicasCaracteristicasActividad(idActividad));
			}
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasActividad(java.lang.Integer)
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad)).addOrder(Order.asc("idCaracteristica.idCaracteristica"));
		List<CaracteristicaActividad> listC = getHibernateTemplate().findByCriteria(d);	
		
		if(listC.size()>0)
		{
			for(Integer i = 0; i<listC.size();i++)
			{
				List<Caracteristica> c = this.getCaracteristica(listC.get(i).getIdCaracteristica().getIdCaracteristica());
				d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",c.get(0).getIdItem().getIdItem()));
				List<Item> listT = getHibernateTemplate().findByCriteria(d);
				
				c.get(0).setIdItem(listT.get(0));
				
				listC.get(i).setIdCaracteristica(c.get(0));
			}
			
		}
		
		return listC;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivo(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivosActividad(java.lang.Integer)
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
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getRecursosActividad(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#deleteActividad(co.edu.javeriana.ASHYI.model.Actividad)
	 */
	public void deleteActividad(Actividad a)
	{		
		Object [] fields = new Object[1];
		fields[0] = a.getIdActividad();
		Integer elimina  = getSession().createSQLQuery("delete from actividad where idActividad = ?").setParameter("?", fields).executeUpdate();

	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#isItUsuario(co.edu.javeriana.ASHYI.model.Usuario)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoUsuario(java.lang.String)
	 */
	public Integer getUltimoUsuario(String idUsuarioSakai)
	{
		Integer id = 0;	
//		Object [] fields = new Object[1];
//		fields[0] = idUsuarioSakai;
//		
		List list = getSession().createSQLQuery("SELECT MAX(idUsuario) AS id FROM usuario WHERE idUsuarioSakai = ?").setString(0, idUsuarioSakai).list();
						
		//List<String> list = getSession().createSQLQuery("select o.nombre from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo").setString(0 , String.valueOf(nivelRecursividad)).setString(1, nombre).list();
    	if(list.size() == 0)
    		id = -1;
    	else
    	{
    		id = Integer.valueOf(String.valueOf(list.get(0)) );
    	}
    	
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicaRecursos(co.edu.javeriana.ASHYI.model.Recurso, co.edu.javeriana.ASHYI.model.Caracteristica)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#flush()
	 */
	public void flush() {
	    getHibernateTemplate().flush();
	}
	
	/**
	 * @param idUsuario
	 * @return
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
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivosActividadObj(java.lang.Integer)
	 */
	public List<ObjetivosActividad> getObjetivosActividadObj(Integer idObjetivo)
	{		
		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.eq("idObjetivo.idObjetivo",idObjetivo)).addOrder(Order.asc("idObjetivo.idObjetivo"));
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivoActividadObj(java.lang.Integer, java.lang.Integer)
	 */
	public ObjetivosActividad getObjetivoActividadObj(Integer idObj, Integer idAct) {
		
		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.eq("idObjetivo.idObjetivo",idObj)).add(Restrictions.eq("idActividad.idActividad",idAct)).addOrder(Order.asc("idObjetivo.idObjetivo"));
		List<ObjetivosActividad> listO = getHibernateTemplate().findByCriteria(d);		
		
		if(listO.size()>0)
		{
			listO.get(0).setIdObjetivo(getObjetivo(listO.get(0).getIdObjetivo().getIdObjetivo()));
			listO.get(0).setIdActividad(getActividad(listO.get(0).getIdActividad().getIdActividad()));
			return listO.get(0);
		}
		
		return new ObjetivosActividadImpl();
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasUsuario(java.lang.String)
	 */
	public List<CaracteristicasUsuario> getCaracteristicasUsuario(String userId)
	{	
		Usuario user = getUsuario(getUltimoUsuario(userId));
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",user.getIdUsuario()));
		List<CaracteristicasUsuario> listC = getHibernateTemplate().findByCriteria(d);		
		
		if(listC.size()>0)
		{
			for(Integer i = 0; i< listC.size(); i++)
			{
				listC.get(i).setIdCaracteristica(getCaracteristica(listC.get(i).getIdCaracteristica().getIdCaracteristica()).get(0));
				listC.get(i).setIdUsuario(user);
			}
			return listC;
		}
		
		return new ArrayList<CaracteristicasUsuario>();
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getActividadRefuerzo(java.lang.Integer)
	 */
	public List<Actividad> getActividadRefuerzo(Integer i) {
		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("es_refuerzo",true)).add(Restrictions.eq("nivel_recursividad",i));
		List<Actividad> listO = getHibernateTemplate().findByCriteria(d);		
		
		if(listO.size()>0)
			return listO;
		return new ArrayList<Actividad>();
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasPorTipo(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemPlan(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasPrePostActividad(co.edu.javeriana.ASHYI.model.Actividad, java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUltimoGrafo()
	 */
	public Grafo getUltimoGrafo()
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getGrafoUsuarioActividad(java.lang.Integer, java.lang.String)
	 */
	public Grafo getGrafoUsuarioActividad(Integer idUsuario, String nombreActividad)
	{
		//3 unidad
		Actividad actividad = getactividad(nombreActividad, 3);
		
		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idUsuario.idUsuario", idUsuario)).add(Restrictions.eq("idActividad", actividad.getIdActividad()));
		List<GrafosUsuario> list = getHibernateTemplate().findByCriteria(d);
		
		if(list.size() > 0)
		{
			GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
			//hay grafos re planificados, encontrar el activo
			for(GrafosUsuario gU : list)
			{
				if(gU.isActivo())
				{
					grafoActivo = gU;
					break;
				}
			}
			return getGrafo(grafoActivo.getIdGrafo().getIdGrafo());
		}
		
		return new GrafoImpl();
	}

	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getGrafo(int)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getIdGrafoUsuarioActividad(java.lang.Integer, java.lang.String)
	 */
	public Integer getIdGrafoUsuarioActividad(Integer idUsuario, String nombreActividad) {
		
		Grafo grafo = getGrafoUsuarioActividad(idUsuario, nombreActividad);
		
		if(grafo.getIdItemPlan_Inicial() != -1)
			return grafo.getIdGrafo();
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemPlan(co.edu.javeriana.ASHYI.model.Actividad, co.edu.javeriana.ASHYI.model.Recurso)
	 */
	public ItemPlan getItemPlan(Actividad idActividad, Recurso idRecurso) {
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idActividad.idActividad", idActividad)).add(Restrictions.eq("idRecurso.idRecurso", idRecurso));
		List<ItemPlan> list = getHibernateTemplate().findByCriteria(d);
		
		if(list.size() > 0)
			return list.get(0);
		
		return new ItemPlanImpl();
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUsuario(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getIdItemsPlan(java.lang.Integer)
	 */
	public List<Integer> getIdItemsPlan(Integer idGrafo)
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
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getIdItemsGrafo(java.lang.Integer)
	 */
	public List<Integer> getIdItemsGrafo(Integer idGrafo)
	{
		List<Integer> list = new ArrayList<Integer>();
		
		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo", idGrafo)).addOrder(Order.asc("idRelacionGrafo"));
		List<GrafoRelaciones> listG = getHibernateTemplate().findByCriteria(d);
		
		if(listG.size() > 0)
		{
			for(GrafoRelaciones iG: listG)
			{
				list.add(iG.getIdItemPlan_Origen());
				list.add(iG.getIdItemPlan_Destino());
			}
		}
		
		return list;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemsActivos(co.edu.javeriana.ASHYI.model.Usuario, java.lang.String)
	 */
	public List<ItemPlan> getItemsActivos(Usuario usuario, String nombreActividad)
	{
		usuario = getUsuario(usuario.getIdUsuario());
		
		Integer idGrafo = getIdGrafoUsuarioActividad(usuario.getIdUsuario(), nombreActividad);
		
		List<Integer> idsItems = getIdItemsPlan(idGrafo);
		List<ItemPlan> list = new ArrayList<ItemPlan>();
		
		if(idsItems.size() > 0)
		{
			for(Integer i = 0;i <idsItems.size(); i++)
			{
				ItemPlan item = getItemPlan(idsItems.get(i));
				list.add(item);
			}
		}
							
		return list;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getUsuario(java.lang.String)
	 */
	public Usuario getUsuario(String userId)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuarioSakai",userId));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);		
		
		if(list.size()==0)
			return new UsuarioImpl();
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemsUsuario(co.edu.javeriana.ASHYI.model.ItemPlan, java.lang.String)
	 */
	public ItemsUsuario getItemsUsuario(ItemPlan itemPlan, String userId)
	{
		Usuario us = getUsuario(userId);
		DetachedCriteria dT = DetachedCriteria.forClass(ItemsUsuario.class).add(Restrictions.eq("idUsuario",us)).add(Restrictions.eq("idItemPlan",itemPlan));
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getRecurso(java.lang.Integer)
	 */
	public Recurso getRecurso(Integer idRecurso) {

		DetachedCriteria dT = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",idRecurso));
		List<Recurso> list = getHibernateTemplate().findByCriteria(dT);	
		
		if(list.size() > 0)
		{		
			
			return list.get(0);
		}

		return null;
		
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasRecurso(java.lang.Integer)
	 */
	public List<CaracteristicaRecurso> getCaracteristicasRecurso(Integer idRecurso) {
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaRecurso.class).add(Restrictions.eq("idRecurso.idRecurso",idRecurso));
		List<CaracteristicaRecurso> listC = getHibernateTemplate().findByCriteria(d);	
		
		if(listC.size()>0)
		{
			for(Integer i = 0; i < listC.size(); i++)
			{
				d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listC.get(0).getIdRecurso().getIdRecurso()));
				List<Recurso> listT = getHibernateTemplate().findByCriteria(d);
				
				listC.get(0).setIdRecurso(listT.get(0));
			}
			return listC;
		}
		return new ArrayList<CaracteristicaRecurso>();
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getCaracteristicasItemUsuario(co.edu.javeriana.ASHYI.model.ItemsUsuario)
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemUsuario(ItemsUsuario iPlan)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasItemsUsuario.class)
				.add(Restrictions.eq("idItemPlan.idItemPlan",iPlan.getIdItemPlan().getIdItemPlan()))
				.add(Restrictions.eq("idUsuario.idUsuario",iPlan.getIdUsuario().getIdUsuario()));
		List<CaracteristicasItemsUsuario> listCIU = getHibernateTemplate().findByCriteria(d);
		//System.out.println("!!! carct de item usu: "+listCIU.size());
		for(Integer i = 0; i < listCIU.size(); i++)
		{
			listCIU.get(i).setIdItemPlan(getItemPlan(listCIU.get(i).getIdItemPlan().getIdItemPlan()));
			listCIU.get(i).setIdUsuario(getUsuario(listCIU.get(i).getIdUsuario().getIdUsuario()));
			listCIU.get(i).setIdCaracteristica(getCaracteristica(listCIU.get(i).getIdCaracteristica().getIdCaracteristica()).get(0));
		}
		
		return listCIU;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getActividadInicial()
	 */
	public Actividad getActividadInicial()
	{
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("nivel","InicioUD"));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
		{
			System.out.println("Actividad inicial no existe, creando ...");
			//'17', '3', '10', 'Actividad de inicio', '4', 'Actividad de inicio', '0', NULL, NULL, '0', '2', 'InicioUD'
			Actividad acInicio = new ActividadImpl(getTipo(1).get(0), getItemId(3), "Actividad de inicio", "", 4, 0, "InicioUD");
			acInicio.setEs_refuerzo(false);
			saveActividad(acInicio, new ArrayList<String>(), "", true);
			
			acInicio.setIdActividad(getUltimaActividad());
			//'Inicio de unidad didactica'			
			System.out.println("Actividad inicial creada");
			
			Objetivo obj = getObjetivo("Inicio de unidad didactica");
			
			if(obj.getNombre().equalsIgnoreCase("Does not exist"))
			{
				System.out.println("Objetivo no encontrado, creando ...");
				obj = new ObjetivoImpl("Inicio de unidad didactica");
				saveCaracteristica(obj, new ArrayList<String>(), "", true);
				
				obj.setIdObjetivo(getUltimoObjetivo());
				
				System.out.println("Objetivo creado");
			}
				
			ObjetivosActividad objAct = new ObjetivosActividadImpl(1, acInicio, obj);
			
			boolean resp = saveCaracteristica(objAct, new ArrayList<String>(), "", true);
			
			System.out.println("Objetivo Actividad creado: "+resp);
			
			return acInicio;
		}
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemsUsuario(java.lang.String, int)
	 */
	public List<ItemsUsuario> getItemsUsuario(String idUsuario, int idActividad)
	{
		Usuario us = getUsuario(idUsuario);
		
		Query queryI = getSession().createSQLQuery("select iu.idItemPlan from "+
				"items_usuario iu, item_plan ip, grafo g, grafo_relaciones gr, grafos_usuario gu "+
				"where iu.idUsuario = :idU "+ 
				"and gu.idActividad = :idA "+
				"and gu.idUsuario = iu.idUsuario "+
				"and g.idGrafo = gu.idGrafo "+
				"and g.idGrafo = gr.idGrafo "+
				"and iu.IdItemPlan = ip.IdItemPlan "+
				"and gr.idItemPlan_Origen = ip.IdItemPlan "+
				"group by iu.idItemPlan "+
				"order by gr.idRelacionGrafo"
				).setParameter("idU", us.getIdUsuario())
				.setParameter("idA", idActividad);	
		List<Integer> listI = queryI.list();		
		
		Query query = getSession().createSQLQuery("select iu.idItemPlan from "+
				"items_usuario iu, item_plan ip, grafo g, grafo_relaciones gr, grafos_usuario gu "+
				"where iu.idUsuario = :idU "+ 
				"and gu.idActividad = :idA "+
				"and gu.idUsuario = iu.idUsuario "+
				"and g.idGrafo = gu.idGrafo "+
				"and g.idGrafo = gr.idGrafo "+
				"and iu.IdItemPlan = ip.IdItemPlan "+
				"and gr.idItemPlan_Destino = ip.IdItemPlan "+
				"group by iu.idItemPlan "+
				"order by gr.idRelacionGrafo"
				).setParameter("idU", us.getIdUsuario())
				.setParameter("idA", idActividad);	

		List<Integer> list = query.list();	
		List<ItemsUsuario> listIU = new ArrayList<ItemsUsuario>();
		if(list.size() == 0)
			return listIU;
		else
		{
			//item inicial
			listIU.add(getItemsUsuario(getItemPlan(listI.get(0)), idUsuario));
			for(Integer id :  list)
			{				
				listIU.add(getItemsUsuario(getItemPlan(id), idUsuario));
			}
			return listIU;
		}		
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getRelacionesGrafo(int)
	 */
	public List<GrafoRelaciones> getRelacionesGrafo(int idGrafo)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class)
				.add(Restrictions.eq("idGrafo.idGrafo",idGrafo));
		return getHibernateTemplate().findByCriteria(d);	
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#estaItemUsuario(co.edu.javeriana.ASHYI.model.ItemPlan, co.edu.javeriana.ASHYI.model.Usuario)
	 */
	public boolean estaItemUsuario(ItemPlan itemPlan, Usuario usuario)
	{
		ItemsUsuario iU = getItemsUsuario(itemPlan, usuario.getIdUsuarioSakai());
		if(iU != null)
			return true;
		else
			return false;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getGrafoUsuario(co.edu.javeriana.ASHYI.model.Usuario, java.lang.Integer)
	 */
	public List<GrafosUsuario> getGrafoUsuario(Usuario idUsuario, Integer idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario.getIdUsuario())).add(Restrictions.eq("idActividad",idActividad));
		List<GrafosUsuario> listU = getHibernateTemplate().findByCriteria(d);
		
		if(!listU.isEmpty())
		{
			for(GrafosUsuario gu : listU)		
				gu.setIdGrafo(getGrafo(gu.getIdGrafo().getIdGrafo()));
			return listU;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#deleteGrafoRelaciones(co.edu.javeriana.ASHYI.model.Grafo)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getGrafoRelaciones(java.lang.Integer)
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(Integer idGrafo)
	{
		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo",idGrafo));
		List<GrafoRelaciones> listU = getHibernateTemplate().findByCriteria(d);
		return listU;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getIdUsuarioInstructor(java.lang.Integer)
	 */
	public int getIdUsuarioInstructor(Integer idActividad)
	{
		Query queryI = getSession().createSQLQuery("select u.idUsuario from usuario u, grafos_usuario gu "
				+ "where u.idUsuario = gu.idUsuario and u.rol = 'Instructor' and gu.idActividad = :idA")
				.setParameter("idA", idActividad);
		List<Integer> list = queryI.list();
		
		if(list.isEmpty())
			return 0;
		else
			return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getObjetivoPorId(java.lang.Integer)
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
	
	/**
	 * @param nivelRecursividad
	 * @param nombre
	 * @return
	 */
	public List<RecursosActividad> getRecursosActividad(int nivelRecursividad, String nombre)
	{	
		Actividad ac = this.getactividad(nombre,nivelRecursividad);

		DetachedCriteria d = DetachedCriteria.forClass(RecursosActividad.class).add(Restrictions.eq("idActividad.idActividad",ac.getIdActividad()));
		List<RecursosActividad> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(int i = 0; i<listC.size();i++)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItems()
	 */
	public List<String> getItems()
	{
		Query queryI = getSession().createSQLQuery("select id from lesson_builder_items where type=1");
		List<Integer> list = queryI.list();
		
		List<String> l = new ArrayList<String>();
		
		if(list.isEmpty())
			return null;
		else
		{
			for(int i = 0; i < list.size(); i++)
				l.add(String.valueOf(list.get(i)));
			return l;
		}
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getIdsObjetivosActividad(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#getItemPlan(co.edu.javeriana.ASHYI.model.Actividad, co.edu.javeriana.ASHYI.model.Actividad, co.edu.javeriana.ASHYI.model.Recurso, java.lang.Integer, java.lang.Integer)
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
	
	public RecursosActividad getActividadRecursos(Integer idActividad, String idRecursoSakai)
	{		
		RecursosActividad lA = new RecursosActividadImpl();

		Actividad a = this.getActividad(idActividad);

		List<RecursosActividad> rA =this.getRecursosActividad(a.getNivel_recursividad(),a.getNombre());

		Integer estar = 0;
		for(Integer i = 0;i<rA.size();i++)
		{
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
	
	public RespuestaItemsUsuario getRespuestaItemUsuario(int idUsuario,int idItemPlan){
		DetachedCriteria d = DetachedCriteria.forClass(RespuestaItemsUsuario.class).add(Restrictions.eq("idItemPlan.idItemPlan",idItemPlan)).add(Restrictions.eq("idUsuario.idUsuario",idUsuario));
       List<RespuestaItemsUsuario> riu=getHibernateTemplate().findByCriteria(d);
      
       int mayor=-1;
       int pos=-1;
       if(riu.size()>0){ 
    	   for(int i=0;i<riu.size();i++){
        	   if(mayor<riu.get(i).getIdRespuesta()){
        		   mayor=riu.get(i).getIdRespuesta();
        		   pos=i;
        	   }
           }
    	   return riu.get(pos);
      }else{
    	  return null;
      }
		
	}
	
	public List<ItemPlan> obtenerFechasItemPlan(){
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class);
		List<ItemPlan> listC = getHibernateTemplate().findByCriteria(d);	

		
			return listC;
		
	}
	
	public List<Integer> obtenerItemsUsuario(int itemPlan){
		Query query = getSession().createSQLQuery("SELECT idUsuario FROM items_usuario WHERE idItemPlan ="+ itemPlan+" and realizada=0");
	
	
		List<Integer> list = query.list();	
		
		
		
			return list;
	      
	}
	
	public String getMail(String usuario){
		Query query = getSession().createSQLQuery("SELECT email FROM sakai_user WHERE USER_ID = '"+usuario+"'");

	
		List<String> list = query.list();	
		if(list.size()>0){
			return list.get(0);
					
		}else{
			return null;
		}
		
	}

	@Override
	public String getUsuarioSakai(Integer usu) {
		Query query = getSession().createSQLQuery("SELECT idUsuarioSakai FROM usuario WHERE idUsuario ="+ usu+"");
		
		
		List<String> list = query.list();	
		
		
		if(list.size()>0){
			return list.get(0);
					
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see model.AshyiToolDao#makeItemPlan(model.Actividad, model.Actividad, boolean, java.lang.Integer)
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad, boolean estaActivo, Integer orden) {
		return new ItemPlanImpl(ud, actividad, estaActivo, orden);
	}
	
	/* (non-Javadoc)
	 * @see model.AshyiToolDao#makeItemPlan(model.Actividad, model.Actividad, model.Recurso, boolean, java.lang.Integer)
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad, Recurso recurso, boolean estaActivo, Integer orden)
	{
		return new ItemPlanImpl(ud, actividad, recurso, estaActivo, orden);
	}

	/* (non-Javadoc)
	 * @see model.AshyiToolDao#getUltimoItemPlan()
	 */
	public Integer getUltimoItemPlan()
	{
		Integer id = 0;

		Query query = getSession().createSQLQuery("SELECT MAX(idItemPlan) AS id FROM item_plan");
		List<Integer> list = query.list();
		
		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.AshyiToolDao#itemEsta(co.edu.javeriana.ASHYI.model.Actividad, co.edu.javeriana.ASHYI.model.Actividad, co.edu.javeriana.ASHYI.model.Recurso, java.lang.Integer, boolean, java.lang.Integer)
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
		{			
			return true;
		}
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
			{
				return false;
			}
		}
	}
	
}