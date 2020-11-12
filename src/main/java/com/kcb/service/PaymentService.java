package com.kcb.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kcb.http.request.Disbursement;
import com.kcb.httpthirdparty.request.MpesaRequest;
import com.kcb.security.SafarcomToken;

@Transactional
@Service
public class PaymentService {
	
	@Autowired
	private SafarcomToken safarcomToken;
	
	private final String URL_PATH = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
	
	private static final Logger Log = LoggerFactory.getLogger(PaymentService.class);
	
	
	public String processPayment(Disbursement disbursement) {
		//generate token
		 safarcomToken.generateToken();
		 
		MpesaRequest mpesaRequest = new MpesaRequest(SafarcomToken.consumerKey, SafarcomToken.AUTH_STRING, "BusinessPayment", disbursement.getRequestPayload().getTransactionInfo().getTransactionAmount(), 
				disbursement.getHeader().getServiceCode(), disbursement.getRequestPayload().getTransactionInfo().getCredintMobileNumber(),
				disbursement.getHeader().getServiceName(), null, "https://localhost:8900/mpesa/payment/response", null);
		
		sendToMpesa(mpesaRequest);
		
		return "Reuest processed";
	
	}
	
	public void mpesaReponse(JSONObject jsonObject) {
		Log.info("payment response:"+jsonObject.toString());
	}
	
   public void sendToMpesa(MpesaRequest mpesaRequest) {
	   
       int tries = 0;
       boolean retry = true;
       while (retry && tries <= 4) {
           if (retry && tries > 0) {
               Log.info("Retrying... No of tries: " + (tries + 1));
           }
           try {
               URL url = new URL(URL_PATH);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               conn.setRequestProperty("Accept", "application/json");
               conn.setDoOutput(true);
               conn.setDoInput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Connection", "close");
               OutputStream os = conn.getOutputStream();
               os.write(new Gson().toJson(mpesaRequest).getBytes("UTF-8"));
               os.flush();
               InputStream in = new BufferedInputStream(conn.getInputStream());
               String result = covertInputStreamToString(in);
               in.close();
               os.close();
               conn.disconnect();
               retry = false;
           } catch (Exception e) {
               retry = true;
               tries++;
               Log.info("Error Creating Entity ", e);
           }
       }
	   
   }
   
   public String covertInputStreamToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();
		byte[] byteArray = buffer.toByteArray();

		String text = new String(byteArray, StandardCharsets.UTF_8);
		return text;
	}
   
}
