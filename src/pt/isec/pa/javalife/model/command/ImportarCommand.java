package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.File;
import java.io.IOException;

public class ImportarCommand extends AbstractCommand implements ICommand {

    private Ecossistema ecossistema;
    private File selectedFile;
    public ImportarCommand(Ecossistema ecossistema, File selectedFile) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.selectedFile = selectedFile;
    }

    @Override
    public boolean execute() throws IOException {
        return ecossistema.importaSimulacao(selectedFile);
    }

    @Override
    public boolean undo() {
        return false;
    }

}
