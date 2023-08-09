package mapApi;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import Practice.Payload;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

public class SerializeTest {
	@Test
	public void map() {
		
		//Setting values for creating input body
		Location l = new Location();
		l.setLat(-38.3834941);
		l.setLng(33.427362);
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side layout, cohen 09");
		p.setLanguage("French-IN");
		p.setLocation(l);
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setTypes(myList);
		p.setWebsite("http://google.com");
		
		RestAssured.baseURI = 	"https://rahulshettyacademy.com";

		Response response  = 
				given().queryParam("key", "qaclick123").log().all()
				.body(p)
				.when().post("maps/api/place/add/json")
				.then().assertThat().statusCode(200).extract().response();
		
		System.out.println(response.asString());
	}
}
