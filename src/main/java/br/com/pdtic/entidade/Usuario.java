/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pdtic.entidade;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "Usuario")
@NamedQueries({
    @NamedQuery(name = "usuario.findAll", query = "SELECT t FROM Usuario t")})
public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idUsuario")
	private Integer idUsuario;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 180)
	@Column(name = "nome")
	private String nome;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 200)
	@Column(name = "email", unique = true)
	private String email;
	
	
	@Basic(optional = false)
	@Size(max = 32)
	@NotNull
	@Column(name = "senha")
	private String senha;
	
	@Column(name = "unidade")
	private String unidade;
	
    @JoinColumn(name = "cargo", referencedColumnName = "idCargo")
    @ManyToOne
    private DominioCargo cargo;

    @JoinColumn(name = "lotacao", referencedColumnName = "idLotacao")
    @ManyToOne
    private DominioLotacao lotacao;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }


}
