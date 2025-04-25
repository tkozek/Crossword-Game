package persistance;

// Classes which implement this interface are able
// to be represented as JSON data
public interface JsonWritable<T> {
    
    //EFFECTS: returns this as JSON object
    T toJson();
}
