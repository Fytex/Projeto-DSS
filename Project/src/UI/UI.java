package UI;

import Business.GEstabelecimento.*;
import Business.IGEstabelecimento;
import Utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {

    private IGEstabelecimento model;
    private Scanner sc;

    public UI() {
        try {
            this.model = IGEstabelecimento.loadState("objects/state");
            this.sc = new Scanner(System.in);

        }catch (IOException | ClassNotFoundException e) {
            if (model == null) {
                this.model = new GEstabelecimentoFacade();
            }
            this.sc = new Scanner(System.in);
        }
    }

    public void run() {
        System.out.println("Bem Vindo ao sistema de Gestão de Estabelecimentos!");
        this.showMenuPrincipal();
        try {
            this.model.saveState("objects/state");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Até Breve.");
    }

    public void showMenuPrincipal() {
        Menu menuPrincipal = new Menu(new String[]{
                "Fazer Login como Funcionário",
                "Fazer Login como Técnico",
                "Fazer Login como Gestor",
                "Criar Conta"
        });
        menuPrincipal.setHandler(1, this::loginFuncionario);
        menuPrincipal.setHandler(2, this::loginTecnico);
        menuPrincipal.setHandler(3, this::loginGestor);
        menuPrincipal.setHandler(4, this::createAccount);
        menuPrincipal.run();
    }

    public void showMenuFuncionario() {
        Menu menuFuncionario = new Menu("Menu Funcionário", new String[]{
                "Registar equipamento",
                "Registar Reparação Expresso",
                "Entregar Equipamento"
        });

        menuFuncionario.setPreCondition(3, () -> (this.model.hasEquips()));

        menuFuncionario.setHandler(1, this::registerEquipment);
        menuFuncionario.setHandler(2, this::registerExpress);
        menuFuncionario.setHandler(3, this::registerDelivery);

        menuFuncionario.run();
    }

    public void showMenuTecnicoExpress() {
        Menu menuFuncionario = new Menu("Menu Funcionário Em Reparação Expresso", new String[]{
                "Concluir Reparação Expresso"
        });
        menuFuncionario.setHandler(1, this::finishExpress);
        menuFuncionario.runOnce();

    }

    public void showMenuTecnicoAvailable() {
        Menu menuTecnico = new Menu("Menu Técnico", new String[]{
                "Fazer Orçamento",
                "Confirmar Reparações",
                "Recusar Orçamentos",
                "Começar Reparação"
        });

        menuTecnico.setPreCondition(1, () -> (this.model.existsBudgetRequests() && this.model.techIsAvailable()));
        menuTecnico.setPreCondition(2, () -> (this.model.existsPendingRequests() && this.model.techIsAvailable()));
        menuTecnico.setPreCondition(3, () -> (this.model.existsPendingRequests()) && this.model.techIsAvailable());
        menuTecnico.setPreCondition(4, () -> (this.model.existsProgress() || !this.model.techIsAvailable()));

        menuTecnico.setHandler(1, this::createWorkPlan);
        menuTecnico.setHandler(2, this::confirmRepair);
        menuTecnico.setHandler(3, this::refuseBudget);
        menuTecnico.setHandler(4, this::startRepair);
        menuTecnico.run();
    }

    public void showMenuGestor() {
        Menu menuGestor = new Menu("Menu Gestor", new String[]{
                "Listagem de números de reparações",
                "Listagem funcionários",
                "Listagem exaustiva de Passos",
                "Criar Conta"
        });

        menuGestor.setPreCondition(1, () -> (this.model.hasRepairProofs()));
        menuGestor.setPreCondition(2, () -> (this.model.hasDeliveries()));

        menuGestor.setHandler(1, this::getTechStats);
        menuGestor.setHandler(2, this::getFuncStats);
        menuGestor.setHandler(3, this::getDetailedStats);
        menuGestor.run();

    }

    public void createAccount(){
        System.out.println("Introduza o username da conta a criar : ");
        String user = sc.nextLine();
        System.out.println("Introduza a password da conta : ");
        String pass = sc.nextLine();

        System.out.println("Tipo de conta a criar : ");
        String type = sc.nextLine().toUpperCase();

        try {
            this.model.createAccount(user, pass, AccountType.valueOf(type));
        }catch (Exception e){
            System.out.println("Tipo errado !!");
        }
    }

    public void loginGestor(){
        try{
            System.out.println("Introduza o username : ");
            String username = sc.nextLine();
            System.out.println("Introduza a password : ");
            String pass = sc.nextLine();

            if (this.model.login(username, pass, AccountType.GESTOR)) {
                this.showMenuGestor();
            } else {
                System.out.println("Credenciais erradas!");
            }
        } catch (UtilizadorNaoExisteException e) {
            System.out.println("Utilizador não existe !");
        } catch (Exception u) {
            u.printStackTrace();
        }
    }

    public void loginFuncionario() {
        try {
            System.out.println("Introduza o username : ");
            String username = sc.nextLine();
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
            u.printStackTrace();
        }
    }

    public void loginTecnico() {
        try {
            System.out.println("Introduza o username : ");
            String username = sc.nextLine();
            System.out.println("Introduza a password : ");
            String pass = sc.nextLine();

            if (this.model.login(username, pass, AccountType.TECNICO)) {
                if (this.model.techIsAvailable()) {
                    this.showMenuTecnicoAvailable();
                } else if (this.model.techInExpressRepair()) {
                    this.showMenuTecnicoExpress();
                } else{
                    this.showMenuTecnicoAvailable();
                }
            } else {
                System.out.println("Credenciais erradas!");
            }
        } catch (UtilizadorNaoExisteException e) {
            System.out.println("Utilizador não existe !");
        } catch (Exception u) {
            System.out.println("Erro !");
        }
    }

    public void registerExpress() {
        System.out.println("Introduzir NIF cliente : ");
        String nif = sc.nextLine();
        System.out.println("""
                **** Tipo de Reparação Expresso ****\s
                A -> Substituir ecrã
                B -> Instalar sistema operativo
                C -> Limpeza de dispositivo""".indent(1));
        String type = sc.nextLine();

        if (!model.hasClient(nif)) {
            this.createClient(nif);
        }
        try {
            this.model.registerExpress(nif, ExpressType.valueOf(type));
        } catch (TecnicoNaoDisponivelException t) {
            System.out.println("Técnico não disponível para reparação Expresso !");
        }
    }

    public void createClient(String nif) {
        System.out.println(" *** Cliente não existe! Procedendo a registo do Cliente! ***");
        System.out.println("Introduza o nome do Cliente : ");
        String name = sc.nextLine();
        System.out.println("Introduza o email do Cliente : ");
        String email = sc.nextLine();
        System.out.println("Introduza o telemovel do Cliente : ");
        String mobile = sc.nextLine();

        this.model.createClient(name, nif, email, mobile);
        System.out.println("Cliente criado !");

    }

    public void registerEquipment() {

        System.out.println("Introduzir NIF cliente : ");
        String nif = sc.nextLine();
        System.out.println("Introduzir descrição do problema : ");
        String description = sc.nextLine();

        if (!model.hasClient(nif)) {
            this.createClient(nif);
        }

        this.model.askBudget(nif, description);
        System.out.println("Equipamento registado com sucesso !");

    }

    public void confirmRepair() {
        List<Tuple<String, String>> equips = this.model.getPendingRepairsInfo();

        for (int i = 0; i < equips.size(); ++i) {
            Tuple<String, String> tup = equips.get(i);
            System.out.println("Indíce : " + i + "  Nif : " + tup.getX() + "  Descrição : " + tup.getY());
        }
        System.out.println("Qual o índice de reparação pode prosseguir ?");

        int indice = -1;
        while (indice < 0) {
            indice = Integer.parseInt(sc.nextLine());
            if (indice < 0 || indice > equips.size()) {
                System.out.println("Introduza um índice válido !");
                indice = -1;
            }
        }
        this.model.confirmRepair(indice);
    }

    public void startRepair() {
        try {
            PassoReparacao step = this.model.startRepair();
            do {
                int op = -1;
                do {
                    System.out.println(" ---- Descrição do passo de reparação : " + step.getDescricao() + " ---- ");
                    System.out.println("""
                            **** Reparação ****\s
                            0 -> Executar passo
                            1 -> Pausar reparação
                            2 -> Sair""".indent(1));

                    op = Integer.parseInt(sc.nextLine());
                    if (op < 0 || op > 2)
                        System.out.println("Introduza uma opção válida!!");
                } while (op < 0 || op > 2);
                if (op == 0) {
                    System.out.println("Introduza o número de total de horas gasto :");
                    int hours = Integer.parseInt(sc.nextLine());
                    System.out.println("Introduza o custo total do passo : ");
                    int cost = Integer.parseInt(sc.nextLine());
                    try {
                        step = this.model.executeStep(hours, cost);

                    } catch (OverPriceException ov) {
                        System.out.println("Preço ultrapassa o limite previsto em 120% !");
                        step = null;
                    }

                } else if (op == 2) {
                    break;
                } else {
                    this.model.pauseRepair();
                    step = null;
                }
            } while (step != null);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro !");
        }
    }

    public void refuseBudget() {
        List<Tuple<String, String>> equips = this.model.getPendingRepairsInfo();

        for (int i = 0; i < equips.size(); ++i) {
            Tuple<String, String> tup = equips.get(i);
            System.out.println("Indíce : " + i + "  Nif : " + tup.getX() + "  Descrição : " + tup.getY());
        }
        System.out.println("Qual o índice de reparação cujo cliente não aceitou ?");
        int indice = -1;
        while (indice < 0) {
            indice = Integer.parseInt(sc.nextLine());
            if (indice < 0 || indice > equips.size()) {
                System.out.println("Introduza um índice válido !");
                indice = -1;
            }
        }
        this.model.cancelRepair(indice);
    }

    public void finishExpress() {
        try {
            System.out.println("Reparação Expresso concluída !");
            this.model.finishExpress();
        } catch (Exception e) {
            System.out.println("Erro !!");
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

                    System.out.println("Introduza a duração prevista do subpasso em horas " + j + " : ");
                    int substepDur = Integer.parseInt(sc.nextLine());
                    System.out.println("Introduza o custo previsto do subpasso " + j + " : ");
                    float substepCost = Float.parseFloat(sc.nextLine());
                    SubPassoReparacao subStep = this.model.createSubStep(substepDur, substepCost);

                    subList.add(subStep);

                }
                return this.model.createStepWithSub(desc, subList);
            } else {
                throw new Exception();
            }
        }
    }

    public void createWorkPlan() {

        String equipProblem = this.model.getFirstBudgetRequest();

        System.out.println("\n O dispositivo mais urgente na lista de orçamentos apresenta o seguinte problema : " + equipProblem + "\n");
        System.out.println("""
                **** Estado do Equipamento ****\s
                0 -> Equipamento não pode ser reparado
                1 -> Equipamento pode ser reparado""".indent(1));

        int reparable = Integer.parseInt(sc.nextLine());

        if (reparable != 0) {
            System.out.println("Total de passos esperado : ");
            int totalSteps;
            while ((totalSteps = Integer.parseInt(sc.nextLine())) < 1) {
                System.out.println("Introduza um número maior que zero !");
            }

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
                    System.out.println("Erro de input!");
                    i--;
                }
            }
            this.model.createWorkPlan(list);
            System.out.println("Plano de trabalhos concluido !");
        } else {
            this.model.declineFirstBudgetRequest();
        }
    }

    public void registerDelivery() {
        System.out.println("Introduza o nif do cliente : ");
        String nif = sc.nextLine();
        try {
            List<Equipamento> equips = this.model.checkInfo(nif);
            if (equips != null) {
                System.out.println("Lista de equipamentos do cliente : ");

                int i = 0;
                for (Equipamento eq : equips) {
                    System.out.println("Indice : " + i + " Descrição do problema : " + eq.getInfo());
                    i++;
                }

                System.out.println("Introduza o indice do equipamento que o cliente quer recolher : ");
                int idx = -1;
                do {
                    idx = Integer.parseInt(sc.nextLine());
                    if (idx < 0 || idx > equips.size())
                        System.out.println("Introduza um indice válido !");
                } while (idx < 0 || idx > equips.size());

                Equipamento eq = equips.get(idx);

                if (eq.isFinished()) {
                    System.out.println("Equipamento não reparado por ordem do cliente ou por ordem do técnico !");
                    System.out.println("Registo de entrega feito com pagamento nulo !!!");
                    this.model.registerDelivery(eq.getId(), 0);
                } else {
                    try {
                        float total = this.model.getTotalPayment(eq.getId());

                        System.out.println("O Total a pagar é : " + total);
                        float paid = -2;
                        do {
                            System.out.println("Introduza o montante pago : ");
                            paid = Float.parseFloat(sc.nextLine());

                            if (paid != total)
                                System.out.println("Montante não correspondente ao valor total !");
                        } while (paid != total);
                        this.model.registerDelivery(eq.getId(), paid);
                    }catch (EquipamentoNaoReparadoException r){
                        System.out.println("Equipamento ainda não foi reparado !!");
                    }
                }
            }
        }catch (EquipamentoNaoExisteException e) {
            System.out.println("Equipamento não existente no sistema !");
        }
    }

    public void getTechStats() {
        Map<String, Triplet<Tuple<Integer,Integer>,Float,Float>> map = this.model.getTechStatusList();

        for(Map.Entry<String, Triplet<Tuple<Integer,Integer>,Float,Float>> entry : map.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Username Técnico : ").append(entry.getKey()).append("\n");
            sb.append("\t---> Número de reparações Normais Concluidas : ").append(entry.getValue().getX().getX()).append("\n");
            sb.append("\t---> Número de reparações Expresso Concluidas : ").append(entry.getValue().getX().getY()).append("\n");
            sb.append("\t---> Duração Média : ").append(entry.getValue().getY()).append("\n");
            sb.append("\t---> Média dos desvios : ").append(entry.getValue().getZ()).append("\n");
            System.out.println(sb);
        }
    }

    public void getFuncStats() {
        Map<String, Tuple<Integer, Integer>> map = this.model.getFunStatusList();

        for (Map.Entry<String, Tuple<Integer, Integer>> entry : map.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Username Funcionário : ").append(entry.getKey()).append("\n");
            sb.append("\t---> Número de receções feitas : ").append(entry.getValue().getX()).append("\n");
            sb.append("\t---> Número de entregas feitas : ").append(entry.getValue().getY()).append("\n");
            System.out.println(sb);
        }
    }

    public void getDetailedStats() {
        Map<String, List<String>> map = this.model.getTechRepairsInfo();

        for(Map.Entry<String, List<String>> en : map.entrySet()) {
            System.out.println("\n\t\tUsername Técnico : " + en.getKey());
            for(String s : en.getValue()) {
                System.out.println(s);
            }
        }
    }
}
