package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.memento.CareTaker;
import pt.isec.pa.javalife.model.memento.Originator;

import java.io.IOException;
import java.io.Serializable;

public class GravarSnapshot extends AbstractCommand implements ICommand {
    private Ecossistema ecossistema;

    Originator originator;
    CareTaker careTaker;


    public GravarSnapshot(Ecossistema ecossistema, Originator originator, CareTaker careTaker) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.originator = originator;
        this.careTaker = careTaker;

    }

    @Override
    public boolean execute() throws IOException {
        originator.state = ecossistema.getElementos();
        careTaker.save();
        return true;
    }

    @Override
    public boolean undo() {
        return false;
    }


}
