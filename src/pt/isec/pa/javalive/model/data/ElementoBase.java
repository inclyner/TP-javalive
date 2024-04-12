package pt.isec.pa.javalive.model.data;

public abstract sealed class ElementoBase
        implements IElemento
        permits Inanimado, Flora, Fauna {

    int x,y;
    int size;

    public int[] getcoords() {
        return new int[] {x,y};
    }

    public void setcoords(int x, int y) {
        this.x = x;
        this.y = y;
    }


}