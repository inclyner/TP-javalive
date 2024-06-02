package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;

import java.io.IOException;

public class AplicarHerbicidaCommand extends AbstractCommand implements ICommand {
    private Ecossistema ecossistema;
    Elemento tipo;
    int id;
    public AplicarHerbicidaCommand(Ecossistema ecossistema, Elemento tipo, int id) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;

    }

    @Override
    public boolean execute() throws IOException {
        return ecossistema.aplicarHerbicida(tipo,id);
    }

    @Override
    public boolean undo() {
        return false;
    }
}
