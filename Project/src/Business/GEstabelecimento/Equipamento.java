package Business.GEstabelecimento;

import java.io.Serializable;

public class Equipamento implements Serializable {
    private String nif;
    private String info;
    private boolean finished;
    private int id;

    static int next_id = 0;

    public Equipamento(String nif, String info) {
        this.nif = nif;
        this.info = info;
        this.finished = false;
        this.id = next_id;
        ++next_id;
    }

    public Equipamento(Equipamento e) {
        this.nif = e.getNif();
        this.info = e.getInfo();
        this.id = e.getId();
        this.finished = e.isFinished();
    }

    public String getNif() {
        return this.nif;
    }

    public void setFinished(){
        this.finished = true;
    }

    public String getInfo() {
        return this.info;
    }

    public int getId() {
        return this.id;
    }

    public boolean isFinished(){
        return this.finished;
    }

    public Equipamento clone() {
        return new Equipamento(this);
    }
}