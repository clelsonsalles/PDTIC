/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pdtic.dao;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;

import br.com.pdtic.entidade.Usuario;
import br.com.pdtic.util.PasswordUtils;

@Stateless
public class UsuarioDAO extends AbstractDAO<Usuario> {
    
    public UsuarioDAO() {
        super(Usuario.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Usuário";
    }

    public Usuario login(String login, String password) throws Exception {
        String select = "SELECT t FROM Usuario t WHERE t.email = :nmLogin and t.senha = :nmSenha";

        HashMap<String, Object> map = new HashMap<>();
        map.put("nmLogin", login);
        map.put("nmSenha", PasswordUtils.getMD5(password).toUpperCase());
        Usuario tbUsuario = selectObject(select, map);

        return tbUsuario;
    }

    public Usuario findByEmail(String eeEmail) throws NonUniqueResultException {
    	Usuario usuario = null;
    	try {
    		String select = "SELECT t FROM Usuario t WHERE t.email = :eeEmail";
    		HashMap<String, Object> map = new HashMap<>();
    		map.put("eeEmail", eeEmail);
    		usuario = selectObject(select, map);
    	} catch (NonUniqueResultException e) {
    		throw e;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return usuario;
    }

    public Usuario findByLogin(String login) throws Exception {
        String select = "SELECT t FROM Usuario t WHERE t.email = :nmLogin";

        HashMap<String, Object> map = new HashMap<>();
        map.put("nmLogin", login);
        Usuario tbUsuario = selectObject(select, map);

        return tbUsuario;
    }

    
    
}
