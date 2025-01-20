package com.itcbusiness.service;

import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.ItcUser;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.request.SignUpRequestModel;
import com.itcbusiness.model.response.SignUpResponseModel;
import com.itcbusiness.repository.ItcUserRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItcUserService implements UserDetailsService {

	private ItcUserRepository itcUserRepo;
	private MapSturtMapper mapper;
	private PasswordEncoder passwordEncoded;

	public ItcUserService(ItcUserRepository itcUserRepo, MapSturtMapper mapper, PasswordEncoder passwordEncoded) {
		super();
		this.itcUserRepo = itcUserRepo;
		this.mapper = mapper;
		this.passwordEncoded = passwordEncoded;
	}

	/**
	 * 
	 * @param userData
	 * @param role
	 * @return signupResponseMode
	 */

	@Transactional(rollbackOn = RuntimeException.class)
	public SignUpResponseModel saveItcUser(SignUpRequestModel userData, String role) {
		try {
			userData.setPassword(this.passwordEncoded.encode(userData.getPassword()));
			ItcUser entityData = this.mapper.itcUserSignUpRequestToItcUser(userData, role,
					geneerateEmpCode(userData.getEmail(), this.itcUserRepo.count() + 1));
			return this.mapper.itcUserToItcUserSignUpResponse(this.itcUserRepo.saveAndFlush(entityData));
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public ItcUser getSingleUserByEmail(String email) {
		try {
			return (ItcUser) this.itcUserRepo.findByEmail(email);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.itcUserRepo.findByEmail(username);
	}

	/**
	 * 
	 * @param email
	 * @param empNo
	 * @return userCode
	 */
	private static String geneerateEmpCode(String email, long empNo) {
		String[] sss = email.split("@");
		return "C" + (empNo + 1) + sss[0].toUpperCase();
	}

	public List<SignUpResponseModel> getAllAuditorUser() {
		try {
			List<ItcUser> reponseData = this.itcUserRepo.findByRole("AUDITOR");
			List<SignUpResponseModel> response = this.mapper.listItcUserToListSignUpResponseModel(reponseData);
			return response;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
