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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "aluno")
@NamedQueries({
    @NamedQuery(name = "Aluno.findAll", query = "SELECT a FROM Aluno a"),
    @NamedQuery(name = "Aluno.findByProntuario", query = "SELECT a FROM Aluno a WHERE a.prontuario = :prontuario"),
    @NamedQuery(name = "Aluno.findByEmail", query = "SELECT a FROM Aluno a WHERE a.email = :email"),
    @NamedQuery(name = "Aluno.findByNome", query = "SELECT a FROM Aluno a WHERE a.nome = :nome"),
    @NamedQuery(name = "Aluno.findBySenha", query = "SELECT a FROM Aluno a WHERE a.senha = :senha"),
    @NamedQuery(name = "Aluno.findByCpf", query = "SELECT a FROM Aluno a WHERE a.cpf = :cpf"),
    @NamedQuery(name = "Aluno.findByRg", query = "SELECT a FROM Aluno a WHERE a.rg = :rg"),
    @NamedQuery(name = "Aluno.findByOrgaoEmissor", query = "SELECT a FROM Aluno a WHERE a.orgaoEmissor = :orgaoEmissor"),
    @NamedQuery(name = "Aluno.findBySemestre", query = "SELECT a FROM Aluno a WHERE a.semestre = :semestre"),
    @NamedQuery(name = "Aluno.findByCelular", query = "SELECT a FROM Aluno a WHERE a.celular = :celular"),
    @NamedQuery(name = "Aluno.findByBanco", query = "SELECT a FROM Aluno a WHERE a.banco = :banco"),
    @NamedQuery(name = "Aluno.findByAgencia", query = "SELECT a FROM Aluno a WHERE a.agencia = :agencia"),
    @NamedQuery(name = "Aluno.findByContaCorrente", query = "SELECT a FROM Aluno a WHERE a.contaCorrente = :contaCorrente"),
    @NamedQuery(name = "Aluno.findByTurno", query = "SELECT a FROM Aluno a WHERE a.turno = :turno")})
public class Aluno implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "prontuario")
    private String prontuario;
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "nome")
    private String nome;
    @Size(max = 255)
    @Column(name = "senha")
    private String senha;
    @Size(max = 14)
    @Column(name = "CPF")
    private String cpf;
    @Size(max = 12)
    @Column(name = "rg")
    private String rg;
    @Size(max = 3)
    @Column(name = "orgao_emissor")
    private String orgaoEmissor;
    @Column(name = "semestre")
    private Integer semestre;
    @Size(max = 14)
    @Column(name = "celular")
    private String celular;
    @Size(max = 20)
    @Column(name = "banco")
    private String banco;
    @Size(max = 20)
    @Column(name = "agencia")
    private String agencia;
    @Size(max = 20)
    @Column(name = "conta_corrente")
    private String contaCorrente;
    @Size(max = 20)
    @Column(name = "turno")
    private String turno;
    @Lob
    @Column(name = "historico")
    private byte[] historico;
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne(optional = false)
    private Curso idCurso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluno")
    private List<Horario> horarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluno")
    private List<Projetoaluno> projetoalunoList;

    public Aluno() {
    }

    public Aluno(String prontuario) {
        this.prontuario = prontuario;
    }

    public byte[] getHistorico() {
        return historico;
    }

    public void setHistorico(byte[] historico) {
        this.historico = historico;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    @XmlTransient
    public List<Horario> getHorarioList() {
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    @XmlTransient
    public List<Projetoaluno> getProjetoalunoList() {
        return projetoalunoList;
    }

    public void setProjetoalunoList(List<Projetoaluno> projetoalunoList) {
        this.projetoalunoList = projetoalunoList;
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
        if (!(object instanceof Aluno)) {
            return false;
        }
        Aluno other = (Aluno) object;
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Aluno[ prontuario=" + prontuario + " ]";
    }

}
