package persistance;

import model.ScrabbleGame;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON 
// representation of Scrabble game to file
// Citation: Based on WorkRoom example on edX
public class JsonWriter {
    
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {

    }

    // MODIFIES: this
    // EFFECTS: opens writer, throws FileNotFoundException
    // if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {

    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of 
    // Scrabble Game to file
    public void write(ScrabbleGame game) {

    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {

    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {

    }

}
