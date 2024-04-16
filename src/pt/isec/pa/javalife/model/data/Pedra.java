package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.data.Inanimado;

public final class Pedra extends Inanimado {

    public Pedra(double cima, double esquerda, double baixo, double direita) {
        super(new Area (cima, esquerda, baixo, direita));;
    }
}
