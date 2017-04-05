package br.com.pdtic.security;




import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.pdtic.entidade.Usuario;
import br.com.pdtic.util.JSFUtils;



@Named("security")
@SessionScoped
public class SecurityService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Usuario user;
	
	public boolean hasRole(String role) {
		return true;
	}
	

	
	/**
	 * @return Nome do usuário Logado
	 */
	public String getUserName() {
		return user == null ? "" :  user.getNome();
	}
	
	
	/**
	 * @return <code>true</code> se o usuário estiver 
	 * logado e <code>false</code> caso contrário
	 */
	public boolean loggedIn() {
		return user != null;
	}
	
	/**
	 * Efetua o <code>logout</code> do usuário
	 */
	public void logout() {
		try {
			JSFUtils.redirect("/logout");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return Usuário logado
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * Seta o usuário logado e a função do mesmo
	 * 
	 * @param user usuário logado
	 */
	protected void setUser(Usuario user) {
		this.user = user;
	}

	
}
