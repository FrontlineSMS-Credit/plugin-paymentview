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
		DELAYED,
		ON_TRACK,
		COMPLETED;
		
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
	
	public BigDecimal getAmountSaved(long tartgetId, Date startDate, Date endDate){
	    List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetIdByDates(tartgetId, startDate, endDate);
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
	
	private List<IncomingPayment> getIncomingPaymentsByTargetIdByDates(long tartgetId, Date startDate, Date endDate){
	    List <IncomingPayment> incomingPayments = incomingPaymentDao.
	    getActiveIncomingPaymentsByTargetAndDates(tartgetId, startDate, endDate);
		return incomingPayments;
	}

	public Status getStatus(long targetId){
		List <IncomingPayment> incomingPayments = getIncomingPaymentsByTargetId(targetId);
		
		BigDecimal amountPaid = calculateAmount(incomingPayments);
		BigDecimal totalTargetCost = targetDao.getTargetById(targetId).
				getServiceItem().getAmount();
		
		if(amountPaid.compareTo(totalTargetCost) >=0){
			return Status.COMPLETED;
		}
		
		BigDecimal amountRem = totalTargetCost.subtract(amountPaid);
		long remDays = getDaysRemaining(targetId);

		long endTime = targetDao.getTargetById(targetId).getEndDate().getTime();
		long startTime = targetDao.getTargetById(targetId).getStartDate().getTime();
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
	
	private Calendar setEndOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 24);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private int getEndOfIntervalByInstalment(String prem, String paymenmtDurtn, String amntPaid){
		BigDecimal premium = new BigDecimal(prem);
		BigDecimal paymentDuration = new BigDecimal(paymenmtDurtn);
		BigDecimal amountPaid = new BigDecimal(amntPaid);

		return amountPaid.multiply(paymentDuration.stripTrailingZeros()).
		divide(premium, 4, RoundingMode.HALF_UP).intValue()+1;
	}
    
	public void computeAnalyticsIntervalDatesAndSavings(long targetId){
		
		int startMonth = 0;
		int monthPoz = 0;
		int endDay = 0;
		Target tgt = targetDao.getTargetById(targetId);
		String prem = tgt.getServiceItem().getAmount().toString();
		String instalmentsCount = "0";
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
		calEndDate = setEndOfDay(calEndDate);
		Date endDate = calEndDate.getTime();

		Calendar calNowDate = Calendar.getInstance();
		Date nowDate = calNowDate.getTime();

		startMonth  = getMonthsDiffFromStart(calStartDate, calNowDate); 
		instalmentsCount = String.valueOf(getMonthsDiffFromStart(calEndDate, calStartDate));
		setInstalments(Integer.parseInt(instalmentsCount));
		
		Calendar calEndOf1stInterval = Calendar.getInstance();
		monthPoz = startMonth+1;
		calEndOf1stInterval.add(Calendar.MONTH, monthPoz);  
		calEndOf1stInterval.set(Calendar.DATE, endDay);
		calEndOf1stInterval = setEndOfDay(calEndOf1stInterval);
		Date endOfInterval = calEndOf1stInterval.getTime();
		
		if(endDate.getTime() > nowDate.getTime()){
			int q = 1;
			for(q = 1; nowDate.getTime() > endOfInterval.getTime(); q++){	
				Calendar calEndOfInterval = Calendar.getInstance();
				monthPoz = monthPoz+1;
				calEndOfInterval.add(Calendar.MONTH, monthPoz); 
				calEndOfInterval.set(Calendar.DATE, endDay);
				calEndOfInterval = setEndOfDay(calEndOfInterval);
				endOfInterval = calEndOfInterval.getTime();
			}
			int instlmntPoz = getEndOfIntervalByInstalment(prem, instalmentsCount, amntPaid);
			setMonthlyTarget(new BigDecimal(prem).divide(new BigDecimal(instalmentsCount), 
					4, RoundingMode.HALF_DOWN));
			getRemAmnt(q, instlmntPoz, startMonth, endDay, endOfInterval, startDate, targetId);
		}
	}

	private int getMonthsDiffFromStart(Calendar calStartDate,
			Calendar calNowDate) {
		if(calStartDate.get(Calendar.YEAR)==calNowDate.get(Calendar.YEAR) && calStartDate.get(Calendar.MONTH) == calNowDate.get(Calendar.MONTH)){
			return 0;
		} else {
			return (calStartDate.get(Calendar.YEAR) - calNowDate.get(Calendar.YEAR)) * 12 +
			(calStartDate.get(Calendar.MONTH)- calNowDate.get(Calendar.MONTH)) + 
			(calStartDate.get(Calendar.DAY_OF_MONTH) >= calNowDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 			
		}
		

	}

	private void getRemAmnt(int datepoz, int amntPoz, int startMonth, int endDay, 
			Date endOfIntervalDate, Date startDate, long targetId){
		int diffInPoz = datepoz-amntPoz;
		BigDecimal amntRem = BigDecimal.ZERO;
		BigDecimal instalmentsBD = getMonthlyTarget();
		BigDecimal amntSavedForPeriod = BigDecimal.ZERO;
		Date endOfInstalIntManDate = getInstalmentPozEndOfIntervalDate(startMonth, endDay, amntPoz);
		
		if(diffInPoz==0){
			amntRem = instalmentsBD.multiply(new BigDecimal(String.valueOf(amntPoz))).
			subtract(getAmntPaidFromStart(targetId, startDate, endOfIntervalDate));
			
			amntSavedForPeriod = instalmentsBD.subtract(amntRem);
		} else if (diffInPoz<0) {
			amntRem = instalmentsBD.multiply(new BigDecimal(String.valueOf(amntPoz))).
			subtract(getAmntPaidFromStart(targetId, startDate, endOfInstalIntManDate));
			
			int pozAboveCurreInstlmnt = (diffInPoz*-1);
			int pozAboveCurreInstlmntDiff = 0;
			if(pozAboveCurreInstlmnt==1){
				amntSavedForPeriod = instalmentsBD.multiply(new BigDecimal(String.valueOf(pozAboveCurreInstlmnt))).
				subtract(amntRem);
			} else {
				Calendar calNowDate = Calendar.getInstance();
				Date nowDate = calNowDate.getTime();
				pozAboveCurreInstlmntDiff = pozAboveCurreInstlmnt - 1;
				amntSavedForPeriod = getAmntPaidFromStart(targetId, startDate, nowDate).
				subtract(instalmentsBD.multiply(new 
						BigDecimal(String.valueOf(datepoz+pozAboveCurreInstlmntDiff))));
			}
		} else if (diffInPoz>0) {
			amntSavedForPeriod = getAmntPaidIntervalBackDate(targetId, endOfInstalIntManDate);
			amntRem = instalmentsBD.multiply(new BigDecimal(String.valueOf(diffInPoz))).
			add(instalmentsBD.subtract(getAmntPaidFromStart(targetId, startDate, endOfIntervalDate)));
		}
		if(endOfIntervalDate.getTime() >= endOfInstalIntManDate.getTime()){
			setEndMonthInterval(formatEndDate(endOfIntervalDate));
		} else {
			setEndMonthInterval(formatEndDate(endOfInstalIntManDate));
		}
		setMonthlyAmountSaved(amntSavedForPeriod);
		setMonthlyAmountDue(amntRem);
	}
	
	private BigDecimal getAmntPaidFromStart(long targetId, Date startDate, Date endDate) {
		return getAmountSaved(targetId, startDate, endDate);
	}

	private BigDecimal getAmntPaidIntervalBackDate(long targetId, Date endOfIntervalDate) {
		int endDay = 0;
		int endMonth = 0;
		
		Calendar calEndDate = Calendar.getInstance();
		calEndDate.setTime(endOfIntervalDate);
		endDay = calEndDate.get(Calendar.DAY_OF_MONTH);
		endMonth = calEndDate.get(Calendar.MONTH);
		calEndDate = setEndOfDay(calEndDate);

		Calendar calStartDate = Calendar.getInstance(); 
		calStartDate.setTime(endOfIntervalDate);
		calStartDate.set(Calendar.MONTH, endMonth-1);  
		calStartDate.set(Calendar.DATE, endDay+1);  
		calStartDate = setStartOfDay(calStartDate);
		Date startDate = calStartDate.getTime();
		
		Calendar calNowDate = Calendar.getInstance();
		Date nowDate = calNowDate.getTime();

		return  getAmountSaved(targetId, startDate, nowDate);
	}
	
	private BigDecimal getAmntPaidInterval(long targetId, Date endOfIntervalDate) {
		int endDay = 0;
		int endMonth = 0;
		
		Calendar calEndDate = Calendar.getInstance();
		calEndDate.setTime(endOfIntervalDate);
		endDay = calEndDate.get(Calendar.DAY_OF_MONTH);
		endMonth = calEndDate.get(Calendar.MONTH);
		calEndDate = setEndOfDay(calEndDate);
		Date endDate = calEndDate.getTime();
		
		Calendar calStartDate = Calendar.getInstance(); 
		calStartDate.setTime(endOfIntervalDate);
		calStartDate.set(Calendar.MONTH, endMonth-1);  
		calStartDate.set(Calendar.DATE, endDay);  
		calStartDate = setStartOfDay(calStartDate);
		Date startDate = calStartDate.getTime();

		return  getAmountSaved(targetId, startDate, endDate);
	}

	private Date getInstalmentPozEndOfIntervalDate(int monthNum, int dayNum, int instalmentPoz){
		Calendar calInstalEOD = Calendar.getInstance();

		calInstalEOD.add(Calendar.MONTH, monthNum+instalmentPoz); 
		calInstalEOD.set(Calendar.DATE, dayNum);
		calInstalEOD = setEndOfDay(calInstalEOD);
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
