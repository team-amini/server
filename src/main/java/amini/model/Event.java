package amini.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {

	long timestamp = System.currentTimeMillis();
	String type = "transfer";
	String instrument = "MetaCoin";
	String senderAddress;
	String senderAccount;
	String senderIp;
	String receiverAddress;
	String receiverAccount;
	String receiverIp;
	float amount;
	
}
