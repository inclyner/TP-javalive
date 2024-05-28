package pt.isec.pa.javalife.model.data;

public record Area(double cima, double esquerda,double baixo, double direita) {
    public boolean compareTo(Area area) {
        // area.compareTo(area)
        //  areaanimal.compareto(areaquequeroir)

        // 0 1 2 3
        // P P P P
        // P   AAA
        // P   AAA
        // P
        // A(1,2,2,3).compareto(0,2,1,3)
        //

        return (area.cima <= this.cima && area.cima >= this.baixo) || (area.baixo <= this.cima && area.baixo >= this.baixo)
        || (area.direita >= this.esquerda && area.direita <= this.direita) || (area.esquerda >= this.esquerda && area.esquerda <= this.direita);
    }
}
