package com.model2.mvc.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.util.HttpUtil;


public class ActionServlet extends HttpServlet {
	
	private RequestMapping mapper;

	@Override
	public void init() throws ServletException {
		super.init();
		String resources=getServletConfig().getInitParameter("resources");
		mapper=RequestMapping.getInstance(resources);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = request.getRequestURI();
		System.out.println("url"+url);
		String contextPath = request.getContextPath();
		System.out.println("contextPath : "+contextPath);
		String path = url.substring(contextPath.length());
		System.out.println(path);
		
		try{
			//RequestMapping mapper
			Action action = mapper.getAction(path);
			action.setServletContext(getServletContext()); // servletcontext주고
			
			String resultPage=action.execute(request, response); //일해 return "redirect:/getUser.do?userId="+userId;
			String result=resultPage.substring(resultPage.indexOf(":")+1); //파싱 /getUser.do?userId="+userId;

			System.out.println("resultPage : "+resultPage);
			System.out.println("result입니다 : "+result);
			
			if(resultPage.startsWith("forward:")) { //포워드 인지 리다이렉트인지
				HttpUtil.forward(request, response, result);
				System.out.println("*************forward*****************");
				}
			else {
				HttpUtil.redirect(response, result);//getUser.do?userId="+userId;
				System.out.println("*************redirect*****************");
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}