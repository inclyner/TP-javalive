package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.IElemento;

import java.io.Serializable;

public class RemoverElementoCommand extends AbstractCommand implements ICommand{
    Ecossistema ecossistema;
    String tipo;
    int id;
    Area a;
    double forca;

    public RemoverElementoCommand(Ecossistema ecossistema,String tipo,int id, Area a, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;
        this.a = a;
        this.forca = forca;
    }

    @Override
    public boolean execute() {
        if (ecossistema.removeElemento(tipo,id))
            return true;
        else if(ecossistema.removeElementoPelaArea(tipo, a))
            return true;
        return false;
    }

    @Override
    public boolean undo() {
        Elemento elemento=null;
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
}
