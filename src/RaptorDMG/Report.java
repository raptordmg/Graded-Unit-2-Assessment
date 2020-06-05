/*
  Name: Stephen Wallace
  Program: CCCP Stock Management for Graded Unit 2
  Date of last modification: 23/04/20
 */

package RaptorDMG;

import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Report {

  //Creates a String Array for the company details
  private final String[] companyDetails = new String[]{
      "12-14 Street Lane Road",
      "Sometown",
      "ST12 3NR",
      "0123 456 7890",
      "www.cccp.co.uk",
      "orders@cccp.co.uk"
  };

  //Declares variables used in the class
  private int invoiceNum = 0;
  private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final LocalDate date = LocalDate.now();
  private final String shippingMethod;
  private final String paymentMethod;
  private final JSONObject customer;
  private final JSONArray items;

  //Returns the company details
  public String[] getCompanyDetails() {
    return companyDetails;
  }

  //Get the invoice number
  public int getInvoiceNum() {
    nextInvoice();

    return invoiceNum;
  }

  //Gets the next invoice number based on the current one
  private void nextInvoice() {
    invoiceNum = getInvoiceNum();
    invoiceNum++;
  }

  //Returns the date
  public LocalDate getDate() {
    return date;
  }

  //Returns the shipping method
  public String getShippingMethod() {
    return shippingMethod;
  }

  //Returns the payment method
  public String getPaymentMethod() {
    return paymentMethod;
  }

  /*
      Constructor for a Report
   */
  public Report(JSONObject customer, String shippingMethod, String paymentMethod, JSONArray items) {

    //Assigns passed values to the class variables
    this.customer = customer;
    this.shippingMethod = shippingMethod;
    this.paymentMethod = paymentMethod;
    this.items = items;
  }

  //Saves items
  public void saveItems(JSONObject itemToSave) {
    items.add(itemToSave);
  }

  /*
      Saves a Report
   */
  public void saveReport() {

    //Checks if the amount of item is not equal to 0
    if (items.size() != 0) {

      //Loads the reports JSON
      JSON reports = new JSON("Reports.json",
          new String[]{"invoiceNum", "date", "shippingMethod", "paymentMethod", "customerID",
              "items"});

      //Creates new JSONObject
      JSONObject report = new JSONObject();

      //Puts data the into the report JSON
      report.put("invoiceNum", reports.returnArray().size() + 1);
      report.put("date", date.toString());
      report.put("shippingMethod", shippingMethod);
      report.put("paymentMethod", paymentMethod);
      report.put("customerID", customer.get("custID"));
      report.put("items", items);

      //Updates the amount of stock left
      updateStockValue();

      //Adds JSONObject to the end of the JSON file
      reports.appendJSON("Reports.json", report);
    }

    //Displays Alert if no items have been added to the invoice
    else {
      Alert noItems = new Alert(Alert.AlertType.ERROR, "No items have been added to the invoice");
      noItems.show();
    }
  }

  /*
      Updates the amount of stock after an invoice
   */
  private void updateStockValue() {

    //Loads stock file
    JSON stock = new JSON("Stock.json",
        new String[]{"itemNum", "itemName", "supplier", "itemUnits", "itemPrice"});

    //For each item in the items JSONArray
    for (int j = 0; j < items.size(); j++) {

      //For each item in the stock JSON
      for (int i = 0; i < stock.returnArray().size(); i++) {

        //JSONObject equals current item from the stock JSON
        JSONObject tempStockItem = (JSONObject) stock.returnArray().get(i);

        //JSONObject equal current item from the items JSONArray
        JSONObject tempInvoiceItem = (JSONObject) items.get(j);

        //Checks if an item from the stock file equals the item from the items JSONArray
        if (tempStockItem.get("itemNum").equals(tempInvoiceItem.get("itemNum"))) {

          //Takes the quantity of items in the JSONArray from the amount of items available in stock
          int itemQuantity = Integer.valueOf(tempStockItem.get("itemUnits").toString()) - Integer
              .valueOf(tempInvoiceItem.get("itemUnits").toString());

          //Adds new quantity to Stock item
          tempStockItem.put("itemUnits", itemQuantity);
        }

        //Saves the stock item to the file with updated quantity
        stock.saveModification(tempStockItem, "Stock.json", i);
      }
    }
  }
}