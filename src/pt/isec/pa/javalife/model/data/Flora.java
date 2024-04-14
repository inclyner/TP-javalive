package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {

    boolean reproduzivel=false; // quando a forca chega aos 90 a erva pode reproduzir
    int numdeReproducoes=0; //quantas vezes a erva ja foi reproduzida (max de 2)
    public Flora(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
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
        numdeReproducoes++;
        //forca=60; setforca(..)
    }


    @Override
    public double getForca() {
        return 0;
    }

    @Override
    public void setForca(double forca) {
        this.for
    }
}
