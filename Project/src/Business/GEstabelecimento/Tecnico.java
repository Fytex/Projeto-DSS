package Business.GEstabelecimento;


import java.io.Serializable;

public class Tecnico extends Utilizador implements Serializable {

    private Reparacao currentRepair = null;

    public Tecnico(String username, String password) {
        super(username, password);
    }

    public boolean isAvailable() {
        return this.currentRepair == null;
    }

    public boolean inExpressRepair() {
        return (this.currentRepair instanceof ReparacaoExpresso);
    }

    public ReparacaoExpresso getCurrentExpressRepair() throws Exception{
        if (!(this.currentRepair instanceof ReparacaoExpresso)) {
            throw new Exception();
        }
        return (ReparacaoExpresso) this.currentRepair;
    }

    public ReparacaoNormal getCurrentNormalRepair() throws Exception{
        if (!(this.currentRepair instanceof ReparacaoNormal)) {
            throw new Exception();
        }
        return (ReparacaoNormal) this.currentRepair;
    }

    public void setCurrentRepair(ReparacaoNormal repair) {
        this.currentRepair = repair;
    }

    public void assignExpress(ReparacaoExpresso repair) {
        this.currentRepair = repair;
    }

}
