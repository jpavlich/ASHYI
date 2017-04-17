package co.edu.javeriana.ashiy.tool.pages;

import co.edu.javeriana.ashiy.model.Actividad;
import co.edu.javeriana.ashiy.model.unidadDidactica;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

public class ActividadesPage extends BasePage
{
  private String nombreUsuario;
  private unidadDidactica unidadD;
  private String actividadSeleccionada;
  private ListChoice<String> listaTemas;
  private int actividadDisponilbe;
  private List<Actividad> actividades;

  public ActividadesPage(unidadDidactica unidad)
  {
    this.unidadD = unidad;
    this.actividadSeleccionada = "";

    add(new Label("unidad", this.unidadD.getNombre()));

    this.actividades = this.unidadD.getActividades();
    List<String> nombres = new ArrayList<String>();
    int contAct = 0;
    for (int i = 0; i < this.actividades.size(); i++)
    {
      if (((Actividad)this.actividades.get(i)).isEstado())
      {
        contAct++;
      }
    }

    if (contAct == 0)
    {
      nombres.add(((Actividad)this.actividades.get(0)).getNombre());
    }
    else if (contAct != this.actividades.size())
    {
      nombres.add(((Actividad)this.actividades.get(contAct)).getNombre());
    }

    add(new ListView<Actividad>("listactividades", actividades)
    {
      protected void populateItem(ListItem<Actividad> item)
      {
        Actividad act = (Actividad)item.getModelObject();
        Label actLabel = new Label("nombre", act.getNombre());
        String styleAttr = "color: green;";
        String styleAttr2 = "color: red;";
        if (act.isEstado()) {
          actLabel.add( new SimpleAttributeModifier("style", styleAttr) );
        }
        else {
          actLabel.add(new SimpleAttributeModifier("style", styleAttr2));
        }
        item.add(actLabel);
      }
     });
    this.listaTemas = new ListChoice("actividades", new PropertyModel(this, "actividadSeleccionada"), nombres);

    this.listaTemas.setMaxRows(1);

    Form<?> form = new Form("formAct")
    {
      protected void onSubmit()
      {
        for (int i = 0; i < ActividadesPage.this.actividades.size(); i++)
        {
          if (((Actividad)ActividadesPage.this.actividades.get(i)).getNombre().equals(ActividadesPage.this.actividadSeleccionada))
          {
            setResponsePage(new ActividadPage((Actividad)ActividadesPage.this.actividades.get(i)));
          }
        }
      }
    };
    add(form );
    form.add(this.listaTemas );
  }

  public String getNombreUsuario()
  {
    return this.nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }
  public ListChoice<String> getListaTemas() {
    return this.listaTemas;
  }

  public void setListaTemas(ListChoice<String> listaTemas) {
    this.listaTemas = listaTemas;
  }

  public int getActividadDisponilbe() {
    return this.actividadDisponilbe;
  }

  public void setActividadDisponilbe(int actividadDisponilbe) {
    this.actividadDisponilbe = actividadDisponilbe;
  }
}