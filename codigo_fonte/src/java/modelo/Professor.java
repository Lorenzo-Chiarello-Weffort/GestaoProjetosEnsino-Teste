/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "professor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Professor.findAll", query = "SELECT p FROM Professor p"),
    @NamedQuery(name = "Professor.findByProntuario", query = "SELECT p FROM Professor p WHERE p.prontuario = :prontuario"),
    @NamedQuery(name = "Professor.findByEmail", query = "SELECT p FROM Professor p WHERE p.email = :email"),
    @NamedQuery(name = "Professor.findByNome", query = "SELECT p FROM Professor p WHERE p.nome = :nome"),
    @NamedQuery(name = "Professor.findBySenha", query = "SELECT p FROM Professor p WHERE p.senha = :senha")})
public class Professor implements Serializable {

   
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "prontuario")
    private String prontuario;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "nome")
    private String nome;
    @Size(max = 255)
    @Column(name = "senha")
    private String senha;
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prontuario")
    private List<Projeto> projetoList;

    public Professor() {
    }

    public Professor(String prontuario) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<Projeto> getProjetoList() {
        return projetoList;
    }

    public void setProjetoList(List<Projeto> projetoList) {
        this.projetoList = projetoList;
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
        if (!(object instanceof Professor)) {
            return false;
        }
        Professor other = (Professor) object;
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Professor[ prontuario=" + prontuario + " ]";
    }
    
}
