package Utils;

public class UtilizadorNaoExisteException extends Exception{
    public UtilizadorNaoExisteException(){
        super("Utilizador não existente!");
    }
}
