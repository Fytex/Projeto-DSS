package Business.GEstabelecimento;

import java.time.LocalDateTime;

public class Orcamento {
    private Equipamento equipamento;
    private EstadoOrcamento estado;

    private float totalCostPrevision;
    private int timePrevision;
    private LocalDateTime created;

    public Orcamento(Equipamento eq) {
        this.equipamento = eq;
        this.estado = EstadoOrcamento.NOTDONE;
        this.totalCostPrevision = 0;
        this.timePrevision = 0;
        this.created = LocalDateTime.now();
    }

    public Orcamento(Orcamento o){
        this.equipamento = o.getEquipamento();
        this.estado = o.getEstado();
        this.totalCostPrevision = o.getTimePrevision();
        this.timePrevision = o.getTimePrevision();
        this.created = o.getTimeCreated();
    }

    public String getEquipmentInfo() {
        return this.equipamento.getInfo();
    }

    public Equipamento getEquipamento() {
        return this.equipamento.clone();
    }

    public EstadoOrcamento getEstado(){
        return this.estado;
    }

    public float getTotalCostPrevision(){
        return this.totalCostPrevision;
    }

    public int getTimePrevision(){
        return this.timePrevision;
    }

    public void setTotalCostPrevision(float totalCostPrevision){
        this.totalCostPrevision = totalCostPrevision;
    }

    public void setTimePrevision(int timePrevision) {
        this.timePrevision = timePrevision;
    }

    public void setEstado(EstadoOrcamento estado) {
        this.estado = estado;
    }

    public LocalDateTime getTimeCreated(){
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getTimeOfExpiricy(){
        return this.created.plusDays(30);
    }

    public Orcamento clone(){
        return new Orcamento(this);
    }
}
