package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.data.Fauna;

public final class Animal extends Fauna {


    public Animal(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
        direcao = (int) (Math.random() * 360);
    }
}
