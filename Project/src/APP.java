import Business.GEstabelecimento.GEstabelecimentoFacade;
import Business.IGEstabelecimento;
import UI.UI;

public class APP {
    public static void main(String[] args){
        IGEstabelecimento est = new GEstabelecimentoFacade();
        UI ui = new UI();
        ui.run();
    }
}
