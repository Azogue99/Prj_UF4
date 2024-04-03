import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

import org.json.*;

// import readJson.readToken;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        
        // Llamamos al fichero generarCode.bat para generar el token token.json.
        String rutaBat = "generarCode.bat";
        ProcessBuilder jsonTokenGetter = new ProcessBuilder("cmd.exe", "/c", rutaBat); // lo ejecutamos con cmd.
        Process exe = jsonTokenGetter.start(); 

        // Se comprueba que se haya ejecutado correctamente
        if (exe.waitFor() == 0) { // el waitFor tiene la capacidad de lanzar una InterruptedExeption
            System.out.println("ok");
        }else{
            System.out.println("no s'ha executat el fitxer .bat correctament");
        }
    
        // Abrimos el fichero token.json y lo leemos
        File json = new File("token.json");
        Scanner sc = new Scanner(json);
        String token = sc.nextLine();
        sc.close();

        readToken(token);
        System.out.println(token);

        // hay que fixear esta expresion, no podemos iterar sobre caracteres
        String subString = token.substring(17,51);
        System.out.println(subString);

        // esto es correcto
        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + subString;
        System.out.println(CONNECT_API_URL);
        URL url = new URI(CONNECT_API_URL).toURL(); // tiene la capacidad de lanzar una URISyntaxException

        URLConnection urlc = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        br.close();


        String jsonResponse = responseBuilder.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        
        JSONArray cardsArray = jsonObject.getJSONArray("cards");

        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject card = cardsArray.getJSONObject(i);
            String cardName = card.getString("name");
            System.out.println("Card Name: " + cardName);
        }
    }


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
