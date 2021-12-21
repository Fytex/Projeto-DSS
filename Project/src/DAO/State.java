package DAO;

import Business.GEstabelecimento.GEstabelecimentoFacade;

import java.io.*;

public class State {

    public static GEstabelecimentoFacade readState(String objectPath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(objectPath);
        BufferedInputStream bi = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bi);
        GEstabelecimentoFacade obj = (GEstabelecimentoFacade) ois.readObject();
        ois.close();
        return obj;
    }

    public static void saveState(String objectPath, GEstabelecimentoFacade obj) throws IOException {
        FileOutputStream fos = new FileOutputStream(objectPath);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

}
