/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "csp")
@NamedQueries({
    @NamedQuery(name = "Csp.findAll", query = "SELECT c FROM Csp c"),
    @NamedQuery(name = "Csp.findByProntuario", query = "SELECT c FROM Csp c WHERE c.prontuario = :prontuario"),
    @NamedQuery(name = "Csp.findByEmail", query = "SELECT c FROM Csp c WHERE c.email = :email"),
    @NamedQuery(name = "Csp.findByNome", query = "SELECT c FROM Csp c WHERE c.nome = :nome"),
    @NamedQuery(name = "Csp.findBySenha", query = "SELECT c FROM Csp c WHERE c.senha = :senha")})
public class Csp implements Serializable {

    @Id
    @Column(name = "prontuario")
    private String prontuario;
    @Column(name = "email")
    private String email;
    @Column(name = "nome")
    private String nome;
    @Column(name = "senha")
    private String senha;

    public Csp() {
    }

    public Csp(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prontuario != null ? prontuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Csp)) {
            return false;
        }
        Csp other = (Csp) object;
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Csp[ prontuario=" + prontuario + " ]";
    }
    
}
