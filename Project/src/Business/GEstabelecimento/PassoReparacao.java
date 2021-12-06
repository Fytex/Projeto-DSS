package Business.GEstabelecimento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PassoReparacao {
    private String descricao;
    private int timePrevison;
    private float costPrevision;
    private int timeUsed;
    private float realCost;
    private List<SubPassoReparacao> substeps;

    public PassoReparacao(int time, float cost, String descricao) {
        this.descricao = descricao;
        this.timePrevison = time;
        this.costPrevision = cost;
        this.timeUsed = 0;
        this.realCost = 0;
        this.substeps = null;
    }

    public PassoReparacao(int substepNumber) {
        this.timePrevison = 0;
        this.costPrevision = 0;
        this.timeUsed = 0;
        this.realCost = 0;
        this.substeps = new ArrayList<>(substepNumber);
    }

    public PassoReparacao(String descricao, List<SubPassoReparacao> substeps){
        this.descricao = descricao;
        this.timePrevison = substeps.stream().map(SubPassoReparacao::getDuration).reduce(0,Integer::sum);
        this.costPrevision = substeps.stream().map(SubPassoReparacao::getCost).reduce((float) 0 , Float::sum);
        this.substeps = substeps.stream().map(SubPassoReparacao::clone).collect(Collectors.toList());
        this.timeUsed = 0;
        this.realCost = 0;
    }

    public PassoReparacao(PassoReparacao p) {
        this.descricao = p.getDescricao();
        this.timePrevison = p.getTimePrevison();
        this.costPrevision = p.getCostPrevision();
        this.substeps = p.getSubsteps();
        this.timeUsed = p.getTimeUsed();
        this.realCost = p.getRealCost();
        this.substeps = p.getSubsteps();
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public float getRealCost() {
        return realCost;
    }

    public void setRealCost(float realCost) {
        this.realCost = realCost;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public void addSubStep(SubPassoReparacao subStep) {
        this.substeps.add(subStep);
        this.timePrevison += subStep.getDuration();
        this.costPrevision += subStep.getCost();

    }

    private List<SubPassoReparacao> getSubsteps() {
        if(this.substeps != null)
            return this.substeps.stream().map(SubPassoReparacao::clone).collect(Collectors.toList());
        else
            return null;

    }

    public String getDescricao() {
        return this.descricao;
    }

    public int getTimePrevison() {
        return this.timePrevison;

    }

    public float getCostPrevision() {
        return this.costPrevision;
    }

    public PassoReparacao clone() {
        return new PassoReparacao(this);
    }


}
