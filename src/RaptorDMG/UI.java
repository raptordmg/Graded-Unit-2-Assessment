/*
  Name: Stephen Wallace
  Program: CCCP Stock Management for Graded Unit 2
  Date of last modification: 23/04/20
 */

package RaptorDMG;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UI extends Application {

  //Create the GridPane for the stock grid
  private GridPane stockList = new GridPane();

  /*
      This is the method that starts the program and calls the UI
  */
  public void start(Stage stage) {
    //Creates the BorderPane
    BorderPane programUI = new BorderPane();

    //Creates a FlowPane and adds more buttons
    FlowPane menuBar = setButtons();

    //Places the menu bar at the top of the UI
    programUI.setTop(menuBar);

    //Places the stock list in the center of the page below the menubar
    programUI.setCenter(addStockList());

    //Creates a scene and gives it the BorderPane
    Scene scene = new Scene(programUI, 660, 700);

    //Assigns the standard.css to the scene
    scene.getStylesheets().add("standard.css");

    //Sets the title of the stage
    stage.setTitle("CCCP Inventory Management");

    //Adds the scene to the stage
    stage.setScene(scene);

    //Displays the stage
    stage.show();
  }

  /*
      Creates the buttons used in the BorderPane
   */
  private FlowPane setButtons() {

    //Creates an array of buttons
    Button[] menuButtons = new Button[]{
        new Button("Create Invoice"),
        new Button("View Invoice"),
        new Button("Manage Stock"),
        new Button("Manage Customers"),
        new Button("View Customers"),
        new Button("Help"),
        new Button("About"),
    };

    //Creates a FlowPane
    FlowPane menuBar = new FlowPane();

    //Adds padding to the outside of the menu bar
    menuBar.setPadding(new Insets(10, 10, 10, 10));

    //Adds a stylesheet identifier to the menu bar
    menuBar.getStyleClass().add("menuBar");

    //Sets a horizontal gap between buttons
    menuBar.setHgap(10);

    //Sets the vertical gap between buttons
    menuBar.setVgap(5);

    //Adds the buttons from the menuButtons array to the menu bar
    menuBar.getChildren().addAll(menuButtons);

    //Assigns actions to the buttons
    menuButtons[0].setOnAction(event -> createInvoice());
    menuButtons[1].setOnAction(event -> viewInvoice());
    menuButtons[2].setOnAction(event -> stockManagement());
    menuButtons[3].setOnAction((event -> customerManagement()));
    menuButtons[4].setOnAction(event -> viewCustomers());
    menuButtons[5].setOnAction(event -> displayHelp());
    menuButtons[6].setOnAction(event -> displayAbout());

    return menuBar;
  }

  /*
      This method creates and saves an invoice
   */
  private void createInvoice() {

    //Creates a GridPane for the menu
    GridPane createInvoiceMenu = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    createInvoiceMenu.getStyleClass().add("background");

    //Sets vertical gap between grids
    createInvoiceMenu.setVgap(10);

    //Sets horizontal gap between grids
    createInvoiceMenu.setHgap(10);

    //Creates a GridPane for the items
    GridPane reportItemGrid = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    reportItemGrid.getStyleClass().add("background");

    //Sets a horizontal gap between Items in the GridPane
    reportItemGrid.setHgap(20);

    //Sets space around the edge of the GridPanes
    reportItemGrid.setPadding(new Insets(10, 10, 10, 10));
    createInvoiceMenu.setPadding(new Insets(10, 10, 10, 10));

    //Creates Text with the field names
    Text[] fieldNames = new Text[]{
        new Text("Customer: "),
        new Text("Item(s): "),
        new Text("Shipping method: "),
        new Text("Payment Method: ")
    };

    //Creates a String ArrayList of item names
    ArrayList<String> itemNames = new ArrayList<>();

    //Creates an integer ArrayList of numbers
    ComboBox<Integer> quantity = new ComboBox<>();

    //Creates a String comboBox of items
    ComboBox<String> items = new ComboBox<>();

    //Creates a JSON object and loads the stock file
    JSON stockJSON = loadStock();

    //For loop to add each item from the file to the itemNames ArrayList
    for (int i = 0; i < stockJSON.returnArray().size(); i++) {

      //Takes a single item from the file
      JSONObject tempItem = (JSONObject) stockJSON.returnArray().get(i);

      //Takes a field from the JSON object
      itemNames.add(tempItem.get("itemName").toString());
    }

    //Adds the items from the item name ArrayList to the items comboBox
    items.getItems().addAll(itemNames);

    //Clears the itemName ArrayList
    itemNames.clear();

    //Triggers an Action when an item is selected
    items.setOnAction(event -> {

      //Clears the values in the quantity ArrayList
      quantity.getItems().clear();

      //Return the JSONObject for the item selected by the user
      JSONObject tempItem = (JSONObject) stockJSON.returnArray()
          .get(items.getSelectionModel().getSelectedIndex());

      //Creates an Integer ArrayList for the quantities
      ArrayList<Integer> quantities = new ArrayList<>();

      //For loop to get the stock level from the JSONObject
      for (int i = 0; i < Integer.parseInt(tempItem.get("itemUnits").toString()); i++) {
        quantities.add(i + 1);
      }

      //Adds the quantities to the quantity comboBox
      quantity.getItems().addAll(quantities);

      //Clears the quantity ArrayList
      quantities.clear();
    });

    //Creates a ComboBox of Strings for the shipping method
    ComboBox<String> shippingMethod = new ComboBox<>();

    //Adds Strings to shipping methods
    shippingMethod.getItems().addAll("Collection", "Post");

    //Select the first item in the shipping method ComboBox
    shippingMethod.getSelectionModel().selectFirst();

    //Creates a ComboBox of Strings for the payment method
    ComboBox<String> paymentMethod = new ComboBox<>();

    //Adds Strings to payment methods
    paymentMethod.getItems().addAll("Cash", "Card", "Paypal");

    //Select the first item in the payment method ComboBox
    paymentMethod.getSelectionModel().selectFirst();

    //Creates a JSON object that loads the customer file
    JSON customerJSON = loadCustomer();

    //Create a new customers ComboBox
    ComboBox<String> customers = new ComboBox<>();

    //Adds items to the customers ComboBox using customer JSON
    custComboBox(customers, customerJSON);

    //Creates a new JSONArray
    JSONArray itemsSelected = new JSONArray();

    //Creates a new button to add an item
    Button addItem = new Button("Add Item");

    //Triggers an action when the button is clicked
    addItem.setOnAction(event -> {

      //Checks to make sure required fields are selected
      if (!items.getSelectionModel().isEmpty() && !quantity.getSelectionModel().isEmpty()) {

        //Gets a JSONObject from a JSONArray
        JSONObject item = (JSONObject) stockJSON.returnArray()
            .get(items.getSelectionModel().getSelectedIndex());

        //Change the number of itemUnits to the quantity selected by the User
        item.replace("itemUnits", quantity.getSelectionModel().getSelectedIndex() + 1);

        //Adds the JSONObject item to the reportItemGrid
        addItemToReportPreview(reportItemGrid, item);

        //Saves the item to the JSON and subtracts the selected in the invoice
        saveItems(item, itemsSelected);
      } else {

        //Creates an alert saying "No items have been added to the invoice
        Alert noItems = new Alert(Alert.AlertType.ERROR, "No items have been added to the invoice");

        //Display the alert
        noItems.show();
      }
    });

    Button createInvoice = new Button("Create Invoice");
    createInvoice.setOnAction(event -> {

          //Checks to make sure customers isn't empty
          if (!customers.getSelectionModel().isEmpty() && !itemsSelected.isEmpty()) {

            //Creates a new Report Object using
            Report invoiceReport = new Report((JSONObject) customerJSON.returnArray()
                .get(customers.getSelectionModel().getSelectedIndex()),
                shippingMethod.getSelectionModel().getSelectedItem(),
                paymentMethod.getSelectionModel().getSelectedItem(), itemsSelected);

            //Saves the report to a JSON
            invoiceReport.saveReport();

            //Creates an alert to let the user know the invoice has been created
            Alert invoiceCreated = new Alert(Alert.AlertType.INFORMATION, "Invoice created");

            //Displays the Alert
            invoiceCreated.show();
            System.out.println("つまらない");
          } else {

            //Creates an Alert prompting the user to create a customer first
            Alert missingCustomer = new Alert(Alert.AlertType.ERROR,
                "You must select a customer and items first");

            //Displays the Alert
            missingCustomer.show();
          }
          updateStockGrid();
        }
    );

    Button cancel = new Button("Cancel");

    //Adds the elements to the UI and sets their positions
    createInvoiceMenu.addColumn(0, fieldNames);
    createInvoiceMenu.add(customers, 1, 0);
    createInvoiceMenu.add(items, 1, 1);
    createInvoiceMenu.add(quantity, 2, 1);
    createInvoiceMenu.add(addItem, 3, 1);
    createInvoiceMenu.add(shippingMethod, 1, 2);
    createInvoiceMenu.add(paymentMethod, 1, 3);
    createInvoiceMenu.add(cancel, 0, 4);
    createInvoiceMenu.add(createInvoice, 1, 4);

    //Creates a BorderPane
    BorderPane invoiceDisplay = new BorderPane();

    //Create a new Stage
    Stage invoiceUI = new Stage();

    //Assigns button to close the window
    cancel.setOnAction(event -> invoiceUI.close());

    //Puts the invoice menu at the top of the BorderPane
    invoiceDisplay.setTop(createInvoiceMenu);

    //Puts the report item grid below the invoice menu
    invoiceDisplay.setCenter(reportItemGrid);

    //Sets the scene to the invoice display BorderPane
    Scene createInvoiceScreen = new Scene(invoiceDisplay, 515, 400);

    //Assigns the standard.css to the scene
    createInvoiceScreen.getStylesheets().add("standard.css");

    //Sets the stage to create invoice screen
    invoiceUI.setScene(createInvoiceScreen);

    //Sets the stage title
    invoiceUI.setTitle("Create Invoice");

    //Displays the stage
    invoiceUI.show();
  }

  /*
      Creates a window to display saved invoices
   */
  private void viewInvoice() {

    //Create a new GridPane
    GridPane viewInvoices = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    viewInvoices.getStyleClass().add("background");

    //Sets vertical gap between grids
    viewInvoices.setVgap(10);

    //Sets horizontal gap between grids
    viewInvoices.setHgap(10);
    viewInvoices.setPadding(new Insets(10, 10, 10, 10));

    //Create a new Stage
    Stage viewInvoiceUI = new Stage();

    //Create a Text Array of field names
    Text[] fieldNames = new Text[]{
        new Text("Select Invoice"),
        new Text("Invoice Number"),
        new Text("Date"),
        new Text("Shipping Method"),
        new Text("Customer ID"),
        new Text("Payment Method"),
        new Text("Items")
    };

    //Create a new String ComboBox
    ComboBox<String> invoices = new ComboBox<>();

    //Create a JSON object using the constructor
    JSON report = new JSON("Reports.json",
        new String[]{"date", "shippingMethod", "customerID", "paymentMethod", "invoiceNum",
            "items"});

    //For loop to add items from JSON file to ComboBox
    for (int i = 0; i < report.returnArray().size(); i++) {
      JSONObject tempReport = (JSONObject) report.returnArray().get(i);
      invoices.getItems().add(tempReport.get("invoiceNum").toString());
    }

    //Create new select button
    Button select = new Button("Select");

    //Create new Text ArrayList
    ArrayList<Text> reportText = new ArrayList<>();

    //Triggers an event when the user click the button
    select.setOnAction(event -> {

      //Gets the amount of items in the viewInvoices GridPane
      int size = viewInvoices.getChildren().size();

      //Removes previously added elements
      while (size > 9) {
        viewInvoices.getChildren().remove(9);
        size = viewInvoices.getChildren().size();
      }

      //Creates a new JSONObject
      JSONObject selectedReport;

      //Sets selectedReport to the invoice selected by the user
      selectedReport = (JSONObject) report.returnArray()
          .get(invoices.getSelectionModel().getSelectedIndex());

      //Adds Text to the reportText from the JSONObject
      reportText.add(new Text(selectedReport.get("invoiceNum").toString()));
      reportText.add(new Text(selectedReport.get("date").toString()));
      reportText.add(new Text(selectedReport.get("shippingMethod").toString()));
      reportText.add(new Text(selectedReport.get("customerID").toString()));
      reportText.add(new Text(selectedReport.get("paymentMethod").toString()));

      //Creates a temporary JSONArray to extract nested JSONArray from report
      JSONArray tempArray = (JSONArray) selectedReport.get("items");

      //Loop for each Object in tempArray, Add fields from the JSONObject to reportText
      for (Object o : tempArray) {
        JSONObject tempItem = (JSONObject) o;
        reportText.add(new Text(
            tempItem.get("itemNum").toString() + "     " + tempItem.get("itemName").toString()
                + "     " + tempItem.get("supplier").toString() + "     " + (
                Double.parseDouble(tempItem.get("sellPrice").toString()) * (Double
                    .parseDouble(tempItem.get("itemUnits").toString()))) + "     " + tempItem
                .get("itemUnits").toString()));
      }

      //For loop to number the items in the reportText
      for (int i = 0; i < reportText.size(); i++) {
        Text reportTextItem = new Text(reportText.get(i).getText());
        viewInvoices.add(reportTextItem, 1, i + 1);
      }

      //Clear the reportText ArrayList
      reportText.clear();

      //Call the stock warning function
      stockWarning();
    });

    //Add the elements to the GridPane
    viewInvoices.addColumn(0, fieldNames);
    viewInvoices.add(invoices, 1, 0);
    viewInvoices.add(select, 2, 0);

    //Create a new Scene
    Scene viewInvoiceScreen = new Scene(viewInvoices, 550, 400);

    //Assigns the standard.css to the scene
    viewInvoiceScreen.getStylesheets().add("standard.css");

    //Adds the Scene to the Stage
    viewInvoiceUI.setScene(viewInvoiceScreen);

    //Sets the Stage title
    viewInvoiceUI.setTitle("View Invoice");

    //Shows the window
    viewInvoiceUI.show();
  }

  /*
      Creates the stock list GridPane
   */
  private GridPane addStockList() {

    //Sets horizontal gap between grids
    stockList.setHgap(10);

    //Sets vertical gap between grids
    stockList.setVgap(10);

    //Adds padding around the edge of the GridPane
    stockList.setPadding(new Insets(10, 10, 10, 10));

    //Calls the update stock grid function
    updateStockGrid();

    //Returns the stockList
    return stockList;
  }

  /*
      Allows the user to create a new customer or edit an existing one
   */
  private void customerManagement() {

    //Load the customers from JSON file
    JSON customerJSON = loadCustomer();

    //Creates a new GridPane
    GridPane createCustomerManagement = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    createCustomerManagement.getStyleClass().add("background");

    //Sets vertical gap between grids
    createCustomerManagement.setVgap(10);

    //Sets horizontal gap between grids
    createCustomerManagement.setHgap(10);

    //Sets padding around the GridPane
    createCustomerManagement.setPadding(new Insets(10, 10, 10, 10));

    //Create a new Scene for the GridPane
    Scene createCustomerScreen = new Scene(createCustomerManagement, 380, 250);

    //Assigns the standard.css to the scene
    createCustomerScreen.getStylesheets().add("standard.css");

    //Creates a new Stage
    Stage customerUI = new Stage();

    //Sets the Stage to the createCustomerScreen Scene
    customerUI.setScene(createCustomerScreen);

    //Sets the title of the window
    customerUI.setTitle("Manage Customers");

    //Displays the window
    customerUI.show();

    //Adds Text to a fieldNames Array
    Text[] fieldNames = new Text[]{
        new Text("Existing Customer: "),
        new Text("Customer Name: "),
        new Text("Customers Address: "),
        new Text("Customers city: "),
        new Text("Customers postcode: "),
        new Text("Customer phone number: ")
    };

    //Create an Array of textFields
    TextField[] fieldInputs = new TextField[]{
        new TextField(),
        new TextField(),
        new TextField(),
        new TextField(),
        new TextField()
    };

    //Create a ComboBox for customers
    ComboBox<String> customers = new ComboBox<>();

    //Adds customers from the JSON to the ComboBox
    custComboBox(customers, customerJSON);

    //Creates a new Button
    Button saveCustomer = new Button("Save Customer");

    //Triggers a action when the Button is clicked
    saveCustomer.setOnAction(event -> {

      //Creates an Array of Strings from the fieldInputs TextField Array and adds the report number
      ArrayList<String> fieldInputsStrings = new ArrayList<>();
      for (TextField t : fieldInputs) {
        fieldInputsStrings.add(t.getCharacters().toString());
      }
      fieldInputsStrings.add(String.valueOf(customerJSON.returnArray().size() + 1));

      //Creates a new Verification Object
      Verification verify = new Verification();

      //Checks the user input to see if it's valid
      if (verify
          .verifyCustomerInput(fieldInputsStrings.toArray(new String[fieldInputsStrings.size()]))) {

        //Checks if an existing customer has been selected and updates their details
        if ((customers.getSelectionModel().getSelectedIndex() != 0)) {
          JSONObject selectedCustomer = (JSONObject) customerJSON.returnArray()
              .get(customers.getSelectionModel().getSelectedIndex());
          fieldInputsStrings.set(5, selectedCustomer.get("custID").toString());
          customerJSON.modifyItem(fieldInputsStrings.toArray(new String[fieldInputsStrings.size()]),
              customers.getSelectionModel().getSelectedIndex());
          Alert customerUpdated = new Alert(Alert.AlertType.INFORMATION, "Customer updated");
          customerUpdated.show();

          for (TextField fieldInput : fieldInputs) {
            fieldInput.clear();
          }
        }

        //If no existing customer selected create a new one
        else {
          customerJSON
              .createNewItem(fieldInputsStrings.toArray(new String[fieldInputsStrings.size()]));
          Alert customerAdded = new Alert(Alert.AlertType.INFORMATION, "Customer added");
          customerAdded.show();
          for (TextField fieldInput : fieldInputs) {
            fieldInput.clear();
          }
        }
      }

      //Fills the customers ComboBox using the customer JSON
      custComboBox(customers, customerJSON);
    });

    //Clears customer fields if user changes selected user
    customers.valueProperty().addListener((observable, oldValue, newValue) -> {
      for (int i = 0; i < fieldInputs.length; i++) {
        fieldInputs[i].clear();
      }
    });

    //Create a cancel button
    Button cancel = new Button("Cancel");

    //When triggered close the windows
    cancel.setOnAction(event -> {
      customerUI.close();
      updateStockGrid();
    });

    //Creates a select button
    Button select = new Button("Select");

    //Triggers when select button is clicked
    select.setOnAction(event -> {

      //Checks if a customer is selected then add their details to the TextFields
      if (!customers.getSelectionModel().isEmpty()) {
        JSONObject SelectedCustomer = (JSONObject) customerJSON.returnArray()
            .get(customers.getSelectionModel().getSelectedIndex());
        fieldInputs[0].setText(String.valueOf(SelectedCustomer.get("custName")));
        fieldInputs[1].setText(String.valueOf(SelectedCustomer.get("custAddress")));
        fieldInputs[2].setText(String.valueOf(SelectedCustomer.get("custCity")));
        fieldInputs[3].setText(String.valueOf(SelectedCustomer.get("custPostCode")));
        fieldInputs[4].setText(String.valueOf(SelectedCustomer.get("custPhoneNum")));
      }

      //If no customer selected display an error
      else {
        Alert noCustomer = new Alert(Alert.AlertType.ERROR, "No customer selected");
        noCustomer.show();
      }
    });

    //Add elements to the GridPane
    createCustomerManagement.add(fieldNames[0], 0, 0);
    createCustomerManagement.add(customers, 1, 0);
    createCustomerManagement.add(select, 2, 0);
    createCustomerManagement.add(fieldNames[1], 0, 1);
    createCustomerManagement.add(fieldInputs[0], 1, 1);
    createCustomerManagement.add(fieldNames[2], 0, 2);
    createCustomerManagement.add(fieldInputs[1], 1, 2);
    createCustomerManagement.add(fieldNames[3], 0, 3);
    createCustomerManagement.add(fieldInputs[2], 1, 3);
    createCustomerManagement.add(fieldNames[4], 0, 4);
    createCustomerManagement.add(fieldInputs[3], 1, 4);
    createCustomerManagement.add(fieldNames[5], 0, 5);
    createCustomerManagement.add(fieldInputs[4], 1, 5);
    createCustomerManagement.add(saveCustomer, 1, 6);
    createCustomerManagement.add(cancel, 0, 6);
  }

  /*
      Allows the user to view customer details
   */
  private void viewCustomers() {

    //Create a new GridPane
    GridPane viewCustomer = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    viewCustomer.getStyleClass().add("background");

    //Sets vertical gap between grids
    viewCustomer.setVgap(10);

    //Sets horizontal gap between grids
    viewCustomer.setHgap(10);

    //Sets padding around the GridPane
    viewCustomer.setPadding(new Insets(10, 10, 10, 10));

    //Creates a new Stage
    Stage viewCustomerUI = new Stage();

    //Create an Array of Text for fieldNames
    Text[] fieldNames = new Text[]{
        new Text("Select Customer: "),
        new Text("Customer ID: "),
        new Text("Customer Name: "),
        new Text("Customer Address: "),
        new Text("Customer City: "),
        new Text("Customer Post Code: "),
        new Text("Customer Phone Number: ")
    };

    //Loads the customer JSON
    JSON customerJSON = loadCustomer();

    //Creates a customer ComboBox
    ComboBox<String> customers = new ComboBox<>();

    //Adds items to the customers ComboBox from JSON
    custComboBox(customers, customerJSON);

    //Creates a new select Button
    Button select = new Button("Select");

    //Creates a new ArrayList of Text
    ArrayList<Text> customerDetails = new ArrayList<>();

    //Triggers when select button is clicked
    select.setOnAction(event -> {

      //Remove elements from GridPane between element 9 and the GridPane size
      viewCustomer.getChildren().remove(9, viewCustomer.getChildren().size());

      //Create a JSONObject
      JSONObject selectedCustomer;

      //Get the selected customers details and add the to TextFields
      selectedCustomer = (JSONObject) customerJSON.returnArray()
          .get(customers.getSelectionModel().getSelectedIndex());
      customerDetails.add(new Text(selectedCustomer.get("custID").toString()));
      customerDetails.add(new Text(selectedCustomer.get("custName").toString()));
      customerDetails.add(new Text(selectedCustomer.get("custAddress").toString()));
      customerDetails.add(new Text(selectedCustomer.get("custCity").toString()));
      customerDetails.add(new Text(selectedCustomer.get("custPostCode").toString()));
      customerDetails.add(new Text(selectedCustomer.get("custPhoneNum").toString()));

      //For each customer detail add it to a column
      for (Text customerDetail : customerDetails) {
        viewCustomer.addColumn(1, customerDetail);
      }

      //Clear the ArrayList
      customerDetails.clear();
    });

    //Adds customer fields to the GridPane
    addCustomerFields(viewCustomer, fieldNames, customers, select);

    //Sets the GridPane as the Scene
    Scene viewCustomerScene = new Scene(viewCustomer, 380, 200);

    //Assigns the standard.css to the scene
    viewCustomerScene.getStylesheets().add("standard.css");

    //Sets the Scene to the Stage
    viewCustomerUI.setScene(viewCustomerScene);

    //Sets the Stage Title
    viewCustomerUI.setTitle("View customers");

    //Displays the customer UI window
    viewCustomerUI.show();
  }

  /*
      Allows the user to add and modify stock
   */
  private void stockManagement() {

    //Creates a new GridPane
    GridPane manageStock = new GridPane();

    //Adds a stylesheet identifier to the GridPane
    manageStock.getStyleClass().add("background");

    //Sets vertical gap between grids
    manageStock.setVgap(10);

    //Sets horizontal gap between grids
    manageStock.setHgap(10);

    //Adds padding around the GridPane
    manageStock.setPadding(new Insets(10, 10, 10, 10));

    //Sets the GridPane to the Scene
    Scene manageStockScreen = new Scene(manageStock, 315, 250);

    //Assigns the standard.css to the scene
    manageStockScreen.getStylesheets().add("standard.css");

    //Creates a new Stage
    Stage manageStockUI = new Stage();

    //Sets the Stage to the Scene
    manageStockUI.setScene(manageStockScreen);

    //Sets the Stage title
    manageStockUI.setTitle("Stock Management");

    //Displays the stock management window
    manageStockUI.show();

    //Creates an Text Array of fieldNames
    Text[] fieldNames = new Text[]{
        new Text("Existing Item: "),
        new Text("Item Name: "),
        new Text("Supplier: "),
        new Text("Item Units: "),
        new Text("Buy Price: "),
        new Text("Sell Price: ")
    };

    //Creates a new ComboBox
    ComboBox<String> existingItems = new ComboBox<>();

    //Loads the stock JSON
    JSON stockJSON = loadStock();

    //Adds items from the stockJSON to the ComboBox
    stockComboBox(existingItems, stockJSON);

    //Creates an Array of fieldInputs
    TextField[] fieldInputs = new TextField[]{
        new TextField(),
        new TextField(),
        new TextField(),
        new TextField(),
        new TextField()
    };

    //Creates a select Button
    Button select = new Button("Select");

    //Triggers when select button is clicked
    select.setOnAction(event -> {

      //Gets the stock item selected by the user
      JSONObject item = (JSONObject) stockJSON.returnArray()
          .get(existingItems.getSelectionModel().getSelectedIndex() - 1);

      //Sets the TextFields to the values in the JSONObject
      fieldInputs[0].setText(String.valueOf(item.get("itemName")));
      fieldInputs[1].setText(String.valueOf(item.get("supplier")));
      fieldInputs[2].setText(String.valueOf(item.get("itemUnits")));
      fieldInputs[3].setText(String.valueOf(item.get("buyPrice")));
      fieldInputs[4].setText(String.valueOf(item.get("sellPrice")));
    });

    //Clears fields when user changes item
    existingItems.valueProperty().addListener((observable, oldValue, newValue) -> {
      for (int i = 0; i < fieldInputs.length; i++) {
        fieldInputs[i].clear();
      }
    });

    //Creates a cancel button
    Button cancel = new Button("Cancel");

    //Makes cancel close the window when clicked and updates the stockList
    cancel.setOnAction(event -> {
      manageStockUI.close();
      updateStockGrid();
    });

    //Creates a new button called "Save Item"
    Button saveItem = new Button("Save Item");

    //Triggers event when saveItem is clicked
    saveItem.setOnAction(event -> {

      //Creates an Array of Strings from the users inputs
      String[] itemDetails = new String[]{
          fieldInputs[0].getCharacters().toString(),
          fieldInputs[1].getCharacters().toString(),
          fieldInputs[2].getCharacters().toString(),
          fieldInputs[3].getCharacters().toString(),
          String.valueOf(stockJSON.returnArray().size() + 1),
          fieldInputs[4].getCharacters().toString()
      };

      //Create a Verification Object
      Verification verify = new Verification();

      //Check if user input is valid
      if (verify.verifyStockInput(itemDetails)) {

        //If selected item is item 0, then creates a new item
        if (existingItems.getSelectionModel().getSelectedIndex() == 0) {
          stockJSON.createNewItem(itemDetails);
          Alert stockAdded = new Alert(Alert.AlertType.INFORMATION, "Item added");
          stockAdded.show();
        }

        //Modify an existing stock item
        else {
          stockJSON
              .modifyItem(itemDetails, existingItems.getSelectionModel().getSelectedIndex() - 1);
          Alert stockAdded = new Alert(Alert.AlertType.INFORMATION, "Item updated");
          stockAdded.show();
        }
      }

      //Adds items from the stock JSON to the stock ComboBox
      stockComboBox(existingItems, stockJSON);

      //Updates the stock grid in the main window
      updateStockGrid();

      //Clears the input fields
      for (TextField fieldInput : fieldInputs) {
        fieldInput.clear();
      }
    });

    //Adds the elements to the GridPane
    manageStock.addColumn(0, fieldNames);
    manageStock.add(existingItems, 1, 0);
    manageStock.addColumn(1, fieldInputs);
    manageStock.add(select, 2, 0);
    manageStock.add(saveItem, 1, fieldInputs.length + 1);
    manageStock.add(cancel, 0, fieldInputs.length + 1);
  }

  /*
      Displays a basic about message
   */
  private void displayAbout() {
    Alert about = new Alert(Alert.AlertType.INFORMATION, "Author: Stephen Wallace\nYear: 2020");
    about.show();
  }

  /*
      Displays a simple help message
   */
  private void displayHelp() {

    //Check if help file is in expected location
    File checkFile = new File("CCCP Stock management system manual.pdf");

    if (checkFile.exists()) {
      try {
        //Opens the PDF through the default pdf viewer
        ProcessBuilder openPDF = new ProcessBuilder("cmd.exe", "/C",
            "explorer " + System.getProperty("user.dir")
                + "\\\"CCCP Stock management system manual\".pdf");
        openPDF.start();
      } catch (IOException e) {
        //If the client is on a non windows OS they should receive this instead or if the program can't run cmd or explorer
        Alert help = new Alert(Alert.AlertType.INFORMATION,
            "Please Refer to the CCCP Stock management system manual.pdf for help");
        help.show();
      }
    } else {
      //If the help file is missing, display this message
      Alert helpMissing = new Alert(Alert.AlertType.ERROR, "The help file appears to be missing");
      helpMissing.show();
    }
  }

  /*
      Updates the items shown in the stock grid
   */
  private void updateStockGrid() {

    //Clear the stockList GridPane
    stockList.getChildren().clear();

    //Add stockList column name Text
    Text[] fieldText = new Text[]{
        new Text("Item Number"),
        new Text("Item Name"),
        new Text("Supplier"),
        new Text("Item Units"),
        new Text("Buy Price"),
        new Text("Sell Price")
    };

    //Adds a stylesheet tag to the column names
    for (Text text : fieldText) {
      text.getStyleClass().add("gridCell");
    }

    //Add column names to GridPane
    stockList.add(fieldText[0], 0, 0);
    stockList.add(fieldText[1], 1, 0);
    stockList.add(fieldText[2], 2, 0);
    stockList.add(fieldText[3], 3, 0);
    stockList.add(fieldText[4], 4, 0);
    stockList.add(fieldText[5], 5, 0);

    //Load stock JSON
    JSON stockJSON = loadStock();

    //For each item in stockJson, add stock details to the GridPane
    for (int i = 0; i < stockJSON.returnArray().size(); i++) {
      JSONObject stockObject = (JSONObject) stockJSON.returnArray().get(i);
      stockList.add(new Text(String.valueOf(stockObject.get("itemNum"))), 0, i + 1);
      stockList.add(new Text(String.valueOf(stockObject.get("itemName"))), 1, i + 1);
      stockList.add(new Text(String.valueOf(stockObject.get("supplier"))), 2, i + 1);
      stockList.add(new Text(String.valueOf(stockObject.get("itemUnits"))), 3, i + 1);
      stockList.add(new Text(String.valueOf(stockObject.get("buyPrice"))), 4, i + 1);
      stockList.add(new Text(String.valueOf(stockObject.get("sellPrice"))), 5, i + 1);
    }

    //Add a stylesheet tag to the stockList
    stockList.getStyleClass().add("grid");

    //Calls the stockWarning() method
    stockWarning();
  }

  /*
      Adds customer data to a ComboBox
   */
  private void custComboBox(ComboBox<String> customers, JSON customerJSON) {

    //Clear ComboBox
    customers.getItems().clear();

    //For each item in the customer JSON, add the name to the ComboBox
    for (int i = 0; i < customerJSON.returnArray().size(); i++) {
      JSONObject tempCustomer = (JSONObject) customerJSON.returnArray().get(i);
      customers.getItems().add(String.valueOf(tempCustomer.get("custName")));
    }
  }

  /*
      Adds stock data to a ComboBox
   */
  private void stockComboBox(ComboBox<String> stock, JSON stockJSON) {

    //Clears the ComboBox
    stock.getItems().clear();

    //Adds an item to the ComboBox
    stock.getItems().add("Add new item");

    //Selects the first item in the ComboBox
    stock.getSelectionModel().selectFirst();

    //For each item in the loop, add the itemNum and itemName fields to the ComboBox
    for (int i = 0; i < stockJSON.returnArray().size(); i++) {
      JSONObject tempItem = (JSONObject) stockJSON.returnArray().get(i);
      stock.getItems()
          .add(tempItem.get("itemNum").toString() + " - " + tempItem.get("itemName").toString());
    }
  }

  /*
      Loads the stockJSON
   */
  private JSON loadStock() {

    return new JSON("Stock.json",
        new String[]{"itemName", "supplier", "itemUnits", "buyPrice", "itemNum", "sellPrice"});
  }

  /*
      Loads the customerJSON
   */
  private JSON loadCustomer() {

    return new JSON("Customer.json",
        new String[]{"custName", "custAddress", "custCity", "custPostCode", "custPhoneNum",
            "custID"});
  }

  /*
      Adds an item to the Report preview GridPane
   */
  private void addItemToReportPreview(GridPane targetedGridPane, JSONObject jsonObjectToAdd) {
    targetedGridPane.addColumn(0, new Text(String.valueOf(jsonObjectToAdd.get("itemName"))));
    targetedGridPane.addColumn(1, new Text(String.valueOf(jsonObjectToAdd.get("itemUnits"))));
  }

  /*
      Saves items to a JSON
   */
  private void saveItems(JSONObject item, JSONArray items) {
    items.add(item);
  }

  /*
      Checks for low stock and displays a warning
   */
  private void stockWarning() {

    //Load stock JSON
    JSONArray stockJSON = loadStock().returnArray();

    //For each item in stock JSON, take a JSONObject from the stockJSON
    for (Object o : stockJSON) {
      JSONObject stockItem = (JSONObject) o;

      //If units of stock remaining is < 5 and not 0, display low stock warning
      if (Integer.parseInt(stockItem.get("itemUnits").toString()) != 0
          && Integer.parseInt(stockItem.get("itemUnits").toString()) < 5) {
        Alert stockWarningAlert = new Alert(Alert.AlertType.WARNING,
            "Stock for " + stockItem.get("itemName") + " is low!");
        stockWarningAlert.show();
      }

      //If unit of stock is 0 display stock outage
      else if (Integer.parseInt(stockItem.get("itemUnits").toString()) == 0) {
        Alert stockOutageAlert = new Alert(Alert.AlertType.WARNING,
            stockItem.get("itemName") + " is out of stock!");
        stockOutageAlert.show();
      }
    }
  }

  /*
      Adds customer fields to the GridPane
   */
  private void addCustomerFields(GridPane viewCustomer, Text[] fieldNames,
      ComboBox<String> customers, Button select) {
    viewCustomer.addColumn(0, fieldNames);
    viewCustomer.add(customers, 1, 0);
    viewCustomer.add(select, 2, 0);
  }
}