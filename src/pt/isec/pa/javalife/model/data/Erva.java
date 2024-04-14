package pt.isec.pa.javalive.model.data;

public class Erva extends Flora{
    double forca=50; //força inicial é 50
    boolean reproduzivel=false; // quando a forca chega aos 90 a erva pode reproduzir
    int numdeReproducoes=0; //quantas vezes a erva ja foi reproduzida (max de 2)
    public Erva(int x, int y) {
        super(x, y);
    }

    public void aumentaForca(){
        forca+=0.5;
    }

    public void reproduz(){
        numdeReproducoes++;
        forca=60;
    }

}

