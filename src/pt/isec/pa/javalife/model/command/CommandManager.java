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

    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        history.clear();
        return false;
    }


    public void execute(ICommand command) {
        command.execute();
    }

    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }

    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }
}
