/*
  Name: Stephen Wallace
  Program: CCCP Stock Management for Graded Unit 2
  Date of last modification: 02/05/20
 */

package RaptorDMG;

import javafx.scene.control.Alert;

public class Verification {

    /*
        Returns if the customer field inputs are valid or not
     */
    public boolean verifyCustomerInput(String[] target) {
        boolean isValid = true;
        String invalidItem = "";

        //Checks each item has a value
        for (int i = 0; i < 6; i++) {
            if (target[i].equals(null)) {
                isValid = false;
                invalidItem = "TextField " + i;
            }
        }

        //Checks the name field
        if (!(target[0].matches("[A-Za-z ]+"))) {
            isValid = false;
            invalidItem = "name";
        }

        //Checks the address field
        if (!(target[1].matches("[0-9A-Za-z ]+"))) {
            isValid = false;
            invalidItem = "address";
        }

        //Checks the city field
        if (!(target[2].matches("[A-Za-z ]+"))) {
            isValid = false;
            System.out.println(target[2]);
            invalidItem = "city";
        }

        //Checks the postcode field
        if (!(target[3].matches("[0-9A-Z]{6,8}"))) {
            isValid = false;
            invalidItem = "Post Code";
        }

        //Checks the phone number
        if (!(target[4].matches("[0-9]{10,11}"))) {
            isValid = false;
            invalidItem = "Phone Number";
        }

        //Checks the item number
        if (!(target[5].matches("[0-9]+"))) {
            isValid = false;
            invalidItem = "Item Num";
        }

        //Display message if any check fails
        if (!isValid) {
            Alert customerInputInvalid = new Alert(Alert.AlertType.ERROR, invalidItem + " is not valid");
            customerInputInvalid.show();
        }

        //return the state of isValid
        return isValid;
    }

    /*
        Returns if the stock field inputs are valid or not
     */
    public boolean verifyStockInput(String[] target) {
        boolean isValid = true;
        String invalidItem = "";

        //Check to make sure each item has a value
        for (int i = 0; i < target.length; i++) {
            if (target[i].equals(null) || target[i].isEmpty()) {
                isValid = false;
                invalidItem = "TextField " + i;
            }
        }

        //Checks item name
        if (!(target[0].matches("[0-9A-Za-z-().\" ]+"))) {
            isValid = false;
            invalidItem = "Item Name";
        }

        //Checks the supplier
        if (!(target[1].matches("[0-9A-Za-z-().\" ]+"))) {
            isValid = false;
            invalidItem = "Supplier";
        }

        //Checks the item units
        if (!(target[2].matches("[0-9]+"))) {
            isValid = false;
            invalidItem = "Item Units";
        }

        //Checks the buy price
        if (!(target[3].matches("^\\d{0,8}(\\.\\d{1,4})?"))) {
            isValid = false;
            invalidItem = "Buy Price";
        }

        //Checks the item number
        if (!(target[4].matches("[0-9]+"))) {
            isValid = false;
            invalidItem = "Item Number";
        }

        //Checks the sell price
        if (!(target[5].matches("^\\d{0,8}(\\.\\d{1,4})?"))) {
            isValid = false;
            invalidItem = "Sell Price";
        }

        //Displays an error message if check fails
        if (!isValid) {
            Alert stockInputInvalid = new Alert(Alert.AlertType.ERROR, invalidItem + " is not valid");
            stockInputInvalid.show();
        }

        //Returns is valid
        return isValid;
    }
}