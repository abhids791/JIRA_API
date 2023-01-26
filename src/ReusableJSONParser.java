import io.restassured.path.json.JsonPath;

public class ReusableJSONParser {
	public static JsonPath rawToJson(String response) {
		JsonPath js= new JsonPath(response);
		return js;
	}

}
