package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.IOException;
import java.io.Serializable;

public class Originator implements Serializable, IOriginator {
    Ecossistema state;

    @Override
    public IMemento save() throws IOException {
        return new Memento(this);
    }

    @Override
    public void restore(IMemento memento) {
        Object obj = memento.getState();
        if (obj instanceof Originator m)
            state = m.state;
    }


}
