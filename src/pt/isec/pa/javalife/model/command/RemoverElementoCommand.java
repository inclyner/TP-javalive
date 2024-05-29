package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.IElemento;

public class RemoverElementoCommand implements ICommand {
    public RemoverElementoCommand(IElemento element) {
        super();
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
