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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "DominioLotacao")
@NamedQueries({
    @NamedQuery(name = "DominioLotacao.findAll", query = "SELECT dl FROM DominioLotacao dl")})
public class DominioLotacao implements Serializable{
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idLotacao")
	private Integer idLotacao;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 180)
	@Column(name = "nome")
	private String nome;
	
    public DominioLotacao() {
    }

    public DominioLotacao(Integer idLotacao) {
        this.idLotacao = idLotacao;
    }


}
