package com.kcb.service;

import java.awt.List;
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
import java.util.ArrayList;

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
import com.kcb.httpthirdparty.response.MpesaResponse;
import com.kcb.model.CallbackUrl;
import com.kcb.repositories.CallbackRepository;
import com.kcb.security.SafarcomToken;

@Transactional
@Service
public class PaymentService {
	
	@Autowired
	private SafarcomToken safarcomToken;
	
	private final String URL_PATH = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
	
	private static final Logger Log = LoggerFactory.getLogger(PaymentService.class);
	
	public String mpesaPassKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c91";
	
	private String shortCode = "174379";
	
	private String queueTimeOutURL = "http://192.168.0.104:8900/mpesa/payment/response";
	
	private String resultURL = "http://192.168.0.104:8900/mpesa/payment/response";
	
	private static  String callBackUrlString = null;
	
	@Autowired
	private CallbackRepository callbackRepository;
	
	
	public String processPayment(Disbursement disbursement) {
		
		if(callBackUrlString == null) {
			ArrayList<CallbackUrl> urlsList = new ArrayList<CallbackUrl>();
			urlsList = (ArrayList<CallbackUrl>) callbackRepository.findAll();
			if(urlsList.isEmpty()) {
				callbackRepository.save(new CallbackUrl(disbursement.getHeader().getCallBackURL()));
				callBackUrlString = disbursement.getHeader().getCallBackURL();
			}else {
				callBackUrlString = urlsList.get(0).getCallbackUrl();
			}
		}
		
		MpesaRequest mpesaRequest = new MpesaRequest("mogada", mpesaPassKey, "BusinessPayment", disbursement.getRequestPayload().getTransactionInfo().getTransactionAmount(), 
				shortCode, disbursement.getRequestPayload().getTransactionInfo().getCredintMobileNumber(),
				disbursement.getHeader().getServiceName(), queueTimeOutURL,resultURL , null);
		
		//generate token
		if(SafarcomToken.token == null) {
			safarcomToken.generateToken();
		}
		 
		return sendToMpesa(mpesaRequest);
			
	}
	
	public void mpesaReponse(MpesaResponse mpesaResponse) {
		Log.info("Submit received mpesa response to banking core system");
		if(callBackUrlString == null) {
			return;
		}
		
		int tries = 0;
	       boolean retry = true;
	       while (retry && tries <= 4) {
	           if (retry && tries > 0) {
	               Log.info("Retrying... No of tries: " + (tries + 1));
	           }
	           try {
	               URL url = new URL(callBackUrlString);
	               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	               conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	               conn.setRequestProperty("Accept", "application/json");
	               conn.setDoOutput(true);
	               conn.setDoInput(true);
	               conn.setRequestMethod("POST");
	               conn.setRequestProperty("Connection", "close");
	               OutputStream os = conn.getOutputStream();
	               os.write(new Gson().toJson(mpesaResponse).getBytes("UTF-8"));
	               os.flush();
	               InputStream in = new BufferedInputStream(conn.getInputStream());
	               String result = covertInputStreamToString(in);
	               //Log.info("Post response core banking system:"+result);
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
	
   public String sendToMpesa(MpesaRequest mpesaRequest) {
	   String result=null;
       int tries = 0;
       boolean retry = true;
       HttpURLConnection conn=null;
       while (retry && tries <= 4) {
           if (retry && tries > 0) {
               Log.info("Retrying... No of tries: " + (tries + 1));
           }
           try {
               URL url = new URL(URL_PATH);
               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               conn.setRequestProperty("Accept", "application/json");
               conn.setDoOutput(true);
               conn.setDoInput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Connection", "close");
               conn.setRequestProperty("Authorization", "Bearer "+SafarcomToken.token);
               OutputStream os = conn.getOutputStream();
               os.write(new Gson().toJson(mpesaRequest).getBytes("UTF-8"));
               os.flush();
               InputStream in = new BufferedInputStream(conn.getInputStream());
               result = covertInputStreamToString(in);
               Log.info(result);
               in.close();
               os.close();
               conn.disconnect();
               retry = false;
           } catch (Exception e) {
        	   
        	   try {
				int httpCode = conn.getResponseCode();
					if(httpCode == 401) {
						safarcomToken.generateToken();
						sendToMpesa(mpesaRequest);
					}else {
						   retry = true;
			               tries++;
			               Log.info("Error Creating Entity ", e);
					}
			   } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			   }
        	   
               //if(conn.getResponseCode())
           }
       }
	   return result;
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
