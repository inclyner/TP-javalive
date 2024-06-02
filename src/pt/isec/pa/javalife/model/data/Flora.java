package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {
    private static int proxID = 1;
    private final int id;
    private double forca;
    private final boolean reproduzivel = false; // quando a forca chega aos 90 a erva pode reproduzir
    private int numdeReproducoes = 0;//quantas vezes a erva ja foi reproduzida (max de 2)
    private float forcaTick = 0.5f;
    private static float forcaSobreposicao = 1;


    public Flora(Area area, double forca, float forcaTick, float forcaSobreposicao) {
        super(area);
        this.id = proxID;
        proxID++;
        setForca(forca);
        this.forcaTick = forcaTick;
        this.forcaSobreposicao = forcaSobreposicao;
    }

    public boolean evoluir(Boolean sol) {
        if (sol) {
            setForca(forca + 2*forcaTick);
        }else
            setForca(forca + forcaTick);
        if (forca >= 90) {
            return numdeReproducoes < 2;
        }
        return false;

    }

    public void reproduziu() {
        numdeReproducoes++;
        setForca(60);
    }

    public void reduzirForcaSobreposicao() {
        setForca(forca - forcaSobreposicao);
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
    public double getForca() {
        return forca;
    }

    @Override
    public void setForca(double forca) {
        this.forca = Math.min(Math.max(forca, 0), 100);
    }

    public static float getForcaSobreposicao() {
        return forcaSobreposicao;
    }

    public void setForcaSobreposicao(float forcaSobreposicao) {
        Flora.forcaSobreposicao = forcaSobreposicao;
    }

    @Override
    public String toString() {
        return super.toString() + "forca:" + forca;
    }
    //endregion
}
