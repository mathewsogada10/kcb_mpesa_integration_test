package com.kcb.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kcb.http.request.Disbursement;
import com.kcb.service.PaymentService;

@RestController
@RequestMapping(value="secured/mpesa/")
public class MpesaDisbursementController {
	
	@Autowired
	public PaymentService paymentService;
	
	
	@RequestMapping(value="/payment/request", method=RequestMethod.POST)
	public void recievePayment(@RequestBody Disbursement disbursement) {
		paymentService.processPayment(disbursement);
	}
	
	@RequestMapping(value="/payment/response", method=RequestMethod.POST)
	public String paymentResponse(@RequestBody JSONObject jsonObject) {
	   paymentService.mpesaReponse(jsonObject);
	   return "response received";
	}

}
