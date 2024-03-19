import java.io.*;
import java.net.*;

public class RestApi {

    public static void main(String[] args) throws IOException {

        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards/678?locale=en_US";
        String CONNECT_API_KEY = "EUyyFWixG6ynnTq9smPseIMB9oExRRz8U5";

        URL url = new URL(CONNECT_API_URL);
        URLConnection urlc = url.openConnection();
        urlc.setRequestProperty("Authorization", "Key " + CONNECT_API_KEY);

        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String l = null;
        while ((l=br.readLine())!=null) {
            System.out.println(l);
        }
        br.close();
    }
}