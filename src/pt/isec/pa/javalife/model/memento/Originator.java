package pt.isec.pa.javalife.model.memento;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public class Originator implements Serializable, IOriginator {
    public Set<IElemento> state;

    public IMemento save() throws IOException {
        return new Memento(this);
    }

    @Override
    public void restore(IMemento memento) throws IOException {
        Object obj = memento.getState();
        if (obj instanceof Originator m)
            state = m.state;
    }

    @Override
    public IMemento getMemento() {
        return null;
    }


}
