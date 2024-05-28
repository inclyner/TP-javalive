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

    private static int proxid = 1;
    private int id;

    public Fauna(Area area) {
        super(area);
        this.id = proxid;
        setState(FaunaState.NAO_PROCURA_COMIDA);
        proxid++;
    }


    private void setState(FaunaState state) {
        this.state = state;
    }

    private void movimentacao(){
        direcao = (int) (Math.random() * 359);
        //cima + velocidade * Math.cos(Math.toRadians(direcao));
        setForca(getForca() - forcaMovimentacao);
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
    }
}
