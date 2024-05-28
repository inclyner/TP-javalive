package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.gameengine.GameEngineState;

public sealed class Fauna extends ElementoBase implements IElementoComForca permits Animal {

    public enum FaunaState {
        PROCURA_COMIDA, NAO_PROCURA_COMIDA
    }

    // Estado atual da Fauna
    private FaunaState state;
    int velocidade, direcao;
    double forca = 50;
    static int proxid = 1;
    int id;

    public Fauna(Area area) {
        super(area);
        this.id = proxid;
        setState(FaunaState.NAO_PROCURA_COMIDA);
        proxid++;

        //diracao varia de 0 a 360
        direcao = (int) (Math.random() * 359);
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
        this.forca = forca;


    }
}
