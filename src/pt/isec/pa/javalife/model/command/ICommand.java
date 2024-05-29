package pt.isec.pa.javalife.model.command;

public interface ICommand {
    boolean execute();

    boolean undo();
}
