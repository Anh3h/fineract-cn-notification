package io.mifos.notification.service.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSSender {

	@Value("${twilio.accountSID}")
	public static String accountSID;

	@Value("${twilio.authToken}")
	public static String authToken;

	@Value("${twilio.senderNumber}")
	public static String sender;

	public void sendSMS(String receiver, String body) {

		Twilio.init(accountSID, authToken);

		Message message = Message
				.creator(new PhoneNumber(receiver), new PhoneNumber(sender), body)
				.create();

		System.out.println(message.getSid());

	}
}
