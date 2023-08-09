package Practice;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Utils.ReusableMethods;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn,String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String response = given().log().all()
		.header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle))
		
		.when().post("/Library/Addbook.php")
		
		.then().log().all().assertThat()
		.statusCode(200)
		.extract().response().asString();
		
		System.out.println("response is - " + response);
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println("id is - " +id);
	}
	
	@Test(dataProvider = "BooksData")
	public void deleteBook(String isbn,String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String response = given().log().all()
				.body("{\r\n"
						+ " \r\n"
						+ "\"ID\" : \""+isbn+aisle+"\"\r\n"
						+ " \r\n"
						+ "} ")
				
				.when().post("/Library/DeleteBook.php")
				.then().assertThat().statusCode(200)
				.extract().response().asString();
		
		System.out.println("response for delete api is - " + response);
		JsonPath js = ReusableMethods.rawToJson(response);
		System.out.println(js.get("msg"));
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		//array - collection on elements
		//Multi dimensional array - collection of array
		return new Object[][] {{"bcdbg","123"},{"bcdbg","1234"},{"bcdbg","12345"}};
		
	}
}
