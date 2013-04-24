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
        	Logger.debug(Play.application().configuration().getString("mongoPort"));
            MorphiaObject.mongo = new Mongo("127.0.0.1", Play.application().configuration().getInt("mongoPort"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MorphiaObject.morphia = new Morphia();
        MorphiaObject.datastore = MorphiaObject.morphia.createDatastore(MorphiaObject.mongo, "push2show");
        MorphiaObject.datastore.ensureIndexes();
        MorphiaObject.datastore.ensureCaps();

        Logger.debug("Morphia datastore: " + MorphiaObject.datastore.getDB());
    }

}