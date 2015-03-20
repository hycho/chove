package com.cho.chove.core.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessHandlerWithAjax implements AccessDeniedHandler{

	// 세팅에서 넘겨줄 errorpage
	private String errorPage;
	
	public void setErrorPage(String errorPage) {
		if ((errorPage != null) && !errorPage.startsWith("/")) {
			throw new IllegalArgumentException("errorPage must begin with '/'");
		}
		this.errorPage = errorPage;
	}
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		String ajaxHeader = request.getHeader("X-Ajax-call"); // Ajax로 호출한 서비스인지 확인 한다.
		String result = "";
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		
		response.setCharacterEncoding("UTF-8");
		
		if(ajaxHeader == null) { //만약 null일경우 ajax가 아닌 일반 Url호출을 통해 접근을 했다는 것을 의미 한다.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();  //Authentication 구함.
			Object principal = auth.getPrincipal(); //principal에는 UserDetail을 확장하는 객체 별도 설정을 하지 않았다면 UserDetail
			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				request.setAttribute("username", username);
			}
			
			request.setAttribute("errormsg", accessDeniedException);
			request.getRequestDispatcher(errorPage).forward(request, response);
		} else { //Ajax호출일경우
			if("true".equals(ajaxHeader)) { //ajax접근
				result = "{\"result\" : \"fail\", \"message\" : \"" + accessDeniedException.getMessage() + "\", \"redirectUrl\" : \""+errorPage+"\"}";
			} else { //X-Ajax-call해더 변수가 있지만 우리가 선언해서 호출한 ajax호출이 아니다.
				result = "{\"result\" : \"fail\", \"message\" : \"Access Denied(Header Value Mismatch)\", \"redirectUrl\" : \""+errorPage+"\"}";
			}
			response.getWriter().print(result);
			response.getWriter().flush();
		}
	}
}
