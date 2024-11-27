
package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "disciplinasProjeto")
@IdClass(DisciplinaProjetoPK.class)
public class DisciplinaProjeto implements Serializable{
    
    @Id
    @ManyToOne
    @JoinColumn(name = "iddisciplina")
    private Disciplina iddisciplina;

    @Id
    @ManyToOne
    @JoinColumn(name = "idProjeto")
    private Projeto idProjeto;

    @Column(name = "nome", length = 50)
    private String nome;

    public Disciplina getIddisciplina() {
        return iddisciplina;
    }

    public void setIddisciplina(Disciplina iddisciplina) {
        this.iddisciplina = iddisciplina;
    }

    public Projeto getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Projeto idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.iddisciplina);
        hash = 31 * hash + Objects.hashCode(this.idProjeto);
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
        final DisciplinaProjeto other = (DisciplinaProjeto) obj;
        if (!Objects.equals(this.iddisciplina, other.iddisciplina)) {
            return false;
        }
        return Objects.equals(this.idProjeto, other.idProjeto);
    }

    
  
}
