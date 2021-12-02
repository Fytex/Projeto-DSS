package Business.GEstabelecimento;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String name;
    private int nif;
    private String email;
    private List<Integer> equipamentos;

    public Cliente(String name, int nif,String email){
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.equipamentos = new ArrayList<>();
    }

    public Cliente(String name, int nif, String email, int equipamento){
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.equipamentos = new ArrayList<>();
        this.equipamentos.add(equipamento);
    }

}
