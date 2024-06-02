package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.File;
import java.io.IOException;

public class AbrirJogoCommand extends AbstractCommand implements ICommand {

    private Ecossistema ecossistema;
    private File selectedFile;

    public AbrirJogoCommand(Ecossistema ecossistema, File selectedFile) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.selectedFile = selectedFile;
    }

    @Override
    public boolean execute() throws IOException {
        return ecossistema.importaSimulacao(selectedFile,true);

    }

    @Override
    public boolean undo() {
        return false;
    }
}
