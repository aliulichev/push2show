package models;

import java.util.List;

import org.bson.types.ObjectId;

import service.MorphiaObject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {

	@Id
	public ObjectId id;

	public String name = "No Name";

	public String googleRegId;
	
	public String uid;
	
	public String externalId;
	
	public String token;
	
	@Override
	public String toString() {
		return "User [name=" + name + ", googleRegId=" + googleRegId + ", uid="
				+ uid + ", externalId=" + externalId + "]";
	}

	public static User findByUid(String uid){
		return MorphiaObject.datastore.find(User.class).field("uid").equal(uid).get();
	}
	
	public static User findByExternalId(String externalId){
		return MorphiaObject.datastore.find(User.class).field("externalId").equal(externalId).get();
	}
	
	public static List<User> allExcept(String uid) {
		return MorphiaObject.datastore.find(User.class).field("uid").notEqual(uid).asList();
	}
	
	
	
}
