package model;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    
    private static Map<Character, Integer> letterPointsMap = new HashMap<>();

    // MODIFIES: this
    // EFFECTS: Adds Tile Point Values to 
    //   Point map for Letters N, O,....,Z,_,
    //     inclusive
    static {
        letterPointsMap.put('A', 1); 
        letterPointsMap.put('B', 3); 
        letterPointsMap.put('C', 3);
        letterPointsMap.put('D', 2); 
        letterPointsMap.put('E', 1); 
        letterPointsMap.put('F', 4);
        letterPointsMap.put('G', 2); 
        letterPointsMap.put('H', 4); 
        letterPointsMap.put('I', 1);
        letterPointsMap.put('J', 8); 
        letterPointsMap.put('K', 5); 
        letterPointsMap.put('L', 1);
        letterPointsMap.put('M', 3);
        letterPointsMap.put('N', 1); 
        letterPointsMap.put('O', 1);
        letterPointsMap.put('P', 3); 
        letterPointsMap.put('Q', 10); 
        letterPointsMap.put('R', 1);
        letterPointsMap.put('S', 1); 
        letterPointsMap.put('T', 1); 
        letterPointsMap.put('U', 1);
        letterPointsMap.put('V', 4); 
        letterPointsMap.put('W', 4); 
        letterPointsMap.put('X', 8);
        letterPointsMap.put('Y', 4); 
        letterPointsMap.put('Z', 10); 
        letterPointsMap.put('-', 0);
    }



    public static int getLetterPoints(char letter) {
        return letterPointsMap.get(letter);
    }
}
