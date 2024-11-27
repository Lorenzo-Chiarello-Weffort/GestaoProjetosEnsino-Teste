/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.Column;
import java.io.Serializable;

/**
 *
 * @author Paulo
 */
public class RelatoriopresencamensalPK implements Serializable {

  
    @Column(name = "idrelatorio")
    private String idrelatorio;
 
    @Column(name = "projetoAlunoidProjeto")
    private int projetoAlunoidProjeto;
   
    @Column(name = "projetoAlunoprontuario")
    private String projetoAlunoprontuario;

    public RelatoriopresencamensalPK() {
    }

    public RelatoriopresencamensalPK(String idrelatorio, int projetoAlunoidProjeto, String projetoAlunoprontuario) {
        this.idrelatorio = idrelatorio;
        this.projetoAlunoidProjeto = projetoAlunoidProjeto;
        this.projetoAlunoprontuario = projetoAlunoprontuario;
    }

    public String getIdrelatorio() {
        return idrelatorio;
    }

    public void setIdrelatorio(String idrelatorio) {
        this.idrelatorio = idrelatorio;
    }

    public int getProjetoAlunoidProjeto() {
        return projetoAlunoidProjeto;
    }

    public void setProjetoAlunoidProjeto(int projetoAlunoidProjeto) {
        this.projetoAlunoidProjeto = projetoAlunoidProjeto;
    }

    public String getProjetoAlunoprontuario() {
        return projetoAlunoprontuario;
    }

    public void setProjetoAlunoprontuario(String projetoAlunoprontuario) {
        this.projetoAlunoprontuario = projetoAlunoprontuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idrelatorio != null ? idrelatorio.hashCode() : 0);
        hash += (int) projetoAlunoidProjeto;
        hash += (projetoAlunoprontuario != null ? projetoAlunoprontuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelatoriopresencamensalPK)) {
            return false;
        }
        RelatoriopresencamensalPK other = (RelatoriopresencamensalPK) object;
        if ((this.idrelatorio == null && other.idrelatorio != null) || (this.idrelatorio != null && !this.idrelatorio.equals(other.idrelatorio))) {
            return false;
        }
        if (this.projetoAlunoidProjeto != other.projetoAlunoidProjeto) {
            return false;
        }
        if ((this.projetoAlunoprontuario == null && other.projetoAlunoprontuario != null) || (this.projetoAlunoprontuario != null && !this.projetoAlunoprontuario.equals(other.projetoAlunoprontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.RelatoriopresencamensalPK[ idrelatorio=" + idrelatorio + ", projetoAlunoidProjeto=" + projetoAlunoidProjeto + ", projetoAlunoprontuario=" + projetoAlunoprontuario + " ]";
    }
    
}
