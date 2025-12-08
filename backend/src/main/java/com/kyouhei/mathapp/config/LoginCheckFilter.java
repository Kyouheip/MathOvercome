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

import com.kyouhei.mathapp.entity.User;


public class LoginCheckFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request,
							ServletResponse response,
							FilterChain chain) 
									throws IOException,ServletException{
		
		//URLパス取得のためダウンキャスト
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		//クロスオリジン設定がないのでFilter内でつける?
		String origin = req.getHeader("Origin");

		if ("http://localhost:3000".equals(origin) 
			|| "https://math-overcome.vercel.app".equals(origin)
			|| "http://52.68.88.3".equals(origin)) {
		    res.setHeader("Access-Control-Allow-Origin", origin);
		}

		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type");
		
		// OPTIONSリクエストは許可して終了？
	    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
	        res.setStatus(HttpServletResponse.SC_OK);
	        return;
	    }
		
		String uri = req.getRequestURI();
		if(uri.startsWith("/session")) {
			//セッションが存在するか
			//引数なしは新しいセッションを作るのでfalseを指定
			HttpSession session = req.getSession(false);
			if(session == null) {
				//sessionがないので401
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				//res.sendRedirect("http://localhost:3000/login");
			}else {
				//sessionにuserが存在するか
				User user = (User)session.getAttribute("user");
				
				if(user == null) {
					//userがないので401
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					//res.sendRedirect("http://localhost:3000/login");
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
