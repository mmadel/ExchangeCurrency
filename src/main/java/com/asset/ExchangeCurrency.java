package com.asset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExchangeCurrency
 */
public class ExchangeCurrency extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExchangeCurrency() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
        ExchangeUtil exchangeUtil = new ExchangeUtil();
        try {
			exchangeUtil.getExchangeRates(json);
			HashMap<String,String> parameter = exchangeUtil.getParamaters(json);
			double result = exchangeUtil.convertCurrancy(parameter.get("before"), parameter.get("after"), Double.parseDouble( parameter.get("amount")));
			DecimalFormat df = new DecimalFormat("###.#");
			String resultStr = df.format(result);
			response.getWriter().write(resultStr +"");
			System.out.println(" Result : " + resultStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
