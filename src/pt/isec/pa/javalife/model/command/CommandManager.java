package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.memento.Originator;

public class CommandManager {




    public static void undo() {
        // undo
    }

    public boolean invokeCommand(ICommand cmd) {
        return cmd.execute();
    }


    public void execute(ICommand command) {

        command.execute();

    }

}
