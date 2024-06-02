package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.memento.CareTaker;
import pt.isec.pa.javalife.model.memento.Originator;

import java.io.IOException;
import java.io.Serializable;

public interface ICommand {
    boolean execute() throws IOException;

    boolean undo();



}

abstract class AbstractCommand implements ICommand {
    protected Ecossistema receiver;
    protected AbstractCommand(Ecossistema receiver) {
        this.receiver = receiver;
    }
}
