package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.AlunoDAO;

import java.util.Date;
import model.enums.Faixa;

public class Aluno extends Usuario {

    private int idTurma;
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
        String telefone, Date dataNascimento, int idTurma) {
        super(nome, cpf, login, senha, email, telefone);
        this.dataNascimento = dataNascimento;
        this.faixaAtual = Faixa.BRANCA;
        this.idTurma = idTurma;
        try {
            AlunoDAO.criarAluno(this);
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel criar o aluno", e);
        }
    }

    public static Aluno criarAluno(String nome, String cpf, String login, String senha, String email,
        String telefone, Date dataNascimento, int idTurma) {
        return new Aluno(nome, cpf, login, senha, email, telefone, dataNascimento, idTurma);
    }

    public static void editarAluno(Aluno aluno, String nome, String cpf, String login, String senha, String email,
        String telefone, boolean ativo, Date dataNascimento, Faixa faixaAtual) {
            editarUsuario(aluno, nome, cpf, login, senha, email, telefone, ativo);
            aluno.dataNascimento = dataNascimento;
            aluno.faixaAtual = faixaAtual;
    }

    public static List<Aluno> listarAlunos() {
        try {
            return AlunoDAO.listarAlunos();
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel listar os alunos", e);
        }
    }

    public static List<Aluno> buscarAlunosPorNome(String nome) {
        List<Aluno> alunos = new ArrayList<>();
        for(Aluno aluno : listarAlunos()) {
            if(nome.equals(aluno.getNome())) {
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

    public Aluno getAluno(int id) {
        try {
            return AlunoDAO.encontrarAluno(id);
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel encontrar o aluno de id: " + id, e);
        }
    }
    
    public int getIdTurma() {
        return this.idTurma;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public Faixa getFaixaAtual() {
        return faixaAtual;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setFaixaAtual(Faixa faixaAtual) {
        this.faixaAtual = faixaAtual;
    }

}