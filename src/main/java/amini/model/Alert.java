package amini.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Alert {

	String id;
	String name;
	String description;
	String epl;
	boolean disabled;
	
}
