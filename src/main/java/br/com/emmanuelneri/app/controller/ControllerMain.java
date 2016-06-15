package br.com.emmanuelneri.app.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.talesolutions.cep.CEP;
import org.talesolutions.cep.CEPService;
import org.talesolutions.cep.CEPServiceFactory;

import br.com.emmanuelneri.app.model.UtilRequest;

@Controller
@RequestMapping("/main/api")
public class ControllerMain {

	private static final String URL = "http://localhost:8080/qat-sysmgmt-controller-rest/";
	private CEPService buscaCEP;



	@RequestMapping(value = "/fetchCep", method = RequestMethod.POST)
	@ResponseBody
	public CEP fetchCep(@RequestBody String cep) {
		buscaCEP = CEPServiceFactory.getCEPService();
		return buscaCEP.obtemPorNumeroCEP(cep);
	}

	@ResponseBody
    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public String listar(@RequestBody UtilRequest request) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Header", "value");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Other-Header", "othervalue");
        headers.set("X-Auth-Token", request.getToken() );

        HttpEntity<String> entity = new HttpEntity<String>("{}",headers);
        String result = restTemplate.postForObject(URL + request.getUrl(), entity, String.class,HttpMethod.GET,request.getRequest());
      //  HttpEntity<String> response = restTemplate.exchange(REST_SERVICE_URI+"/site/api/fetchPage/",HttpMethod.GET, entity,List.class,null);
      //  List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/site/api/fetchPage/", entity,List.class,HttpMethod.GET,{"teste:teste"});

      //  if(usersMap!=null){
      //      for(LinkedHashMap<String, Object> map : usersMap){
       //         System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
      //      }
       // }else{
            System.out.println("No user exist----------");
     //   }
            return result;
    }



}