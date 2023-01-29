package JiraProj;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.apache.http.util.Asserts;
import org.testng.Assert;


public class jiraAPI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="http://localhost:8080/";
		
		SessionFilter session=new SessionFilter();
		//Login
		given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{ \"username\": \"abhids791\", \"password\": \"Asd@1234\" }")
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
		
		System.out.println("Create Issue Create Issue Create Issue Create Issue Create Issue");
		
		//Add Comment
		String AddCommentResponse=given().log().all().pathParam("key", KeyValue).header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \"This is 1st Test Comment\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		String AddCommentResponse2=given().log().all().pathParam("key", KeyValue).header("Content-Type","application/json")
				.body("{\r\n"
						+ "    \"body\": \"This is 2nd Test Comment\",\r\n"
						+ "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n"
						+ "    }\r\n"
						+ "}").filter(session).when().post("rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
				
		JsonPath jsAddComment=new JsonPath(AddCommentResponse2);
	
		
		
		//Update Comment
		String UpdateComment="wUpdated Comment from RestAssured API";
		given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \""+UpdateComment+"\",\r\n"
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
		
		
		//Get Issue
		String getIssueDetails=given().log().all().filter(session).queryParam("fields", "comment").queryParam("fields", "created").pathParam("key", KeyValue).when().get("rest/api/2/issue/{key}").then().log().all().extract().response().asString();
		JsonPath getIssueResponse=new JsonPath(getIssueDetails);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		System.out.println(getIssueResponse.getString("fields.comment.comments.size()"));
		System.out.println(getIssueResponse.getString("fields.comment.comments.created"));
		
		int commentSize=getIssueResponse.getInt("fields.comment.comments.size()");
		
		for(int i=0; i<commentSize;i++) {
			System.out.println(getIssueResponse.getString("fields.comment.comments["+i+"].id"));
			
			if(getIssueResponse.getString("fields.comment.comments["+i+"].id").equals(jsAddComment.getString("id"))) {
				String message=getIssueResponse.getString("fields.comment.comments["+i+"].body");
				Assert.assertEquals(message, UpdateComment);
				System.out.println(message);
			}
			
		}
				
		
		//Delete Issue
		System.out.println("Delete ticketDelete ticketDelete ticketDelete ticket");
		/*
		 * given().log().all().filter(session)
		 * .when().delete("rest/api/2/issue/"+KeyValue+"").then().log().all().assertThat
		 * ().statusCode(204);
		 */
		

	}

}
