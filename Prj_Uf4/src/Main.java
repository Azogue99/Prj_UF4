import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        // Fetching token
        String sToken = getTokenFromJson();

        // Fetching card data from API
        JSONArray cardsArray = fetchAllCardsFromApi(sToken);
        
        // Parsing card data
        ArrayList<Card> cards = parseCards(cardsArray);

        // Save the data into CSV file
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
            pageCount = jsonObject.getInt("pageCount");
            currentPage++;
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
            card.setId(cardObj.getInt("id"));
            card.setCollectible(cardObj.getInt("collectible"));
            card.setSlug(cardObj.getString("slug"));
            card.setClassId(cardObj.getInt("classId"));
            card.setMultiClassIds(toArrayList(cardObj.getJSONArray("multiClassIds")));
            // Check if "spellSchoolId" exists before trying to retrieve its value
            if (cardObj.has("spellSchoolId")) {
                card.setSpellSchoolId(cardObj.getInt("spellSchoolId"));
            }
            card.setCardTypeId(cardObj.getInt("cardTypeId"));
            card.setCardSetId(cardObj.getInt("cardSetId"));
            card.setRarityId(cardObj.getInt("rarityId"));
            card.setArtistName(cardObj.getString("artistName"));
            card.setManaCost(cardObj.getInt("manaCost"));
            card.setName(cardObj.getString("name"));
            card.setText(cardObj.getString("text"));
            card.setImage(cardObj.getString("image"));
            card.setImageGold(cardObj.getString("imageGold"));
            card.setFlavorText(cardObj.getString("flavorText"));
            card.setCropImage(cardObj.getString("cropImage"));
            card.setKeywordIds(toArrayList(cardObj.getJSONArray("keywordIds")));
            card.setZilliaxFunctionalModule(cardObj.getBoolean("isZilliaxFunctionalModule"));
            card.setZilliaxCosmeticModule(cardObj.getBoolean("isZilliaxCosmeticModule"));
            card.setDuels(parseDuels(cardObj.getJSONObject("duels")));
            card.setCopyOfCardId(cardObj.getInt("copyOfCardId"));
            card.setHealth(cardObj.getInt("health"));
            card.setAttack(cardObj.getInt("attack"));
            card.setMinionTypeId(cardObj.getInt("minionTypeId"));
            card.setChildIds(toArrayList(cardObj.getJSONArray("childIds")));
            card.setRuneCost(parseRuneCost(cardObj.getJSONObject("runeCost")));
            cards.add(card);
        }
        return cards;
    }
    

    private static Duels parseDuels(JSONObject duelsObj) {
        Duels duels = new Duels();
        duels.setRelevant(duelsObj.getBoolean("relevant"));
        duels.setConstructed(duelsObj.getBoolean("constructed"));
        return duels;
    }

    private static RuneCost parseRuneCost(JSONObject runeCostObj) {
        RuneCost runeCost = new RuneCost();
        runeCost.setBlood(runeCostObj.getInt("blood"));
        runeCost.setFrost(runeCostObj.getInt("frost"));
        runeCost.setUnholy(runeCostObj.getInt("unholy"));
        return runeCost;
    }

    private static ArrayList<Integer> toArrayList(JSONArray jsonArray) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getInt(i));
        }
        return list;
    }

    private static void saveToCSV(ArrayList<Card> cards) throws IOException {
        String csvFilePath = "cards.csv";

        try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
            // Writing header row
            csvWriter.append("Name,Class,ManaCost,Attack,Health,Text\n");

            // Writing data rows
            for (Card card : cards) {
                csvWriter.append(String.format("%s,%d,%d,%d,%d,%s\n",
                        card.getName(), card.getClassId(), card.getManaCost(),
                        card.getAttack(), card.getHealth(), card.getText()));
            }

            System.out.println("CSV file " + csvFilePath + " created successfully!");
        }
    }
}
