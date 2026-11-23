package model;

import java.util.List;
import java.sql.SQLException;
import dao.UsuarioDAO;

public abstract class Usuario {

    private int id;
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private String email;
    private String telefone;
    private boolean ativo;

    private static int idLogado = -1;

    public Usuario(String nome, String cpf, String login, String senha, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
        this.ativo = true;

        try {
            UsuarioDAO.criarUsuario(this);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar o usuário.", e);
        }
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



    public String getNome() {
        return nome;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }



    public String getCpf() {
        return cpf;
    }



    public void setCpf(String cpf) {
        this.cpf = cpf;
    }



    public String getLogin() {
        return login;
    }



    public void setLogin(String login) {
        this.login = login;
    }



    public String getSenha() {
        return senha;
    }



    public void setSenha(String senha) {
        this.senha = senha;
    }



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getTelefone() {
        return telefone;
    }



    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }



    public boolean isAtivo() {
        return ativo;
    }



    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }



    public void verUsuario() {
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("e-mail: " + email);
        System.out.println("Telefone: " + telefone);
        System.out.println("Status: " + (ativo ? "Ativado" : "Desativado"));
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo) {
        usuario.nome = nome;
        usuario.cpf = cpf;
        usuario.login = login;
        usuario.senha = senha;
        usuario.email = email;
        usuario.telefone = telefone;
        usuario.ativo = ativo;

        try {
            UsuarioDAO.atualizarUsuario(usuario);
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel atualizar o usuario ", e);
        }
    }

    public static void desativarUsuario(Usuario usuario) {
        usuario.ativo = false;
        try {
            UsuarioDAO.atualizarUsuario(usuario);
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel atualizar o usuario ", e);
        }
    }

    public static boolean autenticar(String login, String senha) {
        for(Usuario usuario : listarUsuarios()) {
            if(usuario.ativo == true && usuario.login == login  && usuario.senha == senha) {
                Usuario.idLogado = usuario.id;
                return true;
            }
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        try {
            return UsuarioDAO.listarUsuarios();
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel listar os usuarios", e);
        }
    }

    public static Usuario getUsuario(int id) {
        try {
            return UsuarioDAO.encontrarUsuario(id);
        } catch(SQLException e) {
            throw new IllegalStateException("Não foi possivel encontrar o usuario de id: " + id, e);
        }
    }

    public static int getIdLogado() {
        return idLogado;
    }
}