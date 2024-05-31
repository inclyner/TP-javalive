package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

public class RemoverElementoCommand extends AbstractCommand implements ICommand {
    Ecossistema ecossistema;
    int id;

    public RemoverElementoCommand(Ecossistema ecossistema,int id) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.id = id;

    }

    @Override
    public boolean execute() {

        return ecossistema.removeElemento(id);
    }

    @Override
    public boolean undo() {
        return false;
    }
}
