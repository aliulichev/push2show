package controllers;

import models.Item;
import play.mvc.Controller;
import play.mvc.Result;
import service.MorphiaObject;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
    	MorphiaObject.datastore.save(new Item());
        return ok(index.render("Your new application is ready."));
    }
  
}
