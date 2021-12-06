package Business.GEstabelecimento;

import Business.IGEstabelecimento;

import java.util.*;

public class GEstabelecimentoFacade implements IGEstabelecimento {
    private Map<String, Utilizador> accounts;
    private Map<String, Cliente> clients;
    private List<Equipamento> equipments;
    private List<Orcamento> budgetsRequests;
    private List<Orcamento> budgetsArchived;

    private List<Reparacao> pendingRepairs;
    private List<Reparacao> progressRepairs;

    private Utilizador atualUser;

    public GEstabelecimentoFacade() {
        this.accounts = new HashMap<>();
        this.accounts.put("Joaquim",new Tecnico("Joaquim","Joaquim"));
        this.accounts.put("A",new Funcionario("A","A"));
        this.clients = new HashMap<>();
        this.equipments = new ArrayList<>();
        this.budgetsRequests = new ArrayList<>();
        this.budgetsArchived = new ArrayList<>();
        this.pendingRepairs = new ArrayList<>();
    }


    public boolean login(String username, String password, AccountType type) throws UtilizadorNaoExisteException, Exception {
        Utilizador user = accounts.get(username);

        switch (type) {
            case GESTOR -> {
                if (!(user instanceof Gestor)) {
                    throw new UtilizadorNaoExisteException();
                }
            }

            case TECNICO -> {
                if (!(user instanceof Tecnico)) {
                    throw new UtilizadorNaoExisteException();
                }
            }

            case FUNCIONARIO -> {
                if (!(user instanceof Funcionario)) {
                    throw new UtilizadorNaoExisteException();
                }
            }

            default -> throw new Exception();
        }
        this.atualUser = user;
        return user.login(password);
    }


    public void createAccount(String username, String password, AccountType type) throws Exception {
        Utilizador user;
        switch (type) {
            case FUNCIONARIO -> user = new Funcionario(username, password);
            case TECNICO -> user = new Tecnico(username, password);
            default -> throw new Exception();
        }

        this.accounts.put(username, user);

    }

    public void createClient(String nome, String nif, String email, String mobile) {
        this.clients.put(nif, new Cliente(nome, nif, email, mobile));
    }

    private Equipamento registerEquipment(String nif, String info) {
        Equipamento equipment = new Equipamento(nif, info);
        equipments.add(equipment);

        return equipment;
    }

    private Cliente getClient(String nif) throws ClienteNaoExisteException {
        Cliente client = clients.get(nif);

        if (client == null)
            throw new ClienteNaoExisteException();
        return client;
    }

    public void askBudget(String nif, String info) throws ClienteNaoExisteException {
        Cliente client = getClient(nif);

        Equipamento equipment = registerEquipment(nif, info);

        budgetsRequests.add(new Orcamento(equipment));

    }

    public void registerExpress(String nif, ExpressType type) throws ClienteNaoExisteException, TecnicoNaoDisponivelException, Exception {
        Cliente client = getClient(nif);

        Tecnico tech = this.accounts.values().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast)
                .filter(Tecnico::isAvailable)
                .findFirst()
                .orElse(null);

        if (tech == null)
            throw new TecnicoNaoDisponivelException();

        Equipamento equipment = registerEquipment(nif, ExpressType.getInfo(type));

        ReparacaoExpresso repair = new ReparacaoExpresso(equipment, type);

        tech.assignExpress(repair);

    }

    public int getBudgetRequestSize() {
        return this.budgetsRequests.size();
    }

    public String getFirstBudgetRequest() {
        Orcamento budget = this.budgetsRequests.get(0);
        while (budget.getTimeCreated().isBefore(budget.getTimeOfExpiricy())) {
            this.budgetsRequests.remove(0);
            this.budgetsArchived.add(budget);
            budget = this.budgetsRequests.get(0);
        }
        return budget.getEquipmentInfo();
    }

    public PassoReparacao createStep(String description, int timePrevision, float costPrevision) {
        return new PassoReparacao(timePrevision, costPrevision, description);
    }

    public PassoReparacao createStepWithSub(String description, List<SubPassoReparacao> subs) {
        return new PassoReparacao(description,subs);
    }

    public SubPassoReparacao createSubStep(int duration, float cost){
        return new SubPassoReparacao(duration, cost);
    }

    public void createWorkPlan(List<PassoReparacao> steps) {
        Orcamento budget = this.budgetsRequests.get(0);
        PlanoTrabalho workPlan = new PlanoTrabalho(steps);
        budget.setEstado(EstadoOrcamento.DONE);
        budget.setTimePrevision(workPlan.getTotalHoursPrevision());
        budget.setTotalCostPrevision(workPlan.getTotalCostPrevision());
        this.pendingRepairs.add(new Reparacao(budget,workPlan));
    }

    public void executeStep(int nStep, int totalHours, float cost) {
        Reparacao repair = this.progressRepairs.get(0);
        /*

        Se passar 120% tenho de por noutra lista ou então dar opção a estas funções de usarem um index diferente

         */

        PlanoTrabalho workPlan = repair.getPlan();
        workPlan.incrementHoursUsed(totalHours);
        workPlan.incrementRealCost(cost);

        workPlan.passos.get(nStep).setRealCost(cost);
        workPlan.passos.get(nStep).setTimeUsed(totalHours);

        if (nStep == workPlan.passos.size() - 1) {
            repair.finish();
        }

    }

    public void startRepair() {
        this.progressRepairs.get(0).start();
    }


}
