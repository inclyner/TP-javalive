package pt.isec.pa.javalife.model.data;

public record Area(double cima, double esquerda, double baixo, double direita) {
    public boolean compareTo(Area outra, Area board) { //devolve true se estiverem um em cima do outro
        if ((this.direita > board.direita || this.esquerda < board.esquerda || this.baixo > board.baixo || this.cima < board.cima))
            return true;
        if ((this.direita <= board.direita && this.esquerda >= board.esquerda && this.baixo <= board.baixo && this.cima >= board.cima)) { // se estiver dentro da board
            if (this.cima == outra.cima && this.esquerda == outra.esquerda && this.baixo == outra.baixo && this.direita == outra.direita)
                return true;
            //return !(this.esquerda >= outra.direita  outra.esquerda >= this.direita
            //      this.baixo >= outra.cima || outra.baixo >= this.cima);
            //      3 > 32                          (0, 0, 1, 1)
            return this.direita > outra.esquerda && this.esquerda < outra.direita && this.cima < outra.baixo && this.baixo > outra.cima;
            //(0,0,1,1)
            //(1,1,2,2)
        }
        return false;
    }

    public boolean iguais(Area outra){
        if (this.cima == outra.cima && this.esquerda == outra.esquerda && this.baixo == outra.baixo && this.direita == outra.direita)
            return true;
        return false;
    }

    //
    //(64.0,66.0,66.0,68.0)
        /*
        if (this.cima == outra.cima && this.esquerda==outra.esquerda && this.baixo==outra.baixo && this.esquerda==outra.esquerda)
            return true;
        return !(this.esquerda >= outra.direita ||  outra.esquerda >= this.direita ||
        this.baixo >= outra.cima || outra.baixo >= this.cima);

         */


    public String toString() {
        return "(" + cima +
                ";" + esquerda +
                ";" + baixo +
                ";" + direita +
                ')';
    }
}
