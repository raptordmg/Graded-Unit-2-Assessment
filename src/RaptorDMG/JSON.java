/*
  Name: Stephen Wallace
  Program: CCCP Stock Management for Graded Unit 2
  Date of last modification: 23/04/20
 */

package RaptorDMG;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSON extends JSONfunctions {

  //Declares a couple of variables
  private final String jsonName;
  private final String[] fieldNames;

  /*
      JSON constructor with a parameter for file name and field used
   */
  public JSON(String jsonFileName, String[] fieldNames) {

    //Sets the passed in parameter to the class variables
    this.jsonName = jsonFileName;
    this.fieldNames = fieldNames;
  }

  //Adds a new item to the JSON
  public void createNewItem(String[] fieldInputs) {
    appendJSON(jsonName, input(fieldInputs));
  }

  //Returns the JSONArray from the file
  public JSONArray returnArray() {
    return readJSON(jsonName);
  }

  //Modifies an existing item in the JSONArray
  public void modifyItem(String[] fieldInputs, int itemIndex) {
    saveModification(inputModify(fieldInputs, itemIndex), jsonName, itemIndex);
  }

  /*
      Adds the field inputs to the proper fields
   */
  private JSONObject input(String[] fieldInputs) {

    //Creates a new JSONObject
    JSONObject input = new JSONObject();

    //For loop to add each input to the appropriate field
    for (int i = 0; i < fieldInputs.length; i++) {
      input.put(fieldNames[i], fieldInputs[i]);
    }

    return input;
  }

  /*
      Modifies an existing item in the JSON
   */
  private JSONObject inputModify(String[] fieldInputs, int targetIndex) {

    //Creates a copy of the current values
    JSONObject target = (JSONObject) returnArray().get(targetIndex);

    //Creates a JSONObject
    JSONObject input = new JSONObject();

    //For loop that overwrites each of the field inputs with the one provided
    for (int i = 0; i < fieldInputs.length; i++) {
      input.put(fieldNames[i], fieldInputs[i]);
    }

    //Adds the correct customer/item number to the modified JSONObject
    if (input.containsKey("itemNum")) {
      input.put("itemNum", target.get("itemNum"));
    } else if (input.containsKey("custID")) {
      input.put("custID", target.get("custID"));
    }

    return input;
  }
}