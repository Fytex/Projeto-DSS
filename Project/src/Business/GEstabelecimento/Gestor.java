package Business.GEstabelecimento;

import java.io.Serializable;

public class Gestor extends Utilizador implements Serializable {
    public Gestor(String username, String password) {
        super(username, password);
    }
}
