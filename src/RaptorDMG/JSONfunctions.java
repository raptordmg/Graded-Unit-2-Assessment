/*
  Name: Stephen Wallace
  Program: CCCP Stock Management for Graded Unit 2
  Date of last modification: 23/04/20
 */

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

    /*
        Checks the JSON is valid
     */
    private void checkJSON(String JSONFileName) {

        //Try to open the JSON file
        try (FileReader reader = new FileReader(JSONFileName)){
        }

        //If file not found display message then create new file
        catch (IOException e) {
            Alert IOError = new Alert(Alert.AlertType.ERROR, JSONFileName+" not found\nCreating JSON");
            IOError.show();
            try (FileWriter file = new FileWriter(JSONFileName)) {
                file.write("[]");
                file.flush();
            }

            //Prints an error to console if file can't be created
            catch (IOException f) {
                f.printStackTrace();
            }
        }
    }

    /*
        Adds a JSONObject to an existing JSON file
     */
    public void appendJSON(String JSONFileName, JSONObject newItem) {

        //Checks the JSON file
        checkJSON(JSONFileName);

        //Creates a JSONParser
        JSONParser jsonParser = new JSONParser();

        //Creates a JSONArray
        JSONArray currentContents = new JSONArray();

        //Try to read the JSON file
        try (FileReader reader = new FileReader(JSONFileName)) {

            //Create an Object from the JSON file
            Object obj = jsonParser.parse(reader);

            //Casts the object to the JSONArray
            currentContents = (JSONArray) obj;

            //Adds the new item to the JSONArray
            currentContents.add(newItem);
        }

        //If the file can't be opened or is invalid print error to console
        catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        //Write the JSONArray to file
        writeJSONArray(currentContents,JSONFileName);
    }

    /*
        Reads a JSON file
     */
    public JSONArray readJSON(String JSONFileName) {

        //Checks the JSON file
        checkJSON(JSONFileName);

        JSONParser jsonParser = new JSONParser();
        JSONArray currentContents = new JSONArray();

        //Tries to open the JSON file and reads the contents to a JSONArray
        try(FileReader reader = new FileReader(JSONFileName)) {
            Object obj = jsonParser.parse(reader);
            currentContents = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return  currentContents;
    }

    /*
        Replaces an item in the JSON file with the edited value
     */
    public void saveModification(JSONObject modification, String JSONFileName, int targetIndex) {

        //Gets the JSONArray
        JSONArray targetArray = readJSON(JSONFileName);

        //Replaces the item to be modified
        targetArray.set(targetIndex, modification);

        //Writes changes to file
        writeJSONArray(targetArray,JSONFileName);
    }

    /*
        Writes the JSONArray to the JSON file
     */
    private void writeJSONArray(JSONArray currentContents, String JSONFileName) {

        //Tries to open the JSON file
        try (FileWriter file = new FileWriter(JSONFileName)) {

            //Writes the JSONArray to the JSON file
            file.write(currentContents.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}