/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.ejb.Stateless;
import jakarta.resource.cci.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;
import modelo.Horario;

/**
 *
 * @author Lorenzo
 */
@Stateless
public class HorarioDAO {

    public void create(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("O objeto Horario está nulo.");
        }

        String selectMaxQuery = "SELECT COALESCE(MAX(idhorario), 0) + 1 AS nextId FROM horario";
        String insertQuery = "INSERT INTO horario (idhorario, prontuario, dia_semana, hora) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement selectPst = con.prepareStatement(selectMaxQuery); PreparedStatement insertPst = con.prepareStatement(insertQuery)) {

            // Determinar o próximo ID
            int nextId = 0;
            try (java.sql.ResultSet rs = selectPst.executeQuery()) {
                if (rs.next()) {
                    nextId = rs.getInt("nextId");
                }
            }

            // Configurar os parâmetros da inserção
            insertPst.setInt(1, nextId);
            insertPst.setString(2, horario.getAluno().getProntuario());
            insertPst.setString(3, horario.getDiaSemana());
            insertPst.setString(4, horario.getHora());

            int rowsAffected = insertPst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Horário cadastrado com sucesso! ID: " + nextId);
            } else {
                System.err.println("Falha ao cadastrar o horário: Nenhuma linha foi afetada durante a inserção");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar o horário: " + e.getMessage());
        }
    }

    public void remove(int idHorario, String prontuario) {
        String deleteQuery = "DELETE FROM horario WHERE idhorario = ? AND prontuario = ?";

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            // Configurando os parâmetros para exclusão
            pst.setInt(1, idHorario);
            pst.setString(2, prontuario);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Horário com ID " + idHorario + " e prontuário " + prontuario + " removido com sucesso!");
            } else {
                System.err.println("Nenhum horário encontrado com o ID " + idHorario + " e prontuário " + prontuario + ".");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover o horário: " + e.getMessage());
        }
    }

    public List<Horario> findAllByAluno(String prontuario) {
        String selectQuery = "SELECT idhorario, prontuario, dia_semana, hora "
                + "FROM horario "
                + "WHERE prontuario = ?";
        List<Horario> horarios = new ArrayList<>();

        try (Connection con = ConexaoMysql.obterConexao(); PreparedStatement pst = con.prepareStatement(selectQuery)) {

            // Definindo o parâmetro da consulta
            pst.setString(1, prontuario);

            try (java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Horario horario = new Horario();

                    // Preenchendo os dados do objeto Horario
                    horario.setIdhorario(rs.getInt("idhorario"));

                    // Criando e setando o Aluno relacionado
                    Aluno aluno = new Aluno();
                    aluno.setProntuario(rs.getString("prontuario"));
                    horario.setAluno(aluno);

                    horario.setDiaSemana(rs.getString("dia_semana"));
                    horario.setHora(rs.getString("hora"));

                    // Adicionando o horário à lista
                    horarios.add(horario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar horários do aluno: " + e.getMessage());
        }

           return horarios;
    }
}
