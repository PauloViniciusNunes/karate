package view;

import controller.AulaController;
import controller.TurmaController;
import java.util.List;
import model.Aluno;
import model.Aula;
import model.Turma;

public class TelaAluno implements Leitor {
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
                    verAluno(alunoLogado);
                    break;

                case 2:
                    verTurma(alunoLogado);
                    break;

                case 3:
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
        System.out.println("1 - Ver dados do aluno");
        System.out.println("2 - Ver dados da turma");
        System.out.println("3 - Ver lista de aulas que participou");
    }

    private static void verTurma(Aluno alunoLogado) {
        try {
            Turma turma = TurmaController.obterTurma(alunoLogado.getIdTurma());

            if (turma != null) {
                turma.verTurma();
            } else {
                System.out.println("Turma não encontrada.");
            }

        } catch (IllegalStateException e) {
            System.out.println("Não foi possível encontrar a turma.");
        }
    }

    private static void verAluno(Aluno alunoLogado) {
        alunoLogado.verAluno();
    }

    private static void listarAulas(Aluno alunoLogado) {
        List<Aula> aulas = AulaController.listarAulasPorAluno(alunoLogado);

        System.out.println("Quantidade de aulas: " + aulas.size());

        for (Aula aula : aulas) {
            aula.verAula();
        }
    }
}