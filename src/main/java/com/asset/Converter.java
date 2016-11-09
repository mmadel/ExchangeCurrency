package com.asset;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface Converter {
	public float convert(String fromCurrenct , String toCurrency) throws ClientProtocolException, IOException;
}
