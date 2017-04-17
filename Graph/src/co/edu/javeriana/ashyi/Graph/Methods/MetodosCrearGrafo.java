package co.edu.javeriana.ashyi.Graph.Methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import co.edu.javeriana.ASHYI.hbm.*;
import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.*;
import co.edu.javeriana.ashyi.Graph.Graph;

public class MetodosCrearGrafo<T> extends Graph<T> {
	
	private Consulta c;
	
	/**
	 * ItemPlans organizados por objetivos de actividad.
	 */
	LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> itemPlansPorObjetivo;
	/**
	 * ItemPlans remediales organizados por objetivos de actividad.
	 */
	LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> refuerzosPorObjetivo;
	/**
	 * Objetivos de la unidad did&aacutectica.
	 */
	ArrayList<ObjetivosActividad> objetivos;
	/**
	 * Grafo con los identificadores de los itemPlans de la unidad did&aacutectica.
	 */
	Graph<Integer> grafoActividades;
	/**
	 * Grafo con los identificadores de los itemPlans del estudiante.
	 */
	Graph<Integer> grafoEstudiante;
	
	
	public Consulta getC() {
		return c;
	}


	public void setC(Consulta c) {
		this.c = c;
	}


	public LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> getItemPlansPorObjetivo() {
		return itemPlansPorObjetivo;
	}


	public void setItemPlansPorObjetivo(
			LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> itemPlansPorObjetivo) {
		this.itemPlansPorObjetivo = itemPlansPorObjetivo;
	}


	public LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> getRefuerzosPorObjetivo() {
		return refuerzosPorObjetivo;
	}


	public void setRefuerzosPorObjetivo(
			LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>> refuerzosPorObjetivo) {
		this.refuerzosPorObjetivo = refuerzosPorObjetivo;
	}


	public ArrayList<ObjetivosActividad> getObjetivos() {
		return objetivos;
	}


	public void setObjetivos(ArrayList<ObjetivosActividad> objetivos) {
		this.objetivos = objetivos;
	}


	public Graph<Integer> getGrafoActividades() {
		return grafoActividades;
	}


	public void setGrafoActividades(Graph<Integer> grafoActividades) {
		this.grafoActividades = grafoActividades;
	}


	public Graph<Integer> getGrafoEstudiante() {
		return grafoEstudiante;
	}


	public void setGrafoEstudiante(Graph<Integer> grafoEstudiante) {
		this.grafoEstudiante = grafoEstudiante;
	}


	public MetodosCrearGrafo() {
		super();
		this.c = new Consulta();
	}


	public Object createGarph()
	{
		return new Object();
		
	}
	
	 
	public Graph<Integer> onReceiveActivities(List<Actividad> lista, List<Actividad> refuerzoList, int idActividad, String nomActividad) {
		List<Integer> idItemsUltimoObjetivo = new ArrayList<Integer>();
		ObjetivosActividad objetivoActual, objetivoActualR, ultimoObjetivo=new ObjetivosActividadImpl();
		this.itemPlansPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		//this.refuerzos=new ArrayList<ItemPlan>();
		refuerzosPorObjetivo = new LinkedHashMap<ObjetivosActividad, ArrayList<ItemPlan>>();
		this.objetivos=new ArrayList<ObjetivosActividad>();
		Actividad inicial=c.getActividadInicial();
		//System.out.println("Actividad inicial: "+inicial.getNombre()+" id: "+inicial.getIdActividad());
		objetivoActual = c.getObjetivosActividad(inicial.getNombre(), inicial.getNivel_recursividad()).get(0);
		System.out.println("Objetivo inicial: "+objetivoActual.getIdObjetivo().getNombre()+" id: "+objetivoActual.getIdObjetivo().getIdObjetivo());
		LinkedList<Actividad> list=new LinkedList<Actividad>();
		list.add(inicial);
		for (int i = 0; i < lista.size(); i++) {
			list.add(lista.get(i));
		}
		//==================================a&ntildeadir las actividades originales al mapa==================================
		if (list != null && !list.isEmpty()) {
			objetivoActual = c.getObjetivosActividad(list.get(0).getNombre(), list.get(0).getNivel_recursividad()).get(0);
			objetivos.add(objetivoActual);
			int orden=1;
			for (int i = 0; i < list.size(); i++) {
				Actividad auxActividad = list.get(i);
				ObjetivosActividad ObjetivoAc = c.getObjetivosActividad(auxActividad.getNombre(),auxActividad.getNivel_recursividad()).get(0);
				if (ObjetivoAc.getIdObjetivo().getIdObjetivo() != 
						objetivoActual.getIdObjetivo().getIdObjetivo())
				{
					objetivoActual = ObjetivoAc;
					objetivos.add(ObjetivoAc);
					orden+=2;
				}

				List<RecursosActividad> actividadRecurso = c.actividadesRecurso(c.getItems(), auxActividad);

				if(actividadRecurso.size() > 0)
				{
					for(RecursosActividad ac : actividadRecurso)
					{
						Actividad ud=c.getActividad(nomActividad, 3);
						auxActividad = ac.getIdActividad();
						Recurso auxRecurso = ac.getIdRecurso();
						//nuevo item plan activo
						boolean estaItem = c.itemEsta(ud, auxActividad, auxRecurso, 1, false, orden);
						ItemPlan itemPlan = new ItemPlanImpl();
						itemPlan.setOrden(orden);
						if(!estaItem)
						{
							itemPlan = c.addItemPlan(ud, auxActividad, auxRecurso, true, orden);
						}
						else
						{
							itemPlan = c.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
						}
						if(!itemPlansPorObjetivo.containsKey(objetivoActual))
							itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
						itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
					}
				}else
				{				
					Actividad ud=c.getActividad(nomActividad, 3);

					//nuevo item plan activo sin recurso
					boolean estaItem = c.itemEsta(ud, auxActividad, new RecursoImpl(), 2, false, orden);
					ItemPlan itemPlan = new ItemPlanImpl();
					if(!estaItem)
					{
						itemPlan = c.addItemPlan(ud, auxActividad, true, orden);
					}
					else
					{
						itemPlan = c.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
					}
					if(!itemPlansPorObjetivo.containsKey(objetivoActual))
						itemPlansPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
					itemPlansPorObjetivo.get(objetivoActual).add(itemPlan);
				}
			}
			ultimoObjetivo=objetivoActual;		
		}

		int numObjetivos=itemPlansPorObjetivo.keySet().size();
		//System.out.println("!!!!!!!!!!!!!!!1 : "+numObjetivos);
		//==================================a&ntildeadir las de refuerzo al mapa==================================
		int orden = 2;
		if(refuerzoList.size()>0)
		{			
			for(int j = 0; j <objetivos.size()-1; j++)
			{
				objetivoActual = objetivos.get(j);
				objetivoActualR = c.getObjetivosActividad(refuerzoList.get(0).getNombre(), refuerzoList.get(0).getNivel_recursividad()).get(0);
				for (int i = 0; i < refuerzoList.size(); i++) {
					Actividad auxActividad = refuerzoList.get(i);
					List<RecursosActividad> actividadRecurso = c.actividadesRecurso(c.getItems(), auxActividad);

					if(actividadRecurso.size() > 0)
					{
						for(RecursosActividad ac : actividadRecurso)
						{
							Actividad ud=c.getActividad(nomActividad, 3);
							auxActividad = ac.getIdActividad();
							Recurso auxRecurso = ac.getIdRecurso();
							//nuevo item plan activo
							//1, con recurso y true de refuerzo
							boolean estaItem = c.itemEsta(ud, auxActividad, auxRecurso, 1, true, orden);
							ItemPlan itemPlan = new ItemPlanImpl();
							if(!estaItem)
							{
								itemPlan = c.addItemPlan(ud, auxActividad, auxRecurso, true, orden);
							}
							else
							{
								itemPlan = c.getItemPlan(ud, auxActividad, auxRecurso, 1, orden);
							}
							if(!refuerzosPorObjetivo.containsKey(objetivoActual))
								refuerzosPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
							refuerzosPorObjetivo.get(objetivoActual).add(itemPlan);
						}
						//las de refuerzo
					}else
					{	
						Actividad ud=c.getActividad(nomActividad, 3);
						//nuevo item plan activo sin recurso
						//2, sin recurso y true de refuerzo
						boolean estaItem = c.itemEsta(ud, auxActividad, new RecursoImpl(), 2, true, orden);
						ItemPlan itemPlan = new ItemPlanImpl();
						if(!estaItem)
						{
							itemPlan = c.addItemPlan(ud, auxActividad, true, orden);
							itemPlan.setIdItemPlan(c.getUltimoItemPlan());		
						}
						else
						{
							itemPlan = c.getItemPlan(ud, auxActividad, new RecursoImpl(), 2, orden);
						}
						if(!refuerzosPorObjetivo.containsKey(objetivoActual))
							refuerzosPorObjetivo.put(objetivoActual,new ArrayList<ItemPlan>());
						refuerzosPorObjetivo.get(objetivoActual).add(itemPlan);
					}					
				}
				orden+=2;
			}
			//cerrar ciclo de objetivos
		}

		//==================================crear grafo de verdad==================================
		grafoActividades = new Graph<Integer>();
		Iterator<ObjetivosActividad> it = objetivos.iterator();
		ObjetivosActividad objaux=it.next();
		ObjetivosActividad objauxR=objaux;

		int numObj=1;
		Iterator<ObjetivosActividad> itaux = objetivos.iterator();
		while(itaux.hasNext())
		{
			System.out.println("Objetivo "+numObj);
			ObjetivosActividad auxobj=itaux.next();
			ArrayList<ItemPlan> itemsPlanNivelAnterior=itemPlansPorObjetivo.get(auxobj);
			for (int i = 0; i < itemsPlanNivelAnterior.size(); i++) {
				if(itemsPlanNivelAnterior.get(i).getIdItemPlan() == null)
				{
					itemsPlanNivelAnterior.remove(i);
					i--;
				}
				System.out.print(itemsPlanNivelAnterior.get(i).getIdItemPlan()+" ");
			}
			numObj++;
		}

		ArrayList<ItemPlan> itemsPlanNivelAnterior = itemPlansPorObjetivo.get(objaux);
		int k=0;
		//sizeRefuerzos=refuerzos.size();
		int times=1;
		orden=1;
		while (it.hasNext()) {
			objauxR=objaux;
			objaux=it.next();
			ArrayList<ItemPlan> itemsPlanNivelActual = itemPlansPorObjetivo.get(objaux);

			//conectar originales con siguiente nivel y con refuerzo, luego refuerzo con siguiente nivel
			boolean refuerzosConectados=false;
			for (int i = 0; i < itemsPlanNivelAnterior.size(); i++) {
				ItemPlan itemPlanNivelAnterior = itemsPlanNivelAnterior.get(i);
				for (int j = 0; j < itemsPlanNivelActual.size(); j++) {
					ItemPlan actual=itemsPlanNivelActual.get(j);
					grafoActividades.addEdge(itemPlanNivelAnterior.getIdItemPlan(),
							actual.getIdItemPlan(), 4.0, orden);
				}

				//conectar originales con refuerzo y refuerzo con siguiente nivel				
				if(!refuerzosPorObjetivo.isEmpty()&&refuerzosPorObjetivo.get(objauxR) != null)
				{
					ArrayList<ItemPlan> itemsPlanRzNivelActual = refuerzosPorObjetivo.get(objauxR);

					for (int m = 0; m < itemsPlanRzNivelActual.size(); m++) {
						ItemPlan refuerzo=itemsPlanRzNivelActual.get(m);
						grafoActividades.addEdge(itemPlanNivelAnterior.getIdItemPlan(),refuerzo.getIdItemPlan(), 4.0, orden);

						if(!refuerzosConectados)
						{
							for (int l = 0; l < itemsPlanNivelActual.size(); l++) {
								ItemPlan actual=itemsPlanNivelActual.get(l);
								//System.out.println("Refuerzo origen: "+refuerzo.getIdItemPlan()+", actividad destino "+actual.getIdItemPlan());
								grafoActividades.addEdge(refuerzo.getIdItemPlan(),
										actual.getIdItemPlan(), 4.0, orden+1);
							}
							
						}
					}
					refuerzosConectados=true;
				}	
			}
			refuerzosConectados=false;
			itemsPlanNivelAnterior=itemsPlanNivelActual;
			orden+=2;
		}

		Iterator <ItemPlan> it2=this.itemPlansPorObjetivo.get(ultimoObjetivo).iterator();
		while(it2.hasNext())
			idItemsUltimoObjetivo.add(it2.next().getIdItemPlan());

		//=======================================enviar grafo a gente editor======================= 
		return grafoActividades;

	}
}
