package controller;

import dao.AlunoDAO;
import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Aluno;
import model.Turma;
import model.Usuario;
import model.enums.Faixa;

public class AlunoController {

    public static Aluno criarAluno(String nome, String cpf, String login, String senha, String email, String telefone, Date dataNascimento, int idTurma) {
        Aluno aluno = Aluno.criarAluno(nome, cpf, login, senha, email, telefone, dataNascimento, idTurma);
        try {
            AlunoDAO.criarAluno(aluno);
            return aluno;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar o aluno.", e);
        }
    }

    public static Aluno obterAluno(int id) {
        try {
            return AlunoDAO.encontrarAluno(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível encontrar o aluno de id: " + id, e);
        }
    }

    public static List<Aluno> listarAlunos() {
        try {
            return AlunoDAO.listarAlunos();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar os alunos.", e);
        }
    }

    public static List<Aluno> buscarAlunosPorNome(String nome) {
        return Aluno.buscarAlunosPorNome(nome, listarAlunos());
    }

    public static List<Aluno> listarAlunosPorFaixa(Faixa faixa) {
        return Aluno.listarAlunosPorFaixa(faixa, listarAlunos());
    }

    public static List<Aluno> listarAlunosPorTurma(Turma turma) {
        List<Aluno> alunosTurma = new ArrayList<>();
        if (turma == null) {
            return alunosTurma;
        }
        for (Aluno aluno : listarAlunos()) {
            if (aluno.getIdTurma() == turma.getIdTurma()) {
                alunosTurma.add(aluno);
            }
        }
        return alunosTurma;
    }

    public static void editarAluno(Aluno aluno, String nome, String cpf, String login, String senha, String email, String telefone, boolean ativo, Date dataNascimento, Faixa faixaAtual) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }
        Aluno.editarAluno(aluno, nome, cpf, login, senha, email, telefone, ativo, dataNascimento);
        try {
            AlunoDAO.atualizarAluno(aluno);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar o aluno.", e);
        }
    }

    public static void graduarAluno(Aluno aluno, Faixa novaFaixa) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }
        aluno.graduarAluno(novaFaixa);
        try {
            AlunoDAO.atualizarAluno(aluno);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível graduar o aluno.", e);
        }
    }

    public static void desativarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }
        Usuario.desativarUsuario(aluno);
        try {
            UsuarioDAO.atualizarUsuario(aluno);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível desativar o aluno.", e);
        }
    }

    public static void alocarAlunoNaTurma(Turma turma, Aluno aluno) {
        if (turma == null || aluno == null) {
            throw new IllegalArgumentException("Turma e aluno não podem ser nulos.");
        }
        aluno.alocarNaTurma(turma);
        try {
            AlunoDAO.atualizarAluno(aluno);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível alocar o aluno na turma.", e);
        }
    }
}
