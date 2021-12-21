package Business.GEstabelecimento;

import java.io.Serializable;

public class SubPassoReparacao implements Serializable {
    private int durationHours;
    private float cost;

    public SubPassoReparacao(int duration, float cost) {
        this.durationHours = duration;
        this.cost = cost;
    }

    public SubPassoReparacao(SubPassoReparacao s) {
        this.durationHours = s.getDuration();
        this.cost = s.getCost();
    }

    public float getCost() {
        return this.cost;
    }

    public int getDuration() {
        return this.durationHours;
    }

    public SubPassoReparacao clone() {
        return new SubPassoReparacao(this);
    }

    public String toString(){
        final StringBuilder sb = new StringBuilder("SubPasso de Reparação{");
        sb.append("Previsão em Horas = ").append(durationHours);
        sb.append(", Previsão de Custo = ").append(cost);
        sb.append('}');
        return sb.toString();
    }
}
