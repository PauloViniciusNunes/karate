package view;
import model.*;
import model.enums.*;
import java.util.Scanner;

public class TelaInstrutor implements Leitor{
    public static void iniciarMenu(Instrutor instrutorLogado) {
        System.out.println("Bem-vindo, " + instrutorLogado.getNome() + "! (Instrutor)");
        int op;
        do {
            menu(instrutorLogado);
            op = sc.nextInt();
            if(op < 0) return;
            if(instrutorLogado.getCargo() == CargoInstrutor.AUXILIAR && op > 7) return;
            if(instrutorLogado.getCargo() != CargoInstrutor.SENSEI && op > 10) return;

            switch(op) {
                case 0:
                    break;
                case 1:
                    instrutorLogado.verInstrutor();
                    break;
                case 2:
                    // FALTA
                    break;
                case 3:
                    // FALTA
                    break;
                case 4:
                    // FALTA
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    gerenciarInstrutores(instrutorLogado);
                    break;
                default:
                    break;

            }

        } while(op != 0);

    }

    private static void menu(Instrutor instrutorLogado) {
        System.out.println("0 - Sair");
        System.out.println("1 - Ver dados");
        System.out.println("2 - Relatorio de frequencias");
        System.out.println("3 - Relatorio de aulas");
        System.out.println("4 - Frequencia");
        System.out.println("5 - Ver turmas");
        System.out.println("6 - Ver aulas");
        System.out.println("7 - Ver alunos");

        if(instrutorLogado.getCargo() == CargoInstrutor.AUXILIAR) return;

        System.out.println("8 - Gerenciar turmas");
        System.out.println("9 - Gerenciar aulas");
        System.out.println("10 - Gerenciar alunos");
        if(instrutorLogado.getCargo() == CargoInstrutor.SENSEI) return;

        System.out.println("11 - Gerenciar instrutores");
    }
    private static void menuGerenciarInstrutor(Instrutor instrutorLogado) {
        System.out.println("0 - Sair");
        System.out.println("1 - Cadastrar Instrutor");
        System.out.println("2 - Editar instrutor");
        System.out.println("3 - Desativar instrutor");
    }

    private static void gerenciarInstrutores(Instrutor instrutorLogado) {
        int op;
        String nome, cpf, login, senha, email, telefone, cargo, faixa;
        CargoInstrutor cargoInstrutor;
        Faixa faixaInstrutor;
        do {
            menuGerenciarInstrutor(instrutorLogado);
            op = sc.nextInt();
            if(op == 1) {
                System.out.println("Nome:");
                nome = sc.nextLine();
                System.out.println("cpf:");
                cpf = sc.nextLine();
                System.out.println("login:");
                login = sc.nextLine();
                System.out.println("senha:");
                senha = sc.nextLine();
                System.out.println("email:");
                email = sc.nextLine();
                System.out.println("telefone:");
                telefone = sc.nextLine();
                System.out.println("Cargo:");
                cargo = sc.nextLine();
                System.out.println("Faixa:");
                faixa = sc.nextLine();

                cargoInstrutor = CargoInstrutor.getCargoByName(cargo);
                faixaInstrutor = Faixa.getFaixaByName(faixa);

                if(cargoInstrutor == null || faixaInstrutor == null) {
                    System.out.println("Cargo ou faixa inválidos!");
                    return;
                }

                Instrutor.criarInstrutor(nome, cpf, login, senha, email, telefone, cargoInstrutor, faixaInstrutor);

            } else if(op == 2) {
                System.out.println("Id do instrutor:");
                int id = sc.nextInt();
                Instrutor instrutor = Instrutor.getInstrutor(id);
                if(instrutor == null) {
                    System.out.println("Instrutor não encontrado!");
                    return;
                }
                System.out.println("Nome:");
                nome = sc.nextLine();
                System.out.println("cpf:");
                cpf = sc.nextLine();
                System.out.println("login:");
                login = sc.nextLine();
                System.out.println("senha:");
                senha = sc.nextLine();
                System.out.println("email:");
                email = sc.nextLine();
                System.out.println("telefone:");
                telefone = sc.nextLine();
                System.out.println("Cargo:");
                cargo = sc.nextLine();
                System.out.println("Faixa:");
                faixa = sc.nextLine();

                cargoInstrutor = CargoInstrutor.getCargoByName(cargo);
                faixaInstrutor = Faixa.getFaixaByName(faixa);

                if(cargoInstrutor == null || faixaInstrutor == null) {
                    System.out.println("Cargo ou faixa inválidos!");
                    return;
                }

                Instrutor.editarInstrutor(instrutor, nome, cpf, login, senha, email, telefone, true, cargoInstrutor, faixaInstrutor);
            } else if (op == 3) {
                System.out.println("Id do instrutor:");
                int id = sc.nextInt();
                Instrutor instrutor = Instrutor.getInstrutor(id);
                if(instrutor == null) {
                    System.out.println("Instrutor não encontrado!");
                    return;
                }
                Instrutor.desativarUsuario(instrutor);
            } else {
                System.out.println("Opção inválida!");
            }
        } while(op != 0);


    }
}
