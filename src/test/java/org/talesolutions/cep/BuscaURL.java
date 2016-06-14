package org.talesolutions.cep;

public class BuscaURL {

//
//	private static final String SENHA = "devil";
//	private static final String USER = "taz@qat.com";
//	final String uri = "http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate";
//	@Test
//	public void busca_por_cep_valido() {
//
//		HttpHeaders headers = new HttpHeaders();
////		headers.set("Header", "value");
////		headers.set("Other-Header", "othervalue");
////		headers.setContentType(MediaType.APPLICATION_JSON);
////		headers.set("Access-Control-Allow-Origin", "*");
////		headers.set("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
////		headers.set("Access-Control-Max-Age", "3600");
////		headers.set("X-Auth-Token", "taz@qat.com:1465612675629:4da0fd5742fdfbfe11092f4655ddd2b7");
//		//    headers.add("username", USER);
//		    headers.add("X-Auth-Token", "taz@qat.com:1465612675629:4da0fd5742fdfbfe11092f4655ddd2b7");
//		//    headers.setContentType(MediaType.APPLICATION_XML);
//		//    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
////		    final HttpEntity<MyXmlbeansRequestDocument> httpEntity = new HttpEntity<MyXmlbeansRequestDocument>(
////		            MyXmlbeansRequestDocument.Factory.parse(request), headers);
////		    final ResponseEntity<MyXmlbeansResponseDocument> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,MyXmlbeansResponseDocument.class);
////		    log.info(responseEntity.getBody());
//
//		Map<String, String> params = new HashMap<String, String>();
//	    params.put("username", USER);
//	    params.put("password", SENHA);
//
//		HttpEntity entity = new HttpEntity(params,headers);
//		RestTemplate restTemplate = new RestTemplate();
//		TokenModel response = restTemplate.getForObject(
//				uri,TokenModel.class,HttpMethod.POST,params);
//
//		System.out.println(response);
//
////	    Map<String, String> params = new HashMap<String, String>();
////	    params.put("id", "2");
////
////	    String a =  restTemplate.getForObject(uri, String.class, params);
////
////	    HttpHeaders headers = new HttpHeaders();
////	    headers.setContentType(MediaType.APPLICATION_JSON);
////	    headers.set("Authorization", "Bearer "+accessToken);
////
//		URI uri = restTemplate.postForLocation("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate/username/taz@qat.com/password/devil", "", String.class);
//	    HttpEntity<String> entitys = new HttpEntity<String>(headers);
//	    String n = restTemplate.postForObject(uri+"&username=taz@qat.com&password=devil",HttpMethod.POST, String.class);
//
//	    System.out.println(n);
//	}
//
//	@Test
//	public void busca_por_cep_valido2() {
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Header", "value");
//		headers.set("Other-Header", "othervalue");
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("Access-Control-Allow-Origin", "*");
//		headers.set("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//		headers.set("Access-Control-Max-Age", "3600");
//		headers.set("username", USER);
//		headers.set("password", SENHA);
//		Map<String, String> params = new HashMap<String, String>();
//	    params.put("username", USER);
//	    params.put("password", SENHA);
//	//	URI uri = restTemplate.postForLocation("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate/username/taz@qat.com/password/devil", "", String.class);
//	    HttpEntity<String> entitys = new HttpEntity<String>(headers);
//	 //   TokenModel n = restTemplate.postForObject(uri,HttpMethod.POST, TokenModel.class);
//	    HttpHeaders a = restTemplate.headForHeaders(uri+"?username=eadddd&password=tesra",HttpMethod.POST);
//
//	    System.out.println(HttpHeaders.ACCEPT);
//
//	}
//	@Test
//	public void busca_por_cep_valido3() throws IOException {
//
//		URL url = new URL(uri);
//
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setDoOutput(true);
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty("Accept", "application/json");
//		conn.setRequestProperty("username", USER);
//		conn.setRequestProperty("password", SENHA);
//
//		String input = "{\"username\":100,\"password\":\"iPad 4\"}";
//
//		OutputStream os = conn.getOutputStream();
//		os.write(input.getBytes());
//		os.flush();
//
//		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//			throw new RuntimeException("Failed : HTTP error code : "
//				+ conn.getResponseCode());
//		}
//
//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				(conn.getInputStream())));
//
//		String output;
//		System.out.println("Output from Server .... \n");
//		while ((output = br.readLine()) != null) {
//			System.out.println(output);
//		}
//
//		conn.disconnect();
//	}
//	@Test
//	public void busca_por_cep_valido4() throws Exception {
//		CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(
//                new AuthScope(uri, 8080),
//                new UsernamePasswordCredentials("username :"+ USER, SENHA));
//
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setDefaultCredentialsProvider(credsProvider)
//                .build();
//        try {
//            HttpPost httpget = new HttpPost(uri);
//
//            System.out.println("Executing request " + httpget.getRequestLine());
//            CloseableHttpResponse response = httpclient.execute(httpget);
//            try {
//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                System.out.println(EntityUtils.toString(response.getEntity()));
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpclient.close();
//        }
//	}
//	@Test
//	public void busca_por_cep_valido5() throws ClientProtocolException, IOException  {
//	    HttpClient client = new DefaultHttpClient();
//	    HttpPost post = new HttpPost(uri);
//	    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//	    pairs.add(new BasicNameValuePair("username",USER));
//	    pairs.add(new BasicNameValuePair("password",SENHA));
//	    post.setHeader("Content-type", "application/json");
//	    post.setHeader("Accept", "application/json");
//	    post.setHeader("X-Auth-Token", "taz@qat.com:1465612675629:4da0fd5742fdfbfe11092f4655ddd2b7");
//	    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,"UTF-8");
//	    post.setEntity(entity);
//	    HttpResponse response = client.execute(post);
//
//	    System.out.println("----------------------------------------");
//        System.out.println(response.getStatusLine());
//	}
//
//
//	package org.talesolutions.cep;
//
//	public class BuscaURLTest {
//
////		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
////			System.out.println("Filtering on...........................................................");
////			HttpServletResponse response = (HttpServletResponse) res;
////			response.setHeader("Access-Control-Allow-Origin", "*");
////			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
////			response.setHeader("Access-Control-Max-Age", "3600");
////			response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
////			chain.doFilter(req, res);
////		}
////		 public static final String REST_SERVICE_URI = "http://localhost:8080/qat-sysmgmt-controller-rest";
//	//
////		 	@Test
////		 	public void listAllUsers(){
////		        System.out.println("Testing listAllUsers API-----------");
//	//
////		        RestTemplate restTemplate = new RestTemplate();
//	//
////		        //http://localhost:8080/qat-sysmgmt-controller-rest/site/api/fetchPage
////		        //['X-Auth-Token'] = "anonimo@aninimo.com:1464222873542:eb86a5e265cbdd54d63a03efece46935";
//	//
////		       // HttpHeaders headers = new HttpHeaders();
////		      //  headers.set("Header", "value");
////		      //  headers.setContentType(MediaType.APPLICATION_JSON);
////		      //  headers.set("Other-Header", "othervalue");
////		       // headers.set("X-Auth-Token", "anonimo@aninimo.com:1464222873542:eb86a5e265cbdd54d63a03efece46935" );
//	//
////		        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
////		        map.add("username", "taz@qat.com");
////		        map.add("password", "devil");
//	//
////		        String result = restTemplate.postForObject("http://localhost:8080/qat-sysmgmt-controller-rest/auth/api/authenticate/",map, String.class,HttpMethod.POST);
//	//
//	//
//////		        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//////		        map.add("companyId", companyId);
//////		        map.add("password", password);
//	////
//////		        HttpHeaders headers = new HttpHeaders();
//////		        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	////
//////		        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//	////
//////		        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//////		        messageConverters.add(new MappingJacksonHttpMessageConverter());
//////		        messageConverters.add(new FormHttpMessageConverter());
//////		        restTemplate.setMessageConverters(messageConverters);
//	////
//////		        LoginResponse response = (LoginResponse) restTemplate.postForObject(url, request, LoginResponse.class);
//	//
////		       // List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/site/api/fetchPage/", List.class,HttpMethod.GET,{"teste:teste"});
//	//
////		   //     java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new FileInputStream(new File("fornecedores")));
//	//
////		  //      String lista = (List<Fornecedor>) ois.readObject();
//	//
////		   //     ois.close();
//	/////
////		 //       HttpEntity<String> entity = new HttpEntity<String>("{}",headers);
////		 //       String result = restTemplate.postForObject(REST_SERVICE_URI+"/site/api/fetchPage/", entity, String.class);
////		      //  HttpEntity<String> response = restTemplate.exchange(REST_SERVICE_URI+"/site/api/fetchPage/",HttpMethod.GET, entity,List.class,null);
////		      //  List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/site/api/fetchPage/", entity,List.class,HttpMethod.GET,{"teste:teste"});
//	//
////		      //  if(usersMap!=null){
////		      //      for(LinkedHashMap<String, Object> map : usersMap){
////		       //         System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
////		      //      }
////		       // }else{
////		            System.out.println("No user exist----------");
////		     //   }
////		    }
//	//
////		 	@Test
////		 	public void getUser(){
////		        System.out.println("Testing getUser API----------");
////		        RestTemplate restTemplate = new RestTemplate();
////		        User user = restTemplate.getForObject(REST_SERVICE_URI+"/user/1", User.class);
////		        System.out.println(user);
////		    }
//	//
////		 	@Test
////		 	public void createUser() {
////		        System.out.println("Testing create User API----------");
////		        RestTemplate restTemplate = new RestTemplate();
////		        User user = new User(0,"Sarah",51,134);
////		        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/user/", user, User.class);
////		        System.out.println("Location : "+uri.toASCIIString());
////		    }
//	//
////		 	@Test
////		 	public void updateUser() {
////		        System.out.println("Testing update User API----------");
////		        RestTemplate restTemplate = new RestTemplate();
////		        User user  = new User(1,"Tomy",33, 70000);
////		        restTemplate.put(REST_SERVICE_URI+"/user/1", user);
////		        System.out.println(user);
////		    }
//	//
////		 	@Test
////		 	public void deleteUser() {
////		        System.out.println("Testing delete User API----------");
////		        RestTemplate restTemplate = new RestTemplate();
////		        restTemplate.delete(REST_SERVICE_URI+"/user/3");
////		    }
//	//
//	//
////		 	@Test
////		 	public void deleteAllUsers() {
////		        System.out.println("Testing all delete Users API----------");
////		        RestTemplate restTemplate = new RestTemplate();
////		        restTemplate.delete(REST_SERVICE_URI+"/user/");
////		    }
//	//
//
//	}
//


}
