package jp.nanairo.service.svc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import jp.nanairo.model.UserData;

@Service
public class DataSvc extends HystrixCommand<Object> implements ApplicationContextAware {
	
	private RestTemplate restTemplate;
	@Value("${secondService}")
	private String wsUrl; // to be build using eureka
	@Autowired
	private ObjectMapper objectMapper;
	private ApplicationContext ctx;
	
	public DataSvc(){
		super(HystrixCommandGroupKey.Factory.asKey("DataSvcFirst"));
		restTemplate = new RestTemplate();
	}

	public UserData getUserData(){
		
//		UserData userData = new UserData();
//		userData.setAddress("Address");
//		userData.setEmailAddress("test@test.com");
//		userData.setFirstName("Joseph");
//		userData.setLastName("Tarigan");
//		userData.setPhoneNumber("+626565345234");
		
		DataSvc svc = ctx.getBean(DataSvc.class);
		UserData userData = (UserData) svc.execute();
		
		return userData;
	}

	@Override
	protected UserData run() throws Exception {
		
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		final HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, headers);
		final ResponseEntity<String> responseEntity = restTemplate.exchange(wsUrl + "userData", HttpMethod.GET, requestEntity, String.class);

		String responseBody = responseEntity.getBody();
		UserData userData = objectMapper.readValue(responseBody, UserData.class);
		
		userData.setAddress("Address");
		userData.setEmailAddress("test@test.com");
		userData.setFirstName("Joseph");
		userData.setLastName("Tarigan");
		
		return userData;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ctx = arg0;
	}
}
