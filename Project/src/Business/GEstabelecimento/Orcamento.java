package Business.GEstabelecimento;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Orcamento implements Serializable {

    private int equipmentId;
    private float totalCostPrevision;
    private int timePrevision;
    private LocalDateTime created;
    private LocalDateTime archivedDate;

    public Orcamento(int equipmentId) {

        this.totalCostPrevision = 0;
        this.timePrevision = 0;
        this.created = LocalDateTime.now();
        this.equipmentId = equipmentId;
    }

    public Orcamento(Orcamento o){
        this.equipmentId = o.getEquipmentId();
        this.totalCostPrevision = o.getTotalCostPrevision();
        this.timePrevision = o.getTimePrevision();
        this.created = o.getTimeCreated();
        this.archivedDate = o.getArchivedDate();
    }

    public int getEquipmentId(){
        return this.equipmentId;
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

    public LocalDateTime getTimeCreated(){
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getArchivedDate() {return this.archivedDate;}

    public void setArchivedDate(LocalDateTime date) { this.archivedDate = date; }

    public LocalDateTime getTimeOfExpiricy(){
        return this.created.plusDays(30);
    }

    public LocalDateTime getTimeOfDisposal(){return this.archivedDate.plusDays(90);}

    public Orcamento clone(){
        return new Orcamento(this);
    }
}
