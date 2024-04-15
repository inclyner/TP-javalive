package pt.isec.pa.javalife.model.data;

public abstract sealed class ElementoBase
        implements IElemento
        permits Inanimado, Flora, Fauna {

    Area area;

    public ElementoBase(Area area) {
        this.area = area;
    }

    @Override
    public Area getArea() {
        return area;
    }


}