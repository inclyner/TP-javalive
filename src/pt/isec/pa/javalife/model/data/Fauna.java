package pt.isec.pa.javalife.model.data;

public sealed class Fauna extends ElementoBase permits Animal {

    int velocidade,direcao;
    boolean estadoProcuraComida=false;

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
