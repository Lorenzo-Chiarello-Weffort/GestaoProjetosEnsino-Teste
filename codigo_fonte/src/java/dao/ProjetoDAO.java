package dao;

import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Projeto;
import modelo.Edital;
import modelo.Professor;

@Stateless
public class ProjetoDAO {

    public void create(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("O objeto Projeto está nulo.");
        }

        String insertQuery = "INSERT INTO projeto (titulo, resumo, duracaoSemestre, numBolsista, remuneracao, "
                + "cargaSemanal, perfilBolsista, atividadesPrevistas, dataInicio, "
                + "dataTermino, status, arquivopdf, relatoriofinalpdf, "
                + "prontuario, idedital, observacoes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {

            // Logs para depuração
            System.out.println("Criando Projeto: " + projeto.getTitulo());

            // Definir os parâmetros da query
            pst.setString(1, projeto.getTitulo());
            pst.setString(2, projeto.getResumo());
            pst.setObject(3, projeto.getDuracaoSemestre(), java.sql.Types.INTEGER);
            pst.setObject(4, projeto.getNumBolsista(), java.sql.Types.INTEGER);
            pst.setObject(5, projeto.getRemuneracao(), java.sql.Types.FLOAT);
            pst.setObject(6, projeto.getCargaSemanal(), java.sql.Types.FLOAT);
            pst.setString(7, projeto.getPerfilBolsista());
            pst.setString(8, projeto.getAtividadesPrevistas());
            pst.setDate(9, projeto.getDataInicio() != null ? new java.sql.Date(projeto.getDataInicio().getTime()) : null);
            pst.setDate(10, projeto.getDataTermino() != null ? new java.sql.Date(projeto.getDataTermino().getTime()) : null);
            pst.setString(11, projeto.getStatus());
            pst.setBytes(12, projeto.getArquivopdf());
            pst.setBytes(13, projeto.getRelatoriofinalpdf());
            pst.setString(14, projeto.getProntuario() != null ? projeto.getProntuario().getProntuario() : null);
            pst.setObject(15, projeto.getIdedital() != null ? projeto.getIdedital().getIdedital() : null, java.sql.Types.VARCHAR);
            pst.setString(16, projeto.getObservacoes());

            // Executar a query e verificar o resultado
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Projeto cadastrado com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar o Projeto: Nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o Projeto: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
        }
    }

    public void edit(Projeto projeto) throws SQLException {
        if (projeto == null) {
            throw new IllegalArgumentException("O objeto Projeto está nulo.");
        }

        String updateQuery = "UPDATE projeto SET titulo = ?, resumo = ?, duracaoSemestre = ?, numBolsista = ?, remuneracao = ?, "
                + "cargaSemanal = ?, perfilBolsista = ?, atividadesPrevistas = ?, dataInicio = ?, "
                + "dataTermino = ?, status = ?, observacoes = ?, remuneracao = ?, arquivopdf = ?, relatoriofinalpdf = ?, "
                + "prontuario = ?, idedital = ? WHERE idProjeto = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {

            pst.setString(1, projeto.getTitulo());
            pst.setString(2, projeto.getResumo());
            pst.setInt(3, projeto.getDuracaoSemestre());
            pst.setInt(4, projeto.getNumBolsista());
            pst.setFloat(5, projeto.getRemuneracao());
            pst.setFloat(6, projeto.getCargaSemanal());
            pst.setString(7, projeto.getPerfilBolsista());
            pst.setString(8, projeto.getAtividadesPrevistas());
            pst.setDate(9, projeto.getDataInicio() != null ? new java.sql.Date(projeto.getDataInicio().getTime()) : null);
            pst.setDate(10, projeto.getDataTermino() != null ? new java.sql.Date(projeto.getDataTermino().getTime()) : null);
            pst.setString(11, projeto.getStatus());
            pst.setString(12, projeto.getObservacoes());
            pst.setFloat(13, projeto.getRemuneracao());
            pst.setBytes(14, projeto.getArquivopdf());
            pst.setBytes(15, projeto.getRelatoriofinalpdf());
            pst.setString(16, projeto.getProntuario() != null ? projeto.getProntuario().getProntuario() : null);
            pst.setObject(17, projeto.getIdedital() != null ? projeto.getIdedital().getIdedital() : null, java.sql.Types.VARCHAR);
            pst.setInt(18, projeto.getIdProjeto());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Projeto atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o Projeto: Nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o Projeto: " + e.getMessage());
            throw e;
        }
    }

    public void remove(Integer idProjeto) throws SQLException {
        if (idProjeto == null) {
            throw new IllegalArgumentException("O ID do Projeto está nulo.");
        }

        String deleteQuery = "DELETE FROM projeto WHERE idProjeto = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setInt(1, idProjeto);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Projeto removido com sucesso!");
            } else {
                System.err.println("Falha ao remover o Projeto: Nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover o Projeto: " + e.getMessage());
            throw e;
        }
    }

    public Projeto find(Integer idProjeto) throws SQLException {
        if (idProjeto == null) {
            throw new IllegalArgumentException("O ID do Projeto está nulo.");
        }

        String selectQuery = "SELECT * FROM projeto WHERE idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setInt(1, idProjeto);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("titulo"));
                    projeto.setResumo(rs.getString("resumo"));
                    projeto.setDuracaoSemestre(rs.getInt("duracaoSemestre"));
                    projeto.setNumBolsista(rs.getInt("numBolsista"));
                    projeto.setRemuneracao(rs.getFloat("remuneracao"));
                    projeto.setCargaSemanal(rs.getFloat("cargaSemanal"));
                    projeto.setPerfilBolsista(rs.getString("perfilBolsista"));
                    projeto.setAtividadesPrevistas(rs.getString("atividadesPrevistas"));
                    projeto.setDataInicio(rs.getDate("dataInicio"));
                    projeto.setDataTermino(rs.getDate("dataTermino"));
                    projeto.setStatus(rs.getString("status"));
                    projeto.setArquivopdf(rs.getBytes("arquivopdf"));
                    projeto.setRelatoriofinalpdf(rs.getBytes("relatoriofinalpdf"));
                    projeto.setObservacoes(rs.getString("observacoes"));

                    Professor professor = new ProfessorDAO().find(rs.getString("prontuario"));
                    projeto.setProntuario(professor);

                    Edital edital = new EditalDAO().find(rs.getString("idedital"));
                    projeto.setIdedital(edital);

                    return projeto;
                }
            }
        }
        return null;
    }

    public List<Projeto> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM projeto";
        List<Projeto> projetoList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setIdProjeto(rs.getInt("idProjeto"));
                projeto.setTitulo(rs.getString("titulo"));
                projeto.setResumo(rs.getString("resumo"));
                projeto.setDuracaoSemestre(rs.getInt("duracaoSemestre"));
                projeto.setNumBolsista(rs.getInt("numBolsista"));
                projeto.setRemuneracao(rs.getFloat("remuneracao"));
                projeto.setCargaSemanal(rs.getFloat("cargaSemanal"));
                projeto.setPerfilBolsista(rs.getString("perfilBolsista"));
                projeto.setAtividadesPrevistas(rs.getString("atividadesPrevistas"));
                projeto.setDataInicio(rs.getDate("dataInicio"));
                projeto.setDataTermino(rs.getDate("dataTermino"));
                projeto.setStatus(rs.getString("status"));
                projeto.setArquivopdf(rs.getBytes("arquivopdf"));
                projeto.setRelatoriofinalpdf(rs.getBytes("relatoriofinalpdf"));
                projeto.setObservacoes(rs.getString("observacoes"));

                ProfessorDAO professorDAO = new ProfessorDAO();
                Professor professor = professorDAO.find(rs.getString("prontuario"));
                projeto.setProntuario(professor);

                EditalDAO editalDAO = new EditalDAO();
                Edital edital = editalDAO.find(rs.getString("idedital"));
                projeto.setIdedital(edital);

                projetoList.add(projeto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os projetos: " + e.getMessage());
            throw e;
        }

        return projetoList;
    }

    public List<Projeto> findByTipoAprovado() throws SQLException {
        String sql = "SELECT * FROM projeto WHERE status = ?";
        List<Projeto> projetosAprovados = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "Aprovado");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Projeto projeto = new Projeto();

                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("titulo"));
                    projeto.setResumo(rs.getString("resumo"));
                    projeto.setDuracaoSemestre(rs.getInt("duracaoSemestre"));
                    projeto.setNumBolsista(rs.getInt("numBolsista"));
                    projeto.setRemuneracao(rs.getFloat("remuneracao"));
                    projeto.setCargaSemanal(rs.getFloat("cargaSemanal"));
                    projeto.setPerfilBolsista(rs.getString("perfilBolsista"));
                    projeto.setAtividadesPrevistas(rs.getString("atividadesPrevistas"));
                    projeto.setDataInicio(rs.getDate("dataInicio"));
                    projeto.setDataTermino(rs.getDate("dataTermino"));
                    projeto.setStatus(rs.getString("status"));
                    projeto.setArquivopdf(rs.getBytes("arquivopdf"));
                    projeto.setRelatoriofinalpdf(rs.getBytes("relatoriofinalpdf"));
                    projeto.setObservacoes(rs.getString("observacoes"));

                    ProfessorDAO professorDAO = new ProfessorDAO();
                    Professor professor = professorDAO.find(rs.getString("prontuario"));
                    projeto.setProntuario(professor);

                    EditalDAO editalDAO = new EditalDAO();
                    Edital edital = editalDAO.find(rs.getString("idedital"));
                    projeto.setIdedital(edital);

                    projetosAprovados.add(projeto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos aprovados: " + e.getMessage());
            throw e;
        }

        return projetosAprovados;
    }

    public List<Projeto> findByTipoCorrecao() throws SQLException {
        String sql = "SELECT * FROM projeto WHERE status = ?";
        List<Projeto> projetosAprovados = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, "Correção");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("titulo"));
                    projeto.setResumo(rs.getString("resumo"));
                    projeto.setDuracaoSemestre(rs.getInt("duracaoSemestre"));
                    projeto.setNumBolsista(rs.getInt("numBolsista"));
                    projeto.setRemuneracao(rs.getFloat("remuneracao"));
                    projeto.setCargaSemanal(rs.getFloat("cargaSemanal"));
                    projeto.setPerfilBolsista(rs.getString("perfilBolsista"));
                    projeto.setAtividadesPrevistas(rs.getString("atividadesPrevistas"));
                    projeto.setDataInicio(rs.getDate("dataInicio"));
                    projeto.setDataTermino(rs.getDate("dataTermino"));
                    projeto.setStatus(rs.getString("status"));
                    projeto.setArquivopdf(rs.getBytes("arquivopdf"));
                    projeto.setRelatoriofinalpdf(rs.getBytes("relatoriofinalpdf"));
                    projeto.setObservacoes(rs.getString("observacoes"));
                    ProfessorDAO professorDAO = new ProfessorDAO();
                    Professor professor = professorDAO.find(rs.getString("prontuario"));
                    projeto.setProntuario(professor);
                    EditalDAO editalDAO = new EditalDAO();
                    Edital edital = editalDAO.find(rs.getString("idedital"));
                    projeto.setIdedital(edital);
                    projetosAprovados.add(projeto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos para correção: " + e.getMessage());
            throw e;
        }
        return projetosAprovados;
    }

    public Projeto findByTitulo(String titulo) throws SQLException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }

        String selectQuery = "SELECT * FROM projeto WHERE titulo = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setString(1, titulo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("titulo"));
                    projeto.setResumo(rs.getString("resumo"));
                    projeto.setDuracaoSemestre(rs.getInt("duracaoSemestre"));
                    projeto.setNumBolsista(rs.getInt("numBolsista"));
                    projeto.setRemuneracao(rs.getFloat("remuneracao"));
                    projeto.setCargaSemanal(rs.getFloat("cargaSemanal"));
                    projeto.setPerfilBolsista(rs.getString("perfilBolsista"));
                    projeto.setAtividadesPrevistas(rs.getString("atividadesPrevistas"));
                    projeto.setDataInicio(rs.getDate("dataInicio"));
                    projeto.setDataTermino(rs.getDate("dataTermino"));
                    projeto.setStatus(rs.getString("status"));
                    projeto.setArquivopdf(rs.getBytes("arquivopdf"));
                    projeto.setRelatoriofinalpdf(rs.getBytes("relatoriofinalpdf"));
                    projeto.setObservacoes(rs.getString("observacoes"));

                    Professor professor = new ProfessorDAO().find(rs.getString("prontuario"));
                    projeto.setProntuario(professor);

                    Edital edital = new EditalDAO().find(rs.getString("idedital"));
                    projeto.setIdedital(edital);

                    return projeto;
                }
            }
        }

        return null;
    }

}
