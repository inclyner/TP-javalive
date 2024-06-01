package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

public class RemoverElementoCommand extends AbstractCommand implements ICommand {
    Ecossistema ecossistema;
    String tipo;
    int id;

    public RemoverElementoCommand(Ecossistema ecossistema,String tipo,int id) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;

    }

    @Override
    public boolean execute() {
        return ecossistema.removeElemento(tipo,id);
    }

    @Override
    public boolean undo() {
        return false;
    }
}
