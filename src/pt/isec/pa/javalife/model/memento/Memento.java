package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Memento implements IMemento {
    byte[] state;

    public Memento(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            state = baos.toByteArray();
        } catch (Exception e) {
            state = null;
        }
    }


    @Override
    public Ecossistema getState() {
        if (state == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(state); ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Ecossistema) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }


}