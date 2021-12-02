package Business.GEstabelecimento;

public class Equipamento {
    private int idEquipamento;
    private int idFuncionario;
    private boolean concluida;
    private boolean abandonado;
    private String descricao;
    private int servico;

    private ComprovativoEntrega entrega;

    public Equipamento(int idEquipamento, int idFuncionario, int servico){
        this.idEquipamento = idEquipamento;
        this.idFuncionario = idFuncionario;
        this.concluida = false;
        this.abandonado = false;
        this.servico = servico;
    }

    public Equipamento(int idEquipamento, int idFuncionario, String descricao,int servico){
        this.idEquipamento = idEquipamento;
        this.idFuncionario = idFuncionario;
        this.concluida = false;
        this.abandonado = false;
        this.descricao = descricao;
        this.servico = servico;
    }
}
