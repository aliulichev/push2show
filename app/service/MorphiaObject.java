package service;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 3/25/13
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class MorphiaObject {
    static public Mongo mongo;
    static public Morphia morphia;
    static public Datastore datastore;
}
