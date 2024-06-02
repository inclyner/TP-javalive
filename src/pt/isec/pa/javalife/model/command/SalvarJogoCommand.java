package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.command.AbstractCommand;
import pt.isec.pa.javalife.model.command.ICommand;
import pt.isec.pa.javalife.model.data.Ecossistema;

import java.io.File;
import java.io.IOException;

public class SalvarJogoCommand extends AbstractCommand implements ICommand {

    private Ecossistema ecossistema;
    private File file;
    private double timeUnit;

    public SalvarJogoCommand(Ecossistema ecossistema, File file, double timeUnit) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.file = file;
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean execute() throws IOException {
        return ecossistema.exportaSimulacao(file, true,timeUnit);
    }

    @Override
    public boolean undo() {
        return false;
    }
}
