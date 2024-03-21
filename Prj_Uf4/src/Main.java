import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.common.reflect.TypeToken;
import java.lang.reclect.Type;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        String token = "EUnWcbuuw5cxlNGcaO8z1RkSIo2PEo5jdW";

        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + token;

        URL url = new URI(CONNECT_API_URL).toURL();

        URLConnection urlc = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String fichero = br.readLine();
        br.close();
        System.out.println(fichero);


        Type mapType = new TypeToken<Map<String, Map>>(){}.getType();
        Map<String, String[]> son = new Gson().fromJson(fichero, mapType);


    }
}