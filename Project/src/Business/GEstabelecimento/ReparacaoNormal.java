package Business.GEstabelecimento;

public class ReparacaoNormal extends Reparacao {

    private Orcamento budget;
    private PlanoTrabalho plan;


    public ReparacaoNormal(Orcamento budget, PlanoTrabalho plan, Equipamento eq) {
        super(eq);
        this.budget = budget;
        this.plan = plan;
    }

    public ReparacaoNormal(ReparacaoNormal repair) {
        super(repair.getEquipment());
        this.budget = repair.getBudget();
        this.plan = repair.getPlan();
    }

    public void incNSteps() {
        this.plan.setNStepsFinished(this.plan.getNStepsFinished() + 1);
    }

    public Orcamento getBudget() {
        return this.budget;
    }

    public PlanoTrabalho getPlan() {
        return this.plan;
    }

    public ReparacaoNormal clone() {
        return new ReparacaoNormal(this);
    }

}
