package Business.GEstabelecimento;

public abstract class Utilizador {
    private String username;
    private String password;

    public Utilizador(String username,String password){
        this.username = username;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
