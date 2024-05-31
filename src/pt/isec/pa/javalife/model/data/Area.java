package pt.isec.pa.javalife.model.data;

public record Area(double cima, double esquerda, double baixo, double direita) {
    public boolean compareTo(Area outra) {
        //
        //(64.0,66.0,66.0,68.0)
        if (this.cima == outra.cima && this.esquerda==outra.esquerda && this.baixo==outra.baixo && this.esquerda==outra.esquerda)
            return true;
        return !(this.esquerda >= outra.direita || outra.esquerda >= this.direita ||
                this.baixo >= outra.cima || outra.baixo >= this.cima);
    }

    public String toString() {
        return  "(" + cima +
                "," + esquerda +
                "," + baixo +
                "," + direita +
                ')';
    }
}
