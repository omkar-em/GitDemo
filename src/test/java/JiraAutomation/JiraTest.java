package JiraAutomation;

import org.testng.annotations.Test;

import Utils.ReusableMethods;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	String sessionId = "";
	@Test(priority = 1)
	public void loginJira() {
		RestAssured.baseURI = "http://localhost:8080";
		
		String response =
		given().log().all()
		.header("Content-Type","application/json")
		.body("{ \"username\": \"c2omkar\", \"password\": \"c2omkar\" }")
		
		.when().post("rest/auth/1/session")
		
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(response);
		System.out.println(js.get("session.value"));
		sessionId = js.get("session.value");
		
	}
	
	@Test(priority = 2)
	public void addComment() {
		RestAssured.baseURI = "http://localhost:8080";
		given().log().all()
		.pathParam("key", "10004")
		.header("Cookie","JSESSIONID="+ sessionId +"")
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \"Hello1.\"\r\n"
				+ " \r\n"
				+ "}")
		.when().post("/rest/api/2/issue/{key}/comment")               //when {} is added it will search for any path parameter present in given()
		.then().assertThat().statusCode(201);
	}
}
