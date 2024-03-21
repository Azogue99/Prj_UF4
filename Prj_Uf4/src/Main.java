import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        String token = "EUnWcbuuw5cxlNGcaO8z1RkSIo2PEo5jdW";

        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + token;

        URL url = new URI(CONNECT_API_URL).toURL();

        URLConnection urlc = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String json = sb.toString();
        System.out.println(json);

        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> responseMap = new Gson().fromJson(json, mapType);

        // Now you can work with the responseMap to access the data
        // Example: String cardName = (String) responseMap.get("name");

    }
}
