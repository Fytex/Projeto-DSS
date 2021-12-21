package Business.GEstabelecimento;

import java.io.Serializable;

public abstract class Reparacao implements Serializable {

    public enum EstadoReparacao {
        PENDING,
        EXECUTING,
        PAUSE,
        FINISHED
    }

    public Reparacao(Equipamento eq){
        this.equipamento = eq;
    }

    private EstadoReparacao state = EstadoReparacao.PENDING;
    private Equipamento equipamento;

    public boolean isPending() {
        return this.state == EstadoReparacao.PENDING;
    }
    public void finish() {
        this.state = EstadoReparacao.FINISHED;
    }

    public void run() {
        this.state = EstadoReparacao.EXECUTING;
    }

    public String getEquipmentInfo() {
        return this.equipamento.getInfo();
    }
    public Equipamento getEquipment(){
        return this.equipamento.clone();
    }
    public void pause() {
        this.state = EstadoReparacao.PAUSE;
    }


}
