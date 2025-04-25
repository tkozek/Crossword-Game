package persistance;

// Classes which implement this interface are able
// to be represented as JSON data
public interface JsonWritable<T> {
    
    T toJson();
}
