package RaptorDMG;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSON extends JSONfunctions {
    private String jsonName;
    private String[] fieldNames;

    public JSON(String jsonFileName, String[] fieldNames) {
        this.jsonName = jsonFileName;
        this.fieldNames = fieldNames;
    }

    public void createNewItem(String[] fieldInputs) { appendJSON(jsonName, input(fieldInputs)); }

    public JSONArray returnArray() { return readJSON(jsonName); }

    public void modifyItem(String[] fieldInputs, int itemIndex) { saveModification(input(fieldInputs),jsonName,itemIndex); }

    private JSONObject input(String[] fieldInputs) {
        JSONObject input = new JSONObject();
        for (int i = 0; i < fieldInputs.length; i++) {
            input.put(fieldNames[i], fieldInputs[i]);
        }

        return input;
    }
}