package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {
    static int proxID = 1;
    int id;
    double forca;
    boolean reproduzivel = false; // quando a forca chega aos 90 a erva pode reproduzir
    int numdeReproducoes = 0;//quantas vezes a erva ja foi reproduzida (max de 2)


    public Flora(Area area) {
        super(area);
        this.id = proxID;
        proxID++;
        setForca(50);
    }

    public void evoluir() {
        aumentarForca(0.5);
        if (forca >= 90 && !reproduzivel) {
            reproduzivel = true;
        }

    }

    public boolean reproduz() {
        if (reproduzivel && numdeReproducoes < 2) {
            return true;
        }
        return false;
    }

    public void reproduziu() {
        numdeReproducoes++;
        setForca(60);
    }

    public void aumentarForca(double valor) {
        setForca(forca + valor);
    }

    public void reduzirForca(double valor) {
        setForca(forca - valor);
        if (forca <= 0) {
            //Ecossistema.removerElemento(this);
        }
    }


    //region gets e sets
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


    @Override
    public double getForca() {
        return forca;
    }

    @Override
    public void setForca(double forca) {
        this.forca = Math.min(Math.max(forca, 0), 100);

    }
//endregion
}
