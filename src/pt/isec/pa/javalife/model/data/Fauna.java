package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.gameengine.GameEngineState;

public sealed class Fauna extends ElementoBase implements IElementoComForca permits Animal {

    public enum FaunaState {
        PROCURA_COMIDA, NAO_PROCURA_COMIDA
    }

    // Estado atual da Fauna
    private FaunaState state;
    private int velocidade, direcao;
    private double forca = 50;
    private boolean estadoProcuraComida = false;
    private final float forcaMovimentacao = 0.5f;
    private IElemento elemetoPerseguir;

    private static int proxid = 1;
    private int id;

    public Fauna(Area area) {
        super(area);
        this.id = proxid;
        setState(FaunaState.NAO_PROCURA_COMIDA);
        proxid++;
        //diracao varia de 0 a 360
        //direcao = (int) (Math.random() * 359);
    }

    public FaunaState getState() {
        return state;
    }

    private void setState(FaunaState state) {
        this.state = state;
    }


    public void evoluir() {
        if (state == FaunaState.NAO_PROCURA_COMIDA) {

        }
        else if (state == FaunaState.PROCURA_COMIDA) {

        }

    }

    public Area movimentacao(){
        direcao = (int) (Math.random() * 359);
        //cima + velocidade * Math.cos(Math.toRadians(direcao));
        //esquerda + velocidade * Math.sin(Math.toRadians(direcao));
        //setForca(getForca() - forcaMovimentacao);
        return new Area(getArea().cima() + velocidade * Math.cos(Math.toRadians(direcao)), getArea().esquerda() + velocidade * Math.sin(Math.toRadians(direcao)),
                getArea().baixo() + velocidade * Math.cos(Math.toRadians(direcao)), getArea().direita() + velocidade * Math.sin(Math.toRadians(direcao)));
    }

    public void procuraComida(){

    }

    public Area moverParaComida(Area area, boolean pedra){
        return null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.FAUNA;
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
        this.forca = Math.min(Math.max(forca,0), 100);
        if (forca < 35)
            setState(FaunaState.PROCURA_COMIDA);
        else if (forca >= 80)
            setState(FaunaState.NAO_PROCURA_COMIDA);
    }

    public float getForcaMovimentacao() {
        return forcaMovimentacao;
    }

    public void setElemetoPerseguir(IElemento elemetoPerseguir) {
        this.elemetoPerseguir = elemetoPerseguir;
    }

    public IElemento getElemetoPerseguir() {
        return elemetoPerseguir;
    }

    public boolean verificarAdjacente(){
        return moverParaComida(area, false).compareTo(elemetoPerseguir.getArea());
    }
}
