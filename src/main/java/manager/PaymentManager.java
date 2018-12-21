package manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.InvoiceItem;
import com.paypal.api.payments.MerchantInfo;
import com.paypal.api.payments.Participant;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Component
public class PaymentManager {
	
	@Autowired
	private APIContext contextWired;

	private static APIContext context;

	@PostConstruct     
	private void initStaticFields () {
		context = contextWired;
	}
	
	public static void sendInvoice(String email, String amount) {
		
		MerchantInfo merchInfo = new MerchantInfo();
		BillingInfo billInfo = new BillingInfo();
		Participant participant = new Participant();
		InvoiceItem item = new InvoiceItem();
		Currency currency = new Currency();
		
		merchInfo.setFirstName("Oleh");
		merchInfo.setLastName("Kepsha");
		merchInfo.setBusinessName("Tringo");
		merchInfo.setEmail("oleh.kepsha-facilitator@mycit.ie");
		
		billInfo.setFirstName("Customer");
		billInfo.setEmail(email);
		
		participant.setEmail(email);
		
		currency.setValue(amount);
		currency.setCurrency("EUR");
		
		item.setUnitPrice(currency);
		item.setDescription("Tringo Journey Payment");
		item.setQuantity(1);
		item.setName("Tringo Payment");
		
		List<BillingInfo> billInfoList = new ArrayList<BillingInfo>();
		List<Participant> pList = new ArrayList<Participant>();
		List<InvoiceItem> items = new ArrayList<InvoiceItem>();
		
		items.add(item);
		billInfoList.add(billInfo);
		pList.add(participant);
		
		Invoice invoice = new Invoice();
		try {
			invoice.setMerchantInfo(merchInfo);
			invoice.setBillingInfo(billInfoList);
			invoice.setCcInfo(pList);
			invoice.setItems(items);
			invoice = invoice.create(context);
			invoice.send(context);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
	}
}
