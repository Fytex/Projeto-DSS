package Business.GEstabelecimento;

import java.io.Serializable;

public class Funcionario extends Utilizador implements Serializable {
    private int receptions = 0;
    public Funcionario(String username, String password) {
        super(username, password);
    }

    public void increaseReceptions() {
        this.receptions += 1;
    }

    public int getReceptions() {
        return receptions;
    }
}
