package main.java.model;

import main.java.model.enums.DiaSemana;
import main.java.model.enums.Faixa;
import java.util.List;
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

    private Instrutor responsavel;
    private ArrayList<Aluno> alunos = new ArrayList<>();

    private static ArrayList<Turma> turmas = new ArrayList<>();

    public void alocarNaTurma(Aluno aluno) {
        alunos.add(aluno);
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
        this.nome = nome;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.faixaInicial = faixaInicial;
        this.faixaFinal = faixaFinal;
        this.ativa = true;
        turmas.add(this);
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
        for(Turma turma : turmas) {
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
        return turmas;
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

}