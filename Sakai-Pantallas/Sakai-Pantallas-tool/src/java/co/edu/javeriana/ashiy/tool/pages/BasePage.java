package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import co.edu.javeriana.ashiy.logic.ProjectLogic;
import co.edu.javeriana.ashiy.logic.SakaiProxy;
import co.edu.javeriana.ashiy.model.Actividad;


/**
 * This is our base page for our Sakai app. It sets up the containing markup and top navigation.
 * All top level pages should extend from this page so as to keep the same navigation. The content for those pages will
 * be rendered in the main area below the top nav.
 * 
 * <p>It also allows us to setup the API injection and any other common methods, which are then made available in the other pages.
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class BasePage extends WebPage implements IHeaderContributor {

	private static final Logger log = Logger.getLogger(BasePage.class); 

	@SpringBean(name="co.edu.javeriana.ashiy.logic.SakaiProxy")
	protected SakaiProxy sakaiProxy;

	@SpringBean(name="co.edu.javeriana.ashiy.logic.ProjectLogic")
	protected ProjectLogic projectLogic;

	Link<Void> firstLink;
	//Link<Void> temasLink;
	//Link<Void> ActividadesLink;
	Link<Void> opcEstudianteLink;
	Link<Void> diagnosticoLink;
	//Link<Void> unidadLink;

	FeedbackPanel feedbackPanel;

	public BasePage() {

		log.debug("BasePage()");


		//first link
		firstLink = new Link<Void>("firstLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(new AgentPage());
			}
		};
		firstLink.add(new Label("firstLinkLabel",new ResourceModel("link.first")).setRenderBodyOnly(true));
		firstLink.add(new AttributeModifier("title", true, new ResourceModel("link.first.tooltip")));
		add(firstLink);
		
		//opciones
		opcEstudianteLink = new Link<Void>("opcEstudianteLink") {
			private static final long serialVersionUID = 1L;

		      public void onClick() { 
		    	  setResponsePage(new OpcionesEstudiantePage()); 
		    	  }
		    };
		    this.opcEstudianteLink.add( new Label("opcEstudianteLinkLabel", new ResourceModel("link.opcionesEstudiante")).setRenderBodyOnly(true) );
		    this.opcEstudianteLink.add(new AttributeModifier("OpcionesEstudiante", true, new ResourceModel("link.opcionesEstudiante.tooltip")));
		    add(new Component[] { this.opcEstudianteLink });

/*
		//tema link
		temasLink = new Link<Void>("temasLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(new TemaPage());
			}
		};
		temasLink.add(new Label("temasLinkLabel",new ResourceModel("link.temas")).setRenderBodyOnly(true));
		temasLink.add(new AttributeModifier("Temas", true, new ResourceModel("link.temas.tooltip")));
		add(temasLink);

//		//actividades link
//		ActividadesLink = new Link<Void>("actividadesLink") {
//			private static final long serialVersionUID = 1L;
//			public void onClick() {
//				List<Actividad> actividadesU = new ArrayList<Actividad>();
//				for(int i = 0;i<5;i++)
//				{
//					if(i==0)
//					{
//						actividadesU.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, true));
//					}
//					else
//					{
//						actividadesU.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, false));
//					}
//				}
//				setResponsePage(new ActividadesPage(new UnidadDidactica(actividadesU)));
//			}
//		};
//		ActividadesLink.add(new Label("actividadesLinkLabel",new ResourceModel("link.actividades")).setRenderBodyOnly(true));
//		ActividadesLink.add(new AttributeModifier("Actividades", true, new ResourceModel("link.actividades.tooltip")));
//		add(ActividadesLink);

*/

		//diagnostico link
		diagnosticoLink = new Link<Void>("diagnosticoLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(new Pregunta(new Diagnostico()));
			}
		};
		diagnosticoLink.add(new Label("diagnosticoLinkLabel",new ResourceModel("link.diagnosticoLink")).setRenderBodyOnly(true));
		diagnosticoLink.add(new AttributeModifier("title", true, new ResourceModel("link.diagnosticoLink.tooltip")));
		add(diagnosticoLink);
/*
		//unidad link
		unidadLink = new Link<Void>("unidadLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(new UnidadDidactica());
			}
		};
		unidadLink.add(new Label("unidadLinkLabel",new ResourceModel("link.unidadLink")).setRenderBodyOnly(true));
		unidadLink.add(new AttributeModifier("title", true, new ResourceModel("link.unidadLink.tooltip")));
		add(unidadLink);
*/
		//		
		//		//third link
		//		thirdLink = new Link<Void>("thirdLink") {
		//			private static final long serialVersionUID = 1L;
		//			public void onClick() {
		//				setResponsePage(new ThirdPage());
		//			}
		//		};
		//		thirdLink.add(new Label("thirdLinkLabel",new StringResourceModel("link.third", null, new String[] {"3"})).setRenderBodyOnly(true));
		//		thirdLink.add(new AttributeModifier("title", true, new ResourceModel("link.third.tooltip")));
		//		add(thirdLink);
		//		

		// Add a FeedbackPanel for displaying our messages
		feedbackPanel = new FeedbackPanel("feedback"){

			@Override
			protected Component newMessageDisplayComponent(final String id, final FeedbackMessage message) {
				final Component newMessageDisplayComponent = super.newMessageDisplayComponent(id, message);

				if(message.getLevel() == FeedbackMessage.ERROR ||
						message.getLevel() == FeedbackMessage.DEBUG ||
						message.getLevel() == FeedbackMessage.FATAL ||
						message.getLevel() == FeedbackMessage.WARNING){
					add(new SimpleAttributeModifier("class", "alertMessage"));
				} else if(message.getLevel() == FeedbackMessage.INFO){
					add(new SimpleAttributeModifier("class", "success"));        			
				} 

				return newMessageDisplayComponent;
			}
		};
		add(feedbackPanel); 

	}

	/**
	 * Helper to clear the feedbackpanel display.
	 * @param f	FeedBackPanel
	 */
	public void clearFeedback(FeedbackPanel f) {
		if(!f.hasFeedbackMessage()) {
			f.add(new SimpleAttributeModifier("class", ""));
		}
	}


	/**
	 * This block adds the required wrapper markup to style it like a Sakai tool. 
	 * Add to this any additional CSS or JS references that you need.
	 * 
	 */
	public void renderHead(IHeaderResponse response) {


		//get Sakai skin
		String skinRepo = sakaiProxy.getSkinRepoProperty();
		String toolCSS = sakaiProxy.getToolSkinCSS(skinRepo);
		String toolBaseCSS = skinRepo + "/tool_base.css";

		//Sakai additions
		response.renderJavascriptReference("/library/js/headscripts.js");
		response.renderCSSReference(toolBaseCSS);
		response.renderCSSReference(toolCSS);
		response.renderOnLoadJavascript("setMainFrameHeight( window.name )");

		//Tool additions (at end so we can override if required)
		response.renderString("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		//response.renderCSSReference("css/my_tool_styles.css");
		//response.renderJavascriptReference("js/my_tool_javascript.js");

	}


	/** 
	 * Helper to disable a link. Add the Sakai class 'current'.
	 */
	protected void disableLink(Link<Void> l) {
		l.add(new AttributeAppender("class", new Model<String>("current"), " "));
		l.setRenderBodyOnly(true);
		l.setEnabled(false);
	}



}
