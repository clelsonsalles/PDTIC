package br.com.pdtic.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


@WebServlet(description = "Servlet responsável por efetuar o login do usuário", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOGGED_INDEX_PAGE = 
			"pdtic/002_instrucoes.xhtml";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	    String versao = config.getServletContext().getInitParameter("versao");

	    config.getServletContext().setAttribute("versao", versao);
	    
		super.init(config);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
	    String versao = context.getInitParameter("versao");
	    
		request.setAttribute("versao", versao);
		sendRedirect(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		List<String> errors = new ArrayList<String>();
		try {
			if(request.getUserPrincipal() == null) {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				
				if (StringUtils.isEmpty(username)) {
					errors.add("Usuário é obrigatório");
				}
				
				if (StringUtils.isEmpty(password)) {
					errors.add("Senha é obrigatório");
				}
				
				if(errors == null || errors.isEmpty()) {
					throw new Exception("Campos obrigatórios não preenchidos");
				}
				
				request.login(username, password);
			}
			
			sendRedirect(request, response);
		} catch (Exception e) {
			String errorMessage = (String) request.getAttribute("errorMessage");

			request.setAttribute("errors", errors);
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
			dispatcher.forward(request, response);
		}
	}
	
	private void sendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pageToSend = request.getContextPath();
		
		if(request.getUserPrincipal() != null) {
			pageToSend = pageToSend.concat(LOGGED_INDEX_PAGE);

		} else {
			pageToSend = pageToSend.concat("index.html");
		}
		
		response.sendRedirect(pageToSend);
	}
	
}
