package controller;

import dao.HorarioDAO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;
import modelo.Horario;

@Named
@SessionScoped
public class HorarioController implements Serializable {

    private Horario horario = new Horario();
    private Horario horarioSelecionado = new Horario();
    private List<Horario> horarios = new ArrayList<>();
    private List<Horario> horariosC = new ArrayList<>();
    private HorarioDAO horarioDAO = new HorarioDAO();

    @Inject
    private LoginController usuarioSessao;

    private Aluno aluno;

    @PostConstruct
    public void HorarioController() {
        this.aluno = usuarioSessao.getAlunoAutenticado();
        carregarHorario();

    }

    public void carregarHorario() {
        horariosC = horarioDAO.findAllByAluno(this.aluno.getProntuario());
    }

    public List<Horario> getHorariosC() {
        return horariosC;
    }

    public void setHorariosC(List<Horario> horariosC) {
        this.horariosC = horariosC;
    }

    public LoginController getUsuarioSessao() {
        return usuarioSessao;
    }

    public void setUsuarioSessao(LoginController usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Horario getHorarioSelecionado() {
        return horarioSelecionado;
    }

    public void setHorarioSelecionado(Horario horarioSelecionado) {
        this.horarioSelecionado = horarioSelecionado;
    }

    // Método para adicionar um horário à lista
    public void adicionarHorario() {
        horarios.add(new Horario(horario.getDiaSemana(), horario.getHora()));
        horario = new Horario();
        carregarHorario();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Horario Adicionado com sucesso", ""));
    }

    // Método para editar um horário na lista
    public void editarHorario(Horario h) {
        for (Horario horarioItem : horarios) {
            if (horarioItem.equals(h)) {
                horarioItem.setDiaSemana(h.getDiaSemana());
                horarioItem.setHora(h.getHora());
                carregarHorario();
                break;
            }
        }
    }

    // Método para remover um horário da lista
    public void removerHorario(Horario h) {
        horarios.remove(h);
        carregarHorario();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Horario removido com sucesso", ""));

    }

    public void excluirHorario() {
        horarioDAO.remove(horarioSelecionado.getIdhorario(), horarioSelecionado.getAluno().getProntuario());
        carregarHorario();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Horario excluido com sucesso", ""));
    }

    // Método para salvar os horários do aluno
    public void salvarHorarios() {
        if (aluno != null && !horarios.isEmpty()) {
            for (Horario h : horarios) {
                h.setAluno(aluno);  // Associar o horário ao aluno
                horarioDAO.create(h);
                carregarHorario();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Horarios salvos com sucesso", ""));
            }
            // Limpar a lista de horários após salvar
            horarios.clear();
        }
    }
}
