package pt.isec.pa.javalife.model.data;

public sealed class Fauna extends ElementoBase implements IElementoComImagem permits Animal {

    int velocidade;
    protected int direcao;
    boolean estadoProcuraComida=false;

    public Fauna(int x, int y, int sizeX,int sizeY) {
        super(x, y,sizeX,sizeY);
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Elemento getType() {
        return null;
    }

    @Override
    public Area getArea() {
        return null;
    }

    @Override
    public String getImagem() {
        return null;
    }

    @Override
    public void setImagem(String imagem) {

    }
}
