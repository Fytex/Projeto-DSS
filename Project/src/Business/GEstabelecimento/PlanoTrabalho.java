package Business.GEstabelecimento;

import java.util.List;
import java.util.stream.Collectors;

public class PlanoTrabalho {
    private int totalHoursPrevision;
    private float totalCostPrevision;
    private int hoursUsed;
    private float realCost;

    List<PassoReparacao> passos;

    public PlanoTrabalho(List<PassoReparacao> steps) {
        this.totalHoursPrevision = steps.stream().map(PassoReparacao::getTimePrevison).reduce(0, Integer::sum);
        this.totalCostPrevision = steps.stream().map(PassoReparacao::getCostPrevision).reduce((float) 0, Float::sum);
        this.hoursUsed = 0;
        this.realCost = 0;
        this.passos = steps.stream().map(PassoReparacao::clone).collect(Collectors.toList());
    }

    public PlanoTrabalho(PlanoTrabalho p){
        this.totalHoursPrevision = p.getTotalHoursPrevision();
        this.totalCostPrevision = p.getTotalCostPrevision();
        this.hoursUsed = p.getHoursUsed();
        this.realCost = p.getRealCost();
        this.passos = p.getPassos();
    }

    public float getTotalCostPrevision() {
        return totalCostPrevision;
    }

    public int getTotalHoursPrevision() {
        return totalHoursPrevision;
    }

    public List<PassoReparacao> getPassos() {
        return this.passos.stream().map(PassoReparacao::clone).collect(Collectors.toList());
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
