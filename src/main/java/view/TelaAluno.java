package view;
import dao.*;
import model.*;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;

public class TelaAluno implements Leitor{
    public static void iniciarMenu(Aluno alunoLogado) {
        System.out.println("Bem vindo, " + alunoLogado.getNome() + "! (Aluno)");
        int op;

        do {
            menu(alunoLogado);
            op = sc.nextInt();

            switch (op) {
                case 0:
                    System.out.println("Encerrando...");    

                    break;

                case 1:
                    verTurma(alunoLogado);
                    
                    break;

                case 2:
                    listarAulas(alunoLogado);

                    break;
            
                default:
                    System.out.println("Opção inválida");

                    break;
            }

        } while (op != 0);
    }

    private static void menu(Aluno alunoLogado) {
        System.out.println("0 - Sair");
        System.out.println("1 - Ver dados da turma");
        System.out.println("2 - Ver lista de aulas que participou");
    }

    private static void verTurma(Aluno alunoLogado) {
        try {
            Turma turma = TurmaDAO.encontrarTurma(alunoLogado.getIdTurma());

            if (turma != null) {
                turma.verTurma();
            } else {
                System.out.println("Turma não encontrada.");
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível encontrar a turma.");
        }
    }

    private static void listarAulas(Aluno alunoLogado) {
        List<Aula> aulas = Aula.listarAulasPorAluno(alunoLogado);
        for(Aula aula : aulas) {
            aula.verAula();
        }
    }
    
}
