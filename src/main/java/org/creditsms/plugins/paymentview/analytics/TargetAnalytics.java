/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview.analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

/**
 * 
 * @author Roy
 */
public class TargetAnalytics {
	public enum Status {
		DELAYED,
		ON_TRACK,
		COMPLETED;
		
		public String toString() {
			return name().toLowerCase().replace('_', ' ');
		}
	}
	
	private TargetDao targetDao;
	private IncomingPaymentDao incomingPaymentDao;
	
	public BigDecimal getPercentageToGo(long tartgetId){
	    BigDecimal totalTargetCost = targetDao.getTargetById(tartgetId).
	    		getServiceItem().getAmount();
	    List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(tartgetId);
		
		return calculatePercentageToGo(totalTargetCost, calculateAmount(incomingPayments));
	}
	
	BigDecimal calculateAmount(List<IncomingPayment> payments) {
		BigDecimal amountPaid = BigDecimal.ZERO;
		for(IncomingPayment payment : payments) {
			amountPaid = amountPaid.add(payment.getAmountPaid());
		}
		return amountPaid;
	}
	
	BigDecimal calculatePercentageToGo(BigDecimal totalTargetCost, BigDecimal amountPaid) {
		if(amountPaid.divide(totalTargetCost, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100).stripTrailingZeros()).equals(BigDecimal.ZERO)){
			return amountPaid.divide(totalTargetCost, 4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100).stripTrailingZeros());
		}
		return amountPaid.divide(totalTargetCost, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100).stripTrailingZeros());
	}
	 
	public BigDecimal getAmountSaved(long tartgetId){
	    List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(tartgetId);
		return calculateAmount(incomingPayments);
	}
	
	public BigDecimal getLastAmountPaid(long tartgetId){
	    List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(tartgetId);
	    
	    if(incomingPayments.size()==0){
	    	return new BigDecimal("0.00");
	    }else{
		    int lastPoz = incomingPayments.size()-1;
		    return incomingPayments.get(lastPoz).getAmountPaid();	
	    }

	}
	
	private List<IncomingPayment> getIncomingPaymentsByTargetId(long tartgetId){
	    List <IncomingPayment> incomingPayments = incomingPaymentDao.
	    getActiveIncomingPaymentsByTarget(tartgetId);
		
		return incomingPayments;
	}
	
	public Status getStatus(long tartgetId){
		List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(tartgetId);
		
		BigDecimal amountPaid = calculateAmount(incomingPayments);
		BigDecimal totalTargetCost = targetDao.getTargetById(tartgetId).
				getServiceItem().getAmount();
		
		if(amountPaid.compareTo(totalTargetCost) >=0){
			return Status.COMPLETED;
		}
		
		BigDecimal amountRem = totalTargetCost.subtract(amountPaid);
		long remDays = getDaysRemaining(tartgetId);

		long endTime = targetDao.getTargetById(tartgetId).getEndDate().getTime();
		long startTime = targetDao.getTargetById(tartgetId).getStartDate().getTime();
		long targetDays = getDateDiffDays(startTime, endTime);
		BigDecimal initAmntRate;
		BigDecimal remAmntRate;
		
		if (targetDays>0){
			initAmntRate = totalTargetCost.divide(BigDecimal.valueOf(targetDays), 2, RoundingMode.HALF_UP);
		} else {
			initAmntRate = totalTargetCost;
		}
		
		if (remDays>0){
			remAmntRate = amountRem.divide(BigDecimal.valueOf(remDays), 2, RoundingMode.HALF_UP);
		} else {
			remAmntRate = amountRem;
		}
				

		
		if(initAmntRate.compareTo(remAmntRate) >= 0){
			return Status.ON_TRACK;
		}else{
			return Status.DELAYED;
		}
	}

	private Long getDateDiffDays(long startTime, long endTime){
	    long diff = endTime - startTime;
		long targetDays = diff / (1000 * 60 * 60 * 24);
		
		return targetDays +1;
	}
	
	public Long getDaysRemaining(long tartgetId){
		long endTime = targetDao.getTargetById(tartgetId).getEndDate().getTime();
		return getDateDiffDays(new Date().getTime(), endTime);
	}
	
	public Date getLastDatePaid(long tartgetId){
	    String accountNumber = getAccountNumber(tartgetId);
	    
	    if(incomingPaymentDao.
				getLastActiveIncomingPaymentDateByAccountNumber(accountNumber)!=null){
	    	return new Date(incomingPaymentDao.
					getLastActiveIncomingPaymentDateByAccountNumber(accountNumber));
	    }else{
	    	return null;
	    }
	}

	private String getAccountNumber(long tartgetId) {
		return targetDao.getTargetById(tartgetId).
	    		getAccount().getAccountNumber();
	}
	
	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}
	
	public void setIncomingPaymentDao(IncomingPaymentDao incomingPaymentDao) {
		this.incomingPaymentDao = incomingPaymentDao;
	}
}
