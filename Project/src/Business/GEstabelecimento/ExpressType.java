package Business.GEstabelecimento;

public enum ExpressType {
    A,
    B,
    C;

    public static String getInfo(ExpressType type) {
        String ret;
        switch (type) {
            case A -> ret = "Substituir ecrã";
            case B -> ret = "Instalar sistema operativo";
            case C -> ret = "Limpeza de dispositivo";
            default -> ret = null;
        }

        return ret;
    }

    public static int getPrice(ExpressType type){
        int price;
        switch (type) {
            case A -> price = 100;
            case B -> price = 250;
            case C -> price = 50;
            default -> price = 0;
        }

        return price;
    }
}
