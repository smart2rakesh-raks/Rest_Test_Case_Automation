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
    public String AdminTokenPost() throws ParseException  {
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
    public void AdminLoginPost() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Admin login");
        test.log(Status.INFO, "Starting test case");

        String access_token = AdminTokenPost();
        // post for admin login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        String AdminLoginbody = LoginResponse.getBody().asString();
        LoginResponse.prettyPrint();
        int statusCode = LoginResponse.getStatusCode();
        System.out.println("Status code: "+statusCode);

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

        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        Assert.assertEquals(username, "upskills_admin", "Correct username returned");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");

    }

    @Test
    public void AdminUserDetailsGet() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Admin login user details");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = AdminTokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        //Get request
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

        AdminUserData.prettyPrint();
        int statusCode = AdminUserData.getStatusCode();
        System.out.println("Status code: "+statusCode);

        test.pass("Fetched Details Successfully");
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        Assert.assertEquals(username, "upskills_admin", "Correct username returned");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");
    }

    @Test
    public void AdminAddProductCategoryandsubcategory() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Add product,sub category");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = AdminTokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //Post product category
        Response AdminProductCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(Category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        AdminProductCategoryResponse.prettyPrint();
        int statusCode = AdminProductCategoryResponse.getStatusCode();
        System.out.println("Status code: "+statusCode);

        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        test.pass("Posted Category successfully.");

        //Post subcategory
        Response AdminsubCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(subCategory).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        AdminsubCategoryResponse.prettyPrint();
        int statusCodeSub = AdminsubCategoryResponse.getStatusCode();
        System.out.println("Status code: "+statusCodeSub);
        Assert.assertEquals(statusCodeSub, 200, "Correct status code returned");
        test.pass("Posted SubCategory successfully.");

        //Post Product
        Response AdminproductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(product).when().post("http://rest-api.upskills.in/api/rest_admin/products");

        AdminproductResponse.prettyPrint();
        int statusCodepro = AdminsubCategoryResponse.getStatusCode();
        System.out.println("Status code: "+statusCodepro);
        Assert.assertEquals(statusCodepro, 200, "Correct status code returned");
        test.pass("Posted Product successfully.");
        test.pass("Correct status code returned ");
        test.info("Test completed");

    }

    @Test
    public void AdminAddcustomerPost() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Add new customer,product then create a new order");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = AdminTokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //post for customer
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
        System.out.println(id);
        int statusCodecustome = AdmincustomerResponse.getStatusCode();
        System.out.println("Status code: "+statusCodecustome);
        Assert.assertEquals(statusCodecustome, 200, "Correct status code returned");
        test.pass("New Customer added Successfully");


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

        System.out.println(adminid);
        int statusCodeproduct = AdmincustomerResponse.getStatusCode();
        System.out.println("Status code: "+statusCodeproduct);
        Assert.assertEquals(statusCodeproduct, 200, "Correct status code returned");
        test.pass("New product added Successfully");

        //post order
        Response AdminorderResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(adorder).when().post("http://rest-api.upskills.in/api/rest_admin/orderadmin");
        String orderbody = AdminorderResponse.getBody().asString();
        Pattern orderp = Pattern.compile("\\{([^{}]*)\\}");
        Matcher orderm = orderp.matcher(orderbody);
        List<String> orderl = new ArrayList<String>();
        while (orderm.find()) {
            orderl.add(orderm.group(1));
        }
        String orderresult = orderl.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject orderjsonObject = new JsonParser().parse(orderresult).getAsJsonObject();
        String orderid = orderjsonObject.get("id").getAsString();

        System.out.println(orderid);
        int statusCodeorder = AdmincustomerResponse.getStatusCode();
        System.out.println("Status code: "+statusCodeorder);
        Assert.assertEquals(statusCodeorder, 200, "Correct status code returned");
        test.pass("New order created Successfully");
        test.info("Test completed");

    }

    @Test
    public void AdminLogoutPost() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Admin logout");
        test.log(Status.INFO, "Starting test case");
        //Login
        String access_token = AdminTokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        Response AdminLogoutResponse = RestAssured.given().auth()
                .oauth2(access_token).post("http://rest-api.upskills.in/api/rest_admin/logout");
        AdminLogoutResponse.prettyPrint();

        int statusCode = AdminLogoutResponse.getStatusCode();
        System.out.println("Status code: "+statusCode);
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        test.pass("Log out Successfully");
        test.info("Test completed");

    }
}
