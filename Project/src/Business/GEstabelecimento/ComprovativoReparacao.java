package Business.GEstabelecimento;

import java.io.Serializable;

public class ComprovativoReparacao implements Serializable {
    private Reparacao repair;
    private Tecnico tech;

    public ComprovativoReparacao(Reparacao repair, Tecnico tech){
        this.repair = repair;
        this.tech = tech;
    }

    public Tecnico getTech(){return this.tech;}

    public int getTotalHours() {
        if(this.repair instanceof ReparacaoNormal)
            return ((ReparacaoNormal) this.repair).getPlan().getHoursUsed();
        else
            return -1;
    }
    public int getPrevisionDif(){
        if(this.repair instanceof ReparacaoNormal) {
            PlanoTrabalho workplan = ((ReparacaoNormal) this.repair).getPlan();
            return Math.abs(workplan.getTotalHoursPrevision() - workplan.getHoursUsed());
        }else
            return -1;
    }

    public float getTotalCost() {
        if(this.repair instanceof ReparacaoNormal)
            return ((ReparacaoNormal) this.repair).getPlan().getRealCost();
        else
            return ((ReparacaoExpresso) this.repair).getPrice();
    }

    public String getEquipNif() {
        return this.repair.getEquipment().getNif();
    }

    public boolean isNormal(){
        return this.repair instanceof ReparacaoNormal;
    }

    public String getPassoString(){
        return ((ReparacaoNormal)this.repair).getPlan().getPassos().toString();
    }

    public String getExpressString(){
        return this.repair.toString();
    }

}
