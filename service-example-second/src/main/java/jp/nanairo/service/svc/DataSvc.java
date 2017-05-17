package jp.nanairo.service.svc;

import org.springframework.stereotype.Service;

import jp.nanairo.model.UserData;

@Service
public class DataSvc {

	public UserData getUserData(){
		
		UserData userData = new UserData();
		userData.setPhoneNumber("+626565345234");
		
		return userData;
	}
}
