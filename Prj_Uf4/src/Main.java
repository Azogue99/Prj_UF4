import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        // Obten el token
        String sToken = getTokenFromJson();

        // Obten todas las cartas (de las diferentes paginas)
        JSONArray cardsArray = fetchAllCardsFromApi(sToken);
        
        // Pasa de JsonArray a ArrayList todas las cartas
        ArrayList<Card> cards = parseCards(cardsArray);

        // Guarda las cartas en un fichero csv
        saveToCSV(cards);
    }

    private static String getTokenFromJson() throws FileNotFoundException {
        File json = new File("token.json");
        Scanner sc = new Scanner(json);
        String token = sc.nextLine();
        sc.close();

        JSONObject tokenJsonObject = new JSONObject(token);
        return tokenJsonObject.getString("access_token");
    }

    private static JSONArray fetchAllCardsFromApi(String token) throws IOException, URISyntaxException {
        JSONArray allCardsArray = new JSONArray();
        int pageCount = 0;
        int currentPage = 1;
        do {
            String apiUrl = "https://us.api.blizzard.com/hearthstone/cards?locale=es_ES&page=" + currentPage + "&pageSize=500&access_token=" + token;
            JSONObject jsonObject = fetchJsonFromApi(apiUrl);
            JSONArray cardsArray = jsonObject.getJSONArray("cards");
            for (int i = 0; i < cardsArray.length(); i++) {
                allCardsArray.put(cardsArray.getJSONObject(i));
            }
            pageCount = jsonObject.getInt("pageCount"); // dime cuantas paginas hay
            currentPage++; // pasa de pagina
        } while (currentPage <= pageCount);
        return allCardsArray;
    }
    

    private static JSONObject fetchJsonFromApi(String apiUrl) throws IOException, URISyntaxException {
        URL url = new URI(apiUrl).toURL();
        URLConnection urlc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        br.close();
        return new JSONObject(responseBuilder.toString());
    }

    private static ArrayList<Card> parseCards(JSONArray cardsArray) {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject cardObj = cardsArray.getJSONObject(i);
            Card card = new Card();
            card.setId(cardObj.optInt("id", -1));
            card.setCollectible(cardObj.optInt("collectible", -1));
            card.setSlug(cardObj.optString("slug", ""));
            card.setClassId(cardObj.optInt("classId", -1));
            card.setMultiClassIds(toArrayList(cardObj.optJSONArray("multiClassIds")));
            card.setSpellSchoolId(cardObj.optInt("spellSchoolId", -1));
            card.setCardTypeId(cardObj.optInt("cardTypeId", -1));
            card.setCardSetId(cardObj.optInt("cardSetId", -1));
            card.setRarityId(cardObj.optInt("rarityId", -1));
            card.setArtistName(cardObj.optString("artistName", ""));
            card.setManaCost(cardObj.optInt("manaCost", -1));
            card.setName(cardObj.optString("name", ""));
            card.setText(cardObj.optString("text", ""));
            card.setImage(cardObj.optString("image", ""));
            card.setImageGold(cardObj.optString("imageGold", ""));
            card.setFlavorText(cardObj.optString("flavorText", ""));
            card.setCropImage(cardObj.optString("cropImage", ""));
            card.setKeywordIds(toArrayList(cardObj.optJSONArray("keywordIds")));
            card.setZilliaxFunctionalModule(cardObj.optBoolean("isZilliaxFunctionalModule", false));
            card.setZilliaxCosmeticModule(cardObj.optBoolean("isZilliaxCosmeticModule", false));
            card.setDuels(parseDuels(cardObj.optJSONObject("duels")));
            card.setCopyOfCardId(cardObj.optInt("copyOfCardId", -1));
            card.setHealth(cardObj.optInt("health", -1));
            card.setAttack(cardObj.optInt("attack", -1));
            card.setMinionTypeId(cardObj.optInt("minionTypeId", -1));
            card.setChildIds(toArrayList(cardObj.optJSONArray("childIds")));
            card.setRuneCost(parseRuneCost(cardObj.optJSONObject("runeCost")));
            cards.add(card);
        }
        return cards;
    }
    
    

    private static Duels parseDuels(JSONObject duelsObj) {
        Duels duels = new Duels();
        if (duelsObj == null) {
            return duels;
        }
        duels.setRelevant(duelsObj.optBoolean("relevant", false));
        duels.setConstructed(duelsObj.optBoolean("constructed", false));
        return duels;
    }

    private static RuneCost parseRuneCost(JSONObject runeCostObj) {
        RuneCost runeCost = new RuneCost();
        if (runeCostObj == null) {
            return runeCost;
        }
        runeCost.setBlood(runeCostObj.getInt("blood"));
        runeCost.setFrost(runeCostObj.getInt("frost"));
        runeCost.setUnholy(runeCostObj.getInt("unholy"));
        return runeCost;
    }

    private static ArrayList<Integer> toArrayList(JSONArray jsonArray) {
        ArrayList<Integer> list = new ArrayList<>();
        if (jsonArray == null) {
            return list;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getInt(i));
        }
        return list;
    }
    

    private static void saveToCSV(ArrayList<Card> cards) throws IOException {
        String csvFilePath = "cards.csv";

        try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
            // Writing header row
            csvWriter.append("Name;Class;ManaCost;Attack;Health;Text\n");

            // Writing data rows
            for (Card card : cards) {
                csvWriter.append(String.format("%s;%d;%d;%d;%d;%s\n",
                        card.getName(), card.getClassId(), card.getManaCost(),
                        card.getAttack(), card.getHealth(), card.getText()));
            }

            System.out.println("Fixer csv creat correctament! [" + csvFilePath + "]");
        }
    }
}
