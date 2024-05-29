package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.IElemento;

public class AdicionarElementoCommand implements ICommand {


    public AdicionarElementoCommand(IElemento elemento) {
        super();
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