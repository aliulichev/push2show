package controllers;

import static play.libs.Json.toJson;
import models.Post;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.data.Form;
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

	public static Result updateUser() {
		

		String userId = request().getHeader("x-referer");
		String googleId = Form.form().bindFromRequest().get("googleId");
		User user = null;

		if (userId != null) {
			user = User.findByUid(userId);
		}

		Logger.info("Updating user with user id:" + userId + " and googleId:"
				+ googleId);

		if (user == null) {
			Logger.info("User with id:" + userId + " not found");
			user = new User();
			user.uid = userId;
			Logger.info("User created with googleId:" + googleId);
		} else {
			Logger.info("User found:" + user);
		}

		user.googleRegId = googleId;
		MorphiaObject.datastore.save(user);
		Logger.info("User updated successfully");
		return ok();
	}

	public static Result createPost() {
		
		GCMService googleService = new GCMService();
		JsonNode node = request().body().asJson();
		String userId = request().getHeader("x-referer");
		Logger.info(node.findValuesAsText("to").toString());
		String url = Form.form().bindFromRequest().get("url");
		User user = User.findByUid(userId);
		Post post = new Post();
		post.from = user.uid;
		post.fromName = user.name;
		post.url = url;
		for (JsonNode to : node.get("to")) {
			post.to.add(to.asText().trim());
		}

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
		return ok(toJson(User.allExcept(request().getHeader("x-referer"))));
	}

	public static Result getPost(String postId) {
		return ok(toJson(Post.findById(postId)));
	}

}
