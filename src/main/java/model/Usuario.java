package model;

import java.util.List;
import java.util.ArrayList;

public abstract class Usuario {
    private static List<Usuario> usuarios = new ArrayList<>();

    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private String email;
    private String telefone;
    private boolean ativo;

    public Usuario(String nome, String cpf, String login, String senha, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
        this.ativo = true;

        usuarios.add(this);
    }

    public void verUsuario() {
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
    }

    public static void desativarUsuario(Usuario usuario) {
        usuario.ativo = false;
    }

    public static boolean autenticar(String login, String senha) {
        for(Usuario usuario : usuarios) {
            if(usuario.login == login  && usuario.senha == senha) return true;
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        return usuarios;
    }

    public String getNomeUsuario() {
        return nome;
    }
}