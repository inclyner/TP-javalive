package pt.isec.pa.javalife.model.data;

public sealed class Fauna extends ElementoBase implements IElementoComForca permits Animal {

    int velocidade,direcao;
    double forca=50;
    boolean estadoProcuraComida=false;

    static int proxid = 1;
    int id;

    public Fauna(Area area) {
        super(area);
        this.id = proxid;
        proxid++;
        //diracao varia de 0 a 360
        direcao = (int)(Math.random() * 359);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.FAUNA;
    }

    @Override
    public Area getArea() {
        return area;
    }


    @Override
    public double getForca() {
        return forca;
    }

    @Override
    public void setForca(double forca) {
        this.forca = forca;


    }
}
