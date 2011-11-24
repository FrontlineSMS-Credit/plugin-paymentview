package org.creditsms.plugins.paymentview.data.importexport;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_AMOUNT_SAVED;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_CURRENT_AMOUNT_DUE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_CURRENT_DUE_DATE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_DAYS_REMAINING;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_END_DATE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LAST_PAYMENT;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LAST_PAYMENT_DATE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PAID_THIS_MONTH;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_AMOUNT_REMAINING;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PERCENTAGE_SAVED;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PRODUCTS;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_SAVINGS_TARGET;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_START_DATE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_TARGET_STATUS;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.csv.Utf8FileWriter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;


public class TargetViewCsvExporter extends net.frontlinesms.csv.CsvExporter {
	private static TargetServiceItemDao targetServiceItemDao;
	private static TargetDao targetDao;
	private static IncomingPaymentDao incomingPaymentDao;
	private static TargetAnalytics targetAnalytics = new TargetAnalytics();
	
	public static void exportTarget(File exportFile,
			List<Target> targetList,
			CsvRowFormat targetFormat, PaymentViewPluginController pluginController ) throws IOException {
		targetServiceItemDao = pluginController.getTargetServiceItemDao();
		targetDao = pluginController.getTargetDao();
		incomingPaymentDao = pluginController.getIncomingPaymentDao();
		targetAnalytics.setTargetDao(targetDao);
		targetAnalytics.setIncomingPaymentDao(incomingPaymentDao);
		LOG.trace("ENTER");
		LOG.debug("Target format [" + targetFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			CsvUtils
					.writeLine(out, targetFormat,
							PaymentViewCsvUtils.TARGET_CLIENT_NAME,
							InternationalisationUtils.getI18nString(COMMON_NAME),
							PaymentViewCsvUtils.TARGET_PRODUCTS,
							InternationalisationUtils.getI18nString(COMMON_PRODUCTS),
							PaymentViewCsvUtils.TARGET_STARTDATE,
							InternationalisationUtils.getI18nString(COMMON_START_DATE),
							PaymentViewCsvUtils.TARGET_ENDDATE,
							InternationalisationUtils.getI18nString(COMMON_END_DATE),
							PaymentViewCsvUtils.TARGET_AMOUNT,
							InternationalisationUtils.getI18nString(COMMON_SAVINGS_TARGET),
							PaymentViewCsvUtils.TARGET_AMOUNT_PAID,
							InternationalisationUtils.getI18nString(COMMON_AMOUNT_SAVED),
							PaymentViewCsvUtils.TARGET_AMOUNT_REMAINING,
							InternationalisationUtils.getI18nString(COMMON_AMOUNT_REMAINING),
							PaymentViewCsvUtils.TARGET_PERCENTAGE,
							InternationalisationUtils.getI18nString(COMMON_PERCENTAGE_SAVED),
							PaymentViewCsvUtils.TARGET_LAST_AMOUNT_PAID,
							InternationalisationUtils.getI18nString(COMMON_LAST_PAYMENT),
							PaymentViewCsvUtils.TARGET_DATE_PAID,
							InternationalisationUtils.getI18nString(COMMON_LAST_PAYMENT_DATE),
							PaymentViewCsvUtils.TARGET_MONTHLY_SAVINGS,
							InternationalisationUtils.getI18nString(COMMON_PAID_THIS_MONTH),
							PaymentViewCsvUtils.TARGET_DAYS_REMAINING,
							InternationalisationUtils.getI18nString(COMMON_DAYS_REMAINING),
							PaymentViewCsvUtils.TARGET_STATUS,
							InternationalisationUtils.getI18nString(COMMON_TARGET_STATUS),
							PaymentViewCsvUtils.MONTHLY_DUE,
							InternationalisationUtils.getI18nString(COMMON_CURRENT_AMOUNT_DUE),
							PaymentViewCsvUtils.TARGET_CURRENT_DUE_DATE,
							InternationalisationUtils.getI18nString(COMMON_CURRENT_DUE_DATE));
			
			
			for (Target target : targetList) {
				targetAnalytics.computeAnalyticsIntervalDatesAndSavings(target.getId());
				String lastDatePaid = "";
				if(targetAnalytics.getLastDatePaid(target.getId())==null) {
					lastDatePaid = "No Payment Made Yet";
				} else {
					lastDatePaid = InternationalisationUtils.getDatetimeFormat().format(new Date(targetAnalytics.getLastDatePaid(target.getId()).getTime()));
				}
				CsvUtils.writeLine(out, targetFormat,
						PaymentViewCsvUtils.CLIENT_NAME,
						target.getAccount().getClient().getFullName(),
						PaymentViewCsvUtils.TARGET_PRODUCTS,
                        getTargetProducts(target.getId()),
						PaymentViewCsvUtils.TARGET_STARTDATE,
						InternationalisationUtils.getDatetimeFormat().format(new Date(target.getStartDate().getTime())),
						PaymentViewCsvUtils.TARGET_ENDDATE,
						InternationalisationUtils.getDatetimeFormat().format(new Date(target.getEndDate().getTime())),
						PaymentViewCsvUtils.TARGET_AMOUNT,
						target.getTotalTargetCost().toString(),
						PaymentViewCsvUtils.TARGET_AMOUNT_PAID,
						targetAnalytics.getAmountSaved(target.getId()).toString(),
						PaymentViewCsvUtils.TARGET_AMOUNT_REMAINING,
						target.getTotalTargetCost().subtract(targetAnalytics.getAmountSaved(target.getId())).toString(),
						PaymentViewCsvUtils.TARGET_PERCENTAGE,
						targetAnalytics.getPercentageToGo(target.getId()).toString(),
						PaymentViewCsvUtils.TARGET_LAST_AMOUNT_PAID,
						targetAnalytics.getLastAmountPaid(target.getId()).toString(),
						PaymentViewCsvUtils.TARGET_DATE_PAID,
						lastDatePaid,
						PaymentViewCsvUtils.TARGET_MONTHLY_SAVINGS,
						targetAnalytics.getMonthlyAmountSaved().toString(),
						PaymentViewCsvUtils.TARGET_DAYS_REMAINING,
						targetAnalytics.getDaysRemaining(target.getId()).toString(),
						PaymentViewCsvUtils.TARGET_STATUS,
						targetAnalytics.getStatus(target.getId()).toString(),
						PaymentViewCsvUtils.MONTHLY_DUE,
						targetAnalytics.getMonthlyAmountDue().toString(),
						PaymentViewCsvUtils.TARGET_CURRENT_DUE_DATE,
						InternationalisationUtils.getDatetimeFormat().format(new Date(targetAnalytics.getEndMonthInterval().getTime())));
			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}
	}

	private static String getTargetProducts(long tgtId) {
		String neededitems = "";
		List<TargetServiceItem> lstTSI = targetServiceItemDao.getAllTargetServiceItemByTarget(tgtId);
		for(TargetServiceItem tsi: lstTSI){
			if (neededitems.length()==0) {
				neededitems = tsi.getServiceItem().getTargetName();
			} else {
				neededitems = neededitems+", "+tsi.getServiceItem().getTargetName();
			}
		}
		return neededitems;
	}
}
