package Business;

import Business.GEstabelecimento.*;

import java.util.List;

public interface IGEstabelecimento {

    boolean login(String username, String password, AccountType type) throws UtilizadorNaoExisteException, Exception;
    void askBudget(String nif, String info) throws ClienteNaoExisteException;
    void createClient(String nome, String nif, String email, String mobile);
    int getBudgetRequestSize();
    PassoReparacao createStep(String description, int timePrevision, float costPrevision);
    SubPassoReparacao createSubStep(int duration, float cost);
    void createWorkPlan(List<PassoReparacao> steps);
    PassoReparacao createStepWithSub(String description, List<SubPassoReparacao> subs);
}
