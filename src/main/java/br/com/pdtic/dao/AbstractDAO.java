/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pdtic.dao;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

//import br.com.pdtic.security.SecurityService;

/**
 * @author renato
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractDAO<T> {
	/*
	@Inject
	private SecurityService securityService;
	*/

    @PersistenceContext(unitName = "ouvidoriaPersistenceUnit")
    private EntityManager em;
    private Class<T> entityClass;

    public abstract String getNomeEntidade();

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    
    /**
     * Converte um objeto do tipo java.sql.Clob em String
     * 
     * @param obj
     * @return
     * @throws Exception
     */
	protected String converterTextoClob(Clob clob) throws Exception{
		
		Reader reader = clob.getCharacterStream();
		int c = -1;
		StringBuilder sb = new StringBuilder();
		while((c = reader.read()) != -1) {
			sb.append(((char)c));
		}
		
		String texto =  sb.toString();
		return texto;
	}


	
    public void refresh(T entity) {
        try {
            getEntityManager().flush();
            getEntityManager().refresh(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(T entity) throws Exception {
        try {
            getEntityManager().persist(entity);
            //saveLog(LogOperacaoEnum.INSERIR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    public void edit(T entity) throws Exception {
        try {
            getEntityManager().merge(entity);
            //saveLog(LogOperacaoEnum.ALTERAR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void clear() {
        getEntityManager().clear();
    }

    public void remove(T entity) throws Exception {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            //saveLog(LogOperacaoEnum.EXCLUIR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public T find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }

	public List<T> findAll() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public List<T> findRange(int[] range) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public int count() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
            cq.select(getEntityManager().getCriteriaBuilder().count(rt));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public T getObject(String entityMethod, HashMap<String, ?> map) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);

        for (String key : map.keySet())
            query.setParameter(key, map.get(key));

        try {
            return (T) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
    }

    public T selectObject(String select, HashMap<String, ?> map) throws Exception {
        try {
            Query query = em.createQuery(select);
            
            for (String key : map.keySet()) {
            	query.setParameter(key, map.get(key));
            }
            
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
        	// Do nothing
    	} catch (NonUniqueResultException e) {
    		throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> selectList(String select, HashMap<String, ?> map) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> selectList(String select, HashMap<String, ?> map, Integer first, Integer qtd) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> getList(String entityMethod, HashMap<String, ?> map) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> selectList(String select, HashMap<String, ?> map, int first, int qtd) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> getList(String entityMethod, HashMap<String, ?> map, int first, int qtd) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }

        return res;
    }



    /**
     * @return the entityClass
     */
	public Class getEntityClass() {
        return entityClass;
    }
}
