package pt.isec.pa.javalife.model.data;

public abstract sealed class ElementoBase implements IElemento permits Inanimado, Flora, Fauna {

    Area area;

    public ElementoBase(Area area) {
        this.area = area;
    }

    @Override
    public Area getArea() {
        return area;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Elemento getType() {
        return null;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "id:" + getId() + "type:" + getType()+ "area:" + getArea();
    }
}
