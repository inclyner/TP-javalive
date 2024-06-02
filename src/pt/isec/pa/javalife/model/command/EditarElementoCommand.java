package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.Fauna;

import java.io.Serializable;

public class EditarElementoCommand  extends AbstractCommand implements ICommand{

    private Ecossistema ecossistema;
    private int id;
    private String tipo;
    private double velocidade, forca;
    private double oldvelocidade, oldforca;

    public EditarElementoCommand(Ecossistema ecossistema, String tipo,int id,double velocidade, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;
        this.velocidade = velocidade;
        this.forca = forca;
        if(velocidade>0)
            this.oldvelocidade = ecossistema.obtemValoresAntigos(tipo, id, "Velocidade");
        if(forca>0)
            this.oldforca = ecossistema.obtemValoresAntigos(tipo, id, "Forca");

    }

    @Override
    public boolean execute() {
         return ecossistema.editElemento(tipo,id,velocidade,forca);
    }

    @Override
    public boolean undo() {
        if(velocidade>0 || forca>0)
            return ecossistema.editElemento(tipo,id,oldvelocidade,oldforca);
        return false;
    }
}


