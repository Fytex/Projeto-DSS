package Business;

import Business.GEstabelecimento.*;
import DAO.State;
import Utils.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGEstabelecimento {

    boolean login(String username, String password, AccountType type) throws UtilizadorNaoExisteException, Exception;
    void askBudget(String nif, String info);
    void createClient(String nome, String nif, String email, String mobile);
    boolean existsBudgetRequests();
    PassoReparacao createStep(String description, int timePrevision, float costPrevision);
    SubPassoReparacao createSubStep(int duration, float cost);
    void createWorkPlan(List<PassoReparacao> steps);
    PassoReparacao createStepWithSub(String description, List<SubPassoReparacao> subs);
    boolean hasClient(String nif);
    String getFirstBudgetRequest();
    void declineFirstBudgetRequest();
    void registerExpress(String nif, ExpressType type) throws TecnicoNaoDisponivelException;
    List<Tuple<String, String>> getPendingRepairsInfo();
    void confirmRepair(int idx);
    boolean existsPendingRequests();
    PassoReparacao startRepair() throws Exception;
    PassoReparacao executeStep(int totalHours, float cost) throws Exception;
    boolean existsProgress();
    void pauseRepair() throws Exception;
    void cancelRepair(int idx);
    boolean techIsAvailable();
    void finishExpress() throws Exception;
    boolean techInExpressRepair();
    void registerDelivery(int nif, float payment);
    List<Equipamento> checkInfo(String nif) throws EquipamentoNaoExisteException;
    float getTotalPayment(int id) throws EquipamentoNaoReparadoException;
    Map<String, Triplet<Tuple<Integer,Integer>, Float, Float>> getTechStatusList();
    Map<String, Tuple<Integer,Integer>> getFunStatusList();
    Map<String, List<String>> getTechRepairsInfo();
    boolean hasRepairProofs();

    boolean hasEquips();
    boolean hasDeliveries();

    static GEstabelecimentoFacade loadState(String path) throws IOException, ClassNotFoundException {
        return State.readState(path);
    }
    void saveState(String path) throws IOException;
    void createAccount(String username, String password, AccountType type) throws Exception;


}
