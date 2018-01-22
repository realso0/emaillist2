package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.ldap.Rdn;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.EmaillistDao;
import com.javaex.vo.EmaillistVo;


@WebServlet("/el") // /el은 절대경로 http://localhost:8088/emaillist2/el/

//action=form, a=form는 구분하기 위한 수단일 뿐 다른 구분기호를 써도 상관없음.
public class emaillistServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("환영합니다. Servlet"); //시험가동, 화면에는 아무것도 안보이지만, 출력창에 입력한 글이 출력됨
		
		String actionform=request.getParameter("a");
		//브라우저로 부터 요청받은 것이므로, 요청문서만을 받고 요청문서를 서버에서 서버의 언어인 request로 변환함
		
		if("form".equals(actionform)) { //form.jsp는 controller가 따로 전달해줄 데이터는 없지만, request, response는 전달해주어야함
			//System.out.println("form 진입");
			RequestDispatcher rd=request.getRequestDispatcher("form.jsp"); //값을 입력받아, RequestDispatcher객체가 만들어지고, rd로 객체를 받는다. 객체 내의 getRequestDispatcher메소드를 이용해, form.jsp로 보낸다.
			rd.forward(request, response); //rd의 forword메소드를 이용하여, request문서와 response문서를 form.jsp로 보낸다.
		} else if ("insert".equals(actionform)) {
			//System.out.println("insert 진입"); //화면으로 이동은 하지만, 화면에 보여지는 것은 없음.
			String lastName=request.getParameter("ln");
			String firstName=request.getParameter("fn");
			String email=request.getParameter("email");
			
			EmaillistVo vo=new EmaillistVo();
			
			vo.setLastName(lastName);
			vo.setFirstName(firstName);
			vo.setEmail(email);
			
			//System.out.println(vo.toString()); //잘 입력받았는지 확인 작업
			
			EmaillistDao dao=new EmaillistDao();
			
			dao.insert(vo); //sql.developer에 값이 잘 추가 됬는지 확인
			
			response.sendRedirect("el?a=list");
			//제일 마지막 작업 redirect, 기존에 있던 페이지 이용
		} else if ("list".equals(actionform)) {
			System.out.println("list 진입");
			
			EmaillistDao dao=new EmaillistDao();
			
			List<EmaillistVo> list=dao.getList(); 
			
			//어떤 데이터가 올지 모르므로, RequestDispatcher 객체에 담아 보냄.
			request.setAttribute("elist", list); //리스트 실어 보내기, request.setAttribute(부를 이름, 보낼 데이터), 꺼내쓸 때는 getAttribute
			RequestDispatcher rd=request.getRequestDispatcher("list.jsp"); //포워드 작업
			rd.forward(request,response);
			
		} else {
			System.out.println("잘못된 a값 처리");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response); //doPost로 요청해도 doGet이 실행되고, doGet을 실행하면 그대로 doGet실행
	}

}
