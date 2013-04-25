import java.net.UnknownHostException;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import service.MorphiaObject;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class Global extends GlobalSettings {

    public void onStart(final Application app) {
        super.beforeStart(app);
        Logger.debug("** onStart **");
        try {
        	Logger.debug(Play.application().configuration().getString("mongo.port"));
            MorphiaObject.mongo = new Mongo(Play.application().configuration().getString("mongo.server"),
            		Play.application().configuration().getInt("mongo.port"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MorphiaObject.morphia = new Morphia();
        MorphiaObject.datastore = MorphiaObject.morphia.createDatastore(MorphiaObject.mongo, "push2show");
        MorphiaObject.morphia.createDatastore(MorphiaObject.mongo,
        		Play.application().configuration().getString("mongo.db"),
        		Play.application().configuration().getString("mongo.user"),
        		Play.application().configuration().getString("mongo.password").toCharArray());
        MorphiaObject.datastore.ensureIndexes();
        MorphiaObject.datastore.ensureCaps();

        Logger.debug("Morphia datastore: " + MorphiaObject.datastore.getDB());
    }

}