import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


public class Base_class {
    String auth = "Authorization";
    String base = "Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=";
    String requestBody = "{\n" +
            "  \"username\": \"upskills_admin\",\n" +
            "  \"password\": \"Talent4$$\"\n" +
            "}";

    String Category = "{\n" +
            "\"category_description\": [\n" +
            "    {\n" +
            "      \"name\": \"Computers & Accessories\",\n" +
            "      \"meta_title\": \"Computers & Accessories\",\n" +
            "      \"description\": \"Description of the Computers & Accessories\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String subCategory = "{\n" +
            "\"parent_id\": 62,\n" +
            "\"category_description\": [\n" +
            "        {\n" +
            "      \"name\": \"Laptops\",\n" +
            "      \"meta_title\": \"Laptops\",\n" +
            "      \"description\": \"Description of the Laptops\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String product  = "{\n" +
            "\"model\": \"Lenovo Ideapad Laptop\",\n" +
            " \"quantity\": \"1000\",\n" +
            " \"price\": \"44000.00\",\n" +
            " \"product_description\": [\n" +
            "    {\n" +
            "      \"name\": \"Lenovo IdeaPad S100\", \n" +
            "      \"meta_title\": \"Lenovo IdeaPad S100\"\n" +
            "    }\n" +
            "  ], \n" +
            "\"product_category\":[\n" +
            "     \"25\" ] \n" +
            "}";

    String customer = "{\n" +
            "\"firstname\": \"Ram\",\n" +
            "\"lastname\": \"K\",\n" +
            "\"email\": \"demo6@gmail.com\",\n" +
            "\"password\": \"password\",\n" +
            "\"confirm\": \"password\", \n" +
            "\"telephone\": \"1541-754-3010\"\n" +
            "}";

    String Adminproduct = "{\n" +
            "\"model\": \"Lenovo Ideapad Laptop\",\n" +
            " \"quantity\": \"1000\",\n" +
            " \"price\": \"44000.00\",\n" +
            " \"product_description\": [\n" +
            "    {\n" +
            "      \"name\": \"Lenovo IdeaPad S100\", \n" +
            "      \"meta_title\": \"Lenovo IdeaPad S100\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String order = "{\n" +
            "  \"products\": [\n" +
            "    {\n" +
            "      \"product_id\": \"3804\",\n" +
            "      \"quantity\": \"2\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"shipping_method\": {\n" +
            "    \"title\": \"Flat Shipping Rate\",\n" +
            "    \"code\": \"flat.flat\"\n" +
            "  },\n" +
            "  \"payment_method\": {\n" +
            "    \"title\": \"Cash OnDelivery\",\n" +
            "    \"code\": \"cod\"\n" +
            "  },\n" +
            "  \"payment_address\": {\n" +
            "    \"firstname\": \"Rakesh\",\n" +
            "    \"lastname\": \"l\",\n" +
            "    \"city\": \"delhi\",\n" +
            "    \"address_1\": \"delhi\",\n" +
            "    \"country_id\": \"81\",\n" +
            "    \"postcode\": \"22222\",\n" +
            "    \"zone_id\": \"1256\",\n" +
            "    \"address_2\": \"\",\n" +
            "    \"zone\": \"Berlin\",\n" +
            "    \"country\": \"india\"\n" +
            "  },\n" +
            "  \"shipping_address\": {\n" +
            "    \"firstname\": \"Rakesh\",\n" +
            "    \"lastname\": \"l\",\n" +
            "    \"city\": \"delhi\",\n" +
            "    \"address_1\": \"delhi\",\n" +
            "    \"country_id\": \"81\",\n" +
            "    \"postcode\": \"22222\",\n" +
            "    \"zone_id\": \"1256\",\n" +
            "    \"address_2\": \"\",\n" +
            "    \"zone\": \"Berlin\",\n" +
            "    \"country\": \"india\"\n" +
            "  },\n" +
            "  \"customer\": {\n" +
            "    \"email\": \"nash1@vipmail..hu\",\n" +
            "    \"customer_id\": \"4102\"\n" +
            "  }\n" +
            "}";

    ExtentSparkReporter htmlReporter;
    ExtentReports extent;

    @BeforeSuite
    public void Bsetup(){
        htmlReporter = new ExtentSparkReporter("extentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

    }
    @AfterSuite
    public void BsetupEnd() {
        extent.flush();

    }
}
