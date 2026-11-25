package controller;

import dao.InstrutorDAO;
import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;
import model.Instrutor;
import model.Usuario;
import model.enums.CargoInstrutor;
import model.enums.Faixa;

public class InstrutorController {

    public static Instrutor criarInstrutor(String nome, String cpf, String login, String senha, String email, String telefone, CargoInstrutor cargo, Faixa faixa) {
        Instrutor instrutor = Instrutor.criarInstrutor(nome, cpf, login, senha, email, telefone, cargo, faixa);
        try {
            InstrutorDAO.criarInstrutor(instrutor);
            return instrutor;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar o instrutor.", e);
        }
    }

    public static Instrutor obterInstrutor(int id) {
        try {
            return InstrutorDAO.encontrarInstrutor(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar o instrutor de id: " + id, e);
        }
    }

    public static List<Instrutor> listarInstrutores() {
        try {
            return InstrutorDAO.listarInstrutores();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar os instrutores.", e);
        }
    }

    public static List<Instrutor> listarInstrutoresPorCargo(CargoInstrutor cargo) {
        return Instrutor.listarInstrutoresPorCargo(cargo, listarInstrutores());
    }

    public static void editarInstrutor(Instrutor instrutor, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo, CargoInstrutor cargo, Faixa faixa) {
        if (instrutor == null) {
            throw new IllegalArgumentException("Instrutor não pode ser nulo.");
        }
        Instrutor.editarInstrutor(instrutor, nome, cpf, login, senha, email, telefone, ativo, cargo, faixa);
        try {
            InstrutorDAO.atualizarInstrutor(instrutor);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar o instrutor.", e);
        }
    }

    public static void desativarInstrutor(Instrutor instrutor) {
        if (instrutor == null) {
            throw new IllegalArgumentException("Instrutor não pode ser nulo.");
        }
        Usuario.desativarUsuario(instrutor);
        try {
            UsuarioDAO.atualizarUsuario(instrutor);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar o instrutor.", e);
        }
    }
}