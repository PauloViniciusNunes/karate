package view;

import controller.AlunoController;
import controller.AulaController;
import controller.InstrutorController;
import controller.TurmaController;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import model.Aluno;
import model.Aula;
import model.Instrutor;
import model.Turma;
import model.enums.CargoInstrutor;
import model.enums.DiaSemana;
import model.enums.Faixa;
import model.enums.TipoAula;

public class TelaInstrutor implements Leitor {
    public static void iniciarMenu(Instrutor instrutorLogado) {
        System.out.println("Bem-vindo, " + instrutorLogado.getNome() + "! (Instrutor)");
        int op;
        do {
            menu(instrutorLogado);
            op = lerInt("Opção: ");
            if (op < 0) {
                System.out.println("Opção inválida!");
                continue;
            }
            if (instrutorLogado.getCargo() == CargoInstrutor.AUXILIAR && op > 7) {
                System.out.println("Opção inválida ou sem permissão!");
                continue;
            }
            if (instrutorLogado.getCargo() == CargoInstrutor.SENSEI && op > 10) {
                System.out.println("Opção inválida ou sem permissão!");
                continue;
            }

            switch (op) {
                case 0:
                    System.out.println("Encerrando...");
                    sc.close();
                    return;
                case 1:
                    instrutorLogado.verInstrutor();
                    break;
                case 2:
                    gerarRelatorioFrequencia();
                    break;
                case 3:
                    gerarRelatorioAulas();
                    break;
                case 4:
                    gerenciarFrequencia();
                    break;
                case 5:
                    verTurmas();
                    break;
                case 6:
                    verAulas();
                    break;
                case 7:
                    verAlunos();
                    break;
                case 8:
                    gerenciarTurmas();
                    break;
                case 9:
                    gerenciarAulas();
                    break;
                case 10:
                    gerenciarAlunos();
                    break;
                case 11:
                    gerenciarInstrutores();
                    break;
                case 12:
                    verInstrutores();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

        } while (op != 0);
    }

    private static void menu(Instrutor instrutorLogado) {
        System.out.println("\n===============================================================");
        System.out.println("0 - Sair");
        System.out.println("1 - Ver dados");
        System.out.println("2 - Relatorio de frequencias");
        System.out.println("3 - Relatorio de aulas");
        System.out.println("4 - Frequencia");
        System.out.println("5 - Ver turmas");
        System.out.println("6 - Ver aulas");
        System.out.println("7 - Ver alunos");

        if (instrutorLogado.getCargo() == CargoInstrutor.AUXILIAR) return;

        System.out.println("8 - Gerenciar turmas");
        System.out.println("9 - Gerenciar aulas");
        System.out.println("10 - Gerenciar alunos");
        if (instrutorLogado.getCargo() == CargoInstrutor.SENSEI) return;

        System.out.println("11 - Gerenciar instrutores");
        System.out.println("12 - Ver Instrutores");
    }

    private static void gerarRelatorioFrequencia() {
        int idAluno = lerInt("Id do aluno: ");
        Aluno aluno = AlunoController.obterAluno(idAluno);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }

        LocalDate inicio = lerData("Data inicial (yyyy-MM-dd): ");
        LocalDate fim = lerData("Data final (yyyy-MM-dd): ");
        if (inicio == null || fim == null) {
            return;
        }
        if (fim.isBefore(inicio)) {
            System.out.println("A data final não pode ser anterior à data inicial!");
            return;
        }

        List<Aula> aulasPresentes = AulaController.gerarRelatorioFrequencia(aluno, inicio, fim);
        System.out.println("\n--- Relatório de Frequência: " + aluno.getNome() + " ---");
        System.out.println("Total de presenças no período: " + aulasPresentes.size());
        for (Aula aula : aulasPresentes) {
            System.out.println("--------------------------------");
            System.out.println("ID da aula: " + aula.getId() + " | ID da turma: " + aula.getIdTurma());
            aula.verAula();
        }
    }

    private static void gerarRelatorioAulas() {
        LocalDate inicio = lerData("Data inicial (yyyy-MM-dd): ");
        LocalDate fim = lerData("Data final (yyyy-MM-dd): ");
        if (inicio == null || fim == null) {
            return;
        }
        if (fim.isBefore(inicio)) {
            System.out.println("A data final não pode ser anterior à data inicial!");
            return;
        }

        int idTurma = lerInt("Id da turma: ");
        Turma turma = TurmaController.obterTurma(idTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }

        TipoAula tipo = lerTipoAula();
        if (tipo == null) {
            return;
        }

        List<Aula> aulas = AulaController.gerarRelatorioAulas(inicio, fim, turma, tipo);
        System.out.println("\n--- Relatório de Aulas (" + turma.getNome() + " - " + tipo + ") ---");
        System.out.println("Total de aulas encontradas: " + aulas.size());
        for (Aula aula : aulas) {
            System.out.println("--------------------------------");
            System.out.println("ID da aula: " + aula.getId());
            aula.verAula();
            System.out.println("Presentes: " + aula.getCpfsPresentes().size() + " | Faltosos: " + aula.getCpfsFaltosos().size());
        }
    }

    private static void gerenciarFrequencia() {
        int op;
        do {
            System.out.println("\n===============================================================");
            System.out.println("0 - Voltar");
            System.out.println("1 - Registrar presença individual");
            System.out.println("2 - Registrar falta individual");
            System.out.println("3 - Fazer chamada da turma");
            op = lerInt("Opção: ");

            if (op == 1 || op == 2) {
                int idAula = lerInt("Id da aula: ");
                Aula aula = AulaController.obterAula(idAula);
                if (aula == null) {
                    System.out.println("Aula não encontrada!");
                    continue;
                }
                int idAluno = lerInt("Id do aluno: ");
                Aluno aluno = AlunoController.obterAluno(idAluno);
                if (aluno == null) {
                    System.out.println("Aluno não encontrado!");
                    continue;
                }
                try {
                    if (op == 1) {
                        AulaController.registrarPresenca(aula, aluno);
                        System.out.println("Presença registrada com sucesso!");
                    } else {
                        AulaController.registrarFalta(aula, aluno);
                        System.out.println("Falta registrada com sucesso!");
                    }
                } catch (RuntimeException e) {
                    exibirErro("registrar frequência", e);
                }
            } else if (op == 3) {
                int idAula = lerInt("Id da aula: ");
                Aula aula = AulaController.obterAula(idAula);
                if (aula == null) {
                    System.out.println("Aula não encontrada!");
                    continue;
                }
                Turma turma = TurmaController.obterTurma(aula.getIdTurma());
                if (turma == null) {
                    System.out.println("Turma da aula não encontrada!");
                    continue;
                }
                List<Aluno> alunos = AlunoController.listarAlunosPorTurma(turma);
                if (alunos.isEmpty()) {
                    System.out.println("Nenhum aluno alocado nessa turma.");
                    continue;
                }
                for (Aluno aluno : alunos) {
                    if (!aluno.isAtivo()) {
                        continue;
                    }
                    String resp = lerTexto("Aluno " + aluno.getNome() + " (ID " + aluno.getId() + ") presente? (S/N): ");
                    try {
                        if (resp.equalsIgnoreCase("S")) {
                            AulaController.registrarPresenca(aula, aluno);
                        } else {
                            AulaController.registrarFalta(aula, aluno);
                        }
                    } catch (RuntimeException e) {
                        exibirErro("registrar frequência para " + aluno.getNome(), e);
                    }
                }
                System.out.println("Chamada concluída!");
            } else if (op != 0) {
                System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void verTurmas() {
        List<Turma> turmas = TurmaController.listarTurmas();
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }
        for (Turma turma : turmas) {
            System.out.println("--------------------------------");
            turma.verTurma();
            System.out.println("Quantidade de alunos: " + turma.getAlunos().size());
        }
    }

    private static void verAulas() {
        List<Aula> aulas = AulaController.listarAulas();
        if (aulas.isEmpty()) {
            System.out.println("Nenhuma aula cadastrada.");
            return;
        }
        for (Aula aula : aulas) {
            System.out.println("--------------------------------");
            System.out.println("ID: " + aula.getId() + " | ID da turma: " + aula.getIdTurma());
            aula.verAula();
            System.out.println("Presentes: " + aula.getCpfsPresentes().size() + " | Faltosos: " + aula.getCpfsFaltosos().size());
        }
    }

    private static void verAlunos() {
        List<Aluno> alunos = AlunoController.listarAlunos();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        for (Aluno aluno : alunos) {
            System.out.println("--------------------------------");
            aluno.verAluno();
            System.out.println("ID da turma: " + (aluno.getIdTurma() > 0 ? aluno.getIdTurma() : "Sem turma"));
        }
    }

    private static void verInstrutores() {
        List<Instrutor> instrutores = InstrutorController.listarInstrutores();
        if (instrutores.isEmpty()) {
            System.out.println("Nenhum instrutor cadastrado.");
            return;
        }
        for (Instrutor instrutor : instrutores) {
            System.out.println("--------------------------------");
            instrutor.verInstrutor();
        }
    }

    private static void gerenciarTurmas() {
        int op;
        do {
            System.out.println("\n===============================================================");
            System.out.println("0 - Voltar");
            System.out.println("1 - Cadastrar turma");
            System.out.println("2 - Editar turma");
            System.out.println("3 - Desativar turma");
            op = lerInt("Opção: ");

            if (op == 1) {
                int idInstrutor = lerInt("Id do instrutor responsável: ");
                Instrutor responsavel = InstrutorController.obterInstrutor(idInstrutor);
                if (responsavel == null) {
                    System.out.println("Instrutor não encontrado!");
                    continue;
                }
                String nome = lerTexto("Nome da turma: ");
                List<DiaSemana> dias = lerDiasSemana();
                LocalTime horaInicio = lerHora("Hora de início (HH:mm): ");
                LocalTime horaFim = lerHora("Hora de fim (HH:mm): ");
                Faixa faixaInicial = lerFaixa("Faixa inicial: ");
                Faixa faixaFinal = lerFaixa("Faixa final: ");

                if (dias == null || horaInicio == null || horaFim == null || faixaInicial == null || faixaFinal == null) {
                    continue;
                }

                try {
                    TurmaController.criarTurma(responsavel, nome, dias, horaInicio, horaFim, faixaInicial, faixaFinal);
                    System.out.println("Turma cadastrada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("cadastrar turma", e);
                }
            } else if (op == 2) {
                int idTurma = lerInt("Id da turma: ");
                Turma turma = TurmaController.obterTurma(idTurma);
                if (turma == null) {
                    System.out.println("Turma não encontrada!");
                    continue;
                }
                String nome = lerTexto("Nome da turma: ");
                List<DiaSemana> dias = lerDiasSemana();
                LocalTime horaInicio = lerHora("Hora de início (HH:mm): ");
                LocalTime horaFim = lerHora("Hora de fim (HH:mm): ");
                Faixa faixaInicial = lerFaixa("Faixa inicial: ");
                Faixa faixaFinal = lerFaixa("Faixa final: ");
                boolean ativa = lerTexto("Ativa? (S/N): ").equalsIgnoreCase("S");

                if (dias == null || horaInicio == null || horaFim == null || faixaInicial == null || faixaFinal == null) {
                    continue;
                }

                try {
                    TurmaController.editarTurma(turma, nome, dias, horaInicio, horaFim, faixaInicial, faixaFinal, ativa);
                    System.out.println("Turma atualizada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("editar turma", e);
                }
            } else if (op == 3) {
                int idTurma = lerInt("Id da turma: ");
                Turma turma = TurmaController.obterTurma(idTurma);
                if (turma == null) {
                    System.out.println("Turma não encontrada!");
                    continue;
                }
                try {
                    TurmaController.desativarTurma(turma);
                    System.out.println("Turma desativada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("desativar turma", e);
                }
            } else if (op != 0) {
                System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void gerenciarAulas() {
        int op;
        do {
            System.out.println("\n===============================================================");
            System.out.println("0 - Voltar");
            System.out.println("1 - Cadastrar aula");
            System.out.println("2 - Editar aula");
            System.out.println("3 - Cancelar aula");
            System.out.println("4 - Encerrar aula");
            op = lerInt("Opção: ");

            if (op == 1) {
                int idTurma = lerInt("Id da turma: ");
                Turma turma = TurmaController.obterTurma(idTurma);
                if (turma == null) {
                    System.out.println("Turma não encontrada!");
                    continue;
                }
                LocalDate data = lerData("Data da aula (yyyy-MM-dd): ");
                TipoAula tipo = lerTipoAula();
                if (data == null || tipo == null) {
                    continue;
                }
                try {
                    AulaController.criarAula(turma, data, tipo);
                    System.out.println("Aula cadastrada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("cadastrar aula", e);
                }
            } else if (op == 2) {
                int idAula = lerInt("Id da aula: ");
                Aula aula = AulaController.obterAula(idAula);
                if (aula == null) {
                    System.out.println("Aula não encontrada!");
                    continue;
                }
                LocalDate data = lerData("Nova data da aula (yyyy-MM-dd): ");
                TipoAula tipo = lerTipoAula();
                if (data == null || tipo == null) {
                    continue;
                }
                try {
                    AulaController.editarAula(aula, data, tipo);
                    System.out.println("Aula atualizada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("editar aula", e);
                }
            } else if (op == 3) {
                int idAula = lerInt("Id da aula: ");
                Aula aula = AulaController.obterAula(idAula);
                if (aula == null) {
                    System.out.println("Aula não encontrada!");
                    continue;
                }
                try {
                    AulaController.cancelarAula(aula);
                    System.out.println("Aula cancelada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("cancelar aula", e);
                }
            } else if (op == 4) {
                int idAula = lerInt("Id da aula: ");
                Aula aula = AulaController.obterAula(idAula);
                if (aula == null) {
                    System.out.println("Aula não encontrada!");
                    continue;
                }
                try {
                    AulaController.encerrarAula(aula);
                    System.out.println("Aula encerrada com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("encerrar aula", e);
                }
            } else if (op != 0) {
                System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void gerenciarAlunos() {
        int op;
        do {
            System.out.println("\n===============================================================");
            System.out.println("0 - Voltar");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Editar aluno");
            System.out.println("3 - Graduar aluno");
            System.out.println("4 - Alocar aluno em turma");
            System.out.println("5 - Desativar aluno");
            op = lerInt("Opção: ");

            if (op == 1) {
                String nome = lerTexto("Nome: ");
                String cpf = lerTexto("CPF: ");
                String login = lerTexto("Login: ");
                String senha = lerTexto("Senha: ");
                String email = lerTexto("Email: ");
                String telefone = lerTexto("Telefone: ");
                LocalDate dataNasc = lerData("Data de nascimento (yyyy-MM-dd): ");
                if (dataNasc == null) {
                    continue;
                }
                int idTurma = lerInt("Id da turma (0 para sem turma): ");
                if (idTurma > 0 && TurmaController.obterTurma(idTurma) == null) {
                    System.out.println("Turma não encontrada!");
                    continue;
                }
                try {
                    AlunoController.criarAluno(nome, cpf, login, senha, email, telefone, Date.valueOf(dataNasc), idTurma);
                    System.out.println("Aluno cadastrado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("cadastrar aluno", e);
                }
            } else if (op == 2) {
                int idAluno = lerInt("Id do aluno: ");
                Aluno aluno = AlunoController.obterAluno(idAluno);
                if (aluno == null) {
                    System.out.println("Aluno não encontrado!");
                    continue;
                }
                String nome = lerTexto("Nome: ");
                String cpf = lerTexto("CPF: ");
                String login = lerTexto("Login: ");
                String senha = lerTexto("Senha: ");
                String email = lerTexto("Email: ");
                String telefone = lerTexto("Telefone: ");
                LocalDate dataNasc = lerData("Data de nascimento (yyyy-MM-dd): ");
                Faixa faixa = lerFaixa("Faixa atual: ");
                if (dataNasc == null || faixa == null) {
                    continue;
                }
                boolean ativo = lerTexto("Ativo? (S/N): ").equalsIgnoreCase("S");
                try {
                    AlunoController.editarAluno(aluno, nome, cpf, login, senha, email, telefone, ativo, Date.valueOf(dataNasc), faixa);
                    System.out.println("Aluno atualizado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("editar aluno", e);
                }
            } else if (op == 3) {
                int idAluno = lerInt("Id do aluno: ");
                Aluno aluno = AlunoController.obterAluno(idAluno);
                if (aluno == null) {
                    System.out.println("Aluno não encontrado!");
                    continue;
                }
                Faixa novaFaixa = lerFaixa("Nova faixa: ");
                if (novaFaixa == null) {
                    continue;
                }
                try {
                    AlunoController.graduarAluno(aluno, novaFaixa);
                    System.out.println("Aluno graduado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("graduar aluno", e);
                }
            } else if (op == 4) {
                int idAluno = lerInt("Id do aluno: ");
                Aluno aluno = AlunoController.obterAluno(idAluno);
                if (aluno == null) {
                    System.out.println("Aluno não encontrado!");
                    continue;
                }
                int idTurma = lerInt("Id da turma: ");
                Turma turma = TurmaController.obterTurma(idTurma);
                if (turma == null) {
                    System.out.println("Turma não encontrada!");
                    continue;
                }
                try {
                    AlunoController.alocarAlunoNaTurma(turma, aluno);
                    System.out.println("Aluno alocado na turma com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("alocar aluno", e);
                }
            } else if (op == 5) {
                int idAluno = lerInt("Id do aluno: ");
                Aluno aluno = AlunoController.obterAluno(idAluno);
                if (aluno == null) {
                    System.out.println("Aluno não encontrado!");
                    continue;
                }
                try {
                    AlunoController.desativarAluno(aluno);
                    System.out.println("Aluno desativado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("desativar aluno", e);
                }
            } else if (op != 0) {
                System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void menuGerenciarInstrutor() {
        System.out.println("\n===============================================================");
        System.out.println("0 - Sair");
        System.out.println("1 - Cadastrar Instrutor");
        System.out.println("2 - Editar instrutor");
        System.out.println("3 - Desativar instrutor");
        System.out.print("Opção: ");
    }

    private static void gerenciarInstrutores() {
        int op;
        String nome, cpf, login, senha, email, telefone, cargo, faixa;
        CargoInstrutor cargoInstrutor;
        Faixa faixaInstrutor;
        do {
            menuGerenciarInstrutor();
            op = lerInt("");
            if (op == 1) {
                nome = lerTexto("Nome:");
                cpf = lerTexto("cpf:");
                login = lerTexto("login:");
                senha = lerTexto("senha:");
                email = lerTexto("email:");
                telefone = lerTexto("telefone:");
                cargo = lerTexto("Cargo (AUXILIAR, SENSEI, COORDENADOR):");
                faixa = lerTexto("Faixa:");

                cargoInstrutor = CargoInstrutor.getCargoByName(cargo);
                faixaInstrutor = Faixa.getFaixaByName(faixa);

                if (cargoInstrutor == null || faixaInstrutor == null) {
                    System.out.println("Cargo ou faixa inválidos!");
                    continue;
                }

                try {
                    InstrutorController.criarInstrutor(nome, cpf, login, senha, email, telefone, cargoInstrutor, faixaInstrutor);
                    System.out.println("Instrutor cadastrado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("cadastrar instrutor", e);
                }

            } else if (op == 2) {
                int id = lerInt("Id do instrutor:");
                Instrutor instrutor = InstrutorController.obterInstrutor(id);
                if (instrutor == null) {
                    System.out.println("Instrutor não encontrado!");
                    continue;
                }
                nome = lerTexto("Nome:");
                cpf = lerTexto("cpf:");
                login = lerTexto("login:");
                senha = lerTexto("senha:");
                email = lerTexto("email:");
                telefone = lerTexto("telefone:");
                cargo = lerTexto("Cargo (AUXILIAR, SENSEI, COORDENADOR):");
                faixa = lerTexto("Faixa:");

                cargoInstrutor = CargoInstrutor.getCargoByName(cargo);
                faixaInstrutor = Faixa.getFaixaByName(faixa);

                if (cargoInstrutor == null || faixaInstrutor == null) {
                    System.out.println("Cargo ou faixa inválidos!");
                    continue;
                }

                try {
                    InstrutorController.editarInstrutor(instrutor, nome, cpf, login, senha, email, telefone, true, cargoInstrutor, faixaInstrutor);
                    System.out.println("Instrutor atualizado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("editar instrutor", e);
                }
            } else if (op == 3) {
                int id = lerInt("Id do instrutor:");
                Instrutor instrutor = InstrutorController.obterInstrutor(id);
                if (instrutor == null) {
                    System.out.println("Instrutor não encontrado!");
                    continue;
                }
                try {
                    InstrutorController.desativarInstrutor(instrutor);
                    System.out.println("Instrutor desativado com sucesso!");
                } catch (RuntimeException e) {
                    exibirErro("desativar instrutor", e);
                }
            } else if (op != 0) {
                System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void exibirErro(String acao, RuntimeException e) {
        Throwable causa = e.getCause();
        String detalhe = (causa != null && causa.getMessage() != null && !causa.getMessage().isBlank())
                ? " (" + causa.getMessage() + ")"
                : "";
        System.out.println("Erro ao " + acao + ": " + e.getMessage() + detalhe);
    }

    private static int lerInt(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Digite um número inteiro.");
                if (prompt.isEmpty()) {
                    System.out.print("Opção: ");
                }
            }
        }
    }

    private static String lerTexto(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static LocalDate lerData(String prompt) {
        String entrada = lerTexto(prompt);
        try {
            return LocalDate.parse(entrada);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida! Use o formato yyyy-MM-dd.");
            return null;
        }
    }

    private static LocalTime lerHora(String prompt) {
        String entrada = lerTexto(prompt);
        try {
            return LocalTime.parse(entrada);
        } catch (DateTimeParseException e) {
            System.out.println("Horário inválido! Use o formato HH:mm.");
            return null;
        }
    }

    private static Faixa lerFaixa(String prompt) {
        String entrada = lerTexto(prompt);
        Faixa faixa = Faixa.getFaixaByName(entrada);
        if (faixa == null) {
            System.out.println("Faixa inválida! Opções: BRANCA, AMARELA, VERMELHA, LARANJA, VERDE, ROXA, MARROM, PRETA.");
        }
        return faixa;
    }

    private static TipoAula lerTipoAula() {
        String entrada = lerTexto("Tipo da aula (KIHON, KATA, KUMITE, CONDICIONAMENTO): ");
        try {
            return TipoAula.valueOf(entrada.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de aula inválido!");
            return null;
        }
    }

    private static List<DiaSemana> lerDiasSemana() {
        String entrada = lerTexto("Dias da semana separados por vírgula (ex: SEGUNDA,QUARTA,SEXTA): ");
        List<DiaSemana> dias = new ArrayList<>();
        if (entrada.isEmpty()) {
            System.out.println("Informe ao menos um dia da semana!");
            return null;
        }
        for (String parte : entrada.split(",")) {
            String limpo = parte.trim().toUpperCase();
            if (limpo.isEmpty()) {
                continue;
            }
            try {
                DiaSemana dia = DiaSemana.valueOf(limpo);
                if (!dias.contains(dia)) {
                    dias.add(dia);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Dia da semana inválido: " + limpo);
                return null;
            }
        }
        if (dias.isEmpty()) {
            System.out.println("Informe ao menos um dia da semana!");
            return null;
        }
        return dias;
    }
}