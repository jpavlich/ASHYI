function optionSelect(){
	var usuarios=document.getElementsByName("usuarios")[0].innerHTML;
	var array=usuarios.split(",");
	var tam=array.length;
	
	



	document.write("<select id='selection'>");
	document.write("<option onclick='generarTabla()'>"+"Todos"+"</option>");


	for (var i=0;i<tam;i++){
	   document.write("<option onclick='generarTabla()'>"+array[i]+"</option>");
	} 

	document.write("</select>");    
	document.write("<br></br>");

	
}


function optionSelect2(){
	var usuarios=document.getElementsByName("usuarios")[0].innerHTML;
	var array=usuarios.split(",");
	var tam=array.length;
	
	



	document.write("<select id='selection2'>");
	document.write("<option onclick='generarTabla2()'>"+"Todos"+"</option>");


	for (var i=0;i<tam;i++){
	   document.write("<option onclick='generarTabla2()'>"+array[i]+"</option>");
	} 

	document.write("</select>");    
	document.write("<br></br>");

	
}

function tabla(){

  var table=document.getElementById("dataTable");
  var text=document.getElementsByName("datosTabla")[0].innerHTML;
  
  var array=text.split(";");
  for (var i=0;i<array.length;i++){
  var row=table.insertRow(i+1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  var cell3 = row.insertCell(2);
  var cell4 = row.insertCell(3);
  var cell5 = row.insertCell(4);
  var cell6 = row.insertCell(5);
  var cell7 = row.insertCell(6);
  var array2=array[i].split(",");
  cell1.innerHTML = array2[0];
  cell2.innerHTML = array2[1];
  cell3.innerHTML = array2[2];
  cell4.innerHTML = array2[3];
  cell5.innerHTML = array2[4];
  cell6.innerHTML = array2[5];
  cell7.innerHTML = array2[6];
  }

}


function generarTabla(){
  var table=document.getElementById("dataTable");
  borrar(table);

  var text=document.getElementsByName("datosTabla")[0].innerHTML;
  obtenerDatos(text,table);
  
}

function obtenerDatos(text,table){

	  var x = document.getElementById("mySelect");
	  tama=x.length;
	  for(var i=0;i<tama;i++){
		  x.remove(0);
		  }
	 
	  var y = document.getElementById("mySelectActividad");
	  tamaA=y.length;
	  for(var i=0;i<tamaA;i++){
		  y.remove(0);
		  }
	
	
  var array=text.split(";");
var idEstudiante=document.getElementById("selection").value;
var entra=1;
var tabla="";
var unidades=new Array();
var actividades=new Array();
  for (var i=0;i<array.length;i++){
   var array2=array[i].split(",");
   if(idEstudiante==array2[0]){

	   var row=table.insertRow(entra);
	   var cell1 = row.insertCell(0);
	   var cell2 = row.insertCell(1);
	   var cell3 = row.insertCell(2);
	   var cell4 = row.insertCell(3);
	   var cell5 = row.insertCell(4);
	   var cell6 = row.insertCell(5);
	   var cell7 = row.insertCell(6);
	   cell1.innerHTML = array2[0];
	   cell2.innerHTML = array2[1];
	   cell3.innerHTML = array2[2];
	   cell4.innerHTML = array2[3];
	   cell5.innerHTML = array2[4];
	   cell6.innerHTML = array2[5];
	   cell7.innerHTML = array2[6];
       entra=entra+1;
       unidades.push(array2[2]);
       actividades.push(array2[3]);
       tabla+=array2[0]+","+array2[1]+","+array2[2]+","+array2[3]+","+array2[4]+","+array2[5]+","+array2[6]+";";
       
   }else if(idEstudiante=="Todos"){
       var row=table.insertRow(i+1);

       var cell1 = row.insertCell(0);
       var cell2 = row.insertCell(1);
       var cell3 = row.insertCell(2);
       var cell4 = row.insertCell(3);
       var cell5 = row.insertCell(4);
       var cell6 = row.insertCell(5);
       var cell7 = row.insertCell(6);
       var array2=array[i].split(",");
       cell1.innerHTML = array2[0];
       cell2.innerHTML = array2[1];
       cell3.innerHTML = array2[2];
       cell4.innerHTML = array2[3];
       cell5.innerHTML = array2[4];
       cell6.innerHTML = array2[5];
       cell7.innerHTML = array2[6];

   }
  }
  

  
  if(idEstudiante!="Todos"){
 
	  
	  var optionDefault = document.createElement("option");
	  optionDefault.text = "Todos";
	  x.add(optionDefault);
	  
	  var unicas=unidades.unique();
	  for(var i=0;i<unicas.length;i++){
	  var option = document.createElement("option");
		  option.text = unicas[i];
		  x.add(option); 
	  
	  }
	  
	  var optionDefault2 = document.createElement("option");
	  optionDefault2.text = "Todos";
	  y.add(optionDefault2);
	  
	  var unicasA=actividades.unique();
	  for(var i=0;i<unicasA.length;i++){
	  var option2 = document.createElement("option");
		  option2.text = actividades[i];
		  y.add(option2); 
	  
	  }
	  
	  document.getElementById("mySelect").style.display = "block";
	  document.getElementById("mySelectActividad").style.display = "block";
      document.getElementById("seleccionado").innerHTML=tabla.substring(0,(tabla.length-1));
	  
  }else{
	  var x = document.getElementById("mySelect");
	  var tama=x.length;
	  for(var i=0;i<tama;i++){
		  
		  x.remove(0);
		  }
	  document.getElementById("mySelect").style.display = "none";
	  document.getElementById("mySelectActividad").style.display = "none";
	  
	  
	  
  }
	  
  
  

}


function borrar(table){
  var tam=table.rows.length;
  for (var i=1;i<tam;i++){
     document.getElementById("dataTable").deleteRow(1);
  }
}













function tabla2(){

	  var table=document.getElementById("dataTable2");
	  var text=document.getElementsByName("datosTabla2")[0].innerHTML;
	  
	  var array=text.split(";");
	  for (var i=0;i<array.length;i++){
	  var row=table.insertRow(i+1);
	  var cell1 = row.insertCell(0);
	  var cell2 = row.insertCell(1);
	  var cell3 = row.insertCell(2);
	  var cell4 = row.insertCell(3);
	  var cell5 = row.insertCell(4);
	  var cell6 = row.insertCell(5);
	  var cell7 = row.insertCell(6);
	  var cell8 = row.insertCell(7);
	  var cell9 = row.insertCell(8);
	  var array2=array[i].split(",");
	  cell1.innerHTML = array2[0];
	  cell2.innerHTML = array2[1];
	  cell3.innerHTML = array2[2];
	  cell4.innerHTML = array2[3];
	  cell5.innerHTML = array2[4];
	  var link = document.createElement('a');
		link.setAttribute("href", "ListaGrafo?id="+array2[5]);
		link.setAttribute("target", "_blank");
	  var t = document.createTextNode(array2[5]);
		link.appendChild(t);		
	  cell6.appendChild (link);
	  
	  var link1 = document.createElement('a');
		link1.setAttribute("href", "ListaGrafo?id="+array2[6]);
		link1.setAttribute("target", "_blank");
	  var t1 = document.createTextNode(array2[6]);
		link1.appendChild(t1);
	  cell7.appendChild (link1);
	  cell8.innerHTML = array2[7];
	  cell9.innerHTML = array2[8];
	  }

	}


function generarTabla2(){
	  var table=document.getElementById("dataTable2");
	  borrar2(table);

	  var text=document.getElementsByName("datosTabla2")[0].innerHTML;
	  obtenerDatos2(text,table);
	  
	}






function obtenerDatos2(text,table){

	  var x = document.getElementById("mySelect2");
	  tama=x.length;
	  for(var i=0;i<tama;i++){
		  x.remove(0);
		  }
	 
	  var y = document.getElementById("mySelectActividad2");
	  tamaA=y.length;
	  for(var i=0;i<tamaA;i++){
		  y.remove(0);
		  }
	
	
var array=text.split(";");
var idEstudiante=document.getElementById("selection2").value;
var entra=1;
var tabla="";
var unidades=new Array();
var actividades=new Array();
for (var i=0;i<array.length;i++){
 var array2=array[i].split(",");
 if(idEstudiante==array2[0]){

	   var row=table.insertRow(entra);
	   var cell1 = row.insertCell(0);
	   var cell2 = row.insertCell(1);
	   var cell3 = row.insertCell(2);
	   var cell4 = row.insertCell(3);
	   var cell5 = row.insertCell(4);
	   var cell6 = row.insertCell(5);
	   var cell7 = row.insertCell(6);
	   var cell8 = row.insertCell(7);
	   var cell9 = row.insertCell(8);
	   cell1.innerHTML = array2[0];
	   cell2.innerHTML = array2[1];
	   cell3.innerHTML = array2[2];
	   cell4.innerHTML = array2[3];
	   cell5.innerHTML = array2[4];
	   cell6.innerHTML = array2[5];
	   cell7.innerHTML = array2[6];
	   cell8.innerHTML = array2[7];
	   cell9.innerHTML = array2[8];
     entra=entra+1;
     unidades.push(array2[2]);
     actividades.push(array2[3]);
     tabla+=array2[0]+","+array2[1]+","+array2[2]+","+array2[3]+","+array2[4]+","+array2[5]+","+array2[6]+","+array2[7]+","+array2[8]+";";
     
 }else if(idEstudiante=="Todos"){
     var row=table.insertRow(i+1);

     var cell1 = row.insertCell(0);
     var cell2 = row.insertCell(1);
     var cell3 = row.insertCell(2);
     var cell4 = row.insertCell(3);
     var cell5 = row.insertCell(4);
     var cell6 = row.insertCell(5);
     var cell7 = row.insertCell(6);
     var cell8 = row.insertCell(7);
     var cell9 = row.insertCell(8);
     var array2=array[i].split(",");
     cell1.innerHTML = array2[0];
     cell2.innerHTML = array2[1];
     cell3.innerHTML = array2[2];
     cell4.innerHTML = array2[3];
     cell5.innerHTML = array2[4];
     cell6.innerHTML = array2[5];
     cell7.innerHTML = array2[6];
     cell8.innerHTML = array2[7];
     cell9.innerHTML = array2[8];

 }
}



if(idEstudiante!="Todos"){

	  
	  var optionDefault = document.createElement("option");
	  optionDefault.text = "Todos";
	  x.add(optionDefault);
	  
	  var unicas=unidades.unique();
	  for(var i=0;i<unicas.length;i++){
	  var option = document.createElement("option");
		  option.text = unicas[i];
		  x.add(option); 
	  
	  }
	  
	  var optionDefault2 = document.createElement("option");
	  optionDefault2.text = "Todos";
	  y.add(optionDefault2);
	  
	  var unicasA=actividades.unique();
	  for(var i=0;i<unicasA.length;i++){
	  var option2 = document.createElement("option");
		  option2.text = actividades[i];
		  y.add(option2); 
	  
	  }
	  
	  document.getElementById("mySelect2").style.display = "block";
	  document.getElementById("mySelectActividad2").style.display = "block";
    document.getElementById("seleccionado2").innerHTML=tabla.substring(0,(tabla.length-1));
	  
}else{
	  var x = document.getElementById("mySelect2");
	  var tama=x.length;
	  for(var i=0;i<tama;i++){
		  
		  x.remove(0);
		  }
	  document.getElementById("mySelect2").style.display = "none";
	  document.getElementById("mySelectActividad2").style.display = "none";
	  
	  
	  
}
	  



}


	function borrar2(table){
	  var tam=table.rows.length;
	  for (var i=1;i<tam;i++){
	     document.getElementById("dataTable2").deleteRow(1);
	  }
	}
	
	Array.prototype.unique=function(a){
		  return function(){return this.filter(a);}}(function(a,b,c){return c.indexOf(a,b+1)<0
		});

function realizarFiltroUnidad(){
	var table=document.getElementById("dataTable");
	  borrar(table);
	  var entra=1;
	  
	  var unidades=new Array();
	  var actividades=new Array();
	  
	 
	  
	  
	  var seleccion=document.getElementById("mySelect").value;
	  var seleccion2=document.getElementById("mySelectActividad").value;
	  var datos=document.getElementById("seleccionado").innerHTML;
      var array=datos.split(";");
      for(var i=0;i<array.length;i++){
      	var array2=array[i].split(",");

      	if(seleccion==array2[2] && seleccion2==array2[3]){
      		
      		   var row=table.insertRow(entra);
      		   var cell1 = row.insertCell(0);
      		   var cell2 = row.insertCell(1);
      		   var cell3 = row.insertCell(2);
      		   var cell4 = row.insertCell(3);
      		   var cell5 = row.insertCell(4);
      		   var cell6 = row.insertCell(5);
      		   var cell7 = row.insertCell(6);
      		   cell1.innerHTML = array2[0];
      		   cell2.innerHTML = array2[1];
      		   cell3.innerHTML = array2[2];
      		   cell4.innerHTML = array2[3];
      		   cell5.innerHTML = array2[4];
      		   cell6.innerHTML = array2[5];
      		   cell7.innerHTML = array2[6];
      	       unidades.push(array2[2]);
      	       actividades.push(array2[3]);
      	       entra=entra+1;
	
      	}else if(seleccion=="Todos" && seleccion2==array2[3]){
      		
   		   var row=table.insertRow(entra);
   		   var cell1 = row.insertCell(0);
   		   var cell2 = row.insertCell(1);
   		   var cell3 = row.insertCell(2);
   		   var cell4 = row.insertCell(3);
   		   var cell5 = row.insertCell(4);
   		   var cell6 = row.insertCell(5);
   		   var cell7 = row.insertCell(6);
   		   cell1.innerHTML = array2[0];
   		   cell2.innerHTML = array2[1];
   		   cell3.innerHTML = array2[2];
   		   cell4.innerHTML = array2[3];
   		   cell5.innerHTML = array2[4];
   		   cell6.innerHTML = array2[5];
   		   cell7.innerHTML = array2[6];
   	       unidades.push(array2[2]);
   	    actividades.push(array2[3]);
   	       entra=entra+1;
	
   	}else if(seleccion==array2[2] && seleccion2=="Todos"){
  		
		   var row=table.insertRow(entra);
		   var cell1 = row.insertCell(0);
		   var cell2 = row.insertCell(1);
		   var cell3 = row.insertCell(2);
		   var cell4 = row.insertCell(3);
		   var cell5 = row.insertCell(4);
		   var cell6 = row.insertCell(5);
		   var cell7 = row.insertCell(6);
		   cell1.innerHTML = array2[0];
		   cell2.innerHTML = array2[1];
		   cell3.innerHTML = array2[2];
		   cell4.innerHTML = array2[3];
		   cell5.innerHTML = array2[4];
		   cell6.innerHTML = array2[5];
		   cell7.innerHTML = array2[6];
		   unidades.push(array2[2]);
	   	    actividades.push(array2[3]);
	       
	       entra=entra+1;

	}
      	
      	else if(seleccion=="Todos" && seleccion2=="Todos"){
      		
      		var row=table.insertRow(i+1);

      	       var cell1 = row.insertCell(0);
      	       var cell2 = row.insertCell(1);
      	       var cell3 = row.insertCell(2);
      	       var cell4 = row.insertCell(3);
      	       var cell5 = row.insertCell(4);
      	       var cell6 = row.insertCell(5);
      	       var cell7 = row.insertCell(6);
      	       var array2=array[i].split(",");
      	       cell1.innerHTML = array2[0];
      	       cell2.innerHTML = array2[1];
      	       cell3.innerHTML = array2[2];
      	       cell4.innerHTML = array2[3];
      	       cell5.innerHTML = array2[4];
      	       cell6.innerHTML = array2[5];
      	       cell7.innerHTML = array2[6];
      	       unidades.push(array2[2]);
      	       actividades.push(array2[3]);
      	}
      	
      }
      
      
       if(seleccion!="Todos" && seleccion2=="Todos"){
		  var y = document.getElementById("mySelectActividad");
		  tamaA=y.length;
		  for(var i=0;i<tamaA;i++){
			  y.remove(0);
			  }
 		  
 		
 		  
		  var optionDefault2 = document.createElement("option");
    	  optionDefault2.text = "Todos";
    	  y.add(optionDefault2);
    	  
    	  var unicasA=actividades.unique();
    	  for(var i=0;i<unicasA.length;i++){
    	  var option2 = document.createElement("option");
    		  option2.text = actividades[i];
    		  y.add(option2); 
    	  
    	  }
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  

	}else if(seleccion=="Todos" && seleccion2!="Todos"){
		
		 var x = document.getElementById("mySelect");
		  tama=x.length;
		  for(var i=0;i<tama;i++){
			  x.remove(0);
			  }
		 

		
		
		var optionDefault = document.createElement("option");
		  optionDefault.text = "Todos";
		  x.add(optionDefault);
		  
		  
		  var unicas=unidades.unique();
    	  for(var i=0;i<unicas.length;i++){
    	  var option = document.createElement("option");
    		  option.text = unicas[i];
    		  x.add(option); 
    	  
    	  }
		  


    }
 	
      
      
      
      
      
      
      
 	else if(seleccion=="Todos" && seleccion2=="Todos"){
 		
 		
 		 var x = document.getElementById("mySelect");
		  tama=x.length;
		  for(var i=0;i<tama;i++){
			  x.remove(0);
			  }
		 
		  var y = document.getElementById("mySelectActividad");
		  tamaA=y.length;
		  for(var i=0;i<tamaA;i++){
			  y.remove(0);
			  }
 		
 		var optionDefault = document.createElement("option");
 		  optionDefault.text = "Todos";
 		  x.add(optionDefault);
 		  
 		  var unicas=unidades.unique();
 		  for(var i=0;i<unicas.length;i++){
 		  var option = document.createElement("option");
 			  option.text = unicas[i];
 			  x.add(option); 
 		  
 		  }
 		  
 		  var optionDefault2 = document.createElement("option");
 		  optionDefault2.text = "Todos";
 		  y.add(optionDefault2);
 		  
 		  var unicasA=actividades.unique();
 		  for(var i=0;i<unicasA.length;i++){
 		  var option2 = document.createElement("option");
 			  option2.text = actividades[i];
 			  y.add(option2); 
 		  
 		  }
 	 
 	}
	  
	 
}


function realizarFiltroUnidad2(){
	var table=document.getElementById("dataTable2");
	  borrar2(table);
	  var entra=1;
	  
	  var unidades=new Array();
	  var actividades=new Array();
	  
	 
	  
	  
	  var seleccion=document.getElementById("mySelect2").value;
	  var seleccion2=document.getElementById("mySelectActividad2").value;
	  var datos=document.getElementById("seleccionado2").innerHTML;
      var array=datos.split(";");
      for(var i=0;i<array.length;i++){
      	var array2=array[i].split(",");

      	if(seleccion==array2[2] && seleccion2==array2[3]){
      		
      		   var row=table.insertRow(entra);
      		   var cell1 = row.insertCell(0);
      		   var cell2 = row.insertCell(1);
      		   var cell3 = row.insertCell(2);
      		   var cell4 = row.insertCell(3);
      		   var cell5 = row.insertCell(4);
      		   var cell6 = row.insertCell(5);
      		   var cell7 = row.insertCell(6);
      		   var cell8=row.insertCell(7);
      		   var cell9=row.insertCell(8);
      		   cell1.innerHTML = array2[0];
      		   cell2.innerHTML = array2[1];
      		   cell3.innerHTML = array2[2];
      		   cell4.innerHTML = array2[3];
      		   cell5.innerHTML = array2[4];
      		   cell6.innerHTML = array2[5];
      		   cell7.innerHTML = array2[6];
      		 cell8.innerHTML = array2[7];
      		cell9.innerHTML = array2[8];
      		
      	       unidades.push(array2[2]);
      	       actividades.push(array2[3]);
      	       entra=entra+1;
	
      	}else if(seleccion=="Todos" && seleccion2==array2[3]){
      		
      		var row=table.insertRow(entra);
   		   var cell1 = row.insertCell(0);
   		   var cell2 = row.insertCell(1);
   		   var cell3 = row.insertCell(2);
   		   var cell4 = row.insertCell(3);
   		   var cell5 = row.insertCell(4);
   		   var cell6 = row.insertCell(5);
   		   var cell7 = row.insertCell(6);
   		   var cell8=row.insertCell(7);
   		   var cell9=row.insertCell(8);
   		   cell1.innerHTML = array2[0];
   		   cell2.innerHTML = array2[1];
   		   cell3.innerHTML = array2[2];
   		   cell4.innerHTML = array2[3];
   		   cell5.innerHTML = array2[4];
   		   cell6.innerHTML = array2[5];
   		   cell7.innerHTML = array2[6];
   		 cell8.innerHTML = array2[7];
   		cell9.innerHTML = array2[8];
   		unidades.push(array2[2]);
	       actividades.push(array2[3]);
   	       entra=entra+1;
	
   	}else if(seleccion==array2[2] && seleccion2=="Todos"){
  		
   		var row=table.insertRow(entra);
		   var cell1 = row.insertCell(0);
		   var cell2 = row.insertCell(1);
		   var cell3 = row.insertCell(2);
		   var cell4 = row.insertCell(3);
		   var cell5 = row.insertCell(4);
		   var cell6 = row.insertCell(5);
		   var cell7 = row.insertCell(6);
		   var cell8=row.insertCell(7);
		   var cell9=row.insertCell(8);
		   cell1.innerHTML = array2[0];
		   cell2.innerHTML = array2[1];
		   cell3.innerHTML = array2[2];
		   cell4.innerHTML = array2[3];
		   cell5.innerHTML = array2[4];
		   cell6.innerHTML = array2[5];
		   cell7.innerHTML = array2[6];
		 cell8.innerHTML = array2[7];
		cell9.innerHTML = array2[8];
		unidades.push(array2[2]);
	       actividades.push(array2[3]);
	       entra=entra+1;

	}
      	
      	else if(seleccion=="Todos" && seleccion2=="Todos"){
      		
      		var row=table.insertRow(i+1);

      		var row=table.insertRow(entra);
   		   var cell1 = row.insertCell(0);
   		   var cell2 = row.insertCell(1);
   		   var cell3 = row.insertCell(2);
   		   var cell4 = row.insertCell(3);
   		   var cell5 = row.insertCell(4);
   		   var cell6 = row.insertCell(5);
   		   var cell7 = row.insertCell(6);
   		   var cell8=row.insertCell(7);
   		   var cell9=row.insertCell(8);
   		   cell1.innerHTML = array2[0];
   		   cell2.innerHTML = array2[1];
   		   cell3.innerHTML = array2[2];
   		   cell4.innerHTML = array2[3];
   		   cell5.innerHTML = array2[4];
   		   cell6.innerHTML = array2[5];
   		   cell7.innerHTML = array2[6];
   		 cell8.innerHTML = array2[7];
   		cell9.innerHTML = array2[8];
      	       unidades.push(array2[2]);
      	       actividades.push(array2[3]);
      	}
      	
      }
      
      
       if(seleccion!="Todos" && seleccion2=="Todos"){
		  var y = document.getElementById("mySelectActividad2");
		  tamaA=y.length;
		  for(var i=0;i<tamaA;i++){
			  y.remove(0);
			  }
 		  
 		
 		  
		  var optionDefault2 = document.createElement("option");
    	  optionDefault2.text = "Todos";
    	  y.add(optionDefault2);
    	  
    	  var unicasA=actividades.unique();
    	  for(var i=0;i<unicasA.length;i++){
    	  var option2 = document.createElement("option");
    		  option2.text = actividades[i];
    		  y.add(option2); 
    	  
    	  }
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  

	}else if(seleccion=="Todos" && seleccion2!="Todos"){
		
		 var x = document.getElementById("mySelect2");
		  tama=x.length;
		  for(var i=0;i<tama;i++){
			  x.remove(0);
			  }
		 

		
		
		var optionDefault = document.createElement("option");
		  optionDefault.text = "Todos";
		  x.add(optionDefault);
		  
		  
		  var unicas=unidades.unique();
    	  for(var i=0;i<unicas.length;i++){
    	  var option = document.createElement("option");
    		  option.text = unicas[i];
    		  x.add(option); 
    	  
    	  }
		  


    }
      
 	else if(seleccion=="Todos" && seleccion2=="Todos"){
 		
 		
 		 var x = document.getElementById("mySelect2");
		  tama=x.length;
		  for(var i=0;i<tama;i++){
			  x.remove(0);
			  }
		 
		  var y = document.getElementById("mySelectActividad2");
		  tamaA=y.length;
		  for(var i=0;i<tamaA;i++){
			  y.remove(0);
			  }
 		
 		var optionDefault = document.createElement("option");
 		  optionDefault.text = "Todos";
 		  x.add(optionDefault);
 		  
 		  var unicas=unidades.unique();
 		  for(var i=0;i<unicas.length;i++){
 		  var option = document.createElement("option");
 			  option.text = unicas[i];
 			  x.add(option); 
 		  
 		  }
 		  
 		  var optionDefault2 = document.createElement("option");
 		  optionDefault2.text = "Todos";
 		  y.add(optionDefault2);
 		  
 		  var unicasA=actividades.unique();
 		  for(var i=0;i<unicasA.length;i++){
 		  var option2 = document.createElement("option");
 			  option2.text = actividades[i];
 			  y.add(option2); 
 		  
 		  }
 	 
 	}
	  
	 
}

function listaActivos(){
	var activos=document.getElementsByName("usuariosActivos")[0].innerHTML;
	var usuarios=activos.split(",");

	
	for(var i=0;i<usuarios.length;i++){
	    var x = document.createElement("LI");
	    var t = document.createTextNode(usuarios[i]);
	    x.appendChild(t);
	    document.getElementById("lista").appendChild(x);
		
	}
}

function tablaReplanificacion(){
	var table=document.getElementById("TablaReplanificacion");
	var caracteristicas=document.getElementsByName("datosTabla")[0].innerHTML;
	var grafos=document.getElementsByName("datosTabla2")[0].innerHTML;

	var entra=1;
	var separarCaracteristicas=caracteristicas.split(";");
	var separarGrafos=grafos.split(";");

	for(var i=0;i<separarGrafos.length;i++){

		var datosGrafos=separarGrafos[i].split(",");

		for(var j=0;j<separarCaracteristicas.length;j++){
			
			var datosCaracteristicas=separarCaracteristicas[j].split(",");
			
			if(datosGrafos[0]==datosCaracteristicas[0] && datosGrafos[2]==datosCaracteristicas[2] && datosGrafos[3]==datosCaracteristicas[3] && datosGrafos[4]=="replanificación" && (datosCaracteristicas[4]=="no obtenida" || datosCaracteristicas[4]=="eliminada")){
				var row=table.insertRow(entra);
		 		   var cell1 = row.insertCell(0);
		 		   var cell2 = row.insertCell(1);
		 		   var cell3 = row.insertCell(2);
		 		   var cell4 = row.insertCell(3);
		 		   var cell5 = row.insertCell(4);
		 		   var cell6 = row.insertCell(5);
		 		   var cell7 = row.insertCell(6);
		 		   var cell8 = row.insertCell(7);
		 		   cell1.innerHTML = datosGrafos[0];
		 		   cell2.innerHTML = datosGrafos[1];
		 		   cell3.innerHTML = datosGrafos[2];
		 		   cell4.innerHTML = datosGrafos[3];
		 		   cell5.innerHTML = datosGrafos[5];
		 		   cell6.innerHTML = datosGrafos[6];
		 		   cell7.innerHTML = datosCaracteristicas[4];
		 		   cell8.innerHTML = datosCaracteristicas[5];
		 	       entra=entra+1;
			}
		}
		
	}
	
	
}

function realizarReporteCaracteristicas(){
var text = document.getElementsByName("reportes")[0].innerHTML;
var fdata = $.parseJSON(text);

var text2 = document.getElementsByName("reportes2")[0].innerHTML;
var Data = $.parseJSON(text2);
dashboard('#dashboard', fdata, Data);
function dashboard(id, fData,Data){

    var tF = [ 'nuevas', 'actual', 'noObtenidas','eliminadas', 'yaObtenidas' ].map(function(d) {return {type : d,freq : d3.sum(fData.map(function(t) {return t.freq[d];}))};});   
    var sF = fData.map(function(d){return [d.Estudiante,d.total];});
    var hG=barras(Data);
	var pC = pieChart(tF,Data); // create the pie-chart.
    var leg= legend(tF);  // create the legend.

function segColor(c) {
return {actual : "#FF9900",yaObtenidas : "#0000FF",nuevas : "#20B2AA",eliminadas : "#CC0000",noObtenidas : "#996633"}[c];
}
					
fData.forEach(function(d) {
d.total=d.freq.actual+d.freq.yaObtenidas+d.freq.nuevas+d.freq.eliminadas+d.freq.noObtenidas;
});

function barras(data){

var alturas=[];
var resu=[];
var i=0;
var hG={},margin={top:20,right:0,bottom:250,left:100},width=600-margin.left-margin.right,height=500-margin.top-margin.bottom;
var x = d3.scale.ordinal().rangeRoundBands([0,width],0.1);
var y = d3.scale.linear().rangeRound([height,0]);
var color = d3.scale.ordinal().range(["#20B2AA","#FF9900","#996633","#CC0000","#0000FF"]);
var xAxis = d3.svg.axis().scale(x).orient("bottom");
var yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format(".2s"));
var svg = d3.select(id).append("svg").attr("width",width+margin.left+margin.right).attr("height",height+margin.top+margin.bottom).append("g").attr("transform","translate("+margin.left+","+margin.top+")");
color.domain(d3.keys(data[0]).filter(function(key){return key!=="Estudiante";}));


data.forEach(function(d) {
var y0 = 0;
d.ages = color.domain().map(function(name){return{name:name,y0:y0,y1:y0+=+d[name],freq:d[name]};});
d.prueba=color.domain().map(function(name){return{type:name,freq:d[name]};});
d.total = d.ages[d.ages.length-1].y1;
});

x.domain(data.map(function(d){return d.Estudiante;}));
y.domain([0,d3.max(data,function(d){ return d.total;})]);
svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")").call(xAxis).selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em").attr("transform", function(d) {return "rotate(-90)"});
var state = svg.selectAll(".estudiante").data(data).enter().append("g").attr("class", "g").attr("transform", function(d) { return "translate(" + x(d.Estudiante) + ",0)"; }).on("mouseover",mouseover).on("mouseout",mouseout);
state.selectAll("rect").data(function(d) { return d.ages; }).enter().append("rect").attr("width", x.rangeBand()).attr("y", function(d) { alturas.push(y(d.y1));return y(d.y1); }).attr("height", function(d) {resu.push(d.y1-d.y0);return y(d.y0)-y(d.y1); }).style("fill", function(d) { return color(d.name); });
state.append("text").text(function(d) { return d3.format(",") (d.total); }).attr("x", function(d) { return x.rangeBand()/2; }).attr("y", function(d) { i=i+1; return alturas[(i*5)-1]-5; }).attr("text-anchor", "middle");

function mouseover(d){
    var nD =d.prueba;     
    pC.update(nD);
    leg.update(nD);
}
	
function mouseout(d){   
    pC.update(tF);
    leg.update(tF);
}
	
	hG.update = function(nD,color){
		var mayor=d3.max(nD, function(d) { return d[1]; });
		var medida=height/mayor;
        var cont=-1;
		var cont2=-1;
		var cont3=-1;
        var a;
		var tipo;
		var tt=[];
		var dd=[];
		var titulos=[];
		var pruebaTotales=[];
        var cont4=0;
		
		if(color=="#20B2AA"){
		a=0;
		}else if(color=="#FF9900"){
		a=1;
		}else if(color=="#996633"){
		a=2;
		}else if(color=="#CC0000"){
		a=3;
		}else if(color=="#0000FF"){
		a=4;
		}
		
		var c;
		
for(c = 0; c<resu.length ; c++){
		  if(a==c){
		    tt.push((medida*resu[c]));
            tt.push((medida*resu[c]));
	        tt.push((medida*resu[c]));
            tt.push((medida*resu[c]));
            tt.push((medida*resu[c]));
			titulos.push(resu[c]);
			a=a+5;
		  }
		}
		
		state.selectAll("rect").data(function(d) { return d.ages; }).transition().duration(500).attr("y", function(d) {cont++;pruebaTotales.push(height-tt[cont]);return height-tt[cont]; }).attr("height", function(d) {cont2++;return tt[cont2]; }).style("fill",  color);
		state.select("text").transition().duration(500).text(function(d){ cont3++;return d3.format(",")(titulos[cont3])}).attr("y", function(d) {cont4++;return pruebaTotales[(cont4*5)-1]-5; });       
} 

hG.update2 = function(nD){
var it=0;
y.domain([0, d3.max(nD, function(d) { return d[1]; })]);	      
state.selectAll("rect").data(function(d) { return d.ages; }).transition().duration(500).attr("y", function(d) { return y(d.y1); }).attr("height", function(d) {return y(d.y0) - y(d.y1); }).style("fill",  function(d) { return color(d.name); });
state.select("text").transition().duration(500).text(function(d) { return d3.format(",") (d.total); }).attr("y", function(d) { it=it+1; return alturas[(it*5)-1]-5; });
}		
	
	return hG;
}

function pieChart(pD,Data) {
					var pC = {}, pieDim = {
						w : 200,
						h : 300
					};
					pieDim.r = Math.min(pieDim.w, (pieDim.h + 100)) / 2;

					var piesvg = d3.select(id).append("svg").attr("width",
							pieDim.w).attr("height", (pieDim.h + 200))
							.append("g").attr(
									"transform",
									"translate(" + pieDim.w / 2 + ","
											+ pieDim.h / 2 + ")");

					var arc = d3.svg.arc().outerRadius(pieDim.r - 10)
							.innerRadius(0);

					var pie = d3.layout.pie().sort(null).value(function(d) {
						return d.freq;
					});

					piesvg.selectAll("path").data(pie(pD)).enter().append(
							"path").attr("d", arc).each(function(d) {
						this._current = d;
					}).style("fill", function(d) {
						return segColor(d.data.type);
					}).on("mouseover", mouseover).on("mouseout", mouseout);

					pC.update = function(nD) {
						piesvg.selectAll("path").data(pie(nD)).transition()
								.duration(500).attrTween("d", arcTween);
					}

					function mouseover(d) {

						hG.update(fData.map(function(v) {
							return [ v.Estudiante, v.freq[d.data.type] ];
						}), segColor(d.data.type));
					}

					function mouseout(d) {

						hG.update2(fData.map(function(v) {
							return [ v.Estudiante, v.total ];
						}));
					}

					function arcTween(a) {
						var i = d3.interpolate(this._current, a);
						this._current = i(0);
						return function(t) {
							return arc(i(t));
						};
					}
					return pC;
				}

				function legend(lD) {
					var leg = {};

					var legend = d3.select(id).append("table").attr(
							'class', 'legend');

					var tr = legend.append("tbody").selectAll("tr")
							.data(lD).enter().append("tr");

					tr.append("td").append("svg").attr("width", '16').attr(
							"height", '16').append("rect").attr("width",
							'16').attr("height", '16').attr("fill",
							function(d) {
								return segColor(d.type);
							});

					tr.append("td").text(function(d) {
						return d.type;
					});

					tr.append("td").attr("class", 'legendFreq').text(
							function(d) {
								return d3.format(",")(d.freq);
							});

					tr.append("td").attr("class", 'legendPerc').text(
							function(d) {
								return getLegend(d, lD);
							});

					leg.update = function(nD) {

						var l = legend.select("tbody").selectAll("tr")
								.data(nD);

						l.select(".legendFreq").text(function(d) {
							return d3.format(",")(d.freq);
						});

						l.select(".legendPerc").text(function(d) {
							return getLegend(d, nD);
						});
					}

					function getLegend(d, aD) {
						return d3.format("%")(
								d.freq / d3.sum(aD.map(function(v) {
									return v.freq;
								})));
					}

					return leg;
				}
	
}
}





























function realizarReporteGrafos(){
	var text = document.getElementsByName("datosGrafos")[0].innerHTML;
	var fdata = $.parseJSON(text);

	var text2 = document.getElementsByName("datosGrafos2")[0].innerHTML;
	var Data = $.parseJSON(text2);
	dashboard('#dashboard2', fdata, Data);
	function dashboard(id, fData,Data){

		var tF = [ 'replanificacion', 'contexto', 'origen',
					'mismoGrafo' ].map(function(d) {
				return {
					type : d,
					freq : d3.sum(fData.map(function(t) {
						return t.freq[d];
					}))
				};
			});
	    var sF = fData.map(function(d){return [d.Estudiante,d.total];});
	    var hG=barras(Data);
		var pC = pieChart(tF,Data); // create the pie-chart.
	    var leg= legend(tF);  // create the legend.

	    function segColor(c) {
			return {
				mismoGrafo : "#FF9900",
				replanificacion : "#0000FF",
				origen : "#20B2AA",
				contexto : "#CC0000",

			}[c];
		}
						
	    fData.forEach(function(d) {
			d.total = d.freq.mismoGrafo + d.freq.replanificacion
					+ d.freq.origen + d.freq.contexto;
		});

	function barras(data){

	var alturas=[];
	var resu=[];
	var i=0;
	var hG={},margin={top:20,right:0,bottom:250,left:100},width=600-margin.left-margin.right,height=500-margin.top-margin.bottom;
	var x = d3.scale.ordinal().rangeRoundBands([0,width],0.1);
	var y = d3.scale.linear().rangeRound([height,0]);


	
	
	var color = d3.scale.ordinal().range(["#0000FF","#CC0000","#20B2AA","#FF9900"]);
	var xAxis = d3.svg.axis().scale(x).orient("bottom");
	var yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format(".2s"));
	var svg = d3.select(id).append("svg").attr("width",width+margin.left+margin.right).attr("height",height+margin.top+margin.bottom).append("g").attr("transform","translate("+margin.left+","+margin.top+")");
	color.domain(d3.keys(data[0]).filter(function(key){return key!=="Estudiante";}));


	data.forEach(function(d) {
	var y0 = 0;
	d.ages = color.domain().map(function(name){return{name:name,y0:y0,y1:y0+=+d[name],freq:d[name]};});
	d.prueba=color.domain().map(function(name){return{type:name,freq:d[name]};});
	d.total = d.ages[d.ages.length-1].y1;
	});

	x.domain(data.map(function(d){return d.Estudiante;}));
	y.domain([0,d3.max(data,function(d){ return d.total;})]);
	svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")").call(xAxis).selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em").attr("transform", function(d) {return "rotate(-90)"});
	var state = svg.selectAll(".estudiante").data(data).enter().append("g").attr("class", "g").attr("transform", function(d) { return "translate(" + x(d.Estudiante) + ",0)"; }).on("mouseover",mouseover).on("mouseout",mouseout);
	state.selectAll("rect").data(function(d) { return d.ages; }).enter().append("rect").attr("width", x.rangeBand()).attr("y", function(d) { alturas.push(y(d.y1));return y(d.y1); }).attr("height", function(d) {resu.push(d.y1-d.y0);return y(d.y0)-y(d.y1); }).style("fill", function(d) { return color(d.name); });
	state.append("text").text(function(d) { return d3.format(",") (d.total); }).attr("x", function(d) { return x.rangeBand()/2; }).attr("y", function(d) { i=i+1; return alturas[(i*4)-1]-5; }).attr("text-anchor", "middle");

	function mouseover(d){
	    var nD =d.prueba;     
	    pC.update(nD);
	    leg.update(nD);
	}
		
	function mouseout(d){   
	    pC.update(tF);
	    leg.update(tF);
	}
		
		hG.update = function(nD,color){
			var mayor=d3.max(nD, function(d) { return d[1]; });
			var medida=height/mayor;
	        var cont=-1;
			var cont2=-1;
			var cont3=-1;
	        var a;
			var tipo;
			var tt=[];
			var dd=[];
			var titulos=[];
			var pruebaTotales=[];
	        var cont4=0;
			

	        
			if(color=="#0000FF"){
			a=0;
			}else if(color=="#CC0000"){
			a=1;
			}else if(color=="#20B2AA"){
			a=2;
			}else if(color=="#FF9900"){
			a=3;
			}
			
			var c;
			
	for(c = 0; c<resu.length ; c++){
			  if(a==c){
			    tt.push((medida*resu[c]));
	            tt.push((medida*resu[c]));
		        tt.push((medida*resu[c]));
	            tt.push((medida*resu[c]));
				titulos.push(resu[c]);
				a=a+4;
			  }
			}
			
			state.selectAll("rect").data(function(d) { return d.ages; }).transition().duration(500).attr("y", function(d) {cont++;pruebaTotales.push(height-tt[cont]);return height-tt[cont]; }).attr("height", function(d) {cont2++;return tt[cont2]; }).style("fill",  color);
			state.select("text").transition().duration(500).text(function(d){ cont3++;return d3.format(",")(titulos[cont3])}).attr("y", function(d) {cont4++;return pruebaTotales[(cont4*4)-1]-5; });       
	} 

	hG.update2 = function(nD){
	var it=0;
	y.domain([0, d3.max(nD, function(d) { return d[1]; })]);	      
	state.selectAll("rect").data(function(d) { return d.ages; }).transition().duration(500).attr("y", function(d) { return y(d.y1); }).attr("height", function(d) {return y(d.y0) - y(d.y1); }).style("fill",  function(d) { return color(d.name); });
	state.select("text").transition().duration(500).text(function(d) { return d3.format(",") (d.total); }).attr("y", function(d) { it=it+1; return alturas[(it*4)-1]-5; });
	}		
		
		return hG;
	}

	function pieChart(pD,Data) {
						var pC = {}, pieDim = {
							w : 200,
							h : 300
						};
						pieDim.r = Math.min(pieDim.w, (pieDim.h + 100)) / 2;

						var piesvg = d3.select(id).append("svg").attr("width",
								pieDim.w).attr("height", (pieDim.h + 200))
								.append("g").attr(
										"transform",
										"translate(" + pieDim.w / 2 + ","
												+ pieDim.h / 2 + ")");

						var arc = d3.svg.arc().outerRadius(pieDim.r - 10)
								.innerRadius(0);

						var pie = d3.layout.pie().sort(null).value(function(d) {
							return d.freq;
						});

						piesvg.selectAll("path").data(pie(pD)).enter().append(
								"path").attr("d", arc).each(function(d) {
							this._current = d;
						}).style("fill", function(d) {
							return segColor(d.data.type);
						}).on("mouseover", mouseover).on("mouseout", mouseout);

						pC.update = function(nD) {
							piesvg.selectAll("path").data(pie(nD)).transition()
									.duration(500).attrTween("d", arcTween);
						}

						function mouseover(d) {

							hG.update(fData.map(function(v) {
								return [ v.Estudiante, v.freq[d.data.type] ];
							}), segColor(d.data.type));
						}

						function mouseout(d) {

							hG.update2(fData.map(function(v) {
								return [ v.Estudiante, v.total ];
							}));
						}

						function arcTween(a) {
							var i = d3.interpolate(this._current, a);
							this._current = i(0);
							return function(t) {
								return arc(i(t));
							};
						}
						return pC;
					}

					function legend(lD) {
						var leg = {};

						var legend = d3.select(id).append("table").attr(
								'class', 'legend');

						var tr = legend.append("tbody").selectAll("tr")
								.data(lD).enter().append("tr");

						tr.append("td").append("svg").attr("width", '16').attr(
								"height", '16').append("rect").attr("width",
								'16').attr("height", '16').attr("fill",
								function(d) {
									return segColor(d.type);
								});

						tr.append("td").text(function(d) {
							return d.type;
						});

						tr.append("td").attr("class", 'legendFreq').text(
								function(d) {
									return d3.format(",")(d.freq);
								});

						tr.append("td").attr("class", 'legendPerc').text(
								function(d) {
									return getLegend(d, lD);
								});

						leg.update = function(nD) {

							var l = legend.select("tbody").selectAll("tr")
									.data(nD);

							l.select(".legendFreq").text(function(d) {
								return d3.format(",")(d.freq);
							});

							l.select(".legendPerc").text(function(d) {
								return getLegend(d, nD);
							});
						}

						function getLegend(d, aD) {
							return d3.format("%")(
									d.freq / d3.sum(aD.map(function(v) {
										return v.freq;
									})));
						}

						return leg;
					}
		
	}
	}