import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        // Llamamos al fichero generarCode.bat para generar el token token.json.
        String rutaBat = "generarCode.bat";
        ProcessBuilder jsonTokenGetter = new ProcessBuilder("cmd.exe", "/c", rutaBat); // lo ejecutamos con cmd.
        Process exe = jsonTokenGetter.start(); 

        // Se comprueba que se haya ejecutado correctamente
        if (exe.waitFor() == 0) { // el waitFor tiene la capacidad de lanzar una InterruptedExeption
            System.out.println("OK!");
        }else{
            System.out.println("ERROR: no s'ha pogut executar el fitxer .bat correctament");
        }
    
        // Leemos del fichero json
        File json = new File("token.json");
        Scanner sc = new Scanner(json);
        String token = sc.nextLine();
        sc.close();

        // Obten el token del json
        JSONObject tokenJsonObject = new JSONObject(token);
        String sToken = tokenJsonObject.getString("access_token");
        System.out.println("TOKEN: " + sToken);

        // esto es correcto
        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=es_ES&page=1&pageSize=500&access_token=" + sToken;
        System.out.println("CONNECTANT: " + CONNECT_API_URL);
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
        System.out.println(cardsArray.length());
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
