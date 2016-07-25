var xmlHttp = createXmlHttpRequestObject();

function  createXmlHttpRequestObject() {
	var xmlHttp;
	
	if(window.ActiveXObject) {
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			xmlHttp = false;
		}
	} else {
		try {
			xmlHttp = new XMLHttpRequest();
		} catch (e) {
			xmlHttp = false;
		}
	}
	
	if(!xmlHttp) {
		alert("Cannot create xml http request.");
	} else {
		return xmlHttp;
	}
}

function process() {
	if(xmlHttp.readyState == 4 || xmlHttp.readyState == 0) {
		input = encodeURIComponent(document.getElementById("userInput").value);
		xmlHttp.open("POST", "/");
		xmlHttp.onreadystatechange = handleServerResponse;
		xmlHttp.send(null);
	} else {
		setTimeout('process()', 1000);
	}
}