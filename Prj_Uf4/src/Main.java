import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        String token = "EUyyFWixG6ynnTq9smPseIMB9oExRRz8U5";

        String CONNECT_API_URL = "https://us.api.blizzard.com/hearthstone/cards?locale=en_US&access_token=" + token;

        URL url = new URI(CONNECT_API_URL).toURL();

        URLConnection urlc = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String fichero = br.readLine();
        br.close();
        System.out.println(fichero);
    }
}