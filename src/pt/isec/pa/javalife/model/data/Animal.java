package pt.isec.pa.javalife.model.data;

public final class Animal extends Fauna{


    public Animal(int cima, int esquerda, int baixo, int direita) {
        super(new Area (cima, esquerda, baixo, direita));
        direcao= (int) (Math.random()*360);
    }


}
