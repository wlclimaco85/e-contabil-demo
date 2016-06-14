package org.talesolutions.cep;

//import java.io.IOException;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.FormHttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import com.ctc.wstx.dtd.TokenModel;
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import model.ModelToken;
//import model.User;

public class BuscaURLTest {

//
//	 public static final String REST_SERVICE_URI = "http://localhost:8080/qat-sysmgmt-controller-rest";
//
//	 	@Test
//	 	public void listAllUsers(){
//	        System.out.println("Testing listAllUsers API-----------");
//
//	        RestTemplate restTemplate = new RestTemplate();
//
//	        //http://localhost:8080/qat-sysmgmt-controller-rest/site/api/fetchPage
//	        //['X-Auth-Token'] = "anonimo@aninimo.com:1464222873542:eb86a5e265cbdd54d63a03efece46935";
//
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.set("Header", "value");
//	        headers.setContentType(MediaType.APPLICATION_JSON);
//	        headers.set("Other-Header", "othervalue");
//	        headers.set("X-Auth-Token", "taz@qat.com:1465826926855:8de173cffb7b75bb4f67b335dc2df587" );
//
//	        HttpEntity<String> entity = new HttpEntity<String>("{}",headers);
//	        String result = restTemplate.postForObject("http://localhost:8080/qat-sysmgmt-controller-rest/county/api/fetchPage/", entity, String.class,HttpMethod.GET,"{pageSize: 20, startPage: 0, sortExpressions: null, preQueryCount: true, maxPreQueryCount: 0}");
//	      //  HttpEntity<String> response = restTemplate.exchange(REST_SERVICE_URI+"/site/api/fetchPage/",HttpMethod.GET, entity,List.class,null);
//	      //  List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/site/api/fetchPage/", entity,List.class,HttpMethod.GET,{"teste:teste"});
//
//	      //  if(usersMap!=null){
//	      //      for(LinkedHashMap<String, Object> map : usersMap){
//	       //         System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
//	      //      }
//	       // }else{
//	            System.out.println("No user exist----------");
//	     //   }
//	    }
//
//
//	 	@Test
//	 	public void listAutentication() throws JsonParseException, JsonMappingException, IOException{
//	        System.out.println("Testing listAllUsers API-----------");
//
//	        RestTemplate restTemplate = new RestTemplate();
//
//	        //http://localhost:8080/qat-sysmgmt-controller-rest/site/api/fetchPage
//	        //['X-Auth-Token'] = "anonimo@aninimo.com:1464222873542:eb86a5e265cbdd54d63a03efece46935";
//
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.set("Header", "value");
//	        headers.setContentType(MediaType.APPLICATION_JSON);
//	        headers.set("Other-Header", "othervalue");
//	        headers.set("username", "taz@qat.com" );
//
//			Map<String, String> params = new HashMap<String, String>();
//		    params.put("username", "taz@qat.com");
//		    params.put("password", "taz@qat.com");
//		   // HTTPClient<HashMap<String,Object>> http = new HTTPClient<>();
//		 // GET TGT
//		    RestTemplate rest = new RestTemplate();
//		    rest.setMessageConverters(Arrays.asList(new StringHttpMessageConverter(), new FormHttpMessageConverter()));
//		    MultiValueMap<String, String> paramss = new LinkedMultiValueMap<String, String>();
//		    paramss.set("username", "taz@qat.com");
//		    paramss.set("password", "devil");
//		    URI tgtUrl = rest.postForLocation("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate", paramss, Collections.emptyMap());
//		    System.out.println("[" + tgtUrl + "]");
//
//		 //   TokenModel tgtUrls = rest.postForLocation("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate", paramss, Collections.emptyMap(),  TokenModel.class);
//		    System.out.println("[" + tgtUrl + "]");
//
//
//		    ResponseEntity<String> st = rest.postForEntity("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate", paramss, String.class);
//		    System.out.println("[" + st.getBody() + "]");
//		    System.out.println("[" + st + "]");
//		    String tk = st.getBody();
//		    Class<? extends String> mt = tk.getClass();
//		    System.out.println("[" + mt + "]");
//		  // Gson gson = new Gson();
//		   // MyObject = gson.fromJson(decodedString , MyObjectClass.class);
//		   // Class<? extends TokenModel> c = st.getBody().getClass();
//		    ObjectMapper mapper = new ObjectMapper();
//		    ModelToken obj = mapper.readValue(st.getBody(), ModelToken.class);
//
//		    System.out.println("[" + obj.getToken() + "]");
//
////	        HttpEntity<String> entity = new HttpEntity<String>(headers);
////	        URI location = restTemplate.postForLocation("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate","username=taz@qat.com");
////	        String result = restTemplate.exchange("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate",entity, HttpMethod.POST,  String.class);
////	        ResponseEntity<Map> response=restTemplate.exchange("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate", HttpMethod.POST, entity, Map.class);
////	        System.out.println("No user exist----------"+response.getBody());
////              String result = restTemplate.postForObject("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate", entity, String.class,HttpMethod.POST,paramss);
////	      //  HttpEntity<String> response = restTemplate.exchange(REST_SERVICE_URI+"/site/api/fetchPage/",HttpMethod.GET, entity,List.class,null);
////	      //  List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/site/api/fetchPage/", entity,List.class,HttpMethod.GET,{"teste:teste"});
////
////	      //  if(usersMap!=null){
////	      //      for(LinkedHashMap<String, Object> map : usersMap){
////	       //         System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
////	      //      }
////	       // }else{
////	            System.out.println("No user exist----------");
////	     //   }
//	    }
//
//	 	@Test
//	 	public void getUser(){
//	        System.out.println("Testing getUser API----------");
//	        RestTemplate restTemplate = new RestTemplate();
//	        User user = restTemplate.getForObject(REST_SERVICE_URI+"/user/1", User.class);
//	        System.out.println(user);
//	    }
//
//	 	@Test
//	 	public void createUser() {
//	        System.out.println("Testing create User API----------");
//	        RestTemplate restTemplate = new RestTemplate();
//	        User user = new User(0,"Sarah",51,134);
//	        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/user/", user, User.class);
//	        System.out.println("Location : "+uri.toASCIIString());
//	    }
//
//	 	@Test
//	 	public void updateUser() {
//	        System.out.println("Testing update User API----------");
//	        RestTemplate restTemplate = new RestTemplate();
//	        User user  = new User(1,"Tomy",33, 70000);
//	        restTemplate.put(REST_SERVICE_URI+"/user/1", user);
//	        System.out.println(user);
//	    }
//
//	 	@Test
//	 	public void deleteUser() {
//	        System.out.println("Testing delete User API----------");
//	        RestTemplate restTemplate = new RestTemplate();
//	        restTemplate.delete(REST_SERVICE_URI+"/user/3");
//	    }
//
//
//	 	@Test
//	 	public void deleteAllUsers() {
//	        System.out.println("Testing all delete Users API----------");
//	        RestTemplate restTemplate = new RestTemplate();
//	        restTemplate.delete(REST_SERVICE_URI+"/user/");
//	    }
//

}
