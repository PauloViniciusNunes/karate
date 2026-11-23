package model;

import model.enums.DiaSemana;
import model.enums.Faixa;
import java.util.List;

import dao.AlunoDAO;
import dao.TurmaDAO;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Turma {
    private String nome;
    private List<DiaSemana> diasSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Faixa faixaInicial;
    private Faixa faixaFinal;
    private boolean ativa;
    private int idTurma;
    private int idInstrutor;
    private Instrutor responsavel;

    private ArrayList<Aluno> alunos = new ArrayList<>();


    public void alocarNaTurma(Aluno aluno) {
        alunos.add(aluno);
        aluno.setIdTurma(this.idTurma);
        try {
            AlunoDAO.atualizarAluno(aluno);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível alocar o aluno na turma", e);
        }
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void verTurma() {
        System.out.println("Tuma: " + nome + " Status: " + ativa);
        System.out.println("Instrutor: " + responsavel.getNomeUsuario());
        System.out.println("Horario de aula: " + horaInicio + " as " + horaFim );
        System.out.println("Dias de aula: " + diasSemana);
        System.out.println("Faixas inicial: " + faixaInicial + " e final: " + faixaFinal);
    }

    private Turma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        this.responsavel = responsavel;
        this.idInstrutor = responsavel != null ? responsavel.getId() : 0;
        this.nome = nome;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.faixaInicial = faixaInicial;
        this.faixaFinal = faixaFinal;
        this.ativa = true;
        try{
            TurmaDAO.criarTurma(this);
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel criar a turma", e);
        }
    }

    public static Turma criarTurma(Instrutor responsavel, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        return new Turma(responsavel, nome, diasSemana, horaInicio, horaFim, faixaInicial, faixaFinal);
    }


    public static void editarTurma(Turma turma, String nome, List<DiaSemana> diasSemana,
    LocalTime horaInicio, LocalTime horaFim, Faixa faixaInicial, Faixa faixaFinal) {
        turma.nome = nome;
        turma.diasSemana = diasSemana;
        turma.horaInicio = horaInicio;
        turma.horaFim = horaFim;
        turma.faixaInicial = faixaInicial;
        turma.faixaFinal = faixaFinal;
        turma.ativa = true;
    }

    public static Turma buscarTurmaPorFaixa(Faixa faixa) {
        for(Turma turma : listarTurmas()) {
            if(turma.faixaInicial == faixa) {
                return turma;
            }
        }
        return null;
    }

    public static void desativarTurma(Turma turma) {
        turma.ativa = false;
    }

    public static List<Turma> listarTurmas() {
        try {
            return TurmaDAO.listarTurmas();
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel listar as turmas", e);
        }
    }

    public static List<Turma> listarTurmasPorFaixa(Faixa faixa) {
        List<Turma> turmasPorFaixa = new ArrayList<>();
        for(Turma turma : listarTurmas()) {
            if(faixa == turma.faixaInicial) {
                turmasPorFaixa.add(turma);
            }
        }
        return turmasPorFaixa;
    }

    public Turma geTurma(int idTurma) {
        try {
            return TurmaDAO.encontrarTurma(idTurma);
        } catch(SQLException e) {
             throw new IllegalStateException("Não foi possivel encontrar a turma de id: " + idTurma, e);
        }
    }

    public String getNome() {
        return nome;
    }

    public List<DiaSemana> getDiasSemana() {
        return diasSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public Faixa getFaixaInicial() {
        return faixaInicial;
    }

    public Faixa getFaixaFinal() {
        return faixaFinal;
    }

    public boolean getAtiva() {
        return ativa;
    }
    public int getIdTurma() {
        return idTurma;
    }

    public int getIdInstrutor() {
        return idInstrutor;
    }

    public Instrutor getResponsavel() {
        return responsavel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDiasSemana(List<DiaSemana> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public void setFaixaInicial(Faixa faixaInicial) {
        this.faixaInicial = faixaInicial;
    }

    public void setFaixaFinal(Faixa faixaFinal) {
        this.faixaFinal = faixaFinal;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public void setIdInstrutor(int idInstrutor) {
        this.idInstrutor = idInstrutor;
    }

    public void setResponsavel(Instrutor responsavel) {
        this.responsavel = responsavel;
        if (responsavel != null) {
            this.idInstrutor = responsavel.getId();
        }
    }

}