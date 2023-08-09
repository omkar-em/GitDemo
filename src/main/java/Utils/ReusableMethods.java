package Utils;
import io.restassured.path.json.JsonPath;

public class ReusableMethods {

	public static JsonPath rawToJson(String response) {

		JsonPath js = new JsonPath(response); //for parsing in json
		return js;
	}
}
