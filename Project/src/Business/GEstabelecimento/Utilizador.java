package Business.GEstabelecimento;

import java.io.Serializable;

public abstract class Utilizador implements Serializable {

    private String username;
    private String password;

    public Utilizador(String username, String password){
        this.username = username;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public String getUsername(){
        return this.username;
    }
}
