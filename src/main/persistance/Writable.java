package persistance;

import org.json.JSONObject;

// Classes which implement this interface are able
// to be represented as JSON data
public interface Writable {
    
    //EFFECTS: returns this as JSON object
    JSONObject toJson();
}
