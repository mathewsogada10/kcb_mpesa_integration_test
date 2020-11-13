package com.kcb.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Configuration
public class SafarcomToken {
	
	public static String token;
	private static final String OAUTH_URL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
	public static final String CONSUMER_KEY = "KgCfBAfcfTojpuwX6evn0hLYAW2EuQRq";
	public static final String CONSUMER_SECRET = "Gg19GwSgeaWY1w7R";
	
	public void generateToken() {
        HttpURLConnection connectionToken;
        JsonParser jsonParser;
        try {
        	String appKeySecret = CONSUMER_KEY + ":" + CONSUMER_SECRET;
			byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
			byte[] auth = Base64.encodeBase64(bytes);
			  
            URL tokenURL = new URL(OAUTH_URL);
            connectionToken = (HttpURLConnection) tokenURL.openConnection();
            connectionToken.setConnectTimeout(120000);
            connectionToken.setRequestProperty("Accept", "application/json");
            connectionToken.setRequestProperty("authorization", "Basic " + new String(auth));
            connectionToken.setRequestProperty("cache-control", "no-cache");

            if (connectionToken.getResponseCode() != 200) {
                if (connectionToken.getResponseCode() != 204) {
                    throw new RuntimeException("Failed : HTTP error code : " + connectionToken.getResponseCode());
                }
            } else {
                jsonParser = new JsonParser(); // from gson
                JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) connectionToken.getContent()));
                JsonObject jsonObject = (JsonObject) root;
                token = jsonObject.get("access_token").getAsString();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	

}
