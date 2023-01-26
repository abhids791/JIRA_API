package JiraProj;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;


public class jiraAPI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="http://localhost:8080/";
		
		SessionFilter session=new SessionFilter();
		//Login
		given().header("Content-Type","application/json").body("{ \"username\": \"abhids788\", \"password\": \"Asd@1234\" }")
		.log().all().filter(session).when().post("rest/auth/1/session").then().log().all().extract().response().asString();
		
		//Create Issue
		String response= given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \"PT\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"Issue from Rest Assured\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        },\r\n"
				+ "        \r\n"
				+ "        \"description\": \"This is my description Rest assured\"\r\n"
				+ "        \r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("rest/api/2/issue").then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js=new JsonPath(response);
		String KeyValue=js.getString("key");
		
		//Add Comment
		String AddCommentResponse=given().log().all().pathParam("key", KeyValue).header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \"This is Test Comment\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath jsAddComment=new JsonPath(AddCommentResponse);
	
		
		
		//Update Comment
		given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \"Updated Comment from RestAssured API\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session).when().put("rest/api/2/issue/"+KeyValue+"/comment/"+jsAddComment.getString("id")+"");
		
		System.out.println("Add Attachment Add Attachment  Add Attachment Add Attachment");
		//Add Attachment
		given().log().all().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data").multiPart("file", new File("Jira.txt"))
		.filter(session).when().post("rest/api/2/issue/"+js.getString("id")+"/attachments").then().log().all();
		
		
		//Delete ticket
		System.out.println("Delete ticketDelete ticketDelete ticketDelete ticket");
		/*
		 * given().log().all().filter(session)
		 * .when().delete("rest/api/2/issue/"+KeyValue+"").then().log().all().assertThat
		 * ().statusCode(204);
		 */
		

	}

}
