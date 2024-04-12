package pt.isec.pa.javalive.model.data;

public non-sealed class Fauna extends ElementoBase {
    public Fauna(int x, int y) {
        super(x, y);
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Elemento getType() {
        return null;
    }

    @Override
    public Area getArea() {
        return null;
    }
}
