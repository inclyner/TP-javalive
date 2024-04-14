package pt.isec.pa.javalife.model.data;

public sealed class Inanimado extends ElementoBase permits Pedra {

    int id;

    public Inanimado(int x, int y,int sizeX,int sizeY) {
         super(x, y,sizeX,sizeY);
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



