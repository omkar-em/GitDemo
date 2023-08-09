package Practice;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.path.json.JsonPath;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import Utils.ReusableMethods;

public class JsonFromFile {

	@Test
	public void m2() throws IOException {
		RestAssured.baseURI = 	"https://rahulshettyacademy.com";
	
		//body accepts input as a string. Hence we will read the external file and 
		// pass it's content as a string in body.
		// we have method which reads file and convert it into byte.
		//then byte to string
		
		// Add place API
		String response = given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Eminds\\eclipse-workspace\\ApiAutomation\\src\\test\\"
				+ "resources\\AddPlace.json"))))
		
		.when().post("maps/api/place/add/json")
		
		.then().assertThat().statusCode(200)
		.body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
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
