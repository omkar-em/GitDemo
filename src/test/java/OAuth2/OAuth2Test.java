package OAuth2;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
public class OAuth2Test {

	@Test
	public void getResponse() throws InterruptedException {
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.xpath("//input[@id='identifierId']")).sendKeys("meldneom");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='identifierId']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("M3ld@neom");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		String url = driver.getCurrentUrl();	// we will fetch this url and extract the code
		String partialUrl = url.split("code=")[1];
		String code = partialUrl.split("&scope")[0];
		driver.close();
		System.out.println("code is - " + code);
//		
		String accessTokenResponse =
				given().log().all()
				.urlEncodingEnabled(false) 			//Setting this false as url contains special characters it 
													// doesn't fetch code properly
				.queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type", "authorization_code")
				.when()
				.post("https://www.googleapis.com/oauth2/v4/token")
				.then().extract().response().asString();
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.get("access_token");
		
		String response = given().log().all()
		.queryParam("access_token", accessToken)
		.when()
		.get("https://rahulshettyacademy.com/getCourse.php")
		.asString();
		System.out.println(response);
	}
}
