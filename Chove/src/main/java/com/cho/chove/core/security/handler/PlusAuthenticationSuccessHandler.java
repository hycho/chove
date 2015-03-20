package com.cho.chove.core.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.cho.chove.core.security.intercept.ReloadableFilterInvocationSecurityMetadataSource;

public class PlusAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	private String targetUrlParameter;
	private String defaultUrl;
	private boolean useReferer;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private ReloadableFilterInvocationSecurityMetadataSource rfisem;
	
	public PlusAuthenticationSuccessHandler() {
		targetUrlParameter = "";
		defaultUrl = "/main";
		useReferer = false;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		try {
			rfisem.reload();
			
			clearAuthenticationAttributes(request);
			
			//int intRedirectStrategy = decideRedirectStrategy(request, response);
			/*switch(intRedirectStrategy){
			case 1:
				useTargetUrl(request, response);
				break;
			case 2:
				useSessionUrl(request, response);
				break;
			case 3:
				useRefererUrl(request, response);
				break;
			default:
				useDefaultUrl(request, response);
			}*/
			// Ajax Call로 인해서 리퍼러 URL로 redirect하는 경우에. 다이렉트로 관련 url을 콜하기 때문에 이거 정리 되기 전까지는 default URL을 타도록 수정한다.
			// 차후 관련 로직은 사용 용의 성이 잇어서 주석처리함.
			
			useDefaultUrl(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
	
	private void useTargetUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null){
			requestCache.removeRequest(request, response);
		}
		String targetUrl = request.getParameter(targetUrlParameter);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useRefererUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String targetUrl = request.getHeader("REFERER");
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		redirectStrategy.sendRedirect(request, response, defaultUrl);
	}
	
	private int decideRedirectStrategy(HttpServletRequest request, HttpServletResponse response){
		int result = 0;
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if(!"".equals(targetUrlParameter)){
			String targetUrl = request.getParameter(targetUrlParameter);
			if(StringUtils.hasText(targetUrl)){
				result = 1;
			}else{
				if(savedRequest != null){
					result = 2;
				}else{
					String refererUrl = request.getHeader("REFERER");
					if(useReferer && StringUtils.hasText(refererUrl)){
						result = 3;
					}else{
						result = 0;
					}
				}
			}
			
			return result;
		}
		
		if(savedRequest != null){
			result = 2;
			return result;
		}
		
		String refererUrl = request.getHeader("REFERER");
		if(useReferer && StringUtils.hasText(refererUrl)){
			result = 3;
		}else{
			result = 0;
		}
		
		return result;
	}
	
	public String getTargetUrlParameter() {
		return targetUrlParameter;
	}

	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public boolean isUseReferer() {
		return useReferer;
	}

	public void setUseReferer(boolean useReferer) {
		this.useReferer = useReferer;
	}
	
	public ReloadableFilterInvocationSecurityMetadataSource getRfisem() {
		return rfisem;
	}

	public void setRfisem(ReloadableFilterInvocationSecurityMetadataSource rfisem) {
		this.rfisem = rfisem;
	}

}
