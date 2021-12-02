package Business.GEstabelecimento;

public class FuncionarioNaoExisteException extends Exception{
    public FuncionarioNaoExisteException(){
        super("Funcionário não existente !");
    }
}
