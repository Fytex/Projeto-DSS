package Business.GEstabelecimento;

public class ClienteNaoExisteException extends Exception{
    public ClienteNaoExisteException(){
        super("Cliente não existente no sistema!");
    }
}
