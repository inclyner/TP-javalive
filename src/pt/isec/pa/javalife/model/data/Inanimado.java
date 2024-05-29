package pt.isec.pa.javalife.model.data;

public sealed class Inanimado extends ElementoBase permits Pedra {

    static int proxid = 1;
    int id;


    public Inanimado(Area area) {
        super(area);
        this.id = proxid;
        proxid++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.INANIMADO;
    }

    @Override
    public Area getArea() {
        return area;
    }


}



