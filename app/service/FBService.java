package service;

import play.Logger;
import play.api.PlayException;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

public class FBService {

	public static User getFbUser(String fbToken) {
		try {
			FacebookClient facebookClient = new DefaultFacebookClient(fbToken);
			return facebookClient.fetchObject("me", User.class);
		} catch (FacebookOAuthException oae) {
			Logger.error(oae.getErrorMessage());
			throw  new PlayException("Facebook user with token " + fbToken , oae.getMessage());
		}
	}
}
