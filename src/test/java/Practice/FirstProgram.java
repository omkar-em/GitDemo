package Practice;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.path.json.JsonPath;

import static org.testng.Assert.assertEquals;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import Utils.ReusableMethods;

public class FirstProgram {

	@Test(enabled = false)
	public void m1() {
		given().when().get("https://jsonplaceholder.typicode.com/users").then().log()
		 .all();
		
		   int statusCode= given().when().get("https://jsonplaceholder.typicode.com/users").getStatusCode();
		   System.out.println("status code is - " + statusCode);
	
	System.out.println("------------");
		   System.out.println(given().when().get("https://jsonplaceholder.typicode.com/users").getStatusCode());
	
		   assertEquals(statusCode, 200);
		   
		   given().when().get("https://jsonplaceholder.typicode.com/users");
	}
	
	@Test
	public void m2() {
		RestAssured.baseURI = 	"https://rahulshettyacademy.com";
	
		// Add place API
		String response = 
				given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body(Payload.addPlace())
		
		.when().post("maps/api/place/add/json")
		
		.then()
		.assertThat().statusCode(200)
		.body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println("response is --" );
		System.out.println(response);
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		//Update API
		
		given().log().all().queryParam("key", "qaclick123")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\"pune\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		
		.when().put("maps/api/place/update/json")
		
		.then().assertThat().statusCode(200)
		.body("msg", equalTo("Address successfully updated"));
		
		// Get place API
		System.out.println("Get place API");
		given().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().body().assertThat().statusCode(200)
		.body("address", equalTo("pune"))
		.header("Server", "Apache/2.4.41 (Ubuntu)");
		
	
		
	}
}
