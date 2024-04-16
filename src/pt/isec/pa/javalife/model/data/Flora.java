package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {

    boolean reproduzivel=false; // quando a forca chega aos 90 a erva pode reproduzir
    int numdeReproducoes=0;//quantas vezes a erva ja foi reproduzida (max de 2)

    double forca=50;
    static int proxid = 1;
    int id;
    public Flora(Area area) {
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
        return Elemento.FLORA;
    }

    @Override
    public Area getArea() {
        return area;
    }

    public void reproduz() {
        if (numdeReproducoes < 2){
            numdeReproducoes++;
            forca = 60;
        }
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
