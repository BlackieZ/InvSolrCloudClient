package com.valueclickbrands.solr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class Http {

	private static Logger logger = Logger.getLogger(Http.class);

	private static MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();

	public static void initHttp() {
		cm.getParams().setDefaultMaxConnectionsPerHost(100);
		cm.getParams().setMaxTotalConnections(500);
//		cm.getParams().setConnectionTimeout(30000);
//		cm.getParams().setSoTimeout(120000);
	}

	public static String sendRequest(String url) {
		StringBuilder result = new StringBuilder("");
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		GetMethod method = null;
		try {

			HttpClient client = new HttpClient(cm);
			
			method = new GetMethod(url);
//			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
			int returnCode = client.executeMethod(method);

			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err.println("The Post method is not implemented by this URI");
				// still consume the response body
				result.append(method.getResponseBodyAsString());
			} else {
				reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
				String readLine;
				while (((readLine = reader.readLine()) != null)) {
					result.append(readLine);
				}
			}

		} catch (IOException ex) {
			// ex.printStackTrace();
			logger.error("send request to interface error:" + ex.getMessage());
			return "";
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (connection != null) {
				// 断开连接
				connection.disconnect();
			}
		}

		return result.toString();
	}
}
