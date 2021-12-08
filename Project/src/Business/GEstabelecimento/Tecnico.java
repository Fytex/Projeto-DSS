package Business.GEstabelecimento;

public class Tecnico extends Utilizador {

    private Reparacao currentRepair = null;

    public Tecnico(String username, String password) {
        super(username, password);
    }

    public boolean isAvailable() {
        return false;
    }

    public Reparacao getCurrentRepair() {
        return this.currentRepair;
    }

    public void setCurrentRepair(Reparacao repair) {
        this.currentRepair = repair;
    }

    public void assignExpress(ReparacaoExpresso repair) {
    }

}
