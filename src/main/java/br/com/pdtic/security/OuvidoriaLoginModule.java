package br.com.pdtic.security;


import java.security.acl.Group;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

import br.com.pdtic.dao.UsuarioDAO;
import br.com.pdtic.entidade.Usuario;
import br.com.pdtic.helper.CdiHelper;


public class OuvidoriaLoginModule extends UsernamePasswordLoginModule {
	
	@Inject
	private SecurityService securityService;
	
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private UsuarioDAO usuarioDAO;

	private Usuario user;
	
	@SuppressWarnings("rawtypes")
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		
		// inicializa classes com injeção de dependência
		try {
			CdiHelper.programmaticInjection(OuvidoriaLoginModule.class, this);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String getUsersPassword() throws LoginException {
		return "";
	}
	
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {
		return doLogin(getUsername(), inputPassword);
	}
	
	private boolean doLogin(String login, String password) {
		boolean logged = Boolean.FALSE;
		try {
			// Tenta logar um usuário (login e senha)
            user = usuarioDAO.login(login, password);
            if (user != null) {
            } else {
            	request.setAttribute("errorMessage", "Login ou senha inválidos");
            }
        } catch (Exception e) {
        	System.err.println("Erro ao tentar efetuar login do usuário: " + login);
        	request.setAttribute("errorMessage", "Login ou senha inválidos");
        	e.printStackTrace();
        }
		
		return logged;
    }
	
	
	@Override
	protected Group[] getRoleSets() throws LoginException {
		SimpleGroup group = new SimpleGroup("Roles");
		
		return new Group[] { group };
	}
	
}
