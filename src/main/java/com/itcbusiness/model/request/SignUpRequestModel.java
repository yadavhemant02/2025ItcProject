package com.itcbusiness.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SignUpRequestModel {

	private String name;
	private String email;
	private String mobilNumber;
	private String password;
	private String role;

}
