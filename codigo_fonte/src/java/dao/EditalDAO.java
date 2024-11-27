package dao;

import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Edital;

@Stateless
public class EditalDAO {

    public void create(Edital edital) {
        if (edital == null) {
            throw new IllegalArgumentException("O objeto Edital está nulo.");
        }

        String insertQuery = "INSERT INTO edital (idedital, dataabertura, datafechamento, docpdfedital, resumoedital, tipoedital, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setString(1, edital.getIdedital());
            pst.setDate(2, new java.sql.Date(edital.getDataabertura().getTime()));
            pst.setDate(3, new java.sql.Date(edital.getDatafechamento().getTime()));
            pst.setBytes(4, edital.getDocpdfedital());
            pst.setString(5, edital.getResumoedital());
            pst.setString(6, edital.getTipoedital());
            pst.setString(7, edital.getStatus());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Edital cadastrado com sucesso!");
            } else {
                System.err.println("Falha ao cadastrar o edital: Nenhuma linha foi afetada durante a inserção");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o edital: " + e.getMessage());
        }
    }

    public void edit(Edital edital) throws SQLException {
        if (edital == null) {
            throw new IllegalArgumentException("O objeto Edital está nulo.");
        }
        if (edital.getIdedital() == null || edital.getIdedital().isEmpty()) {
            throw new IllegalArgumentException("O campo idedital não pode ser nulo ou vazio.");
        }

        String updateQuery = "UPDATE edital SET dataabertura = ?, datafechamento = ?, docpdfedital = ?, resumoedital = ?, tipoedital = ?, status = ? WHERE idedital = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(updateQuery)) {
            // Validações para datas
            pst.setDate(1, edital.getDataabertura() != null ? new java.sql.Date(edital.getDataabertura().getTime()) : null);
            pst.setDate(2, edital.getDatafechamento() != null ? new java.sql.Date(edital.getDatafechamento().getTime()) : null);

            // Outros campos
            pst.setBytes(3, edital.getDocpdfedital());
            pst.setString(4, edital.getResumoedital());
            pst.setString(5, edital.getTipoedital());
            pst.setString(6, edital.getStatus());
            pst.setString(7, edital.getIdedital());

            // Execução da atualização
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Edital atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar o edital: Nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o edital: " + e.getMessage());
            throw e;
        }
    }

    public void remove(String idedital) throws SQLException {
        String deleteQuery = "DELETE FROM edital WHERE idedital = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {
            pst.setString(1, idedital);
            pst.executeUpdate();
        }
    }

    public Edital find(String idedital) throws SQLException {
        String selectQuery = "SELECT * FROM edital WHERE idedital = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, idedital);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Edital edital = new Edital();
                    edital.setIdedital(rs.getString("idedital"));
                    return edital;
                }
            }
        }
        return null;
    }

    public List<Edital> findAll() throws SQLException {
        String selectAllQuery = "SELECT * FROM edital";
        List<Edital> editalList = new ArrayList<>();
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectAllQuery); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Edital edital = new Edital();
                try {
                    // Preenchendo os campos do objeto Edital
                    edital.setIdedital(rs.getString("idedital"));
                    edital.setResumoedital(rs.getString("resumoedital"));
                    edital.setDataabertura(rs.getDate("dataabertura"));
                    edital.setDatafechamento(rs.getDate("datafechamento"));
                    edital.setTipoedital(rs.getString("tipoedital"));
                    edital.setDocpdfedital(rs.getBytes("docpdfedital"));
                    edital.setStatus(rs.getString("status"));
                    editalList.add(edital);
                } catch (SQLException e) {
                    System.err.println("Erro ao preencher o Edital: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar consulta findAll: " + e.getMessage());
            throw e;  // Propaga a exceção caso necessário
        }
        return editalList;
    }

    public Edital findByIdedital(String idedital) throws SQLException {
        String selectQuery = "SELECT * FROM edital WHERE idedital = ?";
        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {
            pst.setString(1, idedital);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Edital edital = new Edital();
                    edital.setIdedital(rs.getString("idedital"));
                    edital.setResumoedital(rs.getString("resumoedital"));
                    edital.setDataabertura(rs.getDate("dataabertura"));
                    edital.setDatafechamento(rs.getDate("datafechamento"));
                    edital.setTipoedital(rs.getString("tipoedital"));
                    edital.setDocpdfedital(rs.getBytes("docpdfedital"));
                    edital.setStatus(rs.getString("status"));
                    return edital; // Retorna o objeto encontrado
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar edital por ID: " + e.getMessage());
            throw e; // Propaga a exceção, se necessário
        }
        return null; // Retorna null caso nenhum resultado seja encontrado
    }

}
