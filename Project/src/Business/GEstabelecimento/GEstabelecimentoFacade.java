package Business.GEstabelecimento;

import Business.IGEstabelecimento;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GEstabelecimentoFacade implements IGEstabelecimento {
    private Map<String, Utilizador> accounts;
    private Map<String, Cliente> clients;
    private Map<Integer, Equipamento> equipments;
    private List<Orcamento> budgetsRequests;
    private List<Orcamento> budgetsArchived;

    private List<Reparacao> pendingRepairs;
    private List<Reparacao> progressRepairs;

    private Utilizador currentUser;

    public GEstabelecimentoFacade() {
        this.accounts = new HashMap<>();
        this.accounts.put("Joaquim",new Tecnico("Joaquim","Joaquim"));
        this.accounts.put("A",new Funcionario("A","A"));
        this.clients = new HashMap<>();
        this.equipments = new HashMap<>();
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
        this.currentUser = user;
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
        equipments.put(equipment.getId(), equipment);

        return equipment;
    }

    public boolean hasClient(String nif) {
        return clients.containsKey(nif);
    }

    private Cliente getClient(String nif) throws ClienteNaoExisteException {
        Cliente client = clients.get(nif);

        if (client == null)
            throw new ClienteNaoExisteException();
        return client;
    }

    public void askBudget(String nif, String info) {

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
    public String getFirstBudgetRequest(){
        return this.budgetsRequests.get(0).getEquipmentInfo();
    }

    public int getBudgetRequestSize() {
        return this.budgetsRequests.size();
    }

    public void cancelRepair(int idx) {
        Equipamento equipment = this.pendingRepairs.get(idx).getBudget().getEquipment();
        this.equipments.get(equipment.getId()).setFinished();
        this.pendingRepairs.remove(idx);
        ((Tecnico) this.currentUser).setCurrentRepair(null);
    }

    public void continueRepair(int idx) {
        Reparacao repair = this.pendingRepairs.get(idx);
        this.pendingRepairs.remove(idx);
        repair.updateState(Reparacao.EstadoReparacao.EXECUTING);
        this.progressRepairs.add(repair);
    }

    public List<Tuple<String,String>>  getPendingRepairsInfo() {
        Reparacao repair = this.pendingRepairs.get(0);
        LocalDateTime now = LocalDateTime.now();
        while (now.isAfter(repair.getBudget().getTimeOfExpiricy())) {
            this.pendingRepairs.remove(0);
            if (repair.isPending()) {
                this.budgetsArchived.add(repair.getBudget());
            }
            repair = this.pendingRepairs.get(0);
        }
        return this.pendingRepairs.stream()
                .map(x ->
                        new Tuple<String, String>(x.getBudget().getEquipment().getNif(),
                        x.getBudget().getEquipmentInfo())
                )
                .collect(Collectors.toList());
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

        this.budgetsRequests.remove(0);
        this.pendingRepairs.add(new Reparacao(budget,workPlan));
    }
/**
    public // techGetRepair() {
        Tecnico tech = (Tecnico) this.currentUser;
        if (tech.getCurrentRepair() == null) {
            tech.setCurrentRepair(this.progressRepairs.get(0));
            this.progressRepairs.remove(0);
        }

        //return
    }
**/
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

    public void pauseRepair() {
        ((Tecnico) this.currentUser).getCurrentRepair().updateState(Reparacao.EstadoReparacao.PAUSE);
    }

    public void startRepair() {
        this.progressRepairs.get(0).start();
    }


}
