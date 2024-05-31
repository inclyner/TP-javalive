package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;

public interface ICommand {
    boolean execute();

    boolean undo();
}

abstract class AbstractCommand implements ICommand {
    protected Ecossistema receiver;
    protected AbstractCommand(Ecossistema receiver) {
        this.receiver = receiver;
    }
}
