import Business.GEstabelecimento.GEstabelecimentoFacade;
import Business.IGEstabelecimento;
import controllers.UI;

public class APP {
    public static void main(String[] args){
        IGEstabelecimento est = new GEstabelecimentoFacade();
        UI ui = new UI();
        ui.run();
    }
}
