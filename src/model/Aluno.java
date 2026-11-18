package model;
import java.util.Date;
import model.enums.Faixa;

public class Aluno extends Usuario {
    private Date dataNascimento;
    private Faixa faixaAtual;

    public void verAluno() {
        verUsuario();
        System.out.println("Data de nascimento: " + dataNascimento);
        System.out.println("Faixa atual: " + faixaAtual);
    }

    public void graduarAluno(Faixa faixa) {
        faixaAtual = faixa;
    }

    private Aluno(String nome, String cpf, String login, String senha, String email,
        String telefone, Date dataNascimento, Faixa faixaAtual) {
        super(nome, cpf, login, senha, email, telefone);
        this.dataNascimento = dataNascimento;
        this.faixaAtual = faixaAtual;
    }

    public static Aluno criarAluno(String nome, String cpf, String login, String senha, String email,
        String telefone, Date dataNascimento, Faixa faixaAtual) {
        return new Aluno(nome, cpf, login, senha, email, telefone, dataNascimento, faixaAtual);
    }

    public static void editarAluno(Aluno aluno, String nome, String cpf, String login, String senha, String email,
        String telefone, boolean ativo, Date dataNascimento, Faixa faixaAtual) {
            editarUsuario(aluno, nome, cpf, login, senha, email, telefone, ativo);
            aluno.dataNascimento = dataNascimento;
            aluno.faixaAtual = faixaAtual;
    }

}