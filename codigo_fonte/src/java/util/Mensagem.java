package util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 *
 * @author Lorenzo
 */
public class Mensagem {

    public static void addMessageInformation(String message) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", message));
    }

    public static void addMessageWarning(String message) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", message));
    }

    public static void addMessageError(String message) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", message));
    }

}
