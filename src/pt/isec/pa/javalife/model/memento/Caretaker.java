package pt.isec.pa.javalife.model.memento;

import java.util.Stack;

public class Caretaker {
    private Stack<Memento> undoStack = new Stack<>();
    private Stack<Memento> redoStack = new Stack<>();
    private Originator originator;

    public Caretaker(Originator originator) {
        this.originator = originator;
    }

    public void save() {
        undoStack.push(originator.saveStateToMemento());
        redoStack.clear();  // Limpa a pilha de redo porque um novo estado foi salvo
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Memento memento = undoStack.pop();
            redoStack.push(originator.saveStateToMemento());
            originator.getStateFromMemento(memento);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Memento memento = redoStack.pop();
            undoStack.push(originator.saveStateToMemento());
            originator.getStateFromMemento(memento);
        }
    }
}
