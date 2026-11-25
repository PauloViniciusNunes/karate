package controller;

import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

public class UsuarioController {

    public static boolean autenticar(String login, String senha) {
        if (login == null || senha == null) {
            return false;
        }
        try {
            Usuario usuario = UsuarioDAO.encontrarUsuarioPorLogin(login);
            if (usuario != null && usuario.validarCredenciais(login, senha)) {
                Usuario.setIdLogado(usuario.getId());
                return true;
            }
            return Usuario.autenticar(login, senha, listarUsuarios());
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível autenticar o usuário.", e);
        }
    }

    public static Usuario obterUsuario(int id) {
        try {
            return UsuarioDAO.encontrarUsuario(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar o usuário de id: " + id, e);
        }
    }

    public static Usuario obterUsuarioPorLogin(String login) {
        try {
            return UsuarioDAO.encontrarUsuarioPorLogin(login);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar o usuário de login: " + login, e);
        }
    }

    public static Usuario getUsuarioLogado() {
        int idLogado = Usuario.getIdLogado();
        if (idLogado <= 0) {
            return null;
        }
        return obterUsuario(idLogado);
    }

    public static int getIdLogado() {
        return Usuario.getIdLogado();
    }

    public static List<Usuario> listarUsuarios() {
        try {
            return UsuarioDAO.listarUsuarios();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar os usuários.", e);
        }
    }

    public static List<Usuario> listarUsuariosAtivos() {
        List<Usuario> ativos = new ArrayList<>();
        for (Usuario usuario : listarUsuarios()) {
            if (usuario.isAtivo()) {
                ativos.add(usuario);
            }
        }
        return ativos;
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        Usuario.editarUsuario(usuario, nome, cpf, login, senha, email, telefone, ativo);
        try {
            UsuarioDAO.atualizarUsuario(usuario);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar o usuário.", e);
        }
    }

    public static void desativarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        Usuario.desativarUsuario(usuario);
        try {
            UsuarioDAO.atualizarUsuario(usuario);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar o usuário.", e);
        }
    }
}