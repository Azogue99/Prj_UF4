import java.util.HashMap;
import java.util.Map;

public class JsonStringToMap {
    public static Map<String, Object> jsonStringToMap(String jsonString) throws org.json.JSONException {
        Map<String, Object> keys = new HashMap<>();

        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
        java.util.Iterator<?> keySet = jsonObject.keys();

        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            Object value = jsonObject.get(key);
            System.out.print("\n Key : " + key);
            if (value instanceof org.json.JSONObject) {
                System.out.println("Incoming value is of JSONObject: ");
                keys.put(key, jsonStringToMap(value.toString()));
            } else if (value instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = jsonObject.getJSONArray(key);
                keys.put(key, jsonArray.toList());
            } else {
                keys.put(key, value);
            }
        }
        return keys;
    }
}
