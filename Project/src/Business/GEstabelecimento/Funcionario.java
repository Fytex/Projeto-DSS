package Business.GEstabelecimento;

public class Funcionario extends Utilizador {

    private int receptions;

    public Funcionario(String username, String password) {
        super(username, password);
        this.receptions = 0;
    }

    public void increaseReceptions() {
        this.receptions += 1;
    }

    public int getReceptions() {
        return receptions;
    }
}
