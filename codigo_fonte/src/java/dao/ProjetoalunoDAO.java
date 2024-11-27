package dao;

import jakarta.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;
import modelo.Horario;
import modelo.Projeto;
import modelo.Projetoaluno;

@Stateless
public class ProjetoalunoDAO {

    public void create(Projetoaluno projetoAluno) {
        String insertQuery = "INSERT INTO projetoaluno (idProjeto, prontuario, remunerado, valorbolsa, dataEntrevista, salaHora) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setInt(1, projetoAluno.getIdProjeto().getIdProjeto());
            pst.setString(2, projetoAluno.getProntuario().getProntuario());
            pst.setString(3, projetoAluno.getRemunerado());
            pst.setString(4, projetoAluno.getValorbolsa());
            pst.setDate(5, projetoAluno.getDataEntrevista() != null
                    ? new java.sql.Date(projetoAluno.getDataEntrevista().getTime())
                    : null);
            pst.setString(6, projetoAluno.getSalaHora());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registro inserido com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro de ProjetoAluno: " + e.getMessage());
        }
    }

    public void edit(Projetoaluno projetoAluno) {
        if (projetoAluno == null) {
            throw new IllegalArgumentException("O objeto ProjetoAluno está nulo.");
        }

        // Certifique-se de que os valores principais não sejam nulos.
        if (projetoAluno.getIdProjeto() == null || projetoAluno.getIdProjeto().getIdProjeto() == null) {
            throw new IllegalArgumentException("O ID do Projeto está nulo.");
        }
        if (projetoAluno.getProntuario() == null || projetoAluno.getProntuario().getProntuario() == null) {
            throw new IllegalArgumentException("O prontuário do aluno está nulo.");
        }

        String updateQuery
                = "UPDATE projetoaluno "
                + "SET remunerado = ?, valorbolsa = ?, dataEntrevista = ?, salaHora = ?, statusAluno = ? "
                + "WHERE idProjeto = ? AND prontuario = ?";

        try (
                Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            // Definindo os parâmetros da consulta
            pst.setString(1, projetoAluno.getRemunerado());
            pst.setString(2, projetoAluno.getValorbolsa());
            pst.setDate(3, projetoAluno.getDataEntrevista() != null
                    ? new java.sql.Date(projetoAluno.getDataEntrevista().getTime())
                    : null);
            pst.setString(4, projetoAluno.getSalaHora());
            pst.setString(5, projetoAluno.getStatusAluno());
            pst.setInt(6, projetoAluno.getIdProjeto().getIdProjeto());
            pst.setString(7, projetoAluno.getProntuario().getProntuario());

            // Executando a consulta
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registro atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o registro. Nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o registro de ProjetoAluno: " + e.getMessage(), e);
        }
    }

    public void remove(Projetoaluno projetoAluno) throws SQLException {
        String deleteQuery = "DELETE FROM projetoaluno WHERE idProjeto = ? AND prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setInt(1, projetoAluno.getIdProjeto().getIdProjeto());
            pst.setString(2, projetoAluno.getProntuario().getProntuario());
            pst.executeUpdate();
        }
    }

    public List<Projetoaluno> findAll() {
        String selectAllQuery
                = "SELECT pa.idProjeto, pa.prontuario, pa.remunerado, pa.valorbolsa, "
                + "       pa.dataEntrevista, pa.salaHora, pa.statusAluno, "
                + "       p.titulo AS projetoTitulo, "
                + "       a.nome AS alunoNome, a.semestre AS alunoSemestre, a.turno AS alunoTurno "
                + "FROM projetoaluno pa "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "JOIN aluno a ON pa.prontuario = a.prontuario";

        List<Projetoaluno> projetoAlunoList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Criar e preencher objeto Projetoaluno
                Projetoaluno projetoAluno = new Projetoaluno();

                // Preencher Projeto relacionado
                Projeto projeto = new Projeto();
                projeto.setIdProjeto(rs.getInt("idProjeto"));
                projeto.setTitulo(rs.getString("projetoTitulo"));
                projetoAluno.setIdProjeto(projeto);

                // Preencher Aluno relacionado
                Aluno aluno = new Aluno();
                aluno.setProntuario(rs.getString("prontuario"));
                aluno.setNome(rs.getString("alunoNome"));
                aluno.setSemestre(rs.getInt("alunoSemestre"));
                aluno.setTurno(rs.getString("alunoTurno"));

                projetoAluno.setProntuario(aluno);

                // Preencher os demais campos de Projetoaluno
                projetoAluno.setRemunerado(rs.getString("remunerado"));
                projetoAluno.setValorbolsa(rs.getString("valorbolsa"));
                projetoAluno.setDataEntrevista(rs.getDate("dataEntrevista"));
                projetoAluno.setSalaHora(rs.getString("salaHora"));
                projetoAluno.setStatusAluno(rs.getString("statusAluno"));

                // Adicionar à lista
                projetoAlunoList.add(projetoAluno);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar os alunos no projeto: " + e.getMessage());
        }
        return projetoAlunoList;
    }

    public List<Projetoaluno> findByProjetoAluno(Projetoaluno projeto) throws SQLException {
        if (projeto == null || projeto.getIdProjeto() == null) {
            throw new IllegalArgumentException("Projeto ou idProjeto não pode ser nulo");
        }

        String selectQuery = "SELECT "
                + "pa.idProjeto, pa.prontuario, pa.remunerado, pa.valorbolsa, "
                + "pa.dataEntrevista, pa.salaHora, pa.statusAluno, "
                + "p.titulo AS projetoTitulo, "
                + "a.nome AS alunoNome, a.semestre AS alunoSemestre, a.turno AS alunoTurno "
                + "FROM projetoaluno pa "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "JOIN aluno a ON pa.prontuario = a.prontuario "
                + "WHERE pa.idProjeto = ?";

        List<Projetoaluno> projetoAlunoList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            // Definir o parâmetro do ID do projeto
            pst.setInt(1, projeto.getIdProjeto().getIdProjeto());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) { // Verifica se há resultados
                    do {
                        // Criar e preencher objeto Projetoaluno
                        Projetoaluno projetoAluno = new Projetoaluno();

                        // Preencher Projeto relacionado
                        Projeto projetoRelacionado = new Projeto();
                        projetoRelacionado.setIdProjeto(rs.getInt("idProjeto"));
                        projetoRelacionado.setTitulo(rs.getString("projetoTitulo"));
                        projetoAluno.setIdProjeto(projetoRelacionado);

                        // Preencher Aluno relacionado
                        Aluno aluno = new Aluno();
                        aluno.setProntuario(rs.getString("prontuario"));
                        aluno.setNome(rs.getString("alunoNome"));
                        aluno.setSemestre(rs.getInt("alunoSemestre"));
                        aluno.setTurno(rs.getString("alunoTurno"));
                        projetoAluno.setProntuario(aluno);

                        // Preencher os demais campos de Projetoaluno
                        projetoAluno.setRemunerado(rs.getString("remunerado"));
                        projetoAluno.setValorbolsa(rs.getString("valorbolsa"));
                        projetoAluno.setDataEntrevista(rs.getDate("dataEntrevista"));
                        projetoAluno.setSalaHora(rs.getString("salaHora"));
                        projetoAluno.setStatusAluno(rs.getString("statusAluno"));

                        // Adicionar à lista
                        projetoAlunoList.add(projetoAluno);
                    } while (rs.next());
                } else {
                    System.out.println("Nenhum aluno encontrado para o projeto de ID: " + projeto.getIdProjeto());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar os alunos no projeto: " + e.getMessage());
            throw e;
        }

        return projetoAlunoList;
    }

    public List<Projetoaluno> findByProjetoAlunoRelatorio(Projetoaluno projetoAluno) throws SQLException {
        if (projetoAluno == null || projetoAluno.getIdProjeto() == null) {
            throw new IllegalArgumentException("Projeto ou idProjeto não pode ser nulo");
        }

        String selectQuery = "SELECT "
                + "pa.idProjeto, pa.prontuario, pa.remunerado, pa.valorbolsa, "
                + "pa.dataEntrevista, pa.salaHora, pa.statusAluno, "
                + "p.titulo AS projetoTitulo, "
                + "a.nome AS alunoNome, a.semestre AS alunoSemestre, a.turno AS alunoTurno "
                + "FROM projetoaluno pa "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "JOIN aluno a ON pa.prontuario = a.prontuario "
                + "WHERE pa.idProjeto = ?";

        List<Projetoaluno> projetoAlunoList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            // Definir o parâmetro do ID do projeto
            pst.setInt(1, projetoAluno.getIdProjeto().getIdProjeto());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) { // Verifica se há resultados
                    do {
                        // Preencher os demais campos de Projetoaluno
                        projetoAluno.setRemunerado(rs.getString("remunerado"));
                        projetoAluno.setValorbolsa(rs.getString("valorbolsa"));
                        projetoAluno.setDataEntrevista(rs.getDate("dataEntrevista"));
                        projetoAluno.setSalaHora(rs.getString("salaHora"));
                        projetoAluno.setStatusAluno(rs.getString("statusAluno"));

                        // Adicionar à lista
                        projetoAlunoList.add(projetoAluno);
                    } while (rs.next());
                } else {
                    System.out.println("Nenhum aluno encontrado para o projeto de ID: " + projetoAluno.getIdProjeto());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar os alunos no projeto: " + e.getMessage());
            throw e;
        }

        return projetoAlunoList;
    }

    public List<Projetoaluno> findByProjeto(Projeto projeto) throws SQLException {
        String selectQuery = "SELECT * FROM projetoaluno WHERE idProjeto = ?";
        List<Projetoaluno> projetoAlunoList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setInt(1, projeto.getIdProjeto());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projetoAlunoList.add(mapResultSetToProjetoAluno(rs));
                }
            }
        }
        return projetoAlunoList;
    }

    public List<Projetoaluno> findByAluno(Aluno aluno) throws SQLException {
        String selectQuery = "SELECT * FROM projetoaluno WHERE prontuario = ?";
        List<Projetoaluno> projetoAlunoList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, aluno.getProntuario());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projetoAlunoList.add(mapResultSetToProjetoAluno(rs));
                }
            }
        }
        return projetoAlunoList;
    }

    private Projetoaluno mapResultSetToProjetoAluno(ResultSet rs) throws SQLException {
        Projetoaluno projetoAluno = new Projetoaluno();
        Projeto projeto = new Projeto(); // Supondo que o Projeto tenha um método para buscar pelo id
        projeto.setIdProjeto(rs.getInt("idProjeto"));
        Aluno aluno = new Aluno(); // Supondo que o Aluno tenha um método para buscar pelo prontuário
        aluno.setProntuario(rs.getString("prontuario"));

        projetoAluno.setIdProjeto(projeto);
        projetoAluno.setProntuario(aluno);
        projetoAluno.setRemunerado(rs.getString("remunerado"));
        projetoAluno.setValorbolsa(rs.getString("valorbolsa"));
        projetoAluno.setDataEntrevista(rs.getDate("dataEntrevista"));
        projetoAluno.setSalaHora(rs.getString("salaHora"));
        projetoAluno.setStatusAluno(rs.getString("statusAluno"));

        return projetoAluno;
    }

    private int getNumBolsistasDisponiveis(int idProjeto) throws SQLException {
        String selectBolsasQuery = "SELECT numBolsista FROM Projeto WHERE idProjeto = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectBolsasQuery)) {

            pst.setInt(1, idProjeto);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("numBolsista");  // Retorna o número de bolsas disponíveis
                } else {
                    throw new SQLException("Nenhum projeto encontrado com o ID: " + idProjeto);
                }
            }
        }
    }

    private int getNumeroAlunosAprovados(int idProjeto) throws SQLException {
        String selectAlunosQuery = "SELECT COUNT(pa.prontuario) "
                + "FROM Projetoaluno pa "
                + "WHERE pa.idProjeto = ? AND pa.statusAluno = 'Aprovado'";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAlunosQuery)) {

            pst.setInt(1, idProjeto);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  // Retorna o número de alunos aprovados
                } else {
                    return 0;  // Se não encontrar nenhum aluno, retorna 0
                }
            }
        }
    }

    public boolean verificarLimiteBolsistas(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("O projeto não pode ser nulo");
        }

        try {
            // Obtém o número de bolsas disponíveis no projeto
            int numBolsistas = getNumBolsistasDisponiveis(projeto.getIdProjeto());
            System.out.println("Número de bolsas disponíveis no projeto: " + numBolsistas);

            // Obtém o número de alunos aprovados no projeto
            int numeroAprovados = getNumeroAlunosAprovados(projeto.getIdProjeto());
            System.out.println("Número de alunos aprovados: " + numeroAprovados);

            // Verifica se o número de alunos aprovados ultrapassou o limite de bolsas
            return numeroAprovados < numBolsistas;  // Se ainda houver vagas, retorna true

        } catch (SQLException e) {
            // Log de erro em caso de falha na execução da consulta
            System.err.println("Erro ao verificar o limite de bolsistas para o projeto ID: " + projeto.getIdProjeto());
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
            return false;  // Retorna false em caso de erro na execução da consulta
        }
    }

    public boolean verificarInscricao(Projeto projeto, Aluno aluno) throws SQLException {
        String query = "SELECT COUNT(*) FROM projetoaluno WHERE idProjeto = ? AND prontuario = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, projeto.getIdProjeto());
            pst.setString(2, aluno.getProntuario());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar inscrição: " + e.getMessage());
        }
        return false;
    }

    public List<Projetoaluno> findAllUniqueProjects() {
        String selectQuery = "WITH RankedProjects AS (\n" +
                            "    SELECT pa.idProjeto, \n" +
                            "           p.titulo, \n" +
                            "           pa.remunerado, \n" +
                            "           pa.valorbolsa,\n" +
                            "           ROW_NUMBER() OVER (PARTITION BY pa.idProjeto ORDER BY pa.idProjeto) AS row_num\n" +
                            "    FROM projetoaluno pa\n" +
                            "    JOIN projeto p ON pa.idProjeto = p.idProjeto\n" +
                            ")\n" +
                            "SELECT idProjeto, titulo, remunerado, valorbolsa\n" +
                            "FROM RankedProjects\n" +
                            "WHERE row_num = 1;";
        List<Projetoaluno> uniqueProjects = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Projetoaluno projetoaluno = new Projetoaluno();

                    // Criando o objeto Projeto e setando o idProjeto e o titulo
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("titulo"));
                    projetoaluno.setIdProjeto(projeto); // Setando o Projeto no Projetoaluno

                    projetoaluno.setRemunerado(rs.getString("remunerado")); // Informação sobre remuneração
                    projetoaluno.setValorbolsa(rs.getString("valorbolsa")); // Valor da bolsa

                    uniqueProjects.add(projetoaluno);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos únicos: " + e.getMessage());
        }

        return uniqueProjects;
    }

    public List<Projetoaluno> findAllUniqueAlunosInProjeto() {
        String selectQuery = "SELECT DISTINCT pa.prontuario, pa.remunerado, pa.valorbolsa "
                + "FROM projetoaluno pa";
        List<Projetoaluno> uniqueAlunos = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Projetoaluno projetoaluno = new Projetoaluno();

                    // Criando o objeto Aluno e setando o prontuario
                    Aluno aluno = new Aluno();
                    aluno.setProntuario(rs.getString("prontuario"));
                    projetoaluno.setProntuario(aluno); // Setando o Aluno no Projetoaluno

                    projetoaluno.setRemunerado(rs.getString("remunerado")); // Informação sobre remuneração
                    projetoaluno.setValorbolsa(rs.getString("valorbolsa")); // Valor da bolsa

                    uniqueAlunos.add(projetoaluno);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alunos únicos: " + e.getMessage());
        }

        return uniqueAlunos;
    }

    public boolean isAlunoInscritoNoProjeto(Aluno aluno, Projeto projeto) {
        String query = "SELECT COUNT(*) FROM projetoaluno WHERE prontuario = ? AND idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, aluno.getProntuario());
            pst.setInt(2, projeto.getIdProjeto());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true se houver inscrição
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Projetoaluno findProjetoalunoById(Integer idProjeto) {
        String query = "SELECT pa.idProjeto, pa.remunerado, pa.valorbolsa, pa.statusAluno, "
                + "a.prontuario, a.nome AS alunoNome "
                + "FROM projetoaluno pa "
                + "JOIN aluno a ON pa.prontuario = a.prontuario "
                + "WHERE pa.idProjeto = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, idProjeto);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // Criar e preencher o objeto Projetoaluno
                    Projetoaluno projetoaluno = new Projetoaluno();

                    // Criar e preencher o objeto Projeto
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projetoaluno.setIdProjeto(projeto);

                    // Criar e preencher o objeto Aluno
                    Aluno aluno = new Aluno();
                    aluno.setProntuario(rs.getString("prontuario"));
                    aluno.setNome(rs.getString("alunoNome"));
                    projetoaluno.setProntuario(aluno);

                    // Preencher os demais campos de Projetoaluno
                    projetoaluno.setRemunerado(rs.getString("remunerado"));
                    projetoaluno.setValorbolsa(rs.getString("valorbolsa"));
                    projetoaluno.setStatusAluno(rs.getString("statusAluno"));

                    return projetoaluno;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Projetoaluno> findByStatusEntrevista() {
        String query = "SELECT pa.idProjeto, pa.prontuario, pa.remunerado, pa.valorbolsa, "
                + "pa.dataEntrevista, pa.statusAluno, "
                + "p.titulo AS projetoTitulo, "
                + "a.nome AS alunoNome "
                + "FROM projetoaluno pa "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "JOIN aluno a ON pa.prontuario = a.prontuario "
                + "WHERE pa.statusAluno = ?";

        List<Projetoaluno> alunosEntrevista = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, "Entrevista");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Projetoaluno projetoaluno = new Projetoaluno();

                    // Criar e preencher o objeto Projeto
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("idProjeto"));
                    projeto.setTitulo(rs.getString("projetoTitulo"));
                    projetoaluno.setIdProjeto(projeto);

                    // Criar e preencher o objeto Aluno
                    Aluno aluno = new Aluno();
                    aluno.setProntuario(rs.getString("prontuario"));
                    aluno.setNome(rs.getString("alunoNome"));
                    projetoaluno.setProntuario(aluno);

                    // Preencher os demais campos de Projetoaluno
                    projetoaluno.setRemunerado(rs.getString("remunerado"));
                    projetoaluno.setValorbolsa(rs.getString("valorbolsa"));
                    projetoaluno.setDataEntrevista(rs.getDate("dataEntrevista"));
                    projetoaluno.setStatusAluno(rs.getString("statusAluno"));

                    // Adicionar à lista
                    alunosEntrevista.add(projetoaluno);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunosEntrevista;
    }

    public List<Horario> listarHorariosPorAluno(Aluno aluno) {
        List<Horario> horarios = new ArrayList<>();
        String query = "SELECT h.idhorario, h.dia_semana, h.hora "
                + "FROM horario h "
                + "INNER JOIN aluno a ON h.prontuario = a.prontuario "
                + "WHERE a.prontuario = ?";  // Filtro baseado no prontuario do aluno

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement stmt = con.prepareStatement(query)) {
            // Passando o prontuario do aluno para a consulta
            stmt.setString(1, aluno.getProntuario());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Horario horario = new Horario();
                    horario.setIdhorario(rs.getInt("idhorario"));
                    horario.setDiaSemana(rs.getString("dia_semana"));
                    horario.setHora(rs.getString("hora"));

                    // Setando o aluno no objeto Horario
                    horario.setAluno(aluno); // Associando o aluno com o horário

                    horarios.add(horario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horarios;
    }

    public String findValorBolsaByIdProjeto(int idProjeto) throws SQLException {
        String selectQuery = "SELECT remuneracao FROM Projeto WHERE idProjeto = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            // Definindo o parâmetro do ID do projeto
            pst.setInt(1, idProjeto);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("remuneracao");
                } else {
                    System.out.println("Nenhum valor de bolsa encontrado para o projeto com ID: " + idProjeto);
                    return null;  // Caso não encontre o projeto, retornará null
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar valor da bolsa para o projeto de ID: " + idProjeto);
            throw e;  // Re-throw a exceção para que o chamador possa tratá-la
        }
    }

    public Date findDataEntrevistaByProjetoEAluno(int idProjeto, String prontuario) throws SQLException {
        String query = "SELECT dataEntrevista FROM projetoaluno WHERE idProjeto = ? AND prontuario = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idProjeto);
            pst.setString(2, prontuario);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("dataEntrevista");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a data de entrevista: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public List<Projetoaluno> findAlunosAprovadosByProjeto(Projeto projeto) throws SQLException {
        if (projeto == null || projeto.getIdProjeto() == null) {
            throw new IllegalArgumentException("Projeto ou idProjeto não pode ser nulo");
        }

        // Consulta SQL que busca alunos aprovados para um projeto específico
        String selectQuery = "SELECT "
                + "pa.idProjeto, pa.prontuario, pa.remunerado, pa.valorbolsa, "
                + "pa.dataEntrevista, pa.salaHora, pa.statusAluno, "
                + "p.titulo AS projetoTitulo, "
                + "a.nome AS alunoNome, a.semestre AS alunoSemestre, a.turno AS alunoTurno "
                + "FROM projetoaluno pa "
                + "JOIN projeto p ON pa.idProjeto = p.idProjeto "
                + "JOIN aluno a ON pa.prontuario = a.prontuario "
                + "WHERE pa.idProjeto = ? AND pa.statusAluno = 'Aprovado'";

        List<Projetoaluno> projetoAlunoList = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            // Definir o parâmetro do ID do projeto
            pst.setInt(1, projeto.getIdProjeto());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) { // Verifica se há resultados
                    do {
                        // Criar e preencher objeto Projetoaluno
                        Projetoaluno projetoAluno = new Projetoaluno();

                        // Preencher Projeto relacionado
                        Projeto projetoRelacionado = new Projeto();
                        projetoRelacionado.setIdProjeto(rs.getInt("idProjeto"));
                        projetoRelacionado.setTitulo(rs.getString("projetoTitulo"));
                        projetoAluno.setIdProjeto(projetoRelacionado);

                        // Preencher Aluno relacionado
                        Aluno aluno = new Aluno();
                        aluno.setProntuario(rs.getString("prontuario"));
                        aluno.setNome(rs.getString("alunoNome"));
                        aluno.setSemestre(rs.getInt("alunoSemestre"));
                        aluno.setTurno(rs.getString("alunoTurno"));
                        projetoAluno.setProntuario(aluno);

                        // Preencher os demais campos de Projetoaluno
                        projetoAluno.setRemunerado(rs.getString("remunerado"));
                        projetoAluno.setValorbolsa(rs.getString("valorbolsa"));
                        projetoAluno.setDataEntrevista(rs.getDate("dataEntrevista"));
                        projetoAluno.setSalaHora(rs.getString("salaHora"));
                        projetoAluno.setStatusAluno(rs.getString("statusAluno"));

                        // Adicionar à lista
                        projetoAlunoList.add(projetoAluno);
                    } while (rs.next());
                } else {
                    System.out.println("Nenhum aluno aprovado encontrado para o projeto de ID: " + projeto.getIdProjeto());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alunos aprovados no projeto: " + e.getMessage());
            throw e;
        }

        return projetoAlunoList;
    }

}
