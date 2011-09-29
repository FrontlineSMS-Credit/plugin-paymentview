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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

/**
 * 
 * @author Roy
 */
public class TargetAnalytics {
	public enum Status {
		PAYING,
		OVERDUE,
		INACTIVE,
		PAID;
		
		@Override
		public String toString() {
			return name().toLowerCase().replace('_', ' ');
		}
	}
	
	private TargetDao targetDao;
	private IncomingPaymentDao incomingPaymentDao;
	
	private BigDecimal monthlyAmountSaved = BigDecimal.ZERO;
	private BigDecimal monthlyAmountDue = BigDecimal.ZERO;
	private BigDecimal monthlyTarget = BigDecimal.ZERO;

	private int instalments = 0;
	private Date endMonthInterval = new Date();

	public BigDecimal getPercentageToGo(long tartgetId){
	    BigDecimal totalTargetCost = targetDao.getTargetById(tartgetId).getTotalTargetCost();
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
	    String accountNumber = getAccountNumber(tartgetId);
	    List<IncomingPayment> incList = incomingPaymentDao.getActiveIncomingPaymentsByAccountNumberOrderByTimepaid(accountNumber);
	    
	    if(incList!=null && incList.size()>0){
	    	return incList.get(0).getAmountPaid();
	    }else{
	    	return new BigDecimal(0);
	    }
	}
	
	private List<IncomingPayment> getIncomingPaymentsByTargetId(long tartgetId){
	    List <IncomingPayment> incomingPayments = incomingPaymentDao.getActiveIncomingPaymentsByTarget(tartgetId);
		return incomingPayments;
	}
	
	private List<IncomingPayment> getIncomingPaymentsByTargetIdByDates(long tartgetId, Date startDate, Date endDate){
	    List <IncomingPayment> incomingPayments = incomingPaymentDao.
	    getActiveIncomingPaymentsByTargetAndDates(tartgetId, startDate, endDate);
		return incomingPayments;
	}

	public Status getStatus(long targetId){
		List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(targetId);
		
		BigDecimal amountPaid = calculateAmount(incomingPayments);
		BigDecimal totalTargetCost = targetDao.getTargetById(targetId).getTotalTargetCost();

		long endTime = targetDao.getTargetById(targetId).getEndDate().getTime();
		Calendar calNowDate = Calendar.getInstance();
		long nowTime = calNowDate.getTime().getTime();

		Date lastPaymentdate = new Date();
		if(getLastDatePaid(targetId)==null){
			return Status.INACTIVE;
		} else {
			lastPaymentdate = getLastDatePaid(targetId);
		}
		Calendar lastPaymentCalDate = Calendar.getInstance();
		lastPaymentCalDate.setTime(lastPaymentdate);
		
		if(amountPaid.compareTo(totalTargetCost) >=0){
			return Status.PAID;
		}
		
		if(nowTime > endTime && amountPaid.compareTo(totalTargetCost) < 0){
			return Status.OVERDUE;
		}else if(nowTime < endTime && amountPaid.compareTo(totalTargetCost) < 0 
				&& getMonthsDiffFromStart(calNowDate, lastPaymentCalDate) == 0){
			return Status.PAYING;
		}else if(getMonthsDiffFromStart(calNowDate, lastPaymentCalDate ) > 0 
				&& amountPaid.compareTo(totalTargetCost) < 0){
			return Status.INACTIVE;	
		}
		return Status.INACTIVE;
	}
	
	private Long getDateDiffDays(long startTime, long endTime){
	    long diff = endTime - startTime;
		long targetDays = diff / (1000 * 60 * 60 * 24);
		
		return targetDays +1;
	}
	
	public Long getDaysRemaining(long tartgetId){
		long endTime = targetDao.getTargetById(tartgetId).getEndDate().getTime();
		
	    if(getDateDiffDays(new Date().getTime(), endTime)<0){
	    	return (long) 0;
		} else {
			return getDateDiffDays(new Date().getTime(), endTime);
		}
	}
	
	public Date getLastDatePaid(long tartgetId){
	    String accountNumber = getAccountNumber(tartgetId);
	    List<IncomingPayment> incList = incomingPaymentDao.getActiveIncomingPaymentsByAccountNumberOrderByTimepaid(accountNumber);
	    
	    if(incList!=null && incList.size()>0){
	    	return new Date(incList.get(0).getTimePaid());
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

	private Date formatEndDate(Date dateStr){
		Date date = null;
		Calendar calEndDate = Calendar.getInstance(); 
		calEndDate.setTime(dateStr);
		calEndDate.set(Calendar.SECOND, -1);
		date = calEndDate.getTime();

		return date;
	}
	
	private Calendar setStartOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	//Function which retrieves the theoretical installment based on amount paid
	private int getamountPaidInstallmentPosition(String prem, String paymenmtDurtn, String amntPaid){
		BigDecimal premium = new BigDecimal(prem);
		BigDecimal paymentDuration = new BigDecimal(paymenmtDurtn);
		BigDecimal amountPaid = new BigDecimal(amntPaid);

		return amountPaid.multiply(paymentDuration.stripTrailingZeros()).
			divide(premium, 4, RoundingMode.HALF_UP).intValue()+1;
	}
	
	public void computeAnalyticsIntervalDatesAndSavings(long targetId){
		
		int endDay = 0;
		Target tgt = targetDao.getTargetById(targetId);
		String totalAmount = tgt.getTotalTargetCost().toString();;
		String totalInstalmentsCount = "0";
		Date startDateStr = tgt.getStartDate();
		Date endDateStr = tgt.getEndDate();

		String amntPaid = getAmntPaidFromStart(targetId, startDateStr, endDateStr).toString();
		
		Calendar calStartDate = Calendar.getInstance(); 
		calStartDate.setTime(startDateStr);
		calStartDate = setStartOfDay(calStartDate);
		Date startDate = calStartDate.getTime();

		Calendar calEndDate = Calendar.getInstance();
		calEndDate.setTime(endDateStr);
		endDay = calEndDate.get(Calendar.DAY_OF_MONTH);
		calEndDate.add(Calendar.SECOND, 1);
		Date endDate = calEndDate.getTime();

		Calendar calNowDate = Calendar.getInstance();
		Date nowDate = calNowDate.getTime();

		totalInstalmentsCount = String.valueOf(getMonthsDiffFromStart(calEndDate, calStartDate));
		setInstalments(Integer.parseInt(totalInstalmentsCount));
		
		//Set up the end date of the first interval
		Calendar calEndOf1stInterval = Calendar.getInstance();
		calEndOf1stInterval.setTime(startDate);
		calEndOf1stInterval.add(Calendar.MONTH, 1);  
		Date dateEndOfInterval = calEndOf1stInterval.getTime();
		
		//Set up the next targeted end of interval - amount installment position - current date installment position
		if(endDate.getTime() > nowDate.getTime()){
			int datePoz = 1;
			Calendar calEndOfInterval = Calendar.getInstance();
			calEndOfInterval.setTime(dateEndOfInterval);
			for(datePoz = 1; nowDate.getTime() > dateEndOfInterval.getTime(); datePoz++){	
				calEndOfInterval.add(Calendar.MONTH, 1); 
				dateEndOfInterval = calEndOfInterval.getTime();
			}
			int amountPaidPoz = getamountPaidInstallmentPosition(totalAmount, totalInstalmentsCount, amntPaid);
			setMonthlyTarget(new BigDecimal(totalAmount).divide(new BigDecimal(totalInstalmentsCount), 
					4, RoundingMode.HALF_DOWN));
			getRemAmnt(datePoz, amountPaidPoz, endDay, dateEndOfInterval, startDate, targetId,new BigDecimal(amntPaid));
		} else {
			setMonthlyAmountDue(new BigDecimal(totalAmount).subtract(new BigDecimal(amntPaid)));
		}
	}

	private int getMonthsDiffFromStart(Calendar calEndDate,
			Calendar calStartDate) {
		if(calEndDate.get(Calendar.YEAR)==calStartDate.get(Calendar.YEAR) && calEndDate.get(Calendar.MONTH) == calStartDate.get(Calendar.MONTH)){
			return 0;
		} else {
			return (calEndDate.get(Calendar.YEAR) - calStartDate.get(Calendar.YEAR)) * 12 +
			(calEndDate.get(Calendar.MONTH)- calStartDate.get(Calendar.MONTH)) + 
			(calEndDate.get(Calendar.DAY_OF_MONTH) >= calStartDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 			
		}
	}

	/*
	 * This function retrieves the remaining amount and the paid amount of the active interval
	 */
	private void getRemAmnt(int datepoz, int amntPoz, int endDay, 
				Date endOfIntervalDate, Date startDate, long targetId, BigDecimal amntPaidBD){
		BigDecimal amntRem = BigDecimal.ZERO;
		BigDecimal instalmentsBD = getMonthlyTarget();
		BigDecimal amntSavedForPeriod = BigDecimal.ZERO;
		
		Date amountPaidEndOfIntervalDate = getInstalmentPozEndOfIntervalDate(startDate, amntPoz);
		
		amntRem = instalmentsBD.multiply(new BigDecimal(String.valueOf(new BigDecimal(datepoz).max(new BigDecimal(amntPoz))))).
			subtract(amntPaidBD);
		
		// The amount saved for period represents the amount saved this month interval
		Calendar calStartOfIntervalDate = Calendar.getInstance();
		calStartOfIntervalDate.setTime(endOfIntervalDate);
		calStartOfIntervalDate.add(Calendar.MONTH, -1);
		amntSavedForPeriod = getAmountSaved(targetId,  calStartOfIntervalDate.getTime() , endOfIntervalDate);
		

		if(endOfIntervalDate.getTime() >= amountPaidEndOfIntervalDate.getTime()){
			setEndMonthInterval(formatEndDate(endOfIntervalDate));
		} else {
			setEndMonthInterval(formatEndDate(amountPaidEndOfIntervalDate));
		}
		setMonthlyAmountSaved(amntSavedForPeriod);
		setMonthlyAmountDue(amntRem);
	}
	
	public void clearAnalyticsComputations(){
		setInstalments(0);
		setMonthlyTarget(BigDecimal.ZERO);
		setEndMonthInterval(null);
		setMonthlyAmountSaved(BigDecimal.ZERO);
		setMonthlyAmountDue(BigDecimal.ZERO);
	}
	
	private BigDecimal getAmntPaidFromStart(long targetId, Date startDate, Date endDate) {
		return getAmountSaved(targetId, startDate, endDate);
	}
	
	public BigDecimal getAmountSaved(long tartgetId, Date startDate, Date endDate){
	    List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetIdByDates(tartgetId, startDate, endDate);
		return calculateAmount(incomingPayments);
	}

	// This function calculates the end date of the installment linked to the amount paid
	private Date getInstalmentPozEndOfIntervalDate(Date startDate, int instalmentPoz){
		Calendar calInstalEOD = Calendar.getInstance();
		calInstalEOD.setTime(startDate);
		calInstalEOD.add(Calendar.MONTH, instalmentPoz); 
		return calInstalEOD.getTime();
	}
	
	public BigDecimal getMonthlyAmountSaved() {
		return monthlyAmountSaved.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public void setMonthlyAmountSaved(BigDecimal monthlyAmount) {
		this.monthlyAmountSaved = monthlyAmount;
	}

	public BigDecimal getMonthlyAmountDue() {
		return monthlyAmountDue.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public void setMonthlyAmountDue(BigDecimal monthlyAmountDue) {
		this.monthlyAmountDue = monthlyAmountDue;
	}

	public Date getEndMonthInterval() {
		return endMonthInterval;
	}

	public void setEndMonthInterval(Date endMonthInterval) {
		this.endMonthInterval = endMonthInterval;
	}
	
	public BigDecimal getMonthlyTarget() {
		return monthlyTarget.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public void setMonthlyTarget(BigDecimal monthlyTarget) {
		this.monthlyTarget = monthlyTarget;
	}

	public int getInstalments() {
		return instalments;
	}

	public void setInstalments(int instalments) {
		this.instalments = instalments;
	}
}
