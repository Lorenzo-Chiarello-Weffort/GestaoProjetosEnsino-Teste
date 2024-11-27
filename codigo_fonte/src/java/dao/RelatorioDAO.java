package dao;

import jakarta.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Aluno;
import modelo.Professor;
import modelo.Projeto;
import modelo.Projetoaluno;
import modelo.Relatoriopresencamensal;

@Stateless
public class RelatorioDAO {

    public void create(Relatoriopresencamensal relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("O objeto Relatoriopresencamensal está nulo.");
        }

        String selectMaxIdQuery = "SELECT COALESCE(MAX(idrelatorio), 0) + 1 AS nextId FROM relatoriopresencamensal";
        String insertQuery = "INSERT INTO relatoriopresencamensal (projetoAlunoidProjeto, projetoAlunoprontuario, idrelatorio, data, arquivopresencapdf, dtassinaaluno, dtassinaprofessor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoMysql.obterConexao()) {
            int nextId;

            // Obter o próximo valor para o idrelatorio
            try (PreparedStatement pstSelect = con.prepareStatement(selectMaxIdQuery); ResultSet rs = pstSelect.executeQuery()) {
                nextId = rs.next() ? rs.getInt("nextId") : 1;
            }

            // Inserir o novo relatório
            try (PreparedStatement pstInsert = con.prepareStatement(insertQuery)) {
                pstInsert.setInt(1, relatorio.getProjetoaluno().getIdProjeto().getIdProjeto());
                pstInsert.setString(2, relatorio.getProjetoaluno().getProntuario().getProntuario());
                pstInsert.setInt(3, nextId);
                pstInsert.setDate(4, relatorio.getData() != null
                        ? new java.sql.Date(relatorio.getData().getTime()) : null);
                pstInsert.setBytes(5, relatorio.getArquivopresencapdf());
                pstInsert.setDate(6, relatorio.getDtassinaaluno() != null
                        ? new java.sql.Date(relatorio.getDtassinaaluno().getTime()) : null);
                pstInsert.setDate(7, relatorio.getDtassinaprofessor() != null
                        ? new java.sql.Date(relatorio.getDtassinaprofessor().getTime()) : null);

                int rowsAffected = pstInsert.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Relatório cadastrado com sucesso!");
                } else {
                    System.err.println("Falha ao cadastrar o relatório. Nenhuma linha foi afetada.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void edit(Relatoriopresencamensal relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("O objeto Relatoriopresencamensal está nulo.");
        }

        String updateQuery = "UPDATE relatoriopresencamensal SET data = ?, arquivopresencapdf = ?, dtassinaaluno = ?, dtassinaprofessor = ? "
                + "WHERE projetoAlunoidProjeto = ? AND projetoAlunoprontuario = ? AND idrelatorio = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            pst.setDate(1, relatorio.getData() != null
                    ? new java.sql.Date(relatorio.getData().getTime()) : null);
            pst.setBytes(2, relatorio.getArquivopresencapdf());
            pst.setDate(3, relatorio.getDtassinaaluno() != null
                    ? new java.sql.Date(relatorio.getDtassinaaluno().getTime()) : null);
            pst.setDate(4, relatorio.getDtassinaprofessor() != null
                    ? new java.sql.Date(relatorio.getDtassinaprofessor().getTime()) : null);
            pst.setInt(5, relatorio.getProjetoaluno().getIdProjeto().getIdProjeto());
            pst.setString(6, relatorio.getProjetoaluno().getProntuario().getProntuario());
            pst.setInt(7, relatorio.getIdrelatorio());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Relatório atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o relatório. Verifique os dados informados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void remove(Relatoriopresencamensal relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("O objeto Relatoriopresencamensal está nulo.");
        }

        String deleteQuery = "DELETE FROM relatoriopresencamensal WHERE projetoAlunoidProjeto = ? AND projetoAlunoprontuario = ? AND idrelatorio = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setInt(1, relatorio.getProjetoaluno().getIdProjeto().getIdProjeto());
            pst.setString(2, relatorio.getProjetoaluno().getProntuario().getProntuario());
            pst.setInt(3, relatorio.getIdrelatorio());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Relatório removido com sucesso!");
            } else {
                System.err.println("Falha ao remover o relatório. Verifique se ele existe.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover o relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public byte[] getArquivoPdfById(int idrelatorio) throws SQLException {
        // Query para buscar o arquivo PDF pelo ID do relatório
        String selectQuery = "SELECT arquivopresencapdf FROM relatoriopresencamensal WHERE idrelatorio = ?";

        // Tentando obter o conteúdo do arquivo PDF
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            pst.setInt(1, idrelatorio);  // Passa o idrelatorio para a consulta

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("arquivopresencapdf");  // Retorna o conteúdo do arquivo PDF
                } else {
                    return null;  // Se não encontrar o relatório, retorna null
                }
            }
        }
    }

    // Método para encontrar um relatório pelo ID
    public Relatoriopresencamensal find(Integer idrelatorio, String prontuario, Integer idProjeto) throws SQLException {
        String selectQuery = "SELECT * FROM relatoriopresencamensal WHERE idrelatorio = ? AND projetoAlunoprontuario = ? AND projetoAlunoidProjeto = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setInt(1, idrelatorio);
            pst.setString(2, prontuario);
            pst.setInt(3, idProjeto);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRelatorio(rs);
                }
            }
        }
        return null;
    }

    // Método para listar todos os relatórios
    public List<Relatoriopresencamensal> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM relatoriopresencamensal";
        List<Relatoriopresencamensal> relatorioList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                relatorioList.add(mapResultSetToRelatorio(rs));
            }
        }
        return relatorioList;
    }

    public List<Relatoriopresencamensal> findByProjeto(Projetoaluno projeto) throws SQLException {
        System.out.println("findByProjeto");
        String selectQuery = "SELECT * FROM relatoriopresencamensal WHERE projetoAlunoidProjeto = ?";
        List<Relatoriopresencamensal> relatoriopresencamensal = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setInt(1, projeto.getIdProjeto().getIdProjeto());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    relatoriopresencamensal.add(mapResultSetToRelatorio(rs));
                }
            }
        }
        return relatoriopresencamensal;
    }

    // Método para encontrar um relatório pelo ID
    public Relatoriopresencamensal findById(int idrelatorio) throws SQLException {
        String selectQuery = "SELECT * FROM relatoriopresencamensal WHERE idrelatorio = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setInt(1, idrelatorio);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRelatorio(rs);
                }
            }
        }
        return null; // Retorna null caso não encontre o relatório
    }

    // Método para mapear o ResultSet para o objeto Relatorio
    private Relatoriopresencamensal mapResultSetToRelatorio(ResultSet rs) throws SQLException {
        Relatoriopresencamensal relatorio = new Relatoriopresencamensal();
        relatorio.setIdrelatorio(rs.getInt("idrelatorio"));
        relatorio.setData(rs.getDate("data"));
        relatorio.setArquivopresencapdf(rs.getBytes("arquivopresencapdf"));
        relatorio.setDtassinaaluno(rs.getDate("dtassinaaluno"));
        relatorio.setDtassinaprofessor(rs.getDate("dtassinaprofessor"));

        // Mapear o relacionamento com o Projetoaluno
        Projetoaluno projetoaluno = new Projetoaluno();

        Projeto projeto = new ProjetoDAO().find(rs.getInt("projetoAlunoidProjeto"));
        projetoaluno.setIdProjeto(projeto);
        Aluno aluno = new AlunoDAO().find(rs.getString("projetoAlunoprontuario"));
        projetoaluno.setProntuario(aluno);
        relatorio.setProjetoaluno(projetoaluno);

        return relatorio;
    }

    public List<Relatoriopresencamensal> findAllByAluno(Aluno aluno) throws SQLException {
        String selectQuery = "SELECT * FROM relatoriopresencamensal WHERE projetoAlunoprontuario = ?";
        List<Relatoriopresencamensal> relatoriopresencamensalList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, aluno.getProntuario()); // Passa o prontuário do aluno para a consulta
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    relatoriopresencamensalList.add(mapResultSetToRelatorio(rs)); // Mapeia o ResultSet para o objeto Relatorio
                }
            }
        }
        return relatoriopresencamensalList; // Retorna a lista de relatórios
    }

    public List<Relatoriopresencamensal> findByProfessor(Professor professor) throws SQLException {
        String selectQuery = "SELECT r.idrelatorio, r.data, r.arquivopresencapdf, r.dtassinaaluno, r.dtassinaprofessor, "
                + "r.projetoAlunoidProjeto, r.projetoAlunoprontuario "
                + "FROM relatoriopresencamensal r "
                + "JOIN projetoaluno pa ON r.projetoAlunoidProjeto = pa.idProjeto "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "WHERE p.prontuario = ?";  // Relacionando o professor com o projeto

        List<Relatoriopresencamensal> relatorios = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, professor.getProntuario()); // Passa o prontuário do professor para a consulta

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Criar o objeto Relatoriopresencamensal
                    Relatoriopresencamensal relatorio = new Relatoriopresencamensal();

                    // Definindo as propriedades diretamente a partir do ResultSet
                    relatorio.setIdrelatorio(rs.getInt("idrelatorio"));
                    relatorio.setData(rs.getDate("data"));
                    relatorio.setArquivopresencapdf(rs.getBytes("arquivopresencapdf"));
                    relatorio.setDtassinaaluno(rs.getDate("dtassinaaluno"));
                    relatorio.setDtassinaprofessor(rs.getDate("dtassinaprofessor"));

                    // Mapeando as informações do Projetoaluno
                    Projetoaluno projetoaluno = new Projetoaluno();
                    Projeto projeto = new ProjetoDAO().find(rs.getInt("projetoAlunoidProjeto"));
                    projetoaluno.setIdProjeto(projeto);

                    Aluno aluno = new AlunoDAO().find(rs.getString("projetoAlunoprontuario"));
                    projetoaluno.setProntuario(aluno);

                    relatorio.setProjetoaluno(projetoaluno);

                    // Adiciona o relatório à lista
                    relatorios.add(relatorio);
                }
            }
        }
        return relatorios; // Retorna a lista de relatórios
    }

    public List<Map<String, Object>> findRelatoriosMensaisComDetalhes() throws SQLException {
        String query = "SELECT "
                + "  a.nome AS nomeAluno, "
                + "  pa.valorbolsa, "
                + "  r.dtassinaaluno, "
                + "  r.dtassinaprofessor "
                + "FROM "
                + "  relatoriopresencamensal r "
                + "JOIN "
                + "  projetoaluno pa ON r.projetoAlunoidProjeto = pa.idProjeto AND r.projetoAlunoprontuario = pa.prontuario "
                + "JOIN "
                + "  aluno a ON pa.prontuario = a.prontuario "
                + "WHERE "
                + "  MONTH(r.dtassinaaluno) = MONTH(CURRENT_DATE) "
                + "  AND YEAR(r.dtassinaaluno) = YEAR(CURRENT_DATE)";

        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("nomeAluno", rs.getString("nomeAluno"));
                row.put("valorbolsa", rs.getString("valorbolsa"));
                row.put("dtassinaaluno", rs.getDate("dtassinaaluno"));
                row.put("dtassinaprofessor", rs.getDate("dtassinaprofessor"));

                result.add(row);
            }
        }

        return result;
    }

}
