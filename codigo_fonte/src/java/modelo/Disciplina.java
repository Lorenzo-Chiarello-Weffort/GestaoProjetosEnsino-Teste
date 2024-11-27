package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "disciplina")
@NamedQueries({
    @NamedQuery(name = "Disciplina.findAll", query = "SELECT d FROM Disciplina d"),
    @NamedQuery(name = "Disciplina.findByIddisciplina", query = "SELECT d FROM Disciplina d WHERE d.iddisciplina = :iddisciplina"),
    @NamedQuery(name = "Disciplina.findByNome", query = "SELECT d FROM Disciplina d WHERE d.nome = :nome")})
public class Disciplina implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddisciplina")
    private Integer iddisciplina;
    
    @Size(max = 50)
    @Column(name = "nome")
    private String nome;
    
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne(optional = false)
    private Curso idCurso;
    
    @JoinColumn(name = "idProjeto", referencedColumnName = "idProjeto")
    @ManyToOne(optional = false)
    private Projeto idProjeto;

    public Disciplina() {
    }
    
    public Integer getIddisciplina() {
        return iddisciplina;
    }

    public void setIddisciplina(Integer iddisciplina) {
        this.iddisciplina = iddisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Projeto getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Projeto idProjeto) {
        this.idProjeto = idProjeto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.iddisciplina);
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
        final Disciplina other = (Disciplina) obj;
        return Objects.equals(this.iddisciplina, other.iddisciplina);
    }
    
    
    
}