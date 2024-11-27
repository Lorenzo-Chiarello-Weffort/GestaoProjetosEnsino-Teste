/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "presenca")
@IdClass(PresencaPK.class)
@NamedQueries({
    @NamedQuery(name = "Presenca.findAll", query = "SELECT p FROM Presenca p"),
    @NamedQuery(name = "Presenca.findByProntuario", query = "SELECT p FROM Presenca p WHERE p.presencaPK.prontuario = :prontuario"),
    @NamedQuery(name = "Presenca.findByIdProjeto", query = "SELECT p FROM Presenca p WHERE p.presencaPK.idProjeto = :idProjeto"),
    @NamedQuery(name = "Presenca.findByData", query = "SELECT p FROM Presenca p WHERE p.presencaPK.data = :data"),
    @NamedQuery(name = "Presenca.findByObservacao", query = "SELECT p FROM Presenca p WHERE p.observacao = :observacao"),
    @NamedQuery(name = "Presenca.findByQthoras", query = "SELECT p FROM Presenca p WHERE p.qthoras = :qthoras"),
    @NamedQuery(name = "Presenca.findByParecerprofessor", query = "SELECT p FROM Presenca p WHERE p.parecerprofessor = :parecerprofessor")})
public class Presenca implements Serializable {
    
    @Id
     @JoinColumns({
        @JoinColumn(name = "idProjeto", referencedColumnName = "idProjeto", insertable = false, updatable = false),
        @JoinColumn(name = "prontuario", referencedColumnName = "prontuario", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Projetoaluno projetoaluno;
    
    @Id
    private Date data;
    
    @Size(max = 255)
    @Column(name = "observacao")
    private String observacao;
    
    @Column(name = "qthoras")
    private Integer qthoras;
    
    @Size(max = 255)
    @Column(name = "parecerprofessor")
    private String parecerprofessor;
   

    public Presenca() {
    }

    public Presenca(Projetoaluno projetoaluno, Date data, String observacao, Integer qthoras, String parecerprofessor) {
        this.projetoaluno = projetoaluno;
        this.data = data;
        this.observacao = observacao;
        this.qthoras = qthoras;
        this.parecerprofessor = parecerprofessor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getQthoras() {
        return qthoras;
    }

    public void setQthoras(Integer qthoras) {
        this.qthoras = qthoras;
    }

    public String getParecerprofessor() {
        return parecerprofessor;
    }

    public void setParecerprofessor(String parecerprofessor) {
        this.parecerprofessor = parecerprofessor;
    }

    public Projetoaluno getProjetoaluno() {
        return projetoaluno;
    }

    public void setProjetoaluno(Projetoaluno projetoaluno) {
        this.projetoaluno = projetoaluno;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.projetoaluno);
        hash = 47 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Presenca other = (Presenca) obj;
        if (!Objects.equals(this.projetoaluno, other.projetoaluno)) {
            return false;
        }
        return Objects.equals(this.data, other.data);
    }

   
    
}
