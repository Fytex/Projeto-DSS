package Utils;

public class TecnicoNaoDisponivelException extends Exception{
    public TecnicoNaoDisponivelException(){
        super("Tecnico não disponivel para realizar serviço Expresso!");
    }
}
