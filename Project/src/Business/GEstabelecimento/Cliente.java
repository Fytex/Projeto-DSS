package Business.GEstabelecimento;

import java.io.Serializable;


public class Cliente implements Serializable {
    private String nome;
    private String nif;
    private String email;
    private String mobile;

    public Cliente(String nome, String nif, String email, String mobile){
        this.nome = nome;
        this.nif = nif;
        this.email = email;
        this.mobile = mobile;
    }
}
