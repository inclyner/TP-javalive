package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.memento.CareTaker;
import pt.isec.pa.javalife.model.memento.Originator;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager{
    private Deque<ICommand> history; //private Stack<ICommand> history;
    private Deque<ICommand> redoCmds; //private Stack<ICommand> redoCmds;

    Originator originator;
    CareTaker careTaker;
    public CommandManager() {
        history = new ArrayDeque<>(); //history = new Stack<>();
        redoCmds = new ArrayDeque<>(); //redoCmds = new Stack<>();
        originator = new Originator();
        careTaker = new CareTaker(originator);
    }

    public boolean invokeCommand(ICommand cmd) throws IOException {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        history.clear();
        return false;
    }


    public void execute(ICommand command) throws IOException {
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

    public boolean redo() throws IOException {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }

    public Originator getOriginator() {
        return originator;
    }

    public CareTaker getCareTaker() {
        return careTaker;
    }
}
