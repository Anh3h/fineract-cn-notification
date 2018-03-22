package io.mifos.notification.service.listner;

import io.mifos.core.lang.config.TenantHeaderFilter;
import io.mifos.customer.api.v1.client.CustomerManager;
import io.mifos.customer.api.v1.domain.ContactDetail;
import io.mifos.customer.api.v1.domain.Customer;
import io.mifos.notification.api.v1.events.NotificationEventConstants;
import io.mifos.notification.service.smtp.EmailSender;
import io.mifos.notification.service.twilio.SMSSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class CustomerListener {

	private CustomerManager customerManager;
	private SMSSender smsSender;
	private EmailSender emailSender;

	@Autowired
	public CustomerListener( final CustomerManager customerManager, SMSSender smsSender,
		final EmailSender emailSender ) {
		this.customerManager = customerManager;
		this.smsSender = smsSender;
		this.emailSender = emailSender;
	}

	@JmsListener(
			destination = NotificationEventConstants.CUSTOMER_DESTINATION,
			selector = NotificationEventConstants.SELECTOR_POST_CUSTOMER
	)
	public void customerCreatedEvent(@Header(TenantHeaderFilter.TENANT_HEADER) final String tenant,
			final String payload) {
		System.out.println(payload);
		System.out.println(tenant);

		Customer customer = customerManager.findCustomer(payload);
		if (customer.getContactDetails().size() > 0) {
			customer.getContactDetails().forEach(contactDetail -> {
				if (contactDetail.getType().equals(ContactDetail.Type.PHONE)) {
					String receiverNumber = customer.getContactDetails().get(0).getValue();
					smsSender.sendSMS(receiverNumber, "Account created");
				} else if (contactDetail.getType().equals(ContactDetail.Type.EMAIL)) {
					String email = customer.getContactDetails().get(0).getValue();
					emailSender.sendEmail(email, "Account created", "Account created");
				}
			});
		}

	}

}
