package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;

import co.edu.javeriana.ashiy.model.Actividad;
import co.edu.javeriana.ashiy.model.unidadDidactica;
/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class UnidadDidactica extends BasePage {

	private List<String> objetivoList;
	private List<String> estrategiaList;
	private List<String> actividadList;	
	private String nombre;
	private List<Actividad> actividades;
	private Link<Void> ActividadesLink;
	private unidadDidactica unidad;

	public UnidadDidactica(List<Actividad> actividades) {
		super();
		this.actividades = actividades;
	}
	public UnidadDidactica(unidadDidactica unidadD) {
		
		this.unidad = unidadD;

		objetivoList = new ArrayList<String>();
		estrategiaList = new ArrayList<String>();
		actividadList = new ArrayList<String>();

		objetivoList.add("Problematizar las imágenes de tecnología de cara a la construcción de una mirada crítica frente a la incorporación de las TIC en la educación.");
		objetivoList.add("Aportar herramientas teóricas y conceptuales para la construcción del problema de investigación a trabajar.");
		objetivoList.add("Desarrollar pensamiento crítico con respecto al uso y apropiación de las TIC en los contextos escolares.");

		estrategiaList.add("Para el desarrollo de los objetivos se proponen las siguientes estrategias: lecturas, exposiciones, talleres y escritura de ensayo.");

		actividadList.add("Actividad 1");
		actividadList.add("Actividad 2");

		add(new Label("unidad", unidad.getNombre() ));

		ListView<String> objetivos = new ListView<String>("objetivos", objetivoList){
			protected void populateItem(ListItem<String> item)
			{
				String cosa=(String)item.getModelObject();
				item.add(new Label("o", cosa+""));
			}
		};
		add(objetivos);
		ListView<String> estrategias = new ListView<String>("estrategias", estrategiaList){
			@Override
			protected void populateItem(ListItem<String> item)
			{
				String cosa=(String)item.getModelObject();
				item.add(new Label("e", cosa+""));
			}
		};
		add(estrategias);

		//actividades link
		ActividadesLink = new Link<Void>("actividadesLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				//actividades = new ArrayList<Actividad>();
				actividades = unidad.getActividades();
//				for(int i = 0;i<5;i++)
//				{
//					if(i==0)
//					{
//						actividades.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, true));
//					}
//					else
//					{
//						actividades.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, false));
//					}
//				}
				setResponsePage(new ActividadesPage(unidad));
			}
		};
		ActividadesLink.add(new Label("actividadesLinkLabel",new ResourceModel("link.actividades")).setRenderBodyOnly(true));
		ActividadesLink.add(new AttributeModifier("Actividades", true, new ResourceModel("link.actividades.tooltip")));
		add(ActividadesLink);
	}
	
	public List<Actividad> getActividades() {
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
