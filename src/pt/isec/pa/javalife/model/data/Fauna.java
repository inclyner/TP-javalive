package pt.isec.pa.javalife.model.data;

public sealed class Fauna extends ElementoBase implements IElementoComForca permits Animal {

    public enum FaunaState {
        PROCURA_COMIDA, NAO_PROCURA_COMIDA
    }

    // Estado atual da Fauna
    private FaunaState state;
    private int direcao;
    private double velocidade = 1;
    private double forca;
    private final boolean estadoProcuraComida = false;
    private final float forcaMovimentacao = 0.5f;
    private IElemento elemetoPerseguir;
    private final double forcaReproducao = 25;
    private int unidTempo = 0;
    private static int proxid = 1;
    private final int id;

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

    public boolean reproducao(boolean isInRange) {

        if (isInRange) {
            unidTempo++;
        } else {
            unidTempo = 0;
            return false;
        }
        if (unidTempo == 10) {
            this.setForca(forca - forcaReproducao);
            unidTempo = 0;
            return true;
        }
        return false;
    }


    public Area movimentacao() {
        direcao = (int) (Math.random() * 359);
        //cima + velocidade * Math.cos(Math.toRadians(direcao));
        //esquerda + velocidade * Math.sin(Math.toRadians(direcao));
        //setForca(getForca() - forcaMovimentacao);
        return new Area(getArea().cima() + velocidade * Math.cos(Math.toRadians(direcao)), getArea().esquerda() + velocidade * Math.sin(Math.toRadians(direcao)),
                getArea().baixo() + velocidade * Math.cos(Math.toRadians(direcao)), getArea().direita() + velocidade * Math.sin(Math.toRadians(direcao)));
    }

    public Area moverParaComida(Area area, boolean existePedra) {
        if (!existePedra) {
            double deltaX = area.esquerda() - this.getArea().esquerda();
            double deltaY = area.cima() - this.getArea().cima();

            // Calcula o ângulo em radianos entre a posição atual e a posição de destino
            double angulo = Math.atan2(deltaY, deltaX);

            double passo = 1;

            // Calcula o deslocamento em X e Y baseado no ângulo e no passo
            double deslocamentoX = passo * Math.cos(angulo);
            double deslocamentoY = passo * Math.sin(angulo);

            return new Area(this.getArea().cima() + deslocamentoY,
                    this.getArea().esquerda() + deslocamentoX,
                    this.getArea().baixo() + deslocamentoY,
                    this.getArea().direita() + deslocamentoX);
        } else {
            return movimentacao();
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
        this.forca = Math.min(Math.max(forca, 0), 100);
        if (this.forca < 35)
            setState(FaunaState.PROCURA_COMIDA);
        else if (this.forca >= 80)
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

    public boolean verificarAdjacente() {
        return moverParaComida(area, false).compareTo(elemetoPerseguir.getArea());
    }

    @Override
    public String toString() {
        return super.toString() + "forca:" + forca;
    }
}
