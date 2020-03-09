package RaptorDMG;

public class Verification {
    public boolean verifyCustomerInput(String[] target) {
        boolean isValid = true;

        for (int i = 0; i < target.length; i++) {
            if (target[i].equals(null)) {
                isValid = false;
            }
        }

        if (!(target[0].matches("[A-Za-z ]+") || (target[3].matches("[A-Za-z ]+")))) {
            isValid = false;
        }

        if (!(target[1].matches("[0-9A-Za-z ]+"))) {
            isValid = false;
        }

        if (!(target[3].matches("[0-9A-Z]{6,8}"))) {
            isValid = false;
        }

        if (!(target[4].matches("[0-9]{10,11}"))) {
            isValid = false;
        }

        return isValid;
    }

    public boolean verifyStockInput(String[] target) {
        boolean isValid = true;

        for (int i = 0; i < target.length; i++) {
            if (target[i].equals(null)) {
                isValid = false;
            }
        }

        if (!(target[0].matches("[0-9]+"))) {
            isValid = false;
        }

        if (!(target[1].matches("[0-9A-Za-z ]+"))) {
            isValid = false;
        }

        if (!(target[2].matches("[0-9A-Za-z ]+"))) {
            isValid = false;
        }

        if (!(target[3].matches("[0-9]+"))) {
            isValid = false;
        }

        if (!(target[4].matches("^\\d{0,8}(\\.\\d{1,4})?"))) {
            isValid = false;
        }

        return isValid;
    }
}