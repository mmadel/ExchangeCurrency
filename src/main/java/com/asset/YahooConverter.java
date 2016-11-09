package com.asset;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class YahooConverter implements Converter {

	public float convert(String fromCurrency, String toCurrency) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + fromCurrency + toCurrency + "=X&f=l1&e=.csv");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		 String responseBody = httpclient.execute(httpGet, responseHandler);
		 httpclient.getConnectionManager().shutdown();
		 return Float.parseFloat(responseBody);
	}

}
