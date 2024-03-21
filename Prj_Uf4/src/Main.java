import java.io.*;
import java.net.*;
import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        String token = "EUnWcbuuw5cxlNGcaO8z1RkSIo2PEo5jdW";

        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + token;

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
            String cardType = card.getString("type");
            System.out.println("Card Name: " + cardName + ", Type: " + cardType);
        }
    }
}
