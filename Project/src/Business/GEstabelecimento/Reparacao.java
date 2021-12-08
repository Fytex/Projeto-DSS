package Business.GEstabelecimento;


public class Reparacao {

    public enum EstadoReparacao {
        PENDING,
        EXECUTING,
        PAUSE,
        FINISHED
    }
    private Orcamento budget;
    private PlanoTrabalho plan;
    private EstadoReparacao state = EstadoReparacao.PENDING;

    public Reparacao(Orcamento budget, PlanoTrabalho plan){
        this.budget = budget;
        this.plan = plan;
    }

    public Reparacao(Reparacao repair){
        this.budget = repair.getBudget();
        this.plan = repair.getPlan();
    }

    public boolean isPending() {
        return this.state == EstadoReparacao.PENDING;
    }

    public void updateState(EstadoReparacao state) {
        this.state = state;
    }

    public Orcamento getBudget(){
        return this.budget.clone();
    }

    public PlanoTrabalho getPlan() {
        return this.plan.clone();
    }

    public Reparacao clone(){
        return new Reparacao(this);
    }

    public void pause() {
        this.state = EstadoReparacao.PAUSE;
    }

    public void finish() {
        this.state = EstadoReparacao.FINISHED;
    }

    public void start() {
        this.state = EstadoReparacao.EXECUTING;
    }
}
