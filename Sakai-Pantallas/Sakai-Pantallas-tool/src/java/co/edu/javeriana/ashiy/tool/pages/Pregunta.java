package co.edu.javeriana.ashiy.tool.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.SessionManager;

import co.edu.javeriana.ashiy.tool.pages.Diagnostico.Answer;


/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class Pregunta extends BasePage {

	private Diagnostico diag;

	public Pregunta(Diagnostico d) {

		diag=d;
		// Radio buttons must be part of a Form component.
		final RadioGroup<Answer> group = new RadioGroup<Answer>("group", new Model<Answer>());
		Form <?> form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				editPage();
			}
		};

		add(form);
		form.add(group);

		add(new Label("question", diag.questions.get(diag.currentQuestion).question));
		
		if(diag.currentQuestion==diag.questions.size()-1)
			add(new Label("result", "Estilo de aprendizaje visual"));
		else
			add(new Label("result", ""));
		
		ListView<Answer> answers = new ListView<Answer>("answers", diag.questions.get(diag.currentQuestion).answers)
				{
			@Override
			protected void populateItem(ListItem<Answer> item)
			{
				item.add(new Radio("radio", item.getModel()));
				item.add(new Label("style", new PropertyModel(item.getModel(), "style")));
				item.add(new Label("answer", new PropertyModel(item.getModel(), "answer")));
			}
				};
				group.add(answers);
	}

	public void editPage()
	{
		diag.currentQuestion++;
		if(diag.currentQuestion<diag.questions.size())
		{
			setResponsePage(new Pregunta(diag));
		}
		else
		{
			
		}
	}
}
