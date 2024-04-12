package pt.isec.pa.javalive.model.data;

public class Animal extends Fauna{

    int velocidade,direcao,forca=50;
    boolean estadoProcuraComida=false;
    public Animal(int x, int y) {
        super(x, y);
        direcao= (int) (Math.random()*360);
    }

    public void movimenta(){
        forca-=0.5;
    }

    public void aumentaForca(){
        forca+=0.5;
    }
}
