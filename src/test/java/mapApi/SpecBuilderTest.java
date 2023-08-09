package mapApi;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.RequestBuilder;
import org.testng.annotations.Test;

import Practice.Payload;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

public class SpecBuilderTest {
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

		RequestSpecification req = 	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123")
				.setContentType(ContentType.JSON)
				.build();

		ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200)
		.expectContentType(ContentType.JSON).build();
		
		// We can further breakdown the API code by separating given , when , then. 
		// This will be used in framework

		RequestSpecification res  = given().spec(req).body(p);

		Response response =	res.when().post("maps/api/place/add/json")
				.then().spec(resSpec).extract().response();

		System.out.println(response.asString());
	}
}
