package com.itcbusiness.helper;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@ComponentScan
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private JwtHelper jwtHelper;

	public JwtAuthenticationEntryPoint(JwtHelper jwtHelper) {
		super();
		this.jwtHelper = jwtHelper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		log.warn("ip_Address : " + request.getRemoteAddr());

		String requestHeader = request.getHeader("Authorization");
		log.info(" Header :  [{}]", requestHeader);
		String username = null;
		String message = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			// looking good
//			System.out.print(token);
//			byte[] decodedBytes = Base64.getDecoder().decode(token);
//			String decodedString = new String(decodedBytes);
//			token = decodedString;
			token = requestHeader.substring(7);
			try {

				username = this.jwtHelper.getUsernameFromToken(token);
				log.info("token is valid | " + username);
			} catch (IllegalArgumentException e) {
				log.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				log.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				log.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}

		} else {
			message = "Invalid Header Value !!";
			log.info("Invalid Header Value !! ");
		}

		writer.println("Access Denied !! " + "| token : " + message + authException.getMessage());

	}

}

//import java.util.Base64;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        String src = request.getHeader("Authorization");
//        if (src != null) {
//            try {
//                String token = new String(Base64.getDecoder().decode(src.getBytes(StandardCharsets.UTF_8)));
//                // Process token and set response status
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//            } catch (Exception e) {
//                // Handle decoding exception
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//            }
//        } else {
//            // Handle null authorization header
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing");
//        }
//    }
//
////	@Override
////	public void commence(HttpServletRequest request, HttpServletResponse response,
////			AuthenticationException authException) throws IOException, ServletException {
////		// TODO Auto-generated method stub
////		
////	}
//}
//
