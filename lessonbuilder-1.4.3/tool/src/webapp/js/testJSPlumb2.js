//ultimo commit nov 20 16:15
var uniqueID = 1;
var canvas = document.getElementById('canvas');
var userType="";

$(document).ready(function(){
	if(detectIE())
		jsPlumb.setRenderMode(jsPlumb.VML);
	else
		jsPlumb.setRenderMode(jsPlumb.SVG);
	onPageInitLoad();
});

jsPlumb.draggable($('.window'));

function duplicate(original) {
	var clone = original.cloneNode(true); // "deep" clone
	clone.id = "Duplicater_"+uniqueID++;
	return clone;
}

function createNode(template) {
	var node = duplicate(document.getElementById(template));
	canvas.appendChild(node);
	node.ashiy_active = false;
	node.active_icon = duplicate(document.getElementById('smiley'));
	node.active_icon.style.display = 'none'; 
	node.appendChild(node.active_icon);
	$(node).css({display: 'inline-block', left: 200, top: 50});
	node.input_endpoint = jsPlumb.addEndpoint($(node), inputEndpointSpec);
	node.output_endpoint = jsPlumb.addEndpoint($(node), outputEndpointSpec);
	//jsPlumb.draggable($(node));
	jsPlumb.animate($(node), {"left": 400, "top": 250}, {duration: "fast"});
}

function toggleActive(node) {
	if (node.ashiy_active) {
		node.ashiy_active = false;
		node.active_icon.style.display = 'none';
	} else {
		node.ashiy_active = true;
		node.active_icon.style.display = 'inline-block';
	}
}

var a = $("#a");
var b = $("#b");

//Setting up drop options
var targetDropOptions = {
		activeClass: 'dragActive'
};
var common = {
		cssClass: "myCssClass"
};
//Setting up a Target endPoint
var sourceColor = "black";
var studentColor = "green";
var outputEndpointSpec = {
		anchor: [[1, 0.5, 1, 0]], //, [0.5, 1, 0, 1], [0.5, 0, 0, -1] ],
		endpoint: ["Dot", {radius: 20}],
		paintStyle: {fillStyle: sourceColor},
		isSource: true, //Starting point of the connector
		isTarget: false, //Means same color is allowed to accept the connection
		maxConnections:100000,
		connectorOverlays: [["Arrow", {location: 0.8}]]
};
var studentOutputEndpointSpec = {
		anchor: [[1, 0.5, 1, 0]], //, [0.5, 1, 0, 1], [0.5, 0, 0, -1] ],
		endpoint: ["Dot", {radius: 20}],
		paintStyle: {fillStyle: studentColor},
		isSource: true, //Starting point of the connector
		isTarget: false, //Means same color is allowed to accept the connection
		maxConnections:1,
		connectorOverlays: [["Arrow", {location: 0.8}]]
};
var miniOutputEndpointSpec = {
		anchor: [[1, 0.5, 1, 0]], //, [0.5, 1, 0, 1], [0.5, 0, 0, -1] ],
		endpoint: ["Dot", {radius: 5}],
		paintStyle: {fillStyle: sourceColor},
		isSource: true, //Starting point of the connector
//		scope: "green dot",
		connectorStyle: {strokeStyle: sourceColor, lineWidth: 4}, // Means Bridge width and bridge color
		connector: ["Flowchart"], //Other properties Bezier
		maxConnections: 100, //No upper limit
		isTarget: false, //Means same color is allowed to accept the connection
		connectorOverlays: [["Arrow", {location: 0.8}]]
};

//Setting up a Source endPoint
var targetColor = "black";
var inputEndpointSpec = {
		anchor: [[0, 0.5, -1, 0]], //[0.5, 0, 0, -1], [0.5, 1, 0, 1] ],
		endpoint: ["Rectangle", {width: 40, height: 40}],
		paintStyle: {fillStyle: sourceColor},
		isSource: false,
		isTarget: true,
		maxConnections:100000,
		dropOptions: targetDropOptions
};
var studentInputEndpointSpec = {
		anchor: [[0, 0.5, -1, 0]], //[0.5, 0, 0, -1], [0.5, 1, 0, 1] ],
		endpoint: ["Rectangle", {width: 40, height: 40}],
		paintStyle: {fillStyle: studentColor},
		maxConnections:1,
		isSource: false,
		isTarget: true,
		dropOptions: targetDropOptions
};

var miniInputEndpointSpec = {
		anchor: [[0, 0.5, -1, 0]], //[0.5, 0, 0, -1], [0.5, 1, 0, 1] ],
		endpoint: ["Rectangle", {width: 10, height: 10}],
		paintStyle: {fillStyle: targetColor},
		isSource: false,
//		scope: "green dot",
		maxConnections: 100,
		isTarget: true,
		dropOptions: targetDropOptions
};


jsPlumb.bind("ready", function() {

});


//jsPlumb.bind("dblclick", function(conn) {
//	jsPlumb.detach(conn);
//});

function loadActivity(original, id) {
	var clone = original.cloneNode(true); // "deep" clone
	clone.id = id;
	return clone;
}

function createNode2(id, name, type, mytop, myleft, idActividad, template, user, estadoActividad) {


	if(!elementExists(id))
	{	
		var node = loadActivity(document.getElementById(template), id);
		canvas.appendChild(node);
		node.ashiy_active = false;
		node.idActividad=idActividad;
		node.active_icon = duplicate(document.getElementById('smiley'));
		node.active_icon.style.display = 'none'; 
		node.appendChild(node.active_icon);
		$(node).css({display: 'inline-block', left: myleft, top: mytop});
		
		if(template.indexOf("Inactiva")!=-1&&user=="s")
		{
			$(node).find("img").attr("src","/library/image/silk/cross.png");
			$(node).css({"border-color": "#5D6466", "border-width":"3px"});
		}
		else if (user=="p")
		{
			try
			{
				node.input_endpoint = jsPlumb.addEndpoint($(node), inputEndpointSpec);
				node.output_endpoint = jsPlumb.addEndpoint($(node), outputEndpointSpec);
			}
			catch (err)
			{
				window.location.reload();
			}
		}
		else if (user=="s")
		{
			try
			{
				node.input_endpoint = jsPlumb.addEndpoint($(node), studentInputEndpointSpec);
				node.output_endpoint = jsPlumb.addEndpoint($(node), studentOutputEndpointSpec);
			}
			catch (err)
			{
				window.location.reload();
			}
			switch(parseInt(estadoActividad))
			{
				case 0:
					$(node).find("img").attr("src","/library/image/silk/emoticon_smile.png");
					$(node).css({"border-color": "#13D12C", "border-width":"3px"});
					break;
				case 1:
					$(node).find("img").attr("src","/library/image/silk/emoticon_unhappy.png");
					$(node).css({"border-color": "#D11320", "border-width":"3px"});
					break;
				case 2:
					$(node).find("img").attr("src","/library/image/silk/error_delete.png");
					$(node).css({"border-color": "#D17813", "border-width":"3px"});
					break;
				case 3:
					$(node).find("img").attr("src","/library/image/silk/hourglass.png");
					$(node).css({"border-color": "#1385D1", "border-width":"3px"});
					break;
				case 4:
					$(node).find("img").attr("src","/library/image/silk/pencil.png");
					$(node).css({"border-color": "#D1CE13", "border-width":"3px"});
					break;
				case 5:
					$(node).find("img").attr("src","/library/image/silk/cancel.png");
					$(node).css({"border-color": "#5D6466", "border-width":"3px"});
					break;
				case 6:
					$(node).css({"border-color": "#1385D1", "border-width":"3px"});
					break;
			}
		}
		$(node).children("p").html(name);
		return node;
	}
	return document.getElementById(id);
}

function createObjTitle(id, name, myleft, mytop, tipo)
{
	if(!elementExists(id))
	{
		var template;
		if(tipo=="obj")
			template="template-objNode";
		else
			template="template-refNode";
		var node = loadActivity(document.getElementById(template), id);
		canvas.appendChild(node);
		$(node).text(name);
		$(node).css({display: 'inline-block', left: myleft, top: mytop});
	}
}

var jsonObj = {
		connections : []
};

function onReset() {
	jsPlumb.deleteEveryEndpoint();
	var x=document.getElementById("canvas");
	$( x ).html("");
}

function elementExists(id) {
	return (document.getElementById(id) != null)
}

function onClearConnections() {
	jsPlumb.detachEveryConnection();
}

function loadExistingActivities()
{
	var x=document.getElementsByName("existingActivities")[0];
	
	var jsonText=$( x ).html();
	//console.log(jsonText);
	var data = $.parseJSON(jsonText);
	if(data.user=="p")
	{
		userType="p";
		var left=150;
		var top;
		for ( var i = 0; i < data.activitiesByObj.length; i++) {
			var abo = data.activitiesByObj[i];
			top=100;
			var objetivo=createObjTitle("Obj_"+abo.id, abo.name, left,50, "obj");
			top+=100;
			for (var j = 0; j < abo.activities.length; j++) {
				
				var a = abo.activities[j];
				if(a.type=="actividadInicio")
				{
					var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInicio", "p", "");
					deactivate(node);
				}
				if(a.status=="1")
					var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadNormal", "p", "");
				else
					var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInactiva", "p", "");
				top+=150;
					
			}
			left+=230;
			top=100;
			if(i<(data.activitiesByObj.length-1))
			{
				var objetivo=createObjTitle("Ref_"+abo.id, "Actividades de refuerzo", left,50, "ref");
				top+=100;
				for (var j = 0; j < abo.activitiesRefuerzo.length; j++) {
					
					var a = abo.activitiesRefuerzo[j];
					if(a.status=="1")
						var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadRefuerzo", "p", "");
					else
						var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInactiva", "p", "");
					top+=150;
				}
				left+=230;
			}
		}
	}
	else if(data.user=="s")
	{
		userType="s";
		var left=150;
		var top;
		for ( var i = 0; i < data.activitiesByObj.length; i++) {
			var abo = data.activitiesByObj[i];
			top=100;
			var objetivo=createObjTitle("Obj_"+abo.id, abo.name, left,50, "obj");
			top+=100;
			for (var j = 0; j < abo.activities.length; j++) {
				
				var a = abo.activities[j];
				if(a.student=="1")
				{
					var node;
					if(a.type=="actividadInicio")
					{
							node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInicio", "s", 6);
							deactivate(node);
					}
					else
						node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadNormal", "s", a.activityStatus);
					$(node).attr("IdItemPAshyi", a.IdItemPAshyi);
					$(node).attr("IdItemUAshyi", a.IdItemUAshyi);
					$(node).attr("itemId", a.ItemId);
				}
				else
				{
					var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInactiva", "s", a.activityStatus);
					deactivate(node);
				}
				top+=150;
					
			}
			left+=230;
			top=100;
			if(i<(data.activitiesByObj.length-1))
			{
				var objetivo=createObjTitle("Ref_"+abo.id, "Actividades de refuerzo", left,50, "ref");
				top+=100;
				for (var j = 0; j < abo.activitiesRefuerzo.length; j++) {
					
					var a = abo.activitiesRefuerzo[j];
					if(a.student=="1")
					{
						var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadRefuerzo", "s", a.activityStatus);
						$(node).attr("IdItemPAshyi", a.IdItemPAshyi);
						$(node).attr("IdItemUAshyi", a.IdItemUAshyi);
						$(node).attr("ItemId", a.ItemId);
					}
					else
					{
						var node = createNode2(a.id, a.name, a.type, top, left, a.idActividad, "template-actividadInactiva", "s", a.activityStatus);
						deactivate(node);
					}
					top+=150;
				}
				left+=230;
			}
		}
		var x=document.getElementById("Reload");
		$( x ).attr("style", "visibility: hidden");
		x=document.getElementById("Clear");
		$( x ).attr("style", "visibility: hidden");
		x=document.getElementById("SaveGraph");
		$( x ).attr("style", "visibility: hidden");
	}
}

function deactivate(node)
{
	$(node).children("img").attr("onclick","");
}

function loadExistingFlowChart()
{
	var x=document.getElementsByName("existingFlowChartLabel")[0];
	var jsonText=$( x ).html();
	console.log(jsonText);
	var data = $.parseJSON(jsonText);
	if(data.user=="p")
	{
		for ( var i = 0; i < data.connections.length; i++) {
			var c = data.connections[i];
			var nodeS = document.getElementById(c.source);
			var nodeT = document.getElementById(c.target);
			$(nodeS).attr('order', c.order);
			jsPlumb.connect({
				source : jsPlumb.getEndpoints(c.source)[1],
				target : jsPlumb.getEndpoints(c.target)[0],
				paintStyle:{ lineWidth:10, strokeStyle:"rgb(0, 0, 0)", joinstyle:'round'},
				anchors: [ "Continuous" ],
				connector: "Flowchart" ,
				draggable: true,
				detachable : true
			});
			jsPlumb.bind("dblclick", function(conn) {
				jsPlumb.detach(conn);
			});
		}
	}
	else if(data.user=="s")
	{
		for ( var i = 0; i < data.connections.length; i++) {
			var c = data.connections[i];
			var nodeS = document.getElementById(c.source);
			var nodeT = document.getElementById(c.target);
			$(nodeS).attr('order', c.order);

			jsPlumb.connect({
				source : jsPlumb.getEndpoints(c.source)[1],
				target : jsPlumb.getEndpoints(c.target)[0],
				anchors: [ "Continuous" ],
				connector: "Flowchart",
				paintStyle:{ lineWidth:10, strokeStyle:"rgba(0, 200, 0, 0.5)", joinstyle:'round'},
				draggable: false,
				detachable : false
			});
			jsPlumb.bind("dblclick", function(conn) {
			});
			jsPlumb.bind("connect", function(conn) {
			});
		}
	}
}

function onPageInitLoad() {
	loadExistingActivities();
	loadExistingFlowChart();
}

function onReload(){
	onReset();
	onPageInitLoad();
}

function updateItemPlan(node){
	var name=$(node).attr('id');
	if(name.indexOf("Duplicater_")==-1)
	{
		var jsonObj3 = {
				itemPlans : []
		};
		var pos=$(node).position();
		jsonObj3.itemPlans.push({
			"id" : node.id,
			"top" : ""+pos.top+"px",
			"left" : ""+pos.left+"px"
		});
		var activitiesText=JSON.stringify(jsonObj3);
		document.getElementsByName("itemPlansText")[0].value=itemPlansText;
		var button=document.getElementById("itemPlansUpdate");
		button.click();
	}
}

function createNewActivity(node)
{
	var name=node.text;
}

function editActivity(node)
{
	if(userType=="p")
	{
		var type=$(node).attr('type');
		var pos=$(node).position();
		var jsonObj4 = {
				"id" : node.id,
				"top" : ""+pos.top+"px",
				"left" : ""+pos.left+"px"
		};
		var posicion=JSON.stringify(jsonObj4);
		pos+="id:"+node.idActividad;
		pos+="top:"+pos.top+"px";
		pos+="left:"+pos.left+"px";
		var link=document.getElementById("nuevaActividad-link");
		var str = $(link).attr('href');
		var res = str.replace("tipoActividad=1","tipoActividad="+type);
		res = str.replace("posicionActividad=1","posicionActividad="+posicion);
		$(link).attr('href', res);
		link.click();
	}
	else
	{
		var link=document.getElementById("nuevaActividad-link");
		var str = $(link).attr('href');
		var res = str.replace("idItemUAshyi=0","idItemUAshyi="+$(node).attr("IdItemUAshyi"));
		res = res.replace("idItemPAshyi=0","idItemPAshyi="+$(node).attr("IdItemPAshyi"));
		res = res.replace("itemId=0","itemId="+$(node).attr("ItemId"));
		$(link).attr('href', res);
		link.click();
	}
}

function onSaveGraph()
{
	var jsonObj = {
			graphConnections : []
	};
	var connections=jsPlumb.getConnections();
	for(var i=0;i<connections.length;i++)
	{
		var conn=connections[i];
		var source = conn.sourceId;
		var target = conn.targetId;

		var nodeS = document.getElementById(source);
		var nodeT = document.getElementById(target);
		jsonObj.graphConnections.push({
			"type" : "normal",
			"source" : source,
			"target" : target,
			"order" : $(nodeS).attr('order')
		});

	}
	var text=JSON.stringify(jsonObj);
	document.getElementsByName("connectionText")[0].value=text;
	var button=document.getElementById("saveConnection");
	button.click();
}

function detectIE() {
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
	var trident = ua.indexOf('Trident/');

	if (msie > 0) {
		// IE 10 or older => return version number
		return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
	}

	if (trident > 0) {
		// IE 11 (or newer) => return version number
		var rv = ua.indexOf('rv:');
		return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
	}

	// other browser
	return false;
}