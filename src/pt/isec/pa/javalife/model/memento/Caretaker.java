package pt.isec.pa.javalife.model.memento;

import java.io.IOException;
import java.util.Stack;

public class Caretaker {

    IOriginator originator;
    Stack<IMemento> undoStack;
    Stack<IMemento> redoStack;

    public void CareTaker(IOriginator originator) {
        this.originator = originator;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void save() throws IOException {
        redoStack.clear();  // Limpa a pilha de redo porque um novo estado foi salvo
        undoStack.push(originator.save());
    }

    public void undo() throws IOException {
        if (undoStack.isEmpty())
            return;
        redoStack.push(originator.save());
        originator.restore(undoStack.pop());
    }

    public void redo() throws IOException {
        if (redoStack.isEmpty())
            return;
        undoStack.push(originator.save());
        originator.restore(redoStack.pop());
    }


    public void reset() {
        undoStack.clear();
        redoStack.clear();
    }

    public boolean hasUndo() {
        return !undoStack.isEmpty();
    }

    public boolean hasRedo() {
        return !redoStack.isEmpty();
    }

}
