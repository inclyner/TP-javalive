package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;

import java.io.IOException;

public class injetarForcaCommand extends AbstractCommand implements ICommand {

    private Ecossistema ecossistema;
    private int id;
    private String tipo;
    private double forca;

    public injetarForcaCommand(Ecossistema ecossistema, Elemento elemento, int id, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.id = id;
        this.tipo = elemento.toString();
        this.forca = forca;
    }

    @Override
    public boolean execute() throws IOException {
        int direcao = -1;
        int velocidade = -1;
        return ecossistema.editElemento(tipo,id,velocidade,forca);
    }

    @Override
    public boolean undo() {
        return false;
    }
}
