package pt.isec.pa.javalife.model.data;

public record Area(double cima, double esquerda, double baixo, double direita) {
    public boolean compareTo(Area outra) {
        // area.compareTo(area)
        //  areaanimal.compareto(areaquequeroir)

        // 0 1 2 3
        // P P P P
        // P   AAA
        // P   AAA
        // P
        // A(1,2,2,3).compareto(0,2,1,3)
        //A(0,0,10,10)
        //P(0,0,1,1)
        //P(0,1,1,2)

        //F(1,1,2,2)

        //1<=0 && 1>=1

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
