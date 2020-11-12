package com.kcb.http.request;

import java.util.Date;

public class TransactionInfo {
	
	private String companyCode;
	private String transactionType;
	private String creditAccountNumber;
	private String credintMobileNumber;
	private String transactionAmount;
	private String transactionReference;
	private String currencyCode;
	private String amountCurrency;
	private Date dateTime;
	private String dateString;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCreditAccountNumber() {
		return creditAccountNumber;
	}
	public void setCreditAccountNumber(String creditAccountNumber) {
		this.creditAccountNumber = creditAccountNumber;
	}
	public String getCredintMobileNumber() {
		return credintMobileNumber;
	}
	public void setCredintMobileNumber(String credintMobileNumber) {
		this.credintMobileNumber = credintMobileNumber;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getAmountCurrency() {
		return amountCurrency;
	}
	public void setAmountCurrency(String amountCurrency) {
		this.amountCurrency = amountCurrency;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

}
