package pt.isec.pa.javalife.model.command;

import javafx.application.Platform;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;
import pt.isec.pa.javalife.model.memento.CareTaker;
import pt.isec.pa.javalife.model.memento.Originator;

import java.io.IOException;
import java.io.Serializable;

public class CarregarSnapshotCommand extends AbstractCommand implements ICommand {
    private Ecossistema ecossistema;

    Originator originator;
    CareTaker careTaker;

    public CarregarSnapshotCommand(Ecossistema ecossistema, Originator originator, CareTaker careTaker) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.originator = originator;
        this.careTaker = careTaker;
    }

    @Override
    public boolean execute() throws IOException {
        return careTaker.undo();
    }

    @Override
    public boolean undo() {
        return false;
    }
}
