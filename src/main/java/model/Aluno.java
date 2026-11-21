package model;

import java.util.ArrayList;
import java.util.List;
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
        String telefone, Date dataNascimento) {
        super(nome, cpf, login, senha, email, telefone);
        this.dataNascimento = dataNascimento;
        this.faixaAtual = Faixa.BRANCA;
    }

    public static Aluno criarAluno(String nome, String cpf, String login, String senha, String email,
        String telefone, Date dataNascimento) {
        return new Aluno(nome, cpf, login, senha, email, telefone, dataNascimento);
    }

    public static void editarAluno(Aluno aluno, String nome, String cpf, String login, String senha, String email,
        String telefone, boolean ativo, Date dataNascimento, Faixa faixaAtual) {
            editarUsuario(aluno, nome, cpf, login, senha, email, telefone, ativo);
            aluno.dataNascimento = dataNascimento;
            aluno.faixaAtual = faixaAtual;
    }

    public static List<Aluno> listarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        for(Usuario usuario : listarUsuarios()) {
            if(usuario instanceof Aluno aluno) alunos.add(aluno);
        }
        return alunos;
    }

    public static List<Aluno> buscarAlunosPorNome(String nome) {
        List<Aluno> alunos = new ArrayList<>();
        for(Aluno aluno : listarAlunos()) {
            if(nome.equals(aluno.getNomeUsuario())) {
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public static List<Aluno> listarAlunosPorFaixa(Faixa faixa) {
        List<Aluno> alunos = new ArrayList<>();
        for(Aluno aluno : listarAlunos()) {
            if(faixa == aluno.faixaAtual) {
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public static List<Aluno> listarAlunosPorTurma(Turma turma) {
        List<Aluno> alunos = new ArrayList<>();
        for(Aluno aluno : turma.getAlunos()) {
            alunos.add(aluno);
        }
        return alunos;
    }
    
    public Date getDataNascimento() {
        return dataNascimento;
    }

    public Faixa getFaixaAtual() {
        return faixaAtual;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setFaixaAtual(Faixa faixaAtual) {
        this.faixaAtual = faixaAtual;
    }

}