package RaptorDMG;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Report {
    private final String[] companyDetails = new String[] {
            "12-14 Street Lane Road",
            "Sometown",
            "ST12 3NR",
            "0123 456 7890",
            "www.cccp.co.uk",
            "orders@cccp.co.uk"
    };
    private int invoiceNum = 0;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final LocalDate date = LocalDate.now();
    private String shippingMethod;
    private String paymentMethod;
    private JSONObject customer = new JSONObject();

    public String[] getCompanyDetails() {
        return companyDetails;
    }

    public int getInvoiceNum() {
        nextInvoice();

        return invoiceNum;
    }

    private void nextInvoice() {
        invoiceNum = getInvoiceNum();
        invoiceNum++;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public  Report(JSONObject customer, String shippingMethod, String paymentMethod) {
        this.customer = customer;
        this.shippingMethod = shippingMethod;
        this.paymentMethod = paymentMethod;
    }

    public static void main(String[] args) throws IOException {
        String[] companyDetails = new String[] {
                "12-14 Street Lane Road",
                "Sometown",
                "ST12 3NR",
                "0123 456 7890",
                "www.cccp.co.uk"
        };
        int invoiceNum = 1;
        LocalDate date = LocalDate.now();
        createInvoice(companyDetails, invoiceNum, date);
    }

    private static void createInvoice(String[] companyDetails, int invoiceNum, LocalDate date) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new File("Abomination.pdf"));
        PDAcroForm acroform = pdfDocument.getDocumentCatalog().getAcroForm();

        JSONArray currentContents = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("Stock.json")) {
            Object obj = jsonParser.parse(reader);
            currentContents = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject temp = (JSONObject) currentContents.get(1);

        acroform.getFields().get(0).setValue(companyDetails[0]);
        acroform.getFields().get(1).setValue(companyDetails[1]);
        acroform.getFields().get(2).setValue(companyDetails[2]);
        acroform.getFields().get(3).setValue(companyDetails[3]);
        acroform.getFields().get(4).setValue(companyDetails[4]);
        acroform.getFields().get(5).setValue(String.valueOf(invoiceNum));
        acroform.getFields().get(6).setValue(date.toString());
        acroform.getFields().get(7).setValue("Collection");
        acroform.getFields().get(8).setValue("Cash");
        acroform.getFields().get(9).setValue(temp.get("ItemNum").toString());
        acroform.getFields().get(10).setValue(temp.get("ItemName").toString());
        

        pdfDocument.save("AbominationEdited.pdf");
        pdfDocument.close();
    }
}