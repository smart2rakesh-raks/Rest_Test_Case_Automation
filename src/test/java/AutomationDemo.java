import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.json.simple.parser.ParseException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class AutomationDemo extends Base_class{


    @Test
    public String postMethodAdminToken() throws ParseException  {
        ExtentTest test = extent.createTest("Rest Test Case Get Access Token");
        test.log(Status.INFO, "Starting test case");

        Response TokenResponse = RestAssured.given().header(auth,base).when().post("http://rest-api.upskills.in/api/rest_admin/oauth2/token/client_credentials");
        String body = TokenResponse.getBody().asString();
        Pattern p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher m = p.matcher(body);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group(1));
        }
        String result = l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String access_token = jsonObject.get("access_token").getAsString();

        int statusCode = TokenResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        test.pass("Access token generated successfully.");
        test.pass("Correct status code returned.");
        test.info("Test completed");
        return access_token;
    }

    @Test
    public void postMethodAdminLogin() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Admin login");
        test.log(Status.INFO, "Starting test case");

        String access_token = postMethodAdminToken();

        // post for admin login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        String AdminLoginbody = LoginResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminLoginbody);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminLoginjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminLoginjsonObject = new JsonParser().parse(AdminLoginjson).getAsJsonObject();
        String username = AdminLoginjsonObject.get("username").getAsString();

        test.pass("Login Successfully");

        int statusCode = LoginResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        Assert.assertEquals(username, "upskills_admin", "Correct username returned");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");
    }

    @Test
    public void getMethodAdminUserDetails() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Admin login user details");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = postMethodAdminToken();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        //Get Method
        Response AdminUserData  = RestAssured.given().auth()
                .oauth2(access_token).get("http://rest-api.upskills.in/api/rest_admin/user");
        String AdminLoginbodyget = AdminUserData.getBody().asString();
        Pattern login_pget = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_mget = login_pget.matcher(AdminLoginbodyget);
        List<String> login_lget = new ArrayList<String>();
        while (login_mget.find()) {
            login_lget.add(login_mget.group(1));
        }
        String AdminLoginjsonget = login_lget.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminLoginjsonObjectget = new JsonParser().parse(AdminLoginjsonget).getAsJsonObject();
        String username = AdminLoginjsonObjectget.get("username").getAsString();

        test.pass("Fetched Details Successfully");

        int statusCode = AdminUserData.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        Assert.assertEquals(username, "upskills_admin", "Correct username returned");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");
    }

    @Test
    public void postMethodAdminAddProductCategoryandSubCategory() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Add product,sub category");
        test.log(Status.INFO, "Starting test case");

        //Login Method
        String access_token = postMethodAdminToken();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //Post product category
        Response AdminProductCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(Category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        test.pass("Posted Category successfully.");

        int statusCode = AdminProductCategoryResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code returned");

        //Post subcategory
        Response AdminsubCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(subCategory).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        test.pass("Posted SubCategory successfully.");

        int statusCodeSub = AdminsubCategoryResponse.getStatusCode();
        Assert.assertEquals(statusCodeSub, 200, "Correct status code returned");

        //Post Product
        Response AdminproductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(product).when().post("http://rest-api.upskills.in/api/rest_admin/products");

        test.pass("Posted Product successfully.");

        int statusCodepro = AdminproductResponse.getStatusCode();
        Assert.assertEquals(statusCodepro, 200, "Correct status code returned");
        test.pass("Correct status code returned ");
        test.info("Test completed");

    }

    @Test
    public void postMethodAdminAddCustomer() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Add new customer,product then create a new order");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = postMethodAdminToken();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //post method for customer
        Response AdmincustomerResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(customer).when().post("http://rest-api.upskills.in/api/rest_admin/customers");
        String body = AdmincustomerResponse.getBody().asString();
        Pattern p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher m = p.matcher(body);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group(1));
        }
        String result = l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider()).build();
        DocumentContext json = JsonPath.using(configuration).parse(order);
        String jsonPath = "customer.customer_id";
        String newValue = id;
        String admincustomer = json.set(jsonPath, newValue).jsonString();

        test.pass("New Customer added Successfully");

        int statusCodecustome = AdmincustomerResponse.getStatusCode();
        Assert.assertEquals(statusCodecustome, 200, "Correct status code returned");


        //post for product
        Response AdminProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(Adminproduct).when().post("http://rest-api.upskills.in/api/rest_admin/products");

        String prbody = AdminProductResponse.getBody().asString();
        Pattern pp = Pattern.compile("\\{([^{}]*)\\}");
        Matcher mm = pp.matcher(prbody);
        List<String> ll = new ArrayList<String>();
        while (mm.find()) {
            l.add(mm.group(1));
        }
        String presult = l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject pjsonObject = new JsonParser().parse(presult).getAsJsonObject();
        String adminid = pjsonObject.get("id").getAsString();
        Configuration config = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider()).build();
        DocumentContext pjson = JsonPath.using(config).parse(admincustomer);
        String adminjsonPath = "products[0].product_id";
        String adminnewValue = adminid;
        String adorder = pjson.set(adminjsonPath, adminnewValue).jsonString();

        int statusCodeproduct = AdmincustomerResponse.getStatusCode();
        Assert.assertEquals(statusCodeproduct, 200, "Correct status code returned");
        test.pass("New product added Successfully");

        //post order
        Response AdminorderResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(adorder).when().post("http://rest-api.upskills.in/api/rest_admin/orderadmin");

        int statusCodeorder = AdminorderResponse.getStatusCode();
        Assert.assertEquals(statusCodeorder, 200, "Correct status code returned");
        test.pass("New order created Successfully");
        test.pass("Correct status code returned ");
        test.info("Test completed");

    }

    @Test
    public void postMethodAdminLogout() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Admin logout");
        test.log(Status.INFO, "Starting test case");

        //Post for Login
        String access_token = postMethodAdminToken();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //Post for logout
        Response AdminLogoutResponse = RestAssured.given().auth()
                .oauth2(access_token).post("http://rest-api.upskills.in/api/rest_admin/logout");

        int statusCode = AdminLogoutResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        test.pass("Log out Successfully");
        test.pass("Correct status code returned ");
        test.info("Test completed");
    }
}
