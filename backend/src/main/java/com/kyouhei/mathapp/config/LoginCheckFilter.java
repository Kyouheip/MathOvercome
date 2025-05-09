package com.kyouhei.mathapp.config;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.kyouhei.mathapp.entity.User;

@Component
public class LoginCheckFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request,
							ServletResponse response,
							FilterChain chain) 
									throws IOException,ServletException{
		
		//URLパス取得のためダウンキャスト
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		
		String uri=req.getRequestURI();
		if(uri.startsWith("/session") || uri.startsWith("/api/session")) {
			//セッションが存在するか
			//引数なしは新しいセッションを作るのでfalseを指定
			HttpSession session=req.getSession(false);
			if(session==null) {
				//sessionがないのでログイン画面へ
				res.sendRedirect("http://localhost:3000/login");
			}else {
				//sessionにuserが存在するか
				User user=(User)session.getAttribute("user");
				
				if(user==null) {
					//userがないのでログイン画面へ
					res.sendRedirect("http://localhost:3000/login");
				}else {
					//loginしているのでcontrollerにrequestを渡す
					chain.doFilter(request, response);
				}
				
			}
			 
		}else {
			//check対象外よりcontrollerにrequestを渡す
			chain.doFilter(request, response);
		}
		
		
	}
	
	@Override
	public void init(FilterConfig filterConfig)
		throws ServletException{}
	
	@Override
	public void destroy() {}
}
