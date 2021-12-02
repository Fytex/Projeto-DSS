package Business.GEstabelecimento;

public class Funcionario {
    private int id;
    private String password;
    private int nrRececoes;
    private int nrEntregas;

    public Funcionario(int id, String password) {
        this.id = id;
        this.password = password;
        this.nrEntregas = 0;
        this.nrRececoes = 0;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public Equipamento registaEntregaNormal(int idEquipamento, String descricao){
        Equipamento eq = new Equipamento(idEquipamento,this.id,descricao,0);
        this.nrRececoes++;

        return eq;
    }

    public Equipamento registaEntregaExpresso(int idEquipamento){
        Equipamento eq = new Equipamento(idEquipamento,this.id,1);
        this.nrRececoes++;

        return eq;
    }


}
