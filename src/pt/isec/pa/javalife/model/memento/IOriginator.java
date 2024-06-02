package pt.isec.pa.javalife.model.memento;

import java.io.IOException;

public interface IOriginator {

    IMemento save() throws IOException;

    void restore(IMemento memento) throws IOException;

    IMemento getMemento();
}