/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "projetoaluno")
@IdClass(ProjetoalunoPK.class)
@NamedQueries({
    @NamedQuery(name = "Projetoaluno.findAll", query = "SELECT p FROM Projetoaluno p"),
    @NamedQuery(name = "Projetoaluno.findByIdProjeto", query = "SELECT p FROM Projetoaluno p WHERE p.projetoalunoPK.idProjeto = :idProjeto"),
    @NamedQuery(name = "Projetoaluno.findByProntuario", query = "SELECT p FROM Projetoaluno p WHERE p.projetoalunoPK.prontuario = :prontuario"),
    @NamedQuery(name = "Projetoaluno.findByRemunerado", query = "SELECT p FROM Projetoaluno p WHERE p.remunerado = :remunerado"),
    @NamedQuery(name = "Projetoaluno.findByValorbolsa", query = "SELECT p FROM Projetoaluno p WHERE p.valorbolsa = :valorbolsa")})
public class Projetoaluno implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "idProjeto", referencedColumnName = "idProjeto")
    private Projeto idProjeto;


    @Id
    @ManyToOne
    @JoinColumn(name = "prontuario", referencedColumnName = "prontuario")
    private Aluno prontuario;

    @Size(max = 1)
    @Column(name = "remunerado")
    private String remunerado;
    @Size(max = 45)
    @Column(name = "valorbolsa")
    private String valorbolsa;
    @Column(name = "dataEntrevista")
    @Temporal(TemporalType.DATE)
    private Date dataEntrevista;
    @Size(max = 45)
    @Column(name = "salaHora")
    private String salaHora;
    @Size(max = 10)
    @Column(name = "statusAluno")
    private String statusAluno;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projetoaluno")
    private List<Presenca> presencaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projetoaluno")
    private List<Relatoriopresencamensal> relatoriopresencamensalList;

    public Projetoaluno() {
    }

    public Projetoaluno(Projeto idProjeto, Aluno prontuario, String remunerado, String valorbolsa, List<Presenca> presencaList, List<Relatoriopresencamensal> relatoriopresencamensalList) {
        this.idProjeto = idProjeto;
        this.prontuario = prontuario;
        this.remunerado = remunerado;
        this.valorbolsa = valorbolsa;
        this.presencaList = presencaList;
        this.relatoriopresencamensalList = relatoriopresencamensalList;
    }

    public String getSalaHora() {
        return salaHora;
    }

    public void setSalaHora(String salaHora) {
        this.salaHora = salaHora;
    }

    public String getStatusAluno() {
        return statusAluno;
    }

    public void setStatusAluno(String statusAluno) {
        this.statusAluno = statusAluno;
    }

    public Projeto getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Projeto idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Aluno getProntuario() {
        return prontuario;
    }

    public void setProntuario(Aluno prontuario) {
        this.prontuario = prontuario;
    }

    public String getRemunerado() {
        return remunerado;
    }

    public void setRemunerado(String remunerado) {
        this.remunerado = remunerado;
    }

    public String getValorbolsa() {
        return valorbolsa;
    }

    public void setValorbolsa(String valorbolsa) {
        this.valorbolsa = valorbolsa;
    }

    public Date getDataEntrevista() {
        return dataEntrevista;
    }

    public void setDataEntrevista(Date dataEntrevista) {
        this.dataEntrevista = dataEntrevista;
    }

    @XmlTransient
    public List<Presenca> getPresencaList() {
        return presencaList;
    }

    public void setPresencaList(List<Presenca> presencaList) {
        this.presencaList = presencaList;
    }

    @XmlTransient
    public List<Relatoriopresencamensal> getRelatoriopresencamensalList() {
        return relatoriopresencamensalList;
    }

    public void setRelatoriopresencamensalList(List<Relatoriopresencamensal> relatoriopresencamensalList) {
        this.relatoriopresencamensalList = relatoriopresencamensalList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idProjeto);
        hash = 67 * hash + Objects.hashCode(this.prontuario);
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
        final Projetoaluno other = (Projetoaluno) obj;
        if (!Objects.equals(this.idProjeto, other.idProjeto)) {
            return false;
        }
        return Objects.equals(this.prontuario, other.prontuario);
    }

    @Override
    public String toString() {
        return "Projetoaluno{" + "idProjeto=" + idProjeto.getTitulo() + ", prontuario=" + prontuario + ", remunerado=" + remunerado + ", valorbolsa=" + valorbolsa + ", dataEntrevista=" + dataEntrevista + ", salaHora=" + salaHora + ", statusAluno=" + statusAluno + '}';
    }

    

}
