package Business.GEstabelecimento;

import java.io.Serializable;

public class ComprovativoEntrega implements Serializable {
    private Funcionario func;
    private float payment;

    public ComprovativoEntrega(Funcionario func, float payment){
        this.func = func;
        this.payment = payment;
    }

    public Funcionario getFunc() {
        return func;
    }
}
