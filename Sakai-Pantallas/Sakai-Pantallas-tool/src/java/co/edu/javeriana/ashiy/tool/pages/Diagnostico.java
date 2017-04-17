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


/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class Diagnostico {

	List<Question> questions;
	Integer currentQuestion;

	public Diagnostico() {

		currentQuestion=0;
		questions = new ArrayList<Question> ();
		// Create question
		for(int i=0;i<5;i++)
		{
			List<Answer> ans=new ArrayList<Answer>();
			ans.add(new Answer("A", "Visual"));
			ans.add(new Answer("B", "Textual"));
			ans.add(new Answer("C", "Auditivo"));
			ans.add(new Answer("D", "Otro"));
			questions.add(new Question("Pregunta "+(i+1), ans));
		}

	}

	public class Question implements Serializable
	{
		String question;
		List<Answer> answers;

		public Question(String question, List<Answer> answers) {
			super();
			this.question = question;
			this.answers = answers;
		}
	}

	public class Answer implements Serializable
	{
		String answer;
		String style;

		public Answer(String answer, String style) {
			super();
			this.answer = answer;
			this.style = style;
		}
	}
}
