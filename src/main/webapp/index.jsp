<html>
<head>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/app.js"></script>
<script src="js/yahoo_requester.js"></script>
</head>
<body>
	<div class="container">
		<h2>Exchange Currency</h2>
		<form>
			<div class="form-group">
				<label for="before">Before:</label> <input type="text"
					class="form-control" id="before">
			</div>
			<div class="form-group">
				<label for="after">After:</label> <input type="text"
					class="form-control" id="after">
			</div>
			<div class="form-group">
				<label for="amount">Amount:</label> <input type="text"
					class="form-control" id="amount">
			</div>
			<button type="button" class="btn btn-default" onclick="requestExchangeCurrencyWebService()">Exchange Currency</button>
		</form>
		<div id="resultDivId"></div>
	</div>
</body>
</html>
