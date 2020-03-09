package RaptorDMG;

import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JSONfunctions {
    private void checkJSON(String JSONFileName) {
        try (FileReader reader = new FileReader(JSONFileName)){
        } catch (IOException e) {
            Alert IOError = new Alert(Alert.AlertType.ERROR, JSONFileName+" not found\nCreating JSON");
            IOError.show();
            try (FileWriter file = new FileWriter(JSONFileName)) {
                file.write("[]");
                file.flush();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }

    public void combineJSON(String JSONFileName,JSONArray jsonArray) {
        JSONParser jsonParser = new JSONParser();
        JSONArray currentContents = new JSONArray();

        try (FileReader reader = new FileReader(JSONFileName)) {
            Object obj = jsonParser.parse(reader);
            currentContents = (JSONArray) obj;
            currentContents.addAll(jsonArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        writeJSONArray(currentContents,JSONFileName);
    }

    public void appendJSON(String JSONFileName, JSONObject newItem) {
        checkJSON(JSONFileName);
        JSONParser jsonParser = new JSONParser();
        JSONArray currentContents = new JSONArray();

        try (FileReader reader = new FileReader(JSONFileName)) {
            Object obj = jsonParser.parse(reader);
            currentContents = (JSONArray) obj;
            currentContents.add(newItem);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        writeJSONArray(currentContents,JSONFileName);
    }

    public JSONArray readJSON(String JSONFileName) {
        checkJSON(JSONFileName);
        JSONParser jsonParser = new JSONParser();
        JSONArray currentContents = new JSONArray();

        try(FileReader reader = new FileReader(JSONFileName)) {
            Object obj = jsonParser.parse(reader);
            currentContents = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return  currentContents;
    }

    public void saveModification(JSONObject modification, String JSONFileName, int targetIndex) {
        JSONArray targetArray = readJSON(JSONFileName);
        targetArray.set(targetIndex, modification);

        writeJSONArray(targetArray,JSONFileName);
    }
    private void writeJSONArray(JSONArray currentContents, String JSONFileName) {
        try (FileWriter file = new FileWriter(JSONFileName)) {
            file.write(currentContents.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}