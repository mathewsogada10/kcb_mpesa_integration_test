package com.kcb.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kcb.http.request.Disbursement;
import com.kcb.httpthirdparty.response.MpesaResponse;
import com.kcb.service.PaymentService;

@RestController
@RequestMapping(value="mpesa/")
public class MpesaDisbursementController {
	
	@Autowired
	public PaymentService paymentService;
	
	
	@RequestMapping(value="secured/payment/request", method=RequestMethod.POST)
	public String recievePayment(@RequestBody Disbursement disbursement) {
		return paymentService.processPayment(disbursement);
	}
	
	@RequestMapping(value="/payment/response", method=RequestMethod.POST)
	public String paymentResponse(@RequestBody MpesaResponse mpesaResponse) {
	   paymentService.mpesaReponse(mpesaResponse);
	   return "response received";
	}

}
