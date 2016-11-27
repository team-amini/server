package amini.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {

	long timestamp = System.currentTimeMillis();
	String type = "transfer";
	String instrument = "MetaCoin";
	String senderAccount;
	String cityTo;
	String cityFrom;
	long population;
	String receiverAccount;
	String latitudeFrom;
	String longitudeFrom;
	String latitudeTo;
	String longitudeTo;
	boolean insider = false; 
	
	float amount;
	
}
