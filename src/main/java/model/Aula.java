package model;

import java.util.ArrayList;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Random;
import model.enums.StatusAula;
import model.enums.TipoAula;


public class Aula{
    private int id;
    private int idTurma;
    private LocalDate data = LocalDate.now();
    private TipoAula tipo;
    private StatusAula status;
    private ArrayList<String> cpfsPresentes = new ArrayList<>();
    private ArrayList<String> cpfsFaltosos = new ArrayList<>();


    private static ArrayList<Aula> listaDeAulas = new ArrayList<>();


    private Aula(int id, int idTurma, LocalDate data, TipoAula tipo ,StatusAula status, ArrayList<String> cpfPresentes, ArrayList<String> cpfsFaltosos){
        this.id = id;
        this.idTurma = idTurma;
        this.data = data;
        this.tipo = tipo;
        this.status = status;
        this.cpfsPresentes = cpfPresentes;
        this.cpfsFaltosos = cpfsFaltosos;
        
    }

    public void verAula(){
        System.out.println("Data:" + data);
        System.out.println("Tipo:" + data);
        System.out.println("Status:" + data);

    }


    public void registrarPresenca(Aluno aluno){
        cpfsPresentes.add(aluno.getCpf());



    }

    public void registrarFalta(Aluno aluno){
        cpfsFaltosos.add(aluno.getCpf());


    }


    public void cancelarAula(){
        this.status = StatusAula.CANCELADA;

    }

    public void encerrarAula(){
        this.status = StatusAula.REALIZADA;


    }

    public  static Aula criarAula(Turma turma, LocalDate data, TipoAula tipo){
        Random gerarId = new Random();
        int novoId = gerarId.nextInt(1000);
        int idDaTurma = turma.getIdTurma();
        data = LocalDate.now();
        StatusAula status = StatusAula.AGENDADA;
        ArrayList<String> cpfsPresentes = new ArrayList<>();
        ArrayList<String> cpfsFaltosos = new ArrayList<>();
        
        
        

        return new Aula(novoId, idDaTurma, data, tipo, status, cpfsPresentes, cpfsFaltosos);

    }

    public static void editarAula(Aula aula, LocalDate data, TipoAula tipo){
        aula.data = data;
        aula.tipo = tipo;

    }

    
    public static ArrayList<Aula> listarAulas(){
        return listaDeAulas;

    }
    
    public static ArrayList<Aula> listarAulasPorPeriodo(LocalDate inicio, LocalDate fim){
        ArrayList<Aula> listaAulasPeriodo = new ArrayList<>();

        for (Aula buscaPeriodo : listarAulas()) {
            if(buscaPeriodo.data.isBefore(fim) && buscaPeriodo.data.isAfter(inicio)){
                listaAulasPeriodo.add(buscaPeriodo);
            }
        }
        return listaAulasPeriodo;
    }
    public static ArrayList<Aula> listarAulasPorTurma(Turma turma){
        ArrayList<Aula> listaAulasTurma = new ArrayList<>();
        
        for (Aula listaPorTurma : listarAulas()) {
            if(listaPorTurma.idTurma == turma.getIdTurma()){
                listaAulasTurma.add(listaPorTurma);
            }
            
        }

        return listaAulasTurma;
    }

    public static ArrayList<Aula> listarAulasPorTipo(TipoAula tipo){
        ArrayList<Aula> listaAulasTipo = new ArrayList<>();

        for (Aula listaPorTipo : listarAulas()) {
            if(tipo == listaPorTipo.tipo){ 
                listaAulasTipo.add(listaPorTipo);
            }
        }

        return listaAulasTipo;
    }

    public static ArrayList<Aula> listarAulasPorAluno(Aluno aluno){
        ArrayList<Aula> listaAulasAluno = new ArrayList<>();
        

        for (Aula listandoAluno : listarAulas()){
            if(listandoAluno.cpfsPresentes.contains(aluno.getCpf())){
                listaAulasAluno.add(listandoAluno);

            }
            
        }


        return listaAulasAluno;
    }
    
    public static ArrayList<Aula> gerarRelatorioAulas(LocalDate inicio, LocalDate fim, Turma turma, TipoAula tipo){
        ArrayList<Aula> listaAulasRelatorio = new ArrayList<>();
        ArrayList<Aula> listaAulasRelatorioData = listarAulasPorPeriodo(inicio, fim);
        ArrayList<Aula> porTurma = listarAulasPorTurma(turma); 
        ArrayList<Aula> porTipo = listarAulasPorTipo(tipo);
        
        for (Aula aulaRelatorio  : listaAulasRelatorioData) {

            if(porTurma.contains(aulaRelatorio) && porTipo.contains(aulaRelatorio)){
                listaAulasRelatorio.add(aulaRelatorio);
            } 
            
        }
        
        return listaAulasRelatorio;
    }
    

    public static ArrayList<Aula> gerarRelatorioFrequencia(Aluno aluno, LocalDate inicio, LocalDate fim){ 
        ArrayList<Aula> alunosFrequencia = new ArrayList<>() ;
        ArrayList<Aula> listaAulasRelatorioData = listarAulasPorPeriodo(inicio, fim);
        
        for (Aula aula : listaAulasRelatorioData) {
            if(aula.cpfsPresentes.contains(aluno.getCpf())){
                alunosFrequencia.add(aula);

            }

            
        }
    
    return alunosFrequencia;
    }

        


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipoAula getTipo() {
        return tipo;
    }

    public void setTipo(TipoAula tipo) {
        this.tipo = tipo;
    }

    public StatusAula getStatus() {
        return status;
    }

    public void setStatus(StatusAula status) {
        this.status = status;
    }



}
