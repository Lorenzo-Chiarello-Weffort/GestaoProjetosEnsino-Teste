/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Lorenzo
 */

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.servlet.http.HttpSession;

public class AutenticacaoPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String paginaAtual = facesContext.getViewRoot().getViewId();

        // Páginas públicas que não precisam de autenticação
        boolean paginaPublica = paginaAtual.contains("login.xhtml") || paginaAtual.contains("index.xhtml") || paginaAtual.contains("professorCadastro.xhtml") || paginaAtual.contains("alunoCadastro.xhtml");

        // Verificar se o usuário está autenticado
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        // Se o usuário não estiver logado e a página não for pública, redirecionar para o login
        if (!paginaPublica && usuarioLogado == null) {
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "login?faces-redirect=true");
            facesContext.responseComplete();
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {}

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}

