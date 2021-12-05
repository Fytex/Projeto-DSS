package Business.GEstabelecimento;

public class Tecnico extends Utilizador {

    public Tecnico(String username, String password) {
        super(username, password);
    }

    public boolean isAvailable() {
        return false;
    }

    public void assignExpress(ReparacaoExpresso repair) {
    }

}
