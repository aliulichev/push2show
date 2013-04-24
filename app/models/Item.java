package models;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;

public class Item {
	@Id
	public ObjectId id;
}
