package pt.isec.pa.javalife.model.data;

// x1,y1,x2,y2
public record Area(double cima, double esquerda,double baixo, double direita) {

    public boolean compareTo(Area area){
        // area.compareTo(area)
        //  areaanimal.compareto(areaquequeroir)

        // 0 1 2 3
        // P P P P
        // P   AAA
        // P   AAA
        // P
        // A(1,2,2,3).compareto(0,2,1,3)
        //

        return area.cima <= this.cima && area.baixo >= this.baixo && area.esquerda >= this.esquerda && area.direita <= this.direita;
    }

}