package Utils;

public class EquipamentoNaoExisteException extends Exception{
    public EquipamentoNaoExisteException(){
        super("Equipamento não existente no sistema !");
    }
}
