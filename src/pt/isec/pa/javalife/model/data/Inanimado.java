package pt.isec.pa.javalife.model.data;

public sealed class Inanimado extends ElementoBase permits Pedra{

    public Inanimado(Area area) {
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



