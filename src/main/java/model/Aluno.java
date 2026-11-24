package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    public void alocarNaTurma(Turma turma) {
        if (turma == null) {
            return;
        }
        if (!turma.getAlunos().contains(this)) {
            turma.getAlunos().add(this);
        }
        this.idTurma = turma.getIdTurma();
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
    }

    public Aluno(int id, String nome, String cpf, String login, String senha, String email,
        String telefone, boolean ativo, Date dataNascimento, int idTurma, Faixa faixaAtual) {
        super(id, nome, cpf, login, senha, email, telefone, ativo);
        this.dataNascimento = dataNascimento;
        this.idTurma = idTurma;
        this.faixaAtual = faixaAtual != null ? faixaAtual : Faixa.BRANCA;
    }

    public static Aluno criarAluno(String nome, String cpf, String login, String senha, String email,
        String telefone, Date dataNascimento, int idTurma) {
        return new Aluno(nome, cpf, login, senha, email, telefone, dataNascimento, idTurma);
    }

    public static void editarAluno(Aluno aluno, String nome, String cpf, String login, String senha, String email,
        String telefone, boolean ativo, Date dataNascimento) {
        if (aluno == null) {
            return;
        }
        editarUsuario(aluno, nome, cpf, login, senha, email, telefone, ativo);
        aluno.dataNascimento = dataNascimento;
    }

    public static List<Aluno> listarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        for (Usuario usuario : Usuario.listarUsuarios()) {
            if (usuario instanceof Aluno aluno) {
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public static List<Aluno> buscarAlunosPorNome(String nome) {
        return buscarAlunosPorNome(nome, listarAlunos());
    }

    public static List<Aluno> buscarAlunosPorNome(String nome, List<Aluno> origem) {
        List<Aluno> alunos = new ArrayList<>();
        if (origem == null || nome == null) {
            return alunos;
        }
        for (Aluno aluno : origem) {
            if (aluno != null && Objects.equals(nome, aluno.getNome())) {
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public static List<Aluno> listarAlunosPorFaixa(Faixa faixa) {
        return listarAlunosPorFaixa(faixa, listarAlunos());
    }

    public static List<Aluno> listarAlunosPorFaixa(Faixa faixa, List<Aluno> origem) {
        List<Aluno> alunos = new ArrayList<>();
        if (origem == null) {
            return alunos;
        }
        for (Aluno aluno : origem) {
            if (aluno != null && faixa == aluno.faixaAtual) {
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public static List<Aluno> listarAlunosPorTurma(Turma turma) {
        List<Aluno> alunos = new ArrayList<>();
        if (turma == null || turma.getAlunos() == null) {
            return alunos;
        }
        for (Aluno aluno : turma.getAlunos()) {
            alunos.add(aluno);
        }
        return alunos;
    }

    public Aluno getAluno(int id) {
        for (Aluno aluno : listarAlunos()) {
            if (aluno.getId() == id) {
                return aluno;
            }
        }
        return null;
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
