package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {

    boolean reproduzivel=false; // quando a forca chega aos 90 a erva pode reproduzir
    int numdeReproducoes=0;//quantas vezes a erva ja foi reproduzida (max de 2)
    double forca;
    public Flora(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
        setForca(50);

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

    public void reproduz(){
        if (reproduzivel && numdeReproducoes < 2){
            numdeReproducoes++;
            setForca(60);
        }
    }


    @Override
    public double getForca() {
        return forca ;
    }

    @Override
    public void setForca(double forca) {
        this.forca = forca;
    }
}
