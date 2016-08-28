function requestExchangeCurrencyWebService() {
	var request = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json&callback=requestLocalBackEnd";
	var head = document.getElementsByTagName("head").item(0);
	var script = document.createElement("script");
	script.setAttribute("type", "text/javascript");
	script.setAttribute("src", request);
	head.appendChild(script);
}
function requestLocalBackEnd(JSONData) {
	var paramaters = getParamaters();
	JSONData["paramaters"] = {
		"before" : paramaters['before'],
		"after" : paramaters['after'],
		"amount" : paramaters['amount']
	};
	$.ajax({
		type : "post",
		url : "ExchangeCurrency",
		data : JSON.stringify(JSONData),
		success : function(data, text) {
			$("#resultDivId").html(data);
		},
		error : function(request, status, error) {
			alert(status);
			alert(error);
		}
	});
}
function getParamaters() {
	var paramaters = new Object();
	paramaters['before'] = $("#before").val();
	paramaters['after'] = $("#after").val();
	paramaters['amount'] = $("#amount").val();
	return paramaters;
}