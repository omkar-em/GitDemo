package Practice;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	@Test
	public void printJson() {
		JsonPath js = new JsonPath(Payload.coursePrice());


		//Print no of courses
		int count = js.getInt("courses.size()");
		System.out.println("No of courses - " + count);
		
		//print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase amount is  - " + purchaseAmount);
		
		// print title of first course
		System.out.println("1st course - " + js.getString("courses[0].title"));
		
		// print title of all course
		System.out.println("All courses - " + js.getString("courses.title"));
		
		// print course name and price
		for (int i = 0; i <count; i++) {
			System.out.println("Price of "+ js.getString("courses["+i+"].title") + " is " +  js.getString("courses["+i+"].price"));
		}
		
		//print no of copies sold for rpa
		for (int i = 0; i < count ; i++) {
			if (js.getString("courses["+i+"].title").equalsIgnoreCase("rpa")) {
				System.out.println(js.getString("courses["+i+"].title") + " course sold are - " + js.getInt("courses["+i+"].copies"));
				break;
			}
		}
		
		// Verify if purchase amount matches
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		int total = 0;
		for (int i = 0; i < count; i++) {
			total = total + js.getInt("courses.price["+i+"]") * js.getInt("courses["+i+"].copies");
			System.out.println("total till now - "+ total);
		}
		System.out.println("total amount is - " + totalAmount);
		assertEquals(total, totalAmount);
	}
}
   