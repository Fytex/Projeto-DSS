package Utils;

public class OverPriceException extends Exception{
    public OverPriceException(){
        super("Excede o valor limite de preço !");
    }
}
