package co.edu.javeriana.ashiy.tool.pages;

import co.edu.javeriana.ashiy.model.Curso;
import co.edu.javeriana.ashiy.model.Tema;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.model.PropertyModel;

public class TemaPage extends BasePage
{
  private Curso curso;
  private String temaSeleccionado;
  private ListChoice<String> listaTemas;

  public TemaPage(Curso cursoD)
  {
    this.curso = cursoD;

    add(new Label("nombreCurso", this.curso.getNombre()));
    add(new Label("descripcionCurso", this.curso.getDescripcion()));

    this.temaSeleccionado = "";

    List<String> nombres = new ArrayList<String>();
    for (int i = 0; i < this.curso.getTemas().size(); i++)
    {
      nombres.add((curso.getTemas().get(i)).getNombre());
    }

    this.listaTemas = new ListChoice("temas", new PropertyModel(this, "temaSeleccionado"), nombres);

    this.listaTemas.setMaxRows(this.curso.getTemas().size());

    Form<?> form = new Form("form")
    {
      protected void onSubmit()
      {
        for (int i = 0; i < 7; i++)
        {
          if ((curso.getTemas().get(i)).getNombre().equals(temaSeleccionado))
          {
            setResponsePage(new DescripcionPage(curso.getTemas().get(i)));
          }
        }
      }
    };
    add( form );
    form.add( this.listaTemas );
  }
}