package Business.GEstabelecimento;

public class Equipamento {
    private String nif;
    private String info;

    public Equipamento(String nif, String info) {
        this.nif = nif;
        this.info = info;
    }

    public Equipamento(Equipamento e) {
        this.nif = e.getNif();
        this.info = e.getInfo();
    }

    public String getNif() {
        return this.nif;
    }

    public String getInfo() {
        return this.info;
    }

    public Equipamento clone() {
        return new Equipamento(this);
    }

}
