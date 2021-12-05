package Business.GEstabelecimento;

public class SubPassoReparacao {
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
}
