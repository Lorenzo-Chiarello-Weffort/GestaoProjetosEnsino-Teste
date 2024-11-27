/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author Paulo
 */
public class HorarioPK implements Serializable {

  
    private String prontuario;
    private int idhorario;

    public HorarioPK() {
    }

    public HorarioPK(String prontuario, int idhorario) {
        this.prontuario = prontuario;
        this.idhorario = idhorario;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public int getIdhorario() {
        return idhorario;
    }

    public void setIdhorario(int idhorario) {
        this.idhorario = idhorario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prontuario != null ? prontuario.hashCode() : 0);
        hash += (int) idhorario;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioPK)) {
            return false;
        }
        HorarioPK other = (HorarioPK) object;
        if ((this.prontuario == null && other.prontuario != null) || (this.prontuario != null && !this.prontuario.equals(other.prontuario))) {
            return false;
        }
        if (this.idhorario != other.idhorario) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.HorarioPK[ prontuario=" + prontuario + ", idhorario=" + idhorario + " ]";
    }
    
}
