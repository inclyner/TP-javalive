package pt.isec.pa.javalife.model.data;

public sealed class Fauna extends ElementoBase permits Animal {

    int velocidade,direcao;
    boolean estadoProcuraComida=false;

    public Fauna(Area area) {
        super(area);
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
