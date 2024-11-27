/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "edital")
@NamedQueries({
    @NamedQuery(name = "Edital.findAll", query = "SELECT e FROM Edital e"),
    @NamedQuery(name = "Edital.findByIdedital", query = "SELECT e FROM Edital e WHERE e.idedital = :idedital"),
    @NamedQuery(name = "Edital.findByDataabertura", query = "SELECT e FROM Edital e WHERE e.dataabertura = :dataabertura"),
    @NamedQuery(name = "Edital.findByDatafechamento", query = "SELECT e FROM Edital e WHERE e.datafechamento = :datafechamento"),
    @NamedQuery(name = "Edital.findByResumoedital", query = "SELECT e FROM Edital e WHERE e.resumoedital = :resumoedital"),
    @NamedQuery(name = "Edital.findByTipoedital", query = "SELECT e FROM Edital e WHERE e.tipoedital = :tipoedital")})
public class Edital implements Serializable {

    @Id
    @Column(name = "idedital")
    private String idedital;

    @Column(name = "dataabertura")
    @Temporal(TemporalType.DATE)
    private Date dataabertura;

    @Column(name = "datafechamento")
    @Temporal(TemporalType.DATE)
    private Date datafechamento;

    @Lob
    @Column(name = "docpdfedital")
    private byte[] docpdfedital;

    @Size(max = 255)
    @Column(name = "resumoedital")
    private String resumoedital;
    
    @Column(name = "status")
    private String status;
    
    @Size(max = 20)
    @Column(name = "tipoedital")
    private String tipoedital;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProjeto")
    private List<Projeto> ProjetolList;

    public Edital() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getIdedital() {
        return idedital;
    }

    public void setIdedital(String idedital) {
        this.idedital = idedital;
    }

    public Date getDataabertura() {
        return dataabertura;
    }

    public void setDataabertura(Date dataabertura) {
        this.dataabertura = dataabertura;
    }

    public Date getDatafechamento() {
        return datafechamento;
    }

    public void setDatafechamento(Date datafechamento) {
        this.datafechamento = datafechamento;
    }

    public byte[] getDocpdfedital() {
        return docpdfedital;
    }

    public void setDocpdfedital(byte[] docpdfedital) {
        this.docpdfedital = docpdfedital;
    }

    public String getResumoedital() {
        return resumoedital;
    }

    public void setResumoedital(String resumoedital) {
        this.resumoedital = resumoedital;
    }

    public String getTipoedital() {
        return tipoedital;
    }

    public void setTipoedital(String tipoedital) {
        this.tipoedital = tipoedital;
    }

    public List<Projeto> getProjetolList() {
        return ProjetolList;
    }

    public void setProjetolList(List<Projeto> ProjetolList) {
        this.ProjetolList = ProjetolList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idedital);
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
        final Edital other = (Edital) obj;
        return Objects.equals(this.idedital, other.idedital);
    }

    @Override
    public String toString() {
        return "Edital{" + "idedital=" + idedital + ", dataabertura=" + dataabertura + ", datafechamento=" + datafechamento + ", docpdfedital=" + docpdfedital + ", resumoedital=" + resumoedital + ", status=" + status + ", tipoedital=" + tipoedital + ", ProjetolList=" + ProjetolList + '}';
    }
    

}
