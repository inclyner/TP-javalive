package pt.isec.pa.javalife.model.memento;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class CareTaker implements Serializable {

    private final IOriginator originator;
    Deque<IMemento> history;
    Deque<IMemento> redoStack;


    public CareTaker(IOriginator originator) {
        this.originator = originator;
        history = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }



    public void save() throws IOException {
        redoStack.clear();
        history.push(originator.save());
    }

    public void undo() throws IOException {
        if (history.isEmpty())
            return;
        redoStack.push(originator.save());
        originator.restore(history.pop());
    }

    public void redo() throws IOException {
        if (redoStack.isEmpty())
            return;
        history.push(originator.save());
        originator.restore(redoStack.pop());
    }


    public void reset() {
        history.clear();
        redoStack.clear();
    }

    public boolean hasUndo() {
        return !history.isEmpty();
    }
    public boolean hasRedo() {
        return !redoStack.isEmpty();
    }

}
