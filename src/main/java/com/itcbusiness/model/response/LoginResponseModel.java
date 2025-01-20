package com.itcbusiness.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginResponseModel {

	private String name;
	private String email;
	private String userCode;
	private String accessToken;
	private String status;
	private String role;

}
