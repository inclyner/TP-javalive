package pt.isec.pa.javalife.model.data;

public sealed class Flora extends ElementoBase implements IElementoComForca permits Erva {
    private static int proxID = 1;
    private final int id;
    private double forca;
    private boolean reproduzivel = false; // quando a forca chega aos 90 a erva pode reproduzir
    private int numdeReproducoes = 0;//quantas vezes a erva ja foi reproduzida (max de 2)
    private final float forcaTick = 0.5f;
    private float forcaSobreposicao = 1;


    public Flora(Area area) {
        super(area);
        this.id = proxID;
        proxID++;
        setForca(50);
    }

    public void evoluir() {
        setForca(forca + forcaTick);
        if (forca >= 90 && !reproduzivel) {
            reproduzivel = true;
        }

    }

    public boolean reproduz() {
        return reproduzivel && numdeReproducoes < 2;
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

    public float getForcaSobreposicao() {
        return forcaSobreposicao;
    }

    public void setForcaSobreposicao(float forcaSobreposicao) {
        this.forcaSobreposicao = forcaSobreposicao;
    }

    //endregion
}
