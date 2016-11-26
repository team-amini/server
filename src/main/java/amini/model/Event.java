package amini.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {

	long timestamp;
	String instrument;
	String data;
	
}
