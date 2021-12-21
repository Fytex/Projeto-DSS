package Business.GEstabelecimento;

import java.io.Serializable;

public class ReparacaoExpresso extends Reparacao implements Serializable {

    private ExpressType typeExpress;

   public ReparacaoExpresso(ExpressType type, Equipamento eq){
       super(eq);
       this.typeExpress = type;
   }

   public ReparacaoExpresso(ReparacaoExpresso repair){
       super(repair.getEquipment());
       this.typeExpress = repair.getTypeExpress();
   }

   public ExpressType getTypeExpress(){
       return this.typeExpress;
   }

   public int getPrice(){
       return ExpressType.getPrice(this.typeExpress);
   }

   public ReparacaoExpresso clone(){
       return new ReparacaoExpresso(this);
   }

   public String toString(){
       final StringBuilder sb = new StringBuilder("Reparação Expresso {");
       sb.append("Tipo = ").append(ExpressType.getInfo(typeExpress));
       sb.append(", Custo Fixo = ").append(ExpressType.getPrice(typeExpress));
       sb.append(" }");
       return sb.toString();
   }

}
