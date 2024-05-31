package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

public class AdicionarElementoCommand extends AbstractCommand {


    public AdicionarElementoCommand(Ecossistema ecossistema) {
        super(ecossistema);
// store parameters
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean undo() {
        return false;
    }
}