package pt.isec.pa.javalife.model.data;

public abstract sealed class ElementoBase
        implements IElemento
        permits Inanimado, Flora, Fauna {

    int x,y;
    int sizeX,sizeY;

    public ElementoBase(int x, int y,int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int[] getcoords() {
        return new int[] {x,y};
    }

    public void setcoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getSizeX(){
        return sizeX;
    }
    public int getSizeY(){
        return sizeY;
    }


}