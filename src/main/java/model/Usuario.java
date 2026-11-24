package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static final List<Usuario> listaUsuarios = new ArrayList<>();

    public Usuario(String nome, String cpf, String login, String senha, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
        this.ativo = true;
    }

    protected Usuario(int id, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
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
        if (usuario == null) {
            return;
        }
        usuario.nome = nome;
        usuario.cpf = cpf;
        usuario.login = login;
        usuario.senha = senha;
        usuario.email = email;
        usuario.telefone = telefone;
        usuario.ativo = ativo;
    }

    public static void desativarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuario.ativo = false;
        }
    }

    public boolean validarCredenciais(String login, String senha) {
        return this.ativo
                && Objects.equals(this.login, login)
                && Objects.equals(this.senha, senha);
    }

    public static boolean autenticar(String login, String senha) {
        return autenticar(login, senha, listarUsuarios());
    }

    public static boolean autenticar(String login, String senha, List<Usuario> usuarios) {
        if (usuarios == null) {
            return false;
        }
        for (Usuario usuario : usuarios) {
            if (usuario != null && usuario.validarCredenciais(login, senha)) {
                Usuario.idLogado = usuario.id;
                return true;
            }
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        return new ArrayList<>(listaUsuarios);
    }

    public static Usuario getUsuario(int id) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }

    public static void registrarUsuarioEmMemoria(Usuario usuario) {
        if (usuario != null && !listaUsuarios.contains(usuario)) {
            listaUsuarios.add(usuario);
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

    public static int getIdLogado() {
        return idLogado;
    }

    public static void setIdLogado(int idLogado) {
        Usuario.idLogado = idLogado;
    }
}