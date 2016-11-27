package amini.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {
	
	/**
	 * 		String city = fields[i++];
			String population = fields[i++];
			String latitude = fields[i++];
			String longitude = fields[i++];
			String latitudeFrom = fields[i++];
			String longitudeFrom = fields[i++];
			String to = fields[i++];
			String from = fields[i++];
			String value = fields[i++];
			String time = fields[i++];
			String latitudeTo = fields[i++];
			String longitudeTo = fields[i++];
	 */

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
	
	float amount;
	
}
