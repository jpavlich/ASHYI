package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.SessionManager;

import edu.javeriana.BeanBESA;

/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class AgentPage extends BasePage {

	private SessionManager sessionManager;


	public AgentPage() {
		disableLink(firstLink);

		//get current session
		sessionManager = (SessionManager) ComponentManager.get(SessionManager.class);

		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			BeanBESA bean = (BeanBESA) envCtx.lookup("bean/MyBeanFactory");

			System.out.println("Verificar existencia de agente");
			String aliasA = "";
			if(!bean.existeAgente(sessionManager.getCurrentSession().getUserEid()))
			{
				System.out.println("Agente "+sessionManager.getCurrentSession().getUserEid()+" no existe");
				//aliasA = bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
				aliasA = bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
				
			}
			System.out.println("Obteniendo agente");
			final String aliasAP = bean.getAgent(sessionManager.getCurrentSession().getUserEid());
			System.out.println("Agente "+aliasA+" creado");
			List lista = new ArrayList<String>();
			lista.add(aliasA);
			add(new ListView<String>("listview", lista) {
				
				protected void populateItem(ListItem<String> item) {
					item.add(new Label("alias", aliasAP));
				}
			});
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
