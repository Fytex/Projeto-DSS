package controllers;

import Business.GEstabelecimento.*;
import Business.IGEstabelecimento;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {

    private IGEstabelecimento model;
    private Scanner sc;

    public UI() {
        this.model = new GEstabelecimentoFacade();
        this.sc = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Bem Vindo ao sistema de Gestão de Estabelecimentos!");
        this.showMenuPrincipal();
        System.out.println("Até Breve.");
    }

    public void showMenuPrincipal() {
        Menu menuPrincipal = new Menu(new String[]{
                "Fazer Login como Funcionário",
                "Fazer Login como Técnico",
                "Fazer Login como Gestor"
        });
        menuPrincipal.setHandler(1, this::loginFuncionario);
        menuPrincipal.setHandler(2, this::loginTecnico);
        menuPrincipal.run();
    }

    public void showMenuFuncionario() {
        Menu menuFuncionario = new Menu("Menu Funcionário", new String[]{
                "Registar equipamento"
        });
        menuFuncionario.setHandler(1, this::registerEquipment);
        menuFuncionario.run();
    }

    public void showMenuTecnico() {
        Menu menuTecnico = new Menu("Menu Técnico", new String[]{
                "Fazer Orçamento"
        });

        menuTecnico.setPreCondition(1, () -> (this.model.getBudgetRequestSize() > 0));
        menuTecnico.setHandler(1, this::createWorkPlan);
        menuTecnico.run();
    }


    public void loginFuncionario() {
        try {
            System.out.println("Introduza o username : ");
            String username = sc.nextLine();
            System.out.println(username);
            System.out.println("Introduza a password : ");
            String pass = sc.nextLine();

            if (this.model.login(username, pass, AccountType.FUNCIONARIO)) {
                this.showMenuFuncionario();
            } else {
                System.out.println("Credenciais erradas!");
            }
        } catch (UtilizadorNaoExisteException e) {
            System.out.println("Utilizador não existe !");
        } catch (Exception u) {
            System.out.println("Erro !");
        }
    }

    public void loginTecnico() {
        try {
            System.out.println("Introduza o username : ");
            String username = sc.nextLine();
            System.out.println("Introduza a password : ");
            String pass = sc.nextLine();

            if (this.model.login(username, pass, AccountType.TECNICO)) {
                this.showMenuTecnico();
            } else {
                System.out.println("Credenciais erradas!");
            }
        } catch (UtilizadorNaoExisteException e) {
            System.out.println("Utilizador não existe !");
        } catch (Exception u) {
            System.out.println("Erro !");
        }
    }


    public void registerEquipment() {

        System.out.println("Introduzir NIF cliente : ");
        String nif = sc.nextLine();
        System.out.println("Introduzir descrição do problema : ");
        String description = sc.nextLine();

        try {
            this.model.askBudget(nif, description);
        } catch (ClienteNaoExisteException c) {

            System.out.println(" *** Cliente não existe! Procedendo a registo do Cliente! ***");
            System.out.println("Introduza o nome do Cliente : ");
            String name = sc.nextLine();
            System.out.println("Introduza o email do Cliente : ");
            String email = sc.nextLine();
            System.out.println("Introduza o telemovel do Cliente : ");
            String mobile = sc.nextLine();

            this.model.createClient(name, nif, email, mobile);
            System.out.println("Cliente criado !");
            try {
                this.model.askBudget(nif, description);
                System.out.println("Equipamento registado com sucesso !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PassoReparacao createSteps(int stepType, String desc) throws Exception {

        if (stepType == 0) {

            System.out.println("Introduza o tempo previsto em horas do passo : ");
            int time = Integer.parseInt(sc.nextLine());
            System.out.println("Introduza o custo previsto do passo : ");
            float cost = Float.parseFloat(sc.nextLine());

            return this.model.createStep(desc, time, cost);
        } else {
            List<SubPassoReparacao> subList = new ArrayList<>();

            System.out.println("Introduza o número de subpassos : ");
            int substeps = Integer.parseInt(sc.nextLine());

            if (substeps >= 1) {
                for (int j = 0; j < substeps; ++j) {

                    System.out.println("Introduza a duração prevista do subpasso " + j + " : ");
                    int substepDur = Integer.parseInt(sc.nextLine());
                    System.out.println("Introduza o custo previsto do subpasso " + j + " : ");
                    float substepCost = Float.parseFloat(sc.nextLine());
                    SubPassoReparacao subStep = this.model.createSubStep(substepDur, substepCost);

                    subList.add(subStep);

                }
                return this.model.createStepWithSub(desc,subList);
            } else {
                throw new Exception();
            }
        }
    }

    public void createWorkPlan() {

        System.out.println("Total de passos esperado : ");
        int totalSteps = readOption();

        List<PassoReparacao> list = new ArrayList<>();

        for (int i = 0; i < totalSteps; ++i) {
            int stepType = -1;
            do {
                System.out.println("""
                        **** Tipo de passo necessário ****\s
                        0 -> Passo sem subppassos
                        1 -> Passo com subpassos""".indent(1));
                stepType = Integer.parseInt(sc.nextLine());
                if (stepType < 0 || stepType > 1)
                    System.out.println("Opção inválida");
            } while (stepType < 0 || stepType > 1);

            System.out.println("Introduza a descrição do passo : ");
            String desc = sc.nextLine();
            try {
                PassoReparacao step = createSteps(stepType, desc);
                list.add(step);
            } catch (Exception e) {
                System.out.println("Número de substeps tem de ser maior que 2 !");
                i--;
            }


            this.model.createWorkPlan(list);
            System.out.println("Plano de trabalhos concluido !");
        }
    }

    private int readOption() {
        int op;

        System.out.print("Opção: ");
        try {
            String line = sc.nextLine();
            op = Integer.parseInt(line);
        } catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        return op;
    }

}
