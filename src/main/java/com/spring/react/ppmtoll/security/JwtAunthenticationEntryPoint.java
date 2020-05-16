package com.spring.react.ppmtoll.security;

import com.google.gson.Gson;
import com.spring.react.ppmtoll.exceptions.InvalidLoginResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAunthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse
			httpServletResponse, AuthenticationException e) throws IOException, ServletException {

		InvalidLoginResponse loginResponse=new InvalidLoginResponse();
		String jsonLoginResponse=new Gson().toJson(loginResponse);

		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(401);
		httpServletResponse.getWriter().print(jsonLoginResponse);
	}
}
