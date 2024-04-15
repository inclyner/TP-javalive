package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.data.Inanimado;

public final class Pedra extends Inanimado {

    public Pedra(int cima, int esquerda, int baixo, int direita) {
        super(new Area (cima, esquerda, baixo, direita));;
    }
}
