var xmlHttp = createXmlHttpRequestObject();

function createXmlHttpRequestObject() {
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
		query = encodeURIComponent(document.getElementById("query-input").value);
		xmlHttp.open("GET", "http://localhost:8090/suggest?query=" + query, true);
		xmlHttp.onreadystatechange = handleServerResponse;
		xmlHttp.send(null);
	} else {
		setTimeout('process()', 1000);
	}
}

function handleServerResponse() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
			suggest = xmlHttp.responseText;
			document.getElementById("test").innerHTML = '<span style="color:blue">' + suggest + '</span>';
			setTimeout('process()', 1000);

		} else {
			alert("Something went wrong!");
		}
	}
	
}

function popup(url) {
	newwindow=window.open(url,'name','height=250,width=250,top=200,left=400');
	if (window.focus) {
		newwindow.focus()
	}
//	return false;
}