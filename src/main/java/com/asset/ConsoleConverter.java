package com.asset;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class ConsoleConverter {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		Converter converter = new YahooConverter();
		float rate  = converter.convert("USD", "EGP");
		System.out.println("Rate Is : " + rate);
	}

}
