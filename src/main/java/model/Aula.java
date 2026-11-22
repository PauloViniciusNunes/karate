package model;

import java.util.ArrayList;
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
        System.out.println("Tipo" + data);
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
        int novoId = gerarId.nextInt(100);
        int idDaTurma = turma.getIdTurma();
        data = LocalDate.now();
        StatusAula status = StatusAula.AGENDADA;
        ArrayList<String> cpfsPresentes = new ArrayList<>();
        ArrayList<String> cpfsFaltosos = new ArrayList<>();
        
        
        

        return new Aula(novoId, idDaTurma, data, tipo, status, cpfsPresentes, cpfsFaltosos);

    }

    public static void editarAula(Turma turma, LocalDate data, TipoAula tipo){

    }

    
    public static ArrayList<Aula>  listarAulas(){
        return null;

    }
    
    public static ArrayList<Aula> listarAulasPorPeriodo(){
        return null;
    }

    public static ArrayList<Aula> listarAulasPorTurma(){
        return null;
    }

    public static ArrayList<Aula> listarAulasPorTipo(){
        return null;
    }

    public static ArrayList<Aula> listarAulasPorAluno(){
        return null;
    }
    
    public static ArrayList<Aula> gerarRelatorioAulas(){
        return null;
    }

    public static ArrayList<Aula> gerarRelatorioFrequencia(){
        return null;
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
