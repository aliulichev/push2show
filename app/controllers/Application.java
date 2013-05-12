package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;

import models.Post;
import models.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.GCMService;
import service.MorphiaObject;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		long userCount = MorphiaObject.datastore.find(User.class).countAll();
		long postCount = MorphiaObject.datastore.find(Post.class).countAll();
		return ok(index.render("Posts: " + postCount + " Users: " + userCount));
	}

	// Still ugly
	public static Result updateUser() {
		String userId = currentUid();
		String googleId = param("googleId");
		Logger.info("Updating user with user id:" + userId + " and googleId:"
				+ googleId);

		User user = null;
		if (userId != null) {
			user = User.findByUid(userId);
			if (user == null) {
				user = new User();
			}
		}

		user.uid = userId;
		user.googleRegId = googleId;
		MorphiaObject.datastore.save(user);
		Logger.info("User updated: " + user);

		return ok();
	}

	public static Result createPost() {
		GCMService googleService = new GCMService();
		User user = User.findByUid(currentUid());
		Post post = new Post(recipients(), user, param("url"));
		Logger.info("Creating post :" + post);
		MorphiaObject.datastore.save(post);
		try {
			googleService.sendPost(post);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			return internalServerError("Oops!");
		}
		return ok(post.id.toString());
	}

	public static Result allUsers() {
		return ok(toJson(User.allExcept(currentUid())));
	}

	public static Result getPost(String postId) {
		return ok(toJson(Post.findById(postId)));
	}

	private static String currentUid() {
		return request().getHeader("x-referer");
	}

	private static String param(String param) {
		return Form.form().bindFromRequest().get(param);
	}

	private static List<String> recipients() {
		List<String> recipients = new ArrayList<String>();
		JsonNode node = request().body().asJson();
		for (JsonNode to : node.get("to")) {
			recipients.add(to.asText().trim());
		}

		return recipients;
	}
	
	public static Result auth(){
		ObjectNode result = Json.newObject();
		result.put("access_token", "e765664d9e37eef3ec6527b55abbb16e3f9ef01c91ab843e7613a41c98c7b95e");
		result.put("token_type", "bearer");
		result.put("expires_in", "7200");
		result.put("refresh_token", "null");
		result.put("scope", "public private");
		return ok(result);
	}

}
