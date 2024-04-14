package pt.isec.pa.javalife.model.data;

public final class Animal extends Fauna{


    public Animal(int x, int y) {
        super(x, y);
        direcao= (int) (Math.random()*360);
    }


}
