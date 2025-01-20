package com.itcbusiness.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginRequestModel {

	private String email;
	private String password;

}
