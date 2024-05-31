package pt.isec.pa.javalife.model.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private Deque<ICommand> history; //private Stack<ICommand> history;
    private Deque<ICommand> redoCmds; //private Stack<ICommand> redoCmds;
    public CommandManager() {
        history = new ArrayDeque<>(); //history = new Stack<>();
        redoCmds = new ArrayDeque<>(); //redoCmds = new Stack<>();
    }

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
