package Business.GEstabelecimento;

import Business.IGEstabelecimento;
import DAO.State;
import Utils.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GEstabelecimentoFacade implements IGEstabelecimento, Serializable {
    private Map<String, Utilizador> accounts;
    private Map<String, Cliente> clients;
    private Map<Integer, Equipamento> equipments;

    private List<Orcamento> budgetsRequests;
    private List<Orcamento> budgetsArchived;

    private List<ReparacaoNormal> pendingRepairs;
    private List<ReparacaoNormal> progressRepairs;

    private List<ComprovativoReparacao> repairProofs;
    private List<ComprovativoEntrega> deliveryProofs;

    private List<Equipamento> disposedEquipments;

    private Utilizador currentUser;

    public GEstabelecimentoFacade() {
        this.accounts = new HashMap<>();
        this.accounts.put("Admin", new Gestor("Admin", "Admin"));
        this.clients = new HashMap<>();
        this.equipments = new HashMap<>();
        this.budgetsRequests = new ArrayList<>();
        this.budgetsArchived = new ArrayList<>();
        this.pendingRepairs = new ArrayList<>();
        this.progressRepairs = new ArrayList<>();
        this.repairProofs = new ArrayList<>();
        this.deliveryProofs = new ArrayList<>();
        this.disposedEquipments = new ArrayList<>();

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

        return equipment.clone();
    }

    public void askBudget(String nif, String info) {
        Equipamento equipment = registerEquipment(nif, info);
        ((Funcionario) this.currentUser).increaseReceptions();

        budgetsRequests.add(new Orcamento(equipment.getId()));
    }

    public void registerExpress(String nif, ExpressType type) throws TecnicoNaoDisponivelException {

        Tecnico tech = this.accounts.values().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast)
                .filter(Tecnico::isAvailable)
                .findFirst()
                .orElse(null);

        if (tech == null)
            throw new TecnicoNaoDisponivelException();

        Equipamento equipment = registerEquipment(nif, ExpressType.getInfo(type));

        ReparacaoExpresso repair = new ReparacaoExpresso(type, equipment);
        ((Funcionario) this.currentUser).increaseReceptions();

        tech.assignExpress(repair);

    }

    public String getFirstBudgetRequest() {
        return this.equipments.get(this.budgetsRequests.get(0).getEquipmentId()).getInfo();
    }

    public void declineFirstBudgetRequest() {
        Orcamento budget = this.budgetsRequests.remove(0);
        this.equipments.get(budget.getEquipmentId()).setFinished();
        budget.setArchivedDate(LocalDateTime.now());
        this.budgetsArchived.add(budget);
    }

    public void cancelRepair(int idx) {
        Equipamento equipment = this.pendingRepairs.get(idx).getEquipment();
        this.equipments.get(equipment.getId()).setFinished();
        this.pendingRepairs.remove(idx);
    }


    public List<Tuple<String, String>> getPendingRepairsInfo() {
        ReparacaoNormal repair = this.pendingRepairs.get(0);
        LocalDateTime now = LocalDateTime.now();
        int i = 0;
        while (i < this.pendingRepairs.size() && now.isAfter(repair.getBudget().getTimeOfExpiricy())) {
            this.pendingRepairs.remove(0);
            if (repair.isPending()) {
                Orcamento budget = repair.getBudget();
                budget.setArchivedDate(LocalDateTime.now());
                this.budgetsArchived.add(budget);
            }
            repair = this.pendingRepairs.get(0);
            i++;
        }
        return this.pendingRepairs.stream()
                .map(x ->
                        new Tuple<>(x.getEquipment().getNif(),
                                x.getEquipmentInfo())
                )
                .collect(Collectors.toList());
    }

    public PassoReparacao createStep(String description, int timePrevision, float costPrevision) {
        return new PassoReparacao(timePrevision, costPrevision, description);
    }

    public PassoReparacao createStepWithSub(String description, List<SubPassoReparacao> subs) {
        return new PassoReparacao(description, subs);
    }

    public SubPassoReparacao createSubStep(int duration, float cost) {
        return new SubPassoReparacao(duration, cost);
    }

    public void createWorkPlan(List<PassoReparacao> steps) {
        Orcamento budget = this.budgetsRequests.remove(0);
        Equipamento eq = this.equipments.get(budget.getEquipmentId()).clone();

        PlanoTrabalho workPlan = new PlanoTrabalho(steps);
        budget.setTimePrevision(workPlan.getTotalHoursPrevision());
        budget.setTotalCostPrevision(workPlan.getTotalCostPrevision());

        this.pendingRepairs.add(new ReparacaoNormal(budget, workPlan, eq));
    }

    private PassoReparacao techGetRepairStep() throws Exception {
        PlanoTrabalho workPlan = ((Tecnico) this.currentUser).getCurrentNormalRepair().getPlan();

        int nStep = workPlan.getNStepsFinished();
        return workPlan.getPassos().get(nStep);
    }

    public void finishExpress() throws Exception {
        Tecnico tech = (Tecnico) this.currentUser;
        ReparacaoExpresso repair = tech.getCurrentExpressRepair();

        repair.finish();
        tech.setCurrentRepair(null);
        this.repairProofs.add(new ComprovativoReparacao(repair.clone(), tech));

    }

    public PassoReparacao executeStep(int totalHours, float cost) throws Exception, OverPriceException {
        Tecnico tech = (Tecnico) this.currentUser;

        ReparacaoNormal repair = tech.getCurrentNormalRepair();
        PlanoTrabalho workPlan = repair.getPlan();

        if (workPlan.getTotalCostPrevision() * 1.2 < workPlan.getRealCost() + cost) {
            this.pauseRepair();
            throw new OverPriceException();
        }

        int nStep = workPlan.getNStepsFinished();

        workPlan.incrementHoursUsed(totalHours);
        workPlan.incrementRealCost(cost);

        workPlan.getStep(nStep).setRealCost(cost);
        workPlan.getStep(nStep).setTimeUsed(totalHours);

        ++nStep;
        workPlan.setNStepsFinished(nStep);

        if (nStep == workPlan.getNSteps()) {
            repair.finish();
            tech.setCurrentRepair(null);
            this.repairProofs.add(new ComprovativoReparacao(repair.clone(), tech));
            return null;
        } else {
            return this.techGetRepairStep();
        }
    }

    public void confirmRepair(int idx) {
        ReparacaoNormal repair = this.pendingRepairs.remove(idx);
        this.progressRepairs.add(repair);
    }

    public void pauseRepair() throws Exception {
        Tecnico tech = ((Tecnico) this.currentUser);
        ReparacaoNormal repair = tech.getCurrentNormalRepair();
        repair.pause();

        this.pendingRepairs.add(repair);
        tech.setCurrentRepair(null);

    }

    public PassoReparacao startRepair() throws Exception {
        if (((Tecnico) this.currentUser).isAvailable()) {
            ReparacaoNormal repair = this.progressRepairs.remove(0);
            repair.run();

            ((Tecnico) this.currentUser).setCurrentRepair(repair);
        }
        return this.techGetRepairStep();
    }

    private void disposeEquipment() {
        if (this.budgetsArchived.size() > 0) {
            Orcamento budget = this.budgetsArchived.get(0);
            LocalDateTime now = LocalDateTime.now();
            int i = 0;
            while (i < this.budgetsArchived.size() && now.isAfter(budget.getTimeOfDisposal())) {
                Equipamento equip = this.equipments.remove(budget.getEquipmentId());
                this.disposedEquipments.add(equip);
                this.budgetsArchived.remove(0);

                budget = this.budgetsArchived.get(0);
                i++;
            }
        }
    }

    public List<Equipamento> checkInfo(String nif) throws EquipamentoNaoExisteException {
        disposeEquipment();

        List<Equipamento> list = new ArrayList<>();

        for (Map.Entry<Integer, Equipamento> en : this.equipments.entrySet())
            if (en.getValue().getNif().equals(nif))
                list.add(en.getValue().clone());

        if (list.size() == 0)
            throw new EquipamentoNaoExisteException();

        return list;
    }

    public float getTotalPayment(int id) throws EquipamentoNaoReparadoException {
        Equipamento equip = this.equipments.get(id);

        for (ComprovativoReparacao rp : this.repairProofs)
            if (rp.getEquipNif().equals(equip.getNif()))
                return rp.getTotalCost();

        throw new EquipamentoNaoReparadoException();
    }

    public void registerDelivery(int id, float payment) {

        Funcionario func = (Funcionario) this.currentUser;

        this.deliveryProofs.add(new ComprovativoEntrega(func, payment));
        this.equipments.remove(id);

    }

    public Map<String, Triplet<Tuple<Integer, Integer>, Float, Float>> getTechStatusList() {

        Map<String, Triplet<Tuple<Integer, Integer>, Float, Float>> techStats = new HashMap<>();

        for (Map.Entry<String, Utilizador> ut : this.accounts.entrySet()) {
            Utilizador e = ut.getValue();
            if (e instanceof Tecnico) {
                int normalRepairs = 0;
                int expressRepairs = 0;
                int totalHours = 0;
                int deviation = 0;
                for (ComprovativoReparacao proof : this.repairProofs) {
                    if (proof.getTech().equals(e)) {
                        if (proof.isNormal()) {
                            normalRepairs++;
                            totalHours += proof.getTotalHours();
                            deviation += proof.getPrevisionDif();
                        } else
                            expressRepairs++;
                    }
                    if (normalRepairs != 0)
                        techStats.put(e.getUsername(),
                                new Triplet(new Tuple<>(normalRepairs, expressRepairs),
                                        totalHours / normalRepairs, deviation / normalRepairs));
                    else
                        techStats.put(e.getUsername(),
                                new Triplet(new Tuple<>(normalRepairs, expressRepairs), 0, 0));
                }
            }
        }
        return techStats;
    }

    public Map<String, Tuple<Integer, Integer>> getFunStatusList() {
        Map<String, Tuple<Integer, Integer>> stats = new HashMap<>();

        for (Map.Entry<String, Utilizador> ut : this.accounts.entrySet()) {
            Utilizador u = ut.getValue();
            if (u instanceof Funcionario) {
                int receptions = ((Funcionario) u).getReceptions();
                int deliveries = 0;
                for (ComprovativoEntrega en : this.deliveryProofs)
                    if (en.getFunc().getUsername().equals(u.getUsername())) {
                        deliveries++;
                        System.out.println(deliveries);
                    }
                stats.put(u.getUsername(), new Tuple<>(receptions, deliveries));
            }
        }

        return stats;
    }

    public Map<String, List<String>> getTechRepairsInfo() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, Utilizador> en : this.accounts.entrySet()) {
            Utilizador u = en.getValue();
            if (u instanceof Tecnico) {

                for (ComprovativoReparacao cr : this.repairProofs) {
                    if (cr.getTech().equals(u)) {
                        if (cr.isNormal())
                            list.add(cr.getPassoString());
                        else {
                            list.add(cr.getExpressString());
                        }
                    }
                }
                map.put(u.getUsername(), list);
            }
        }
        return map;
    }

    public boolean techInExpressRepair() {
        return ((Tecnico) this.currentUser).inExpressRepair();
    }

    public void saveState(String path) throws IOException {
        State.saveState(path, this);
    }

    public boolean hasClient(String nif) {
        return clients.containsKey(nif);
    }

    public boolean hasEquips() {
        return this.equipments.size() != 0;
    }

    public boolean hasRepairProofs() {
        return this.repairProofs.size() != 0;
    }

    public boolean hasDeliveries() {
        return this.deliveryProofs.size() != 0;
    }
    public boolean existsBudgetRequests() {
        return this.budgetsRequests.size() > 0;
    }

    public boolean existsPendingRequests() {
        return this.pendingRepairs.size() > 0;
    }

    public boolean existsProgress() {
        return this.progressRepairs.size() > 0;
    }

    public boolean techIsAvailable() {
        return ((Tecnico) this.currentUser).isAvailable();
    }
}
