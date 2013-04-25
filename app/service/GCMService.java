package service;

import java.io.IOException;

import models.Post;
import models.User;
import play.Logger;
import play.Play;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GCMService {
	
	Sender sender = new Sender(key());

	public void sendPost(Post post) throws IOException {
		// Very ugly but it might work
		for (String id : post.to) {
			User recipient = User.findByUid(id);
			Logger.info("Sending push notification to " + recipient.googleRegId);
			Message message = new Message.Builder().
					addData("postId", post.id.toString())
					.addData("fromName", post.fromName).collapseKey("push2show").build();
			Result result = sender.send(message, recipient.googleRegId, 5);
			Logger.info("Message sent :" + result.getMessageId());
		}
	}

	private static String key() {
		return Play.application().configuration().getString("apikey").trim();
	}
	
	

}
