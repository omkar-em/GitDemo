package EcommerceApi;

import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import	static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class EcomApiTest {

	@Test
	public void login() {
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON)
				.build();

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("mmane9716@gmail.com");
		loginRequest.setUserPassword("Abcd@123");

		RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);

		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").
				then().log().all().extract().response().as(LoginResponse.class);

		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();

		// Add Product (form parameters in request, multipart is used in request for sending multimedia)
		RequestSpecification addProductBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.build();

		RequestSpecification reqAddProduct =  given().log().all().spec(addProductBaseReq)
				.param("productName", "qwerty").param("productAddedBy", userId)
				.param("productCategory", "qwerty").param("productSubCategory", "qwerty")
				.param("productPrice", 1).param("productDescription", "qwerty")
				.param("productFor", "qwerty")
				.multiPart("productImage", new File("C:\\Users\\Eminds\\Pictures\\uncategorized\\6MAR.png"));

		String addProductResponse = reqAddProduct.when().post("api/ecom/product/add-product")
				.then().log().all().extract().response().asString();

		JsonPath js = new JsonPath(addProductResponse);
		String productID = js.get("productId");
		System.out.println("Product ID -" + productID);


		// Create order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.setContentType(ContentType.JSON)
				.build();

		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCountry("India");
		orderDetails.setProductOrderedId(productID);

		List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
		orderDetailsList.add(orderDetails);

		Orders orders=new Orders();
		orders.setOrders(orderDetailsList);

		RequestSpecification createOrderReq = given().spec(createOrderBaseReq).body(orders);

		String createOrderResponse	= createOrderReq.when().post("/api/ecom/order/create-order")
				.then().log().all().extract().response().asString();


		// Delete product (path parameters in request)
		RequestSpecification deleteProductBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.build();

		RequestSpecification deleteOrderReq = 
				given().spec(deleteProductBaseReq)
				.pathParam("productID", productID);

		String deleteProductResponse = 
				deleteOrderReq.when().delete("/api/ecom/product/delete-product/{productID}")
				.then().log().all().extract().response().asString();

		JsonPath js2 = new JsonPath(deleteProductResponse);
		String message = js2.get("message");
		System.out.println("Message is - " + message);
	}

}
