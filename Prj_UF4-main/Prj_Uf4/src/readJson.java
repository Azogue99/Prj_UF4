

import java.util.Arrays;

public class readJson {
    public static void readToken(String jsonString) {
        // elimina inicio y final
        jsonString = jsonString.replace("{", ""); 
        jsonString = jsonString.replace("}", "");

        // separa por las comas
        String[] JsonArray = jsonString.split(",");

        
        // print
        String stringArray = Arrays.toString(JsonArray);
        System.out.println(stringArray);
    }
}