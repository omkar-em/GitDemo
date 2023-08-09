package JiraAutomation;

import org.testng.annotations.Test;

import Utils.ReusableMethods;

import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class UsingSession {

	
	//Session filter is used for passing session info.
	SessionFilter sessionFilter = new SessionFilter();
	String commentID ="";
	@Test(priority = 1)
	public void loginJira() {
		RestAssured.baseURI = "http://localhost:8080";
		// relaxedHTTPSValidation() is used to bypass the https cetificate valiation
		String response = given().relaxedHTTPSValidation().filter(sessionFilter).header("Content-Type", "application/json")
				.body("{ \"username\": \"c2omkar\", \"password\": \"c2omkar\" }")

				.when().post("rest/auth/1/session")

				.then().assertThat().statusCode(200).extract().response().asString();

	}

	@Test(priority = 2)
	public void addComment() {
		RestAssured.baseURI = "http://localhost:8080";
		String addCommentResponse = 
		given().pathParam("key", "10005")
				.header("Content-Type", "application/json")
				.filter(sessionFilter)
				.body("{\"body\": \"Last comment\"}")
				.when().post("/rest/api/2/issue/{key}/comment") // when {} is added it will search for any path parameter
															// present in given()
				.then().assertThat().statusCode(201).extract().response().asString();
		
		
		JsonPath js = new JsonPath(addCommentResponse);
		commentID= js.get("id");
	}
	
	@Test(priority = 3)
	public void addAttachment() {
		RestAssured.baseURI = "http://localhost:8080";
		given()
		.header("X-Atlassian-Token","no-check")
		.filter(sessionFilter)
		.pathParam("key", "10005")
		.multiPart("file", new File("C:\\Users\\Eminds\\eclipse-workspace\\ApiAutomation\\src\\test\\resources\\jira.txt"))
		.when().post("/rest/api/2/issue/{key}/attachments")
		.then().assertThat().statusCode(200);
	}
	
	@Test(priority = 4)
	public void getIssue() {
		String issueDetails = given().pathParam("key", "10005")
		.filter(sessionFilter)
		.queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{key}")
		.then().assertThat().statusCode(200).log().all().extract().response().asString();
	
		JsonPath js = new JsonPath(issueDetails);
		int commentCount = js.get("fields.comment.comments.size()");
		
		for (int i = 0; i < commentCount; i++) {
			if (js.get("fields.comment.comments["+i+"].id").equals(commentID)) {
				
				System.out.println("recent comment is - " + js.get("fields.comment.comments["+i+"].body"));
			}
		}
		
	}
}
