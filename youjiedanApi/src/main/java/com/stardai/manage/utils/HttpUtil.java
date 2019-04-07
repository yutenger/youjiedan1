package com.stardai.manage.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author jdw
 * @date 2017/10/16
 */
@SuppressWarnings("all")
public class HttpUtil {

	public HttpUtil() {
	}

	public static String requestByPost(Map<String, String> params, String strURL) {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		String result = "";

		try {
			StringBuilder e = new StringBuilder();
			if (params != null && !params.isEmpty()) {

				for (Object o : params.entrySet()) {
					Map.Entry url = (Map.Entry) o;
					e.append((String) url.getKey()).append("=");
					e.append(URLEncoder.encode((String) url.getValue(), "UTF-8"));
					e.append("&");
				}

				e.deleteCharAt(e.length() - 1);
			}

			byte[] entity1 = e.toString().getBytes();
			URL url1 = new URL(strURL);
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", String.valueOf(entity1.length));
			OutputStream outStream = connection.getOutputStream();
			outStream.write(entity1);
			if (connection.getResponseCode() == 200) {
				inputStream = connection.getInputStream();
				result = covert2String(inputStream);
			}
		} catch (Exception var17) {
			var17.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException var16) {
					var16.printStackTrace();
				}
			}

			if (connection != null) {
				connection.disconnect();
			}

		}

		return result;
	}

	public static String postSpecficalData(String params, String requestURL) {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		String result = null;

		try {
			byte[] e = params.getBytes();
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", String.valueOf(e.length));
			OutputStream outStream = connection.getOutputStream();
			outStream.write(e);
			if (connection.getResponseCode() == 200) {
				inputStream = connection.getInputStream();
				result = covert2String(inputStream);
			}
		} catch (Exception var16) {
			var16.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException var15) {
					var15.printStackTrace();
				}
			}

			if (connection != null) {
				connection.disconnect();
			}

		}

		return result;
	}

	public static String covert2String(InputStream input) {
		String str = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		boolean len = false;
		byte[] data = new byte[1024];

		try {
			int len1;
			while ((len1 = input.read(data)) != -1) {
				outputStream.write(data, 0, len1);
			}

			str = new String(outputStream.toByteArray(), "utf-8");
		} catch (IOException var14) {
			var14.printStackTrace();
		} finally {
			try {
				input.close();
				outputStream.close();
			} catch (IOException var13) {
				var13.printStackTrace();
			}

		}

		return str;
	}
}
