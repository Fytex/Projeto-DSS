package Business.GEstabelecimento;

import java.util.List;

public class GReparacaoFacade {
    private List<Reparacao> pendingRepairs;
    private List<Reparacao> progressRepairs;


    public void addToPendingRepairs(Reparacao repair){
        this.pendingRepairs.add(repair.clone());
    }
}
