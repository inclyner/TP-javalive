package pt.isec.pa.javalife.model.memento;

public class Originator {
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Memento saveStateToMemento() {
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento) {
        state = memento.getState();
    }

    public void evolve() {
        // Simula a evolução do estado
        this.state = "Novo Estado após tick " + System.currentTimeMillis();
    }
}
