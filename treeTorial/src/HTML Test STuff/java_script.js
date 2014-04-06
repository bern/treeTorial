xmlToJson = function(xml) {
    var obj = {};
    if (xml.nodeType == 1) {                
        if (xml.attributes.length > 0) {
            obj["@attributes"] = {};
            for (var j = 0; j < xml.attributes.length; j++) {
                var attribute = xml.attributes.item(j);
                obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
            }
        }
    } else if (xml.nodeType == 3) { 
        obj = xml.nodeValue;
    }            
    if (xml.hasChildNodes()) {
        for (var i = 0; i < xml.childNodes.length; i++) {
            var item = xml.childNodes.item(i);
            var nodeName = item.nodeName;
            if (typeof (obj[nodeName]) == "undefined") {
                obj[nodeName] = xmlToJson(item);
            } else {
                if (typeof (obj[nodeName].push) == "undefined") {
                    var old = obj[nodeName];
                    obj[nodeName] = [];
                    obj[nodeName].push(old);
                }
                obj[nodeName].push(xmlToJson(item));
            }
        }
    }
    return obj;
}

getXmlDoc = function(xml){
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.open("GET",xml,false);
	xmlhttp.send();
	xmlDoc=xmlhttp.responseXML; 
	return xmlDoc;
}

getKnowledgeNodes = function(obj){
	var length = obj.nodeIndex.Node.length;
	var array = new Array();
	
	for (var i=0;i<length;i++){ 
		xmlDoc = getXmlDoc(obj.nodeIndex.Node[i].filename["#text"]);
		temp = xmlToJson(xmlDoc);
		array[i] = temp;
	}
	return array;
}

bsFunc = function(){
	return 0;
}

printKnowledgeNodes = function(knowledgeNodes){
	var length = knowledgeNodes.length;
	for (var i=0;i<length;i++){ 
		var div = document.createElement("div");
		div.style.width = "150px";
		div.style.height = "30px";
		div.innerHTML = "<a href=javascript:change_frame('"+escape(arrayKnowledgeNodes[i].KnowledgeNode.name['#text'])+"')>" + arrayKnowledgeNodes[i].KnowledgeNode.name['#text'] + "</a>";
		document.body.appendChild(div);
	}
}
