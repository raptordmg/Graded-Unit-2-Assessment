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
import org.json.simple.JSONObject;

public class UI extends Application {
    private JSON stockJSON = new JSON("Stock.json", new String[] {"ItemNum", "ItemName", "Supplier", "ItemUnits", "ItemPrice"});
    private GridPane stockList = new GridPane();

    public void start(Stage stage) {
        BorderPane programUI = new BorderPane();

        FlowPane menuBar = setButtons();
        programUI.setTop(menuBar);
        programUI.setCenter(addStockList());

        Scene scene = new Scene(programUI, 900,700);
        stage.setTitle("CCCP Inventory Management");
        stage.setScene(scene);
        stage.show();
    }

    private FlowPane setButtons(){
        Button[] menuButtons =  new Button[] {
                new Button("Create Invoice"),
                new Button("Manage Stock"),
                new Button("Manage Customers"),
                new Button("Generate Report"),
                new Button("Display Information"),
                new Button("Transaction Log"),
                new Button("Help"),
                new Button("About")
        };

        FlowPane menuBar = new FlowPane();
        menuBar.setPadding(new Insets(10,10,10,10));
        menuBar.setStyle("-fx-background-color: #97CEF9;");
        menuBar.setHgap(10);
        menuBar.setVgap(5);
        menuBar.getChildren().addAll(menuButtons);

        menuButtons[0].setOnAction(event -> createInvoice());
        menuButtons[1].setOnAction(event -> stockManagement());
        menuButtons[2].setOnAction((event -> customerManagement()));
        menuButtons[3].setOnAction(event -> generateReport());

        menuButtons[4].setOnAction(event -> displayInformation());
        menuButtons[5].setOnAction(event -> {
            //Transaction log
        });

        menuButtons[6].setOnAction(event -> displayHelp());

        menuButtons[7].setOnAction(event -> displayAbout());

        return menuBar;
    }

    private void createInvoice() {
        JSON customerJSON = new JSON("Customer.json", new String[] {"custName", "custAddress", "custCity", "custPostCode", "custPhoneNum","custID"});
        GridPane createInvoiceMenu = new GridPane();
        createInvoiceMenu.setPadding(new Insets(10,10,10,10));
        Scene createInvoiceScreen = new Scene(createInvoiceMenu,480,230);

        Stage invoiceUI = new Stage();
        invoiceUI.setScene(createInvoiceScreen);
        invoiceUI.setTitle("Create Invoice");
        invoiceUI.show();

        Text[] fieldNames = new Text[] {
                new Text("Customer: "),
                new Text("Item(s): "),
                new Text("Shipping method: "),
                new Text("Payment Method: ")
        };

        TextField itemName = new TextField();
        TextField itemQuantity = new TextField();

        ComboBox<String> shippingMethod = new ComboBox<>();
        shippingMethod.getItems().addAll("Collection", "Post");

        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("Cash", "Card", "Paypal");

        ComboBox<String> customers = new ComboBox<>();
        custComboBox(customers,customerJSON);

        Button addItem = new Button();
        addItem.setOnAction(event -> {
            for (int i = 0; i < stockJSON.returnArray().size(); i++) {
                JSONObject item = (JSONObject) stockJSON.returnArray().get(i);
                String currentItemName = item.get("ItemName").toString();
                if (currentItemName.contains(itemName.getCharacters())) {
                    //Add code to add item to pdf
                    Alert itemFound = new Alert(Alert.AlertType.INFORMATION, currentItemName + " has been added to the invoice");
                    itemFound.show();
                } else {
                    Alert itemNotFound = new Alert(Alert.AlertType.ERROR, currentItemName + " has not been found\nPlease try again!");
                    itemNotFound.show();
                }
            }
        });

        //createInvoiceMenu
    }

    private GridPane addStockList() {
        stockList.setHgap(10);
        stockList.setVgap(10);
        stockList.setPadding(new Insets(10,10,10,10));

        Text[] fieldText = new Text[] {
                new Text("Item Number"),
                new Text("Item Name"),
                new Text("Supplier"),
                new Text("Item Units"),
                new Text("Item Price")
        };

        stockList.add(fieldText[0],0,0);
        stockList.add(fieldText[1],1,0);
        stockList.add(fieldText[2],2,0);
        stockList.add(fieldText[3],3,0);
        stockList.add(fieldText[4],4,0);

        updateGrid();
        stockList.setGridLinesVisible(true);

        return stockList;
    }

    private void customerManagement() {
        JSON customerJSON = new JSON("Customer.json", new String[] {"custName", "custAddress", "custCity", "custPostCode", "custPhoneNum","custID"});
        GridPane createCustomerManagement = new GridPane();
        createCustomerManagement.setPadding(new Insets(10,10,10,10));
        Scene createCustomerScreen = new Scene(createCustomerManagement, 480, 230);

        Stage customerUI = new Stage();
        customerUI.setScene(createCustomerScreen);
        customerUI.setTitle("Manage Customers");
        customerUI.show();

        Text[] fieldNames = new Text[] {
                new Text("Existing Customer: "),
                new Text("Customer Name: "),
                new Text("Customers Address: "),
                new Text("Customers city: "),
                new Text("Customers postcode: "),
                new Text("Customer number: ")
        };

        TextField[] fieldInputs = new TextField[] {
                new TextField(),
                new TextField(),
                new TextField(),
                new TextField(),
                new TextField()
        };

        ComboBox<String> customers = new ComboBox<>();
        custComboBox(customers,customerJSON);

        Button saveCustomer = new Button("Save Customer");
        saveCustomer.setOnAction(event -> {
            String[] fieldInputsStrings = new String[] {
                    fieldInputs[0].getCharacters().toString(),
                    fieldInputs[1].getCharacters().toString(),
                    fieldInputs[2].getCharacters().toString(),
                    fieldInputs[3].getCharacters().toString(),
                    fieldInputs[4].getCharacters().toString()
            };

            Verification verify = new Verification();
            if (verify.verifyCustomerInput(fieldInputsStrings)) {
                if(!(customers.getSelectionModel().getSelectedIndex() <= 0)) {
                    customerJSON.modifyItem(fieldInputsStrings,customers.getSelectionModel().getSelectedIndex()-1);
                } else {
                    customerJSON.createNewItem(fieldInputsStrings);
                }
            }
        });


        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> customerUI.close());

        Button select = new Button("Select");
        select.setOnAction(event -> {
            if (customers.getSelectionModel().getSelectedIndex() != 0) {
                JSONObject customer = (JSONObject) customerJSON.returnArray().get(customers.getSelectionModel().getSelectedIndex() - 1);
                fieldInputs[0].setText(String.valueOf(customer.get("custName")));
                fieldInputs[1].setText(String.valueOf(customer.get("custAddress")));
                fieldInputs[2].setText(String.valueOf(customer.get("custCity")));
                fieldInputs[3].setText(String.valueOf(customer.get("custPostCode")));
                fieldInputs[4].setText(String.valueOf(customer.get("custPhoneNum")));
            }
        });

        createCustomerManagement.add(fieldNames[0],0,0);
        createCustomerManagement.add(customers,2,0);
        createCustomerManagement.add(select, 3,0);
        createCustomerManagement.add(fieldNames[1],0,1);
        createCustomerManagement.add(fieldInputs[0],2,1);
        createCustomerManagement.add(fieldNames[2],0,2);
        createCustomerManagement.add(fieldInputs[1],2,2);
        createCustomerManagement.add(fieldNames[3], 0,3);
        createCustomerManagement.add(fieldInputs[2],2,3);
        createCustomerManagement.add(fieldNames[4],0,4);
        createCustomerManagement.add(fieldInputs[3],2,4);
        createCustomerManagement.add(fieldNames[5],0,5);
        createCustomerManagement.add(fieldInputs[4],2,5);
        createCustomerManagement.add(saveCustomer,0,6);
        createCustomerManagement.add(cancel, 1,6);
    }

    private void stockManagement() {
        GridPane manageStock = new GridPane();
        manageStock.setPadding(new Insets(10,10,10,10));
        Scene manageStockScreen = new Scene(manageStock,350,250);

        Stage manageStockUI = new Stage();
        manageStockUI.setScene(manageStockScreen);
        manageStockUI.setTitle("Stock Management");
        manageStockUI.show();

        Text[] fieldNames = new Text[] {
                new Text("Existing Item: "),
                new Text("Item Number: "),
                new Text("Item Name: "),
                new Text("Supplier: "),
                new Text("Item Units: "),
                new Text("Item Price: ")
        };

        ComboBox<String> existingItems = new ComboBox<>();
        existingItems.getItems().add("Add new item");
        existingItems.getSelectionModel().selectFirst();
        for (int i = 0; i < stockJSON.returnArray().size(); i++) {
            JSONObject tempItem = (JSONObject) stockJSON.returnArray().get(i);
            existingItems.getItems().add(tempItem.get("ItemNum").toString() + " - " + tempItem.get("ItemName").toString());
        }

        TextField[] fieldInputs = new TextField[] {
                new TextField(),
                new TextField(),
                new TextField(),
                new TextField(),
                new TextField()
        };

        Button select = new Button("Select");
        select.setOnAction(event -> {
            JSONObject item = (JSONObject) stockJSON.returnArray().get(existingItems.getSelectionModel().getSelectedIndex()-1);

            fieldInputs[0].setText(String.valueOf(item.get("ItemNum")));
            fieldInputs[1].setText(String.valueOf(item.get("ItemName")));
            fieldInputs[2].setText(String.valueOf(item.get("Supplier")));
            fieldInputs[3].setText(String.valueOf(item.get("ItemUnits")));
            fieldInputs[4].setText(String.valueOf(item.get("ItemPrice")));
        });

        Button cancel = new Button();
        cancel.setOnAction(event -> manageStockUI.close());

        Button saveItem = new Button("Save Item");
        saveItem.setOnAction(event -> {
            String[] itemDetails = new String[] {
                    fieldInputs[0].getCharacters().toString(),
                    fieldInputs[1].getCharacters().toString(),
                    fieldInputs[2].getCharacters().toString(),
                    fieldInputs[3].getCharacters().toString(),
                    fieldInputs[4].getCharacters().toString()
            };
            Verification verify = new Verification();

            if (verify.verifyStockInput(itemDetails)) {
                if (existingItems.getSelectionModel().getSelectedIndex() == 0) {
                    stockJSON.createNewItem(itemDetails);
                } else {
                    stockJSON.modifyItem(itemDetails, existingItems.getSelectionModel().getSelectedIndex()-1);
                }
            }

        });

        manageStock.addColumn(0,fieldNames);
        manageStock.add(existingItems,1,0);
        manageStock.addColumn(1,fieldInputs);
        manageStock.add(select, 2,0);
        manageStock.add(saveItem, 1, fieldInputs.length + 1);
    }

    private void generateReport() {
        FlowPane selectReport = new FlowPane();
        selectReport.setPadding(new Insets(10,10,10,10));
        Scene selectReportScreen = new Scene(selectReport, 450,80);

        Stage generateReportUI = new Stage();
        generateReportUI.setScene(selectReportScreen);
        generateReportUI.setTitle("Select Report Type");
        generateReportUI.show();

        Button[] reportButtons = new Button[] {
                new Button("Sales Analysis"),
                new Button("VAT Analysis"),
                new Button("Stock Turnover"),
                new Button("Profitability Reports")
        };

        selectReport.getChildren().addAll(reportButtons);

    }

    private void displayInformation() {
        FlowPane showInformation = new FlowPane();
        showInformation.setPadding(new Insets(10,10,10,10));
        Scene showInformationScreen = new Scene(showInformation,450, 80);

        Stage showInformationUI = new Stage();
        showInformationUI.setScene(showInformationScreen);
        showInformationUI.setTitle("Display Information");
        showInformationUI.show();

        Button[] informationSelection = new Button[] {
                new Button("Customer details"),
                new Button("Product details"),
                new Button("Supplier details"),
                new Button("Invoices")
        };

        showInformation.getChildren().addAll(informationSelection);
    }

    /*private void viewTransactions() {
        Code this later
    }*/

    private void displayAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION, "Author: Stephen Wallace\nYear: 2020");
        about.show();
    }

    private void displayHelp() {
        Alert help = new Alert(Alert.AlertType.INFORMATION, "Help goes here");
        help.show();
    }

    private void updateGrid() {
        for (int i = 0; i < stockJSON.returnArray().size(); i++) {
            JSONObject stockObject = (JSONObject) stockJSON.returnArray().get(i);
            stockList.add(new Text(String.valueOf(stockObject.get("ItemNum"))),0,i+1);
            stockList.add(new Text(String.valueOf(stockObject.get("ItemName"))),1,i+1);
            stockList.add(new Text(String.valueOf(stockObject.get("Supplier"))),2,i+1);
            stockList.add(new Text(String.valueOf(stockObject.get("ItemUnits"))),3,i+1);
            stockList.add(new Text(String.valueOf(stockObject.get("ItemPrice"))),4,i+1);
        }
    }

    private void custComboBox(ComboBox<String> customers, JSON customerJSON) {
        customers.getItems().add("Create new customer");
        for (int i = 0; i < customerJSON.returnArray().size(); i++) {
            JSONObject tempCustomer = (JSONObject) customerJSON.returnArray().get(i);
            customers.getItems().add(String.valueOf(tempCustomer.get("custName")));
        }
        customers.getSelectionModel().selectFirst();
    }
}