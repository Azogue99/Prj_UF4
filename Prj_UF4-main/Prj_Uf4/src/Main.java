import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String rutaArc="generarCode.bat";
        ProcessBuilder jsontoken= new ProcessBuilder("cmd.exe","/c",rutaArc);
        try {
            Process exe= jsontoken.start();
            int compro=exe.waitFor();
            if (compro==0) {
                System.out.println("se ejcuto");
            }else{
                System.out.println("error");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        File json= new File("token.json");
        Scanner sc=new Scanner(json);
        String token = sc.nextLine();
        String subString= token.substring(17,51);
        System.out.println(subString);
        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + subString;
        System.out.println(CONNECT_API_URL);
        URL url = new URI(CONNECT_API_URL).toURL();

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
}
