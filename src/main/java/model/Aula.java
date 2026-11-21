package model;

import java.util.ArrayList;
import java.time.LocalDate;
import model.enums.StatusAula;
import model.enums.TipoAula;


public class Aula{
    private LocalDate data = LocalDate.now();
    private TipoAula tipo;
    private StatusAula status;
    private ArrayList<String> cpfspresentes = new ArrayList<>();
    private ArrayList<String> cpfsfaltosos = new ArrayList<>();




public void verAula(){
    System.out.println("Data:" + data);
    System.out.println("Tipo" + data);
    System.out.println("Status:" + data);

}


public void registrarPresenca(Aluno aluno){



}

public void registrarFalta(Aluno aluno){




}


public void cancelarAula(){
    this.status = StatusAula.CANCELADA;

}

public void encerrarAula(){
    this.status = StatusAula.REALIZADA;


}

public static Aula criarAula(Turma turma, LocalDate data, TipoAula tipo){

    return null;

}

public static void editarAula(Turma turma, LocalDate data, TipoAula tipo){

}

}