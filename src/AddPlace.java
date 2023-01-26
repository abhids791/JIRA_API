import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class AddPlace {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		//Create Record
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(PayLoad.addPlace()).when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		//JsonPath js= new JsonPath(response);
		
		String place_ID=ReusableJSONParser.rawToJson(response).getString("place_id");
		
		
		
		
		//Update Record
		String newAddress="765 Summer walk, Austri";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+place_ID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//Get Record
		String response1=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_ID)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(ReusableJSONParser.rawToJson(response1).getString("address"));
		
		
		
	}

}
