package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.IElemento;
import pt.isec.pa.javalife.model.factory.ElementFactory;

public class AdicionarElementoCommand extends AbstractCommand implements ICommand{

    Ecossistema ecossistema;
    String tipo;
    double x, y, altura, largura, forca;

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
//! ainda nao esta a por forca
        IElemento elemento = null;
        Area a = new Area(x, y, altura, largura);
        if (tipo == "FAUNA") {
            elemento = ElementFactory.createElement(Elemento.FAUNA, a);
        } else if (tipo == "INANIMADO") {
            elemento = ElementFactory.createElement(Elemento.INANIMADO, a);
        } else if (tipo == "FLORA") {
            elemento = ElementFactory.createElement(Elemento.FLORA, a);
        }
        System.out.println("elemnento.to = " + elemento.toString());
        if(ecossistema.verificaSobreposicao(elemento)==0)
        {
            ecossistema.addElemento(elemento.getType(),a);
            return true;
        }


        return false;
    }

    @Override
    public boolean undo() {
        return false;
    }
}