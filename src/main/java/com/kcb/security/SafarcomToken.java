package com.kcb.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class SafarcomToken {
	
	public static String token;
	private static final String OAUTH_URL = "https://sandbox.safaricom.co.ke/oauth/v1/generate";
	public static String AUTH_STRING;
	public static final String consumerKey = "test";
	public static final String consumerSecret = "test";
	
	public static void generateToken() {
		HttpURLConnection connection = null;
        JsonParser jsonParser;
		
		String authString = consumerKey + ":" + consumerSecret;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		AUTH_STRING = new String(authEncBytes);
        
        try {
			connection = (HttpURLConnection) new URL(OAUTH_URL).openConnection();
			connection.setConnectTimeout(120000);
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setDoInput(true);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	        connection.setRequestProperty("charset", "utf-8");
	        connection.setRequestProperty("Authorization", "Basic "+AUTH_STRING);
	        if (connection.getResponseCode() != 200) {
                if (connection.getResponseCode() != 204) {
                    throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
                }
            } else {
                jsonParser = new JsonParser();
                JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) connection.getContent()));
                token = root.toString();
                
            }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	

}
