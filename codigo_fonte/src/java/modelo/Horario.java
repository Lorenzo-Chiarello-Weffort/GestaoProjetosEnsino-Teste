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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "horario")
@IdClass(HorarioPK.class)
@NamedQueries({
    @NamedQuery(name = "Horario.findAll", query = "SELECT h FROM Horario h"),
    @NamedQuery(name = "Horario.findByProntuario", query = "SELECT h FROM Horario h WHERE h.horarioPK.prontuario = :prontuario"),
    @NamedQuery(name = "Horario.findByIdhorario", query = "SELECT h FROM Horario h WHERE h.horarioPK.idhorario = :idhorario"),
    @NamedQuery(name = "Horario.findByDiaSemana", query = "SELECT h FROM Horario h WHERE h.diaSemana = :diaSemana"),
    @NamedQuery(name = "Horario.findByHora", query = "SELECT h FROM Horario h WHERE h.hora = :hora")})
public class Horario implements Serializable {
   
    @Id
    @JoinColumn(name = "prontuario", referencedColumnName = "prontuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)   
    private Aluno aluno;
    
    @Id
    @Column(name = "idhorario")
    private Integer idhorario;
 
    @Size(max = 45)
    @Column(name = "dia_semana")
    private String diaSemana;
    @Size(max = 15)
    @Column(name = "hora")
    private String hora;
   

    public Horario() {
    }

    public Horario( String diaSemana, String hora) {
        this.diaSemana = diaSemana;
        this.hora = hora;
    }

    public Integer getIdhorario() {
        return idhorario;
    }

    public void setIdhorario(Integer idhorario) {
        this.idhorario = idhorario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.aluno);
        hash = 23 * hash + Objects.hashCode(this.idhorario);
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
        final Horario other = (Horario) obj;
        if (!Objects.equals(this.aluno, other.aluno)) {
            return false;
        }
        return Objects.equals(this.idhorario, other.idhorario);
    }

   
}
