package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.File;
import java.io.IOException;

public class ExportarCommand extends AbstractCommand implements ICommand {

    private Ecossistema ecossistema;
    private File file;
    public ExportarCommand(Ecossistema ecossistema, File file) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.file = file;

    }

    @Override
    public boolean execute() throws IOException {
        return ecossistema.exportaSimulacao(file);    }

    @Override
    public boolean undo() {
        return false;
    }
}
