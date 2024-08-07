package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.IElemento;
import pt.isec.pa.javalife.model.factory.ElementFactory;

import java.io.Serializable;

public class AdicionarElementoCommand extends AbstractCommand implements ICommand{

    private Ecossistema ecossistema;
    private String tipo;
    private double x, y, altura, largura, forca;



    public AdicionarElementoCommand(Ecossistema ecossistema, String tipo, double x, double y, double altura, double largura, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.largura = largura;
        this.forca = forca;
    }

    @Override
    public boolean execute() {
        Elemento elemento=null;
        Area a = new Area(x, y, altura, largura);
        if (tipo == "FAUNA") {
            elemento = Elemento.FAUNA;
        } else if (tipo == "INANIMADO") {
            elemento = Elemento.INANIMADO;
        } else if (tipo == "FLORA") {
            elemento = Elemento.FLORA;
        }
        if(ecossistema.isAreaLivre(a) && elemento!=null)
        {
            ecossistema.addElemento(elemento,a,forca);
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        Area a = new Area(x, y, altura, largura);
        if(ecossistema.removeElementoPelaArea(tipo, a))
            return true;
        return false;
    }
}