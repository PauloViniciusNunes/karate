package controller;

import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Usuario;

public class UsuarioController {

    static void recarregarUsuarios() throws SQLException {
        List<Usuario> usuarios = UsuarioDAO.listarUsuarios();
        Usuario.limparUsuariosEmMemoria();
        for (Usuario usuario : usuarios) {
            Usuario.registrarUsuarioEmMemoria(usuario);
        }
    }

    public static boolean autenticar(String login, String senha) {
        if (login == null || senha == null) {
            return false;
        }
        return Usuario.autenticar(login, senha);
    }

    public static Usuario obterUsuario(int id) {
        return Usuario.getUsuario(id);
    }

    public static Usuario obterUsuarioPorLogin(String login) {
        if (login == null) {
            return null;
        }
        for (Usuario usuario : Usuario.listarUsuarios()) {
            if (Objects.equals(login, usuario.getLogin())) {
                return usuario;
            }
        }
        return null;
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
        return Usuario.listarUsuarios();
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
            recarregarUsuarios();
            TurmaController.recarregarTurmas();
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
            recarregarUsuarios();
            TurmaController.recarregarTurmas();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar o usuário.", e);
        }
    }
}