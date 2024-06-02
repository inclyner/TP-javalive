package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.*;

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
    public Object getState() {
        if (state == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(state);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            System.out.println("passou no getstate");
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }


    }


