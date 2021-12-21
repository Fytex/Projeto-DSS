package Business.GEstabelecimento;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class PlanoTrabalho implements Serializable {
    private int totalHoursPrevision;
    private float totalCostPrevision;
    private int hoursUsed;
    private float realCost;
    private int nStepsFinished = 0;
    private List<PassoReparacao> steps;

    public PlanoTrabalho(List<PassoReparacao> steps) {
        this.totalHoursPrevision = steps.stream().map(PassoReparacao::getTimePrevison).reduce(0, Integer::sum);
        this.totalCostPrevision = steps.stream().map(PassoReparacao::getCostPrevision).reduce((float) 0, Float::sum);
        this.hoursUsed = 0;
        this.realCost = 0;
        this.steps = steps.stream().map(PassoReparacao::clone).collect(Collectors.toList());
    }

    public PlanoTrabalho(PlanoTrabalho p){
        this.totalHoursPrevision = p.getTotalHoursPrevision();
        this.totalCostPrevision = p.getTotalCostPrevision();
        this.hoursUsed = p.getHoursUsed();
        this.realCost = p.getRealCost();
        this.steps = p.getPassos();
    }

    public int getNSteps() {
        return this.steps.size();
    }

    public int getNStepsFinished() {
        return this.nStepsFinished;
    }


    public void setNStepsFinished(int value) {
        this.nStepsFinished = value;
    }

    public PassoReparacao getStep(int idx) {
        return this.steps.get(idx);
    }

    public float getTotalCostPrevision() {
        return totalCostPrevision;
    }

    public int getTotalHoursPrevision() {
        return totalHoursPrevision;
    }

    public List<PassoReparacao> getPassos() {
        return this.steps.stream().map(PassoReparacao::clone).collect(Collectors.toList());
    }

    public void incrementRealCost(float realCost) {
        this.realCost += realCost;
    }

    public int getHoursUsed() {
        return this.hoursUsed;
    }

    public float getRealCost() {
        return this.realCost;
    }

    public void incrementHoursUsed(int hoursUsed) {
        this.hoursUsed += hoursUsed;
    }

    public PlanoTrabalho clone(){
        return new PlanoTrabalho(this);
    }
}
